package com.project.Mental.model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "assessment_results")
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    @Column(name = "raw_answers", nullable = false, columnDefinition = "TEXT")
    private String rawAnswers;
    @Column(name = "score", nullable = false)
    private Double score;
    @Column(name = "feedback", nullable = false, columnDefinition = "TEXT")
    private String feedback;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AssessmentResult() { this.createdAt = LocalDateTime.now(); }
    
    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getRawAnswers() { return rawAnswers; }
    public void setRawAnswers(String rawAnswers) { this.rawAnswers = rawAnswers; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}