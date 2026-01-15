package com.project.Mental.controller;

import com.project.Mental.model.AssessmentResult;
import com.project.Mental.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @GetMapping("/assessment")
    public String showAssessmentForm() { return "student/assessment_form"; }

    @PostMapping("/assessment/submit")
    public String submitAssessment(@RequestParam(defaultValue="0") int q1, @RequestParam(defaultValue="0") int q2, @RequestParam(defaultValue="0") int q3, @RequestParam(defaultValue="0") int q4, @RequestParam(defaultValue="0") int q5, @RequestParam(defaultValue="0") int q6, @RequestParam(defaultValue="0") int q7, @RequestParam(defaultValue="0") int q8, @RequestParam(defaultValue="0") int q9, @RequestParam(defaultValue="0") int opt1, @RequestParam(defaultValue="0") int opt2, @RequestParam(defaultValue="0") int opt3, @RequestParam(defaultValue="0") int opt4, @RequestParam(defaultValue="0") int opt5, @RequestParam(defaultValue="0") int opt6, @RequestParam(defaultValue="0") int opt7, @RequestParam(defaultValue="0") int opt8, @RequestParam(defaultValue="0") int opt9, Model model) {
        
        int baseScore = q1 + q2 + q3 + q4 + q5 + q6 + q7 + q8 + q9;
        int extraScore = opt1 + opt2 + opt3 + opt4 + opt5 + opt6 + opt7 + opt8 + opt9;
        int totalScore = baseScore + extraScore;
        int maxPossible = (extraScore > 0) ? 54 : 27;

        String status = (totalScore / (double) maxPossible <= 0.15) ? "Low Stress" :
                        (totalScore / (double) maxPossible <= 0.35) ? "Mild Stress" :
                        (totalScore / (double) maxPossible <= 0.60) ? "Moderate Stress" : "High Stress";
        String feedbackText = "Based on your responses, you are experiencing " + status;
        String badgeClass = status.equals("High Stress") ? "bg-danger text-white" : "bg-success-subtle text-success";

        AssessmentResult result = new AssessmentResult();
        result.setStudentId(1L);
        result.setScore((double) totalScore);
        result.setFeedback(status + ": " + feedbackText);
        result.setRawAnswers(q1+","+q2+","+q3+","+q4+","+q5+","+q6+","+q7+","+q8+","+q9);
        assessmentRepository.save(result);

        model.addAttribute("score", totalScore);
        model.addAttribute("maxScore", maxPossible);
        model.addAttribute("status", status);
        model.addAttribute("feedbackText", feedbackText);
        model.addAttribute("badgeClass", badgeClass);
        model.addAttribute("name", "Student");
        model.addAttribute("date", java.time.LocalDate.now());
        model.addAttribute("downloadData", "Score: " + totalScore);

        return "student/assessment_result";
    }

    @GetMapping("/assessment/history")
    public String showAssessmentHistory(Model model) {
        model.addAttribute("history", assessmentRepository.findByStudentId(1L));
        return "student/history";
    }

    @GetMapping("/assessment/delete/{id}")
    public String deleteAssessment(@PathVariable("id") Long id) {
        assessmentRepository.deleteById(id);
        return "redirect:/assessment/history";
    }

    @GetMapping("/assessment/edit/{id}")
    public String showEditAssessmentForm(@PathVariable("id") Long id, Model model) {
        AssessmentResult result = assessmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("result", result);
        return "student/edit_assessment";
    }

    @PostMapping("/assessment/update/{id}")
    public String updateAssessment(@PathVariable("id") Long id, @RequestParam("score") Double score, @RequestParam("feedback") String feedback) {
        AssessmentResult result = assessmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        result.setScore(score);
        result.setFeedback(feedback);
        assessmentRepository.save(result);
        return "redirect:/assessment/history";
    }
}