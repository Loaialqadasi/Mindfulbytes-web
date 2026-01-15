package com.project.Mental.model;
import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    private String id;
    private String title;
    private String type; 
    private String description;
    private String duration;
    private int toolkitId;
    private String fileName; 

    public Resource() {}
    public Resource(String id, String title, String type, String description, String duration, int toolkitId, String fileName) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.toolkitId = toolkitId;
        this.fileName = fileName;
    }
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public int getToolkitId() { return toolkitId; }
    public void setToolkitId(int toolkitId) { this.toolkitId = toolkitId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}