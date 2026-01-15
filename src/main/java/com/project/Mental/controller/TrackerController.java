package com.project.Mental.controller;

import com.project.Mental.model.MoodEntry;
import com.project.Mental.repository.MoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TrackerController {

    @Autowired
    private MoodRepository moodRepository;


    @GetMapping("/student/tracker")
    public String showTracker(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (user == null)
            return "redirect:/login";
        if ("Admin".equals(role))
            return "redirect:/admin/dashboard";
        if ("Counselor".equals(role))
            return "redirect:/login";

        model.addAttribute("username", user);
        List<MoodEntry> entries = moodRepository.findByUsername(user);
        model.addAttribute("entries", entries);
        return "student/tracker";
    }

    @PostMapping("/student/tracker/add")
    public String addMood(@RequestParam int moodLevel, @RequestParam String note, HttpSession session) {
        String user = (String) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (user == null)
            return "redirect:/login";
        if (!"Student".equals(role))
            return "redirect:/admin/dashboard";

        String today = java.time.LocalDate.now().toString();
        MoodEntry newEntry = new MoodEntry(note, moodLevel, today, user);
        moodRepository.save(newEntry);

        return "redirect:/student/tracker";
    }
}