package com.example.taskreminder.entity;

import java.time.LocalDateTime;

public class Task {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime time;
    private String status;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}