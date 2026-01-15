package com.project.Mental.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Mental.model.Appointment;
import com.project.Mental.model.AppointmentStatus;

import jakarta.persistence.LockModeType;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	boolean existsByCounselorIdAndStartTimeAndStatus(Long counselorId, LocalDateTime startTime, AppointmentStatus status);

	boolean existsByCounselorIdAndStartTimeAndStatusIn(Long counselorId, LocalDateTime startTime, Collection<AppointmentStatus> statuses);

	List<Appointment> findByStatusOrderByStartTimeAsc(AppointmentStatus status);

	@Query("select a from Appointment a join fetch a.student join fetch a.counselor where a.status = :status order by a.startTime asc")
	List<Appointment> findByStatusWithStudentAndCounselorOrderByStartTimeAsc(@Param("status") AppointmentStatus status);

	List<Appointment> findByCounselorIdAndStatusOrderByStartTimeAsc(Long counselorId, AppointmentStatus status);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Appointment a where a.counselor.id = :counselorId and a.startTime = :startTime")
	List<Appointment> lockAllForSlot(@Param("counselorId") Long counselorId, @Param("startTime") LocalDateTime startTime);

	Optional<Appointment> findFirstByCounselorIdAndStartTimeAndStatus(Long counselorId, LocalDateTime startTime, AppointmentStatus status);
}
