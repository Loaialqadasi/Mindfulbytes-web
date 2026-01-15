package com.project.Mental.service;
import java.util.ArrayList;
import java.util.List;
import com.project.Mental.model.User;
import com.project.Mental.model.MoodEntry; // If exists, otherwise remove
public class DataStore {
    public static final List<User> users = new ArrayList<>();
    // public static final List<MoodEntry> moods = new ArrayList<>();
    static {
        users.add(new User("100", "Admin", "Admin", "Active"));
    }
}