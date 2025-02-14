package com.example.orphanage;

public class Event {
    private String title;
    private String description;
    private String date;
    private String time;
    private String category;

    // Constructor
    public Event(String title, String description, String date, String time, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
