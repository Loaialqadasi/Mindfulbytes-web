package com.project.Mental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mood_entries")
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;
    private int moodLevel;
    private String date;
    private String username;

    public MoodEntry() {
    }

    public MoodEntry(String note, int moodLevel, String date, String username) {
        this.note = note;
        this.moodLevel = moodLevel;
        this.date = date;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // New Getters/Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}