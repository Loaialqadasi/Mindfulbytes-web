package com.project.Mental.controller;
import com.project.Mental.model.Resource;
import com.project.Mental.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class CommunityController {
    @Autowired private ResourceRepository resourceRepository;

  @GetMapping("/dashboard")
    public String showStudentDashboard(HttpSession session, Model model) {
        // 1. Get User from Session
        String user = (String) session.getAttribute("user");
        
        // 2. Security Check: If not logged in, go to login
        if (user == null) {
            return "redirect:/login";
        }

        // 3. Pass username to HTML for "Hello, [Name]"
        model.addAttribute("username", user);
        
        return "student/dashboard";
    }
    @GetMapping("/library") public String showLibraryPage() { return "student/library"; }
    @GetMapping("/chatbot") public String showChatbotPage() { return "student/chatbot"; }
    @GetMapping("/counselors") public String showCounselorsPage() { return "student/counselors"; }

    @GetMapping("/booking/{counselorId}")
    public String showBookingPage(@PathVariable("counselorId") Long counselorId, Model model) {
        String name = (counselorId == 1) ? "Dr. Nurul Aida" : (counselorId == 2) ? "Dr. Alex Park" : "Unknown";
        model.addAttribute("counselorName", name);
        return "student/booking";
    }

    @GetMapping("/toolkit/{toolkitId}")
    public String showToolkitPage(@PathVariable("toolkitId") int toolkitId, Model model) {
        String title = (toolkitId == 1) ? "Mindful Moments" : "Resilience Toolkit";
        model.addAttribute("toolkitTitle", title);
        model.addAttribute("resources", resourceRepository.findByToolkitId(toolkitId));
        return "student/toolkit";
    }
}