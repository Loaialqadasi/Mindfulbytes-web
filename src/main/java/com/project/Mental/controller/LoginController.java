package com.project.Mental.controller;

import com.project.Mental.model.User;
import com.project.Mental.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // --- LOGIN LOGIC ---

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {

        // 1. Database User Check
        // We filter by name (Case Insensitive for better UX)
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> u.getName().equalsIgnoreCase(username))
                .findFirst();

        if (userOpt.isPresent()) {
            User dbUser = userOpt.get();

            // Check Password
            if (dbUser.getPassword().equals(password)) {

                // Check if Banned
                if ("Banned".equalsIgnoreCase(dbUser.getStatus())) {
                    return "redirect:/login?error=banned";
                }

                // SUCCESS
                session.setAttribute("user", dbUser.getName());
                session.setAttribute("role", dbUser.getRole());

                // Redirect based on Role
                if ("Admin".equalsIgnoreCase(dbUser.getRole())) {
                    return "redirect:/admin/dashboard";
                } else if ("Counselor".equalsIgnoreCase(dbUser.getRole())) {
                    return "redirect:/counselor/dashboard";
                } else {
                    return "redirect:/student/dashboard";
                }
            }
        }

        // If user not found OR password wrong -> Error
        return "redirect:/login?error=true";
    }

    // --- REGISTRATION LOGIC (NEW) ---

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username, @RequestParam String password) {

        // 1. Check if username already exists
        boolean exists = userRepository.findAll().stream()
                .anyMatch(u -> u.getName().equalsIgnoreCase(username));

        if (exists) {
            return "redirect:/register?error=duplicate";
        }

        // 2. Create New Student
        String newId = String.valueOf(System.currentTimeMillis()).substring(8);

        // Force Role to "Student" for self-registration security
        User newUser = new User(newId, username, "Student", "Active", password);

        userRepository.save(newUser);

        // 3. Redirect to Login
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}