package com.project.Mental.controller; // Make sure package matches your project

import com.project.Mental.model.Resource;
import com.project.Mental.model.User;
import com.project.Mental.model.MoodEntry; // Ensure you have this Model
import com.project.Mental.repository.UserRepository;
import com.project.Mental.repository.MoodRepository; // Ensure you have this Repo
import com.project.Mental.repository.ResourceRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private MoodRepository moodRepository;
    @Autowired private ResourceRepository resourceRepository; // YOUR WORK

    // --- Helper Method for Security ---
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "Admin".equals(role);
    }

    // ==========================================
    // 1. DASHBOARD
    // ==========================================
    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        String currentAdmin = (String) session.getAttribute("user");
        model.addAttribute("adminName", currentAdmin);

        List<User> users = userRepository.findAll();
        // Check if mood repo is empty to avoid crashes if no data exists yet
        List<MoodEntry> moods = moodRepository.findAll();

        model.addAttribute("totalUsers", users.size());
        
        long activeUsers = users.stream().filter(u -> "Active".equalsIgnoreCase(u.getStatus())).count();
        model.addAttribute("activeUsers", activeUsers);

        long happy = moods.stream().filter(m -> m.getMoodLevel() >= 4).count();
        long neutral = moods.stream().filter(m -> m.getMoodLevel() == 3).count();
        long sad = moods.stream().filter(m -> m.getMoodLevel() <= 2).count();

        model.addAttribute("happyCount", happy);
        model.addAttribute("neutralCount", neutral);
        model.addAttribute("sadCount", sad);
        model.addAttribute("userCount", users.size());

        return "admin/dashboard";
    }

    // ==========================================
    // 2. USER MANAGEMENT
    // ==========================================
    @GetMapping("/admin/users")
    public String showUserList(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("adminName", (String) session.getAttribute("user"));
        model.addAttribute("users", userRepository.findAll());
        return "admin/user_list";
    }

    @PostMapping("/admin/user/add")
    public String addUser(@RequestParam String name, @RequestParam String role, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        boolean exists = userRepository.findAll().stream().anyMatch(u -> u.getName().equalsIgnoreCase(name));
        if (exists) return "redirect:/admin/users?error=duplicate";

        String newId = String.valueOf(System.currentTimeMillis()).substring(8);
        // Using name as default password based on teammate's logic
        User newUser = new User(newId, name, role, "Active", name); 
        userRepository.save(newUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/ban")
    public String banUser(@RequestParam String userId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User targetUser = userOpt.get();
            String currentUser = (String) session.getAttribute("user");
            if (targetUser.getName().equalsIgnoreCase(currentUser)) return "redirect:/admin/users?error=selfban";

            targetUser.setStatus("Banned");
            userRepository.save(targetUser);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/unban")
    public String unbanUser(@RequestParam String userId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("Active");
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // ==========================================
    // 3. RESOURCE MANAGEMENT (YOUR WORK RESTORED)
    // ==========================================
    
    @GetMapping("/admin/resources")
    public String showResourcePage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login"; // Added security here too
        
        model.addAttribute("resources", resourceRepository.findAll());
        return "admin/resource_management";
    }

    @PostMapping("/admin/resource/add")
    public String addResource(@RequestParam String title, 
                              @RequestParam String type,
                              @RequestParam String duration,
                              @RequestParam int toolkitId,
                              @RequestParam String description,
                              @RequestParam("file") MultipartFile file,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        String fileName = null;
        if (!file.isEmpty()) {
            try {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) { e.printStackTrace(); }
        }
        
        // Simple ID generation for resources
        String id = String.valueOf(System.currentTimeMillis());
        Resource newRes = new Resource(id, title, type, description, duration, toolkitId, fileName);
        resourceRepository.save(newRes);
        
        return "redirect:/admin/resources";
    }

    @PostMapping("/admin/resource/delete")
    public String deleteResource(@RequestParam String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        resourceRepository.deleteById(id);
        return "redirect:/admin/resources";
    }
    
    @GetMapping("/admin/resource/edit/{id}")
    public String showEditPage(@PathVariable("id") String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        Optional<Resource> result = resourceRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("resource", result.get());
            return "admin/resource_edit";
        }
        return "redirect:/admin/resources";
    }

    @PostMapping("/admin/resource/update")
    public String updateResource(@RequestParam String id,
                                 @RequestParam String title, 
                                 @RequestParam String type,
                                 @RequestParam String duration,
                                 @RequestParam int toolkitId,
                                 @RequestParam String description,
                                 @RequestParam String currentFileName, 
                                 @RequestParam("file") MultipartFile file,
                                 HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        String fileNameToSave = currentFileName;
        if (!file.isEmpty()) {
            try {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
                fileNameToSave = newFileName;
            } catch (IOException e) { e.printStackTrace(); }
        }

        Resource updatedRes = new Resource(id, title, type, description, duration, toolkitId, fileNameToSave);
        resourceRepository.save(updatedRes);
        
        return "redirect:/admin/resources";
    }
}