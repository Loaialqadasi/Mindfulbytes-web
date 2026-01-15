package com.project.Mental.config;

import com.project.Mental.model.User;
import com.project.Mental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Create Default Admin (If not exists)
        if (userRepository.findByName("admin").isEmpty()) {
            User admin = new User("1", "admin", "Admin", "Active", "admin123");
            userRepository.save(admin);
            System.out.println("✅ ADMIN CREATED: User: admin | Pass: admin123");
        }

        // 2. Create Default Counselor (If not exists)
        if (userRepository.findByName("counselor").isEmpty()) {
            User counselor = new User("2", "counselor", "Counselor", "Active", "counselor123");
            userRepository.save(counselor);
            System.out.println("✅ COUNSELOR CREATED: User: counselor | Pass: counselor123");
        }

        // 3. Create Default Student (If not exists)
        if (userRepository.findByName("student").isEmpty()) {
            User student = new User("3", "student", "Student", "Active", "student123");
            userRepository.save(student);
            System.out.println("✅ STUDENT CREATED: User: student | Pass: student123");
        }

        // 4. Create Specific Students (From teammate's list)
        createStudentIfMissing("Khalid Ahmed", "A23CS0001");
        createStudentIfMissing("Sarah Lee", "A23CS0002");
        createStudentIfMissing("Youssef Badr", "A23CS0003");
    }

    private void createStudentIfMissing(String name, String password) {
        if (userRepository.findByName(name).isEmpty()) {
            // Generate a random ID
            String id = UUID.randomUUID().toString();
            User u = new User(id, name, "Student", "Active", password);
            userRepository.save(u);
            System.out.println("Created Student: " + name);
        }
    }
}