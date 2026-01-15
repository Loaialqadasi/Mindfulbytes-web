package com.project.Mental.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
		name = "appointments",
		indexes = {
				@Index(name = "idx_appointments_slot", columnList = "counselor_id,start_time")
		}
)
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "counselor_id", nullable = false)
	private Counselor counselor;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status = AppointmentStatus.PENDING;

	@Column(length = 500)
	private String notes;

	public Appointment(Student student, Counselor counselor, LocalDateTime startTime) {
		this.student = student;
		this.counselor = counselor;
		this.startTime = startTime;
		this.status = AppointmentStatus.PENDING;
	}
}
