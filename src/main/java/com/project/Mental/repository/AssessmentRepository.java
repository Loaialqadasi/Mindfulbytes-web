package com.project.Mental.repository;
import com.project.Mental.model.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AssessmentRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByStudentId(Long studentId);
}