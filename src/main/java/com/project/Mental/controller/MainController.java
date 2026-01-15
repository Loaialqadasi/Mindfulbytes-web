package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "student/dashboard";
    }
}