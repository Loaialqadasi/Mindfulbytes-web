package com.project.Mental.service;

import com.project.Mental.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private List<User> userList = new ArrayList<>();

    // Initialize with some dummy data so the page isn't empty
    public AdminService() {
        userList.add(new User("U001", "Ali Ahmed", "Student", "Active"));
        userList.add(new User("U002", "Sarah Lee", "Counselor", "Active"));
        userList.add(new User("U003", "John Doe", "Student", "Banned"));
        userList.add(new User("U004", "Admin User", "Admin", "Active"));
        userList.add(new User("U005", "Fatima X", "Student", "Active"));
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public void addUser(String name, String role) {
        // Generate a random ID
        String newId = "U" + UUID.randomUUID().toString().substring(0, 4);
        userList.add(new User(newId, name, role, "Active"));
    }

    public void updateUserStatus(String userId, String newStatus) {
        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                user.setStatus(newStatus);
                break;
            }
        }
    }

    // --- Stats for Dashboard ---
    public int getTotalUsers() { return userList.size(); }
    
    public int getActiveUsers() {
        return (int) userList.stream().filter(u -> "Active".equals(u.getStatus())).count();
    }

    // Fake mood data for the chart (In real app, fetch from MoodTracker DB)
    public int getHappyCount() { return 15; }
    public int getNeutralCount() { return 8; }
    public int getSadCount() { return 5; }
}