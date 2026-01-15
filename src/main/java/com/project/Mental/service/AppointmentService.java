package com.project.Mental.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Mental.model.Appointment;
import com.project.Mental.model.AppointmentStatus;
import com.project.Mental.model.ConfirmedSlot;
import com.project.Mental.model.Counselor;
import com.project.Mental.model.Student;
import com.project.Mental.repository.AppointmentRepository;
import com.project.Mental.repository.ConfirmedSlotRepository;
import com.project.Mental.repository.CounselorRepository;
import com.project.Mental.repository.StudentRepository;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final StudentRepository studentRepository;
	private final CounselorRepository counselorRepository;
	private final ConfirmedSlotRepository confirmedSlotRepository;

	public AppointmentService(
			AppointmentRepository appointmentRepository,
			StudentRepository studentRepository,
			CounselorRepository counselorRepository,
			ConfirmedSlotRepository confirmedSlotRepository
	) {
		this.appointmentRepository = appointmentRepository;
		this.studentRepository = studentRepository;
		this.counselorRepository = counselorRepository;
		this.confirmedSlotRepository = confirmedSlotRepository;
	}

	@Transactional
	public Appointment book(Long studentId, Long counselorId, LocalDateTime startTime) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found"));
		Counselor counselor = counselorRepository.findById(counselorId)
				.orElseThrow(() -> new IllegalArgumentException("Counselor not found"));

		if (appointmentRepository.existsByCounselorIdAndStartTimeAndStatusIn(
				counselorId,
				startTime,
				List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED)
		)) {
			throw new IllegalStateException("This slot is already booked.");
		}

		try {
			confirmedSlotRepository.save(new ConfirmedSlot(counselor, startTime));
		} catch (DataIntegrityViolationException ex) {
			throw new IllegalStateException("This slot is already booked.");
		}

		Appointment appointment = new Appointment(student, counselor, startTime);
		appointment.setStatus(AppointmentStatus.PENDING);
		return appointmentRepository.save(appointment);
	}

	@Transactional
	public Appointment confirm(Long appointmentId) {
		Appointment target = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

		Long counselorId = target.getCounselor().getId();
		LocalDateTime startTime = target.getStartTime();

		if (!confirmedSlotRepository.existsByCounselorIdAndStartTime(counselorId, startTime)) {
			try {
				confirmedSlotRepository.save(new ConfirmedSlot(target.getCounselor(), startTime));
			} catch (DataIntegrityViolationException ignored) {
				// Slot already locked by another transaction.
			}
		}

		List<Appointment> slotAppointments = appointmentRepository.lockAllForSlot(counselorId, startTime);

		for (Appointment a : slotAppointments) {
			if (a.getId().equals(appointmentId)) {
				a.setStatus(AppointmentStatus.CONFIRMED);
			} else if (a.getStatus() == AppointmentStatus.PENDING) {
				a.setStatus(AppointmentStatus.REJECTED);
			}
		}

		appointmentRepository.saveAll(slotAppointments);
		return target;
	}

	@Transactional
	public Appointment reject(Long appointmentId) {
		Appointment target = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

		if (target.getStatus() == AppointmentStatus.REJECTED || target.getStatus() == AppointmentStatus.CANCELLED) {
			return target;
		}

		target.setStatus(AppointmentStatus.REJECTED);
		Appointment saved = appointmentRepository.save(target);
		boolean stillBooked = appointmentRepository.existsByCounselorIdAndStartTimeAndStatusIn(
				saved.getCounselor().getId(),
				saved.getStartTime(),
				List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED)
		);
		if (!stillBooked) {
			confirmedSlotRepository.deleteByCounselorIdAndStartTime(saved.getCounselor().getId(), saved.getStartTime());
		}
		return saved;
	}

	@Transactional(readOnly = true)
	public List<Appointment> getPendingAppointments() {
		return appointmentRepository.findByStatusWithStudentAndCounselorOrderByStartTimeAsc(AppointmentStatus.PENDING);
	}

	@Transactional(readOnly = true)
	public List<Appointment> getConfirmedAppointments() {
		return appointmentRepository.findByStatusWithStudentAndCounselorOrderByStartTimeAsc(AppointmentStatus.CONFIRMED);
	}
}
