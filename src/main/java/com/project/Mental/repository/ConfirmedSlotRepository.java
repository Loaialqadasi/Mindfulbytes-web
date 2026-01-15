package com.project.Mental.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Mental.model.ConfirmedSlot;

public interface ConfirmedSlotRepository extends JpaRepository<ConfirmedSlot, Long> {
	boolean existsByCounselorIdAndStartTime(Long counselorId, LocalDateTime startTime);

	void deleteByCounselorIdAndStartTime(Long counselorId, LocalDateTime startTime);
}
