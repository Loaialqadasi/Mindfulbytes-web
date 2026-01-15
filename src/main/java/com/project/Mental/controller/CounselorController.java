package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CounselorController {

    // MODULE 5: Case Management Dashboard (UC010 & UC012)
    @GetMapping("/counselor/dashboard")
    public String dashboard(Model model) {
        // MOCK DATA: Simulating a database fetch
        // In Phase 3, we will replace this with a real Database call.
        List<Map<String, Object>> students = new ArrayList<>();
        
        students.add(Map.of("id", 1, "name", "Khalid Ahmed", "matric", "A23CS0001", "status", "High Risk", "score", 18));
        students.add(Map.of("id", 2, "name", "Sarah Lee", "matric", "A23CS0002", "status", "Stable", "score", 5));
        students.add(Map.of("id", 3, "name", "Youssef Badr", "matric", "A23CS0003", "status", "Attention Needed", "score", 12));

        model.addAttribute("counselorName", "Dr. Nurul Aida"); // Dummy logged-in counselor
        model.addAttribute("students", students);
        
        return "counselor/dashboard"; // Loads templates/counselor/dashboard.html
    }

    // MODULE 6: Session & Follow-up Management (UC011)
    @GetMapping("/counselor/student/{id}")
    public String viewStudentProfile(@PathVariable("id") int id, Model model) {
        // MOCK DATA for a specific student
        model.addAttribute("studentName", "Khalid Ahmed");
        model.addAttribute("studentId", id);
        
        // Mock History of Notes
        List<String> history = List.of(
            "10 Oct: Student reported trouble sleeping.",
            "02 Nov: Showing signs of improvement. Recommended meditation."
        );
        model.addAttribute("history", history);

        return "counselor/session_notes"; // Loads templates/counselor/session_notes.html
    }

    // Saving a new note (Phase 2 Demo Logic)
    @PostMapping("/counselor/note/save")
    public String saveNote(@RequestParam String noteContent, @RequestParam int studentId, Model model) {
        // In Phase 3, this will save to MySQL.
        // For Phase 2, we just reload the page and show a success message.
        
        model.addAttribute("success", "Note saved successfully!");
        model.addAttribute("studentName", "Khalid Ahmed"); // Keep dummy data consistent
        model.addAttribute("studentId", studentId);
        model.addAttribute("history", List.of(
            "10 Oct: Student reported trouble sleeping.",
            "02 Nov: Showing signs of improvement. Recommended meditation.",
            "Just Now: " + noteContent // Show the new note immediately
        ));
        
        return "counselor/session_notes";
    }
}