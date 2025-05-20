package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;

public class Exercise {
    private Integer id;
    private String name;
    private String demonstrationUrl;
    private LocalDateTime creationDate;

    public Exercise(Integer id, String name, String demonstrationUrl, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.demonstrationUrl = demonstrationUrl;
        this.creationDate = creationDate;
    }

    public Exercise() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDemonstrationUrl() {
        return demonstrationUrl;
    }

    public void setDemonstrationUrl(String demonstrationUrl) {
        this.demonstrationUrl = demonstrationUrl;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", demonstrationUrl='" + demonstrationUrl + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
