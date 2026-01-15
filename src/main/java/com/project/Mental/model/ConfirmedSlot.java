package com.project.Mental.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
		name = "confirmed_slots",
		uniqueConstraints = @UniqueConstraint(name = "uk_confirmed_slot", columnNames = {"counselor_id", "start_time"})
)
public class ConfirmedSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "counselor_id", nullable = false)
	private Counselor counselor;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	public ConfirmedSlot(Counselor counselor, LocalDateTime startTime) {
		this.counselor = counselor;
		this.startTime = startTime;
	}
}
