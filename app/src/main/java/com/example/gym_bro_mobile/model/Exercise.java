package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Exercise {
    private Long id;
    private String name;
    private String demonstrationUrl;
    private LocalDateTime creationDate;

    public Exercise(Long id, String name, String demonstrationUrl, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.demonstrationUrl = demonstrationUrl;
        this.creationDate = creationDate;
    }

    public Exercise() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) && Objects.equals(name, exercise.name) && Objects.equals(demonstrationUrl, exercise.demonstrationUrl) && Objects.equals(creationDate, exercise.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, demonstrationUrl, creationDate);
    }
}
