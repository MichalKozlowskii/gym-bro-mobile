package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class WorkoutPlan {
    private Long id;
    private String name;
    private List<Exercise> exercises;
    private List<SetsReps> setsReps;
    private LocalDateTime creationDate;

    public WorkoutPlan(Long id, String name, List<Exercise> exercises, List<SetsReps> setsReps, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.exercises = exercises;
        this.setsReps = setsReps;
        this.creationDate = creationDate;
    }

    public WorkoutPlan() {
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

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<SetsReps> getSetsReps() {
        return setsReps;
    }

    public void setSetsReps(List<SetsReps> setsReps) {
        this.setsReps = setsReps;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutPlan that = (WorkoutPlan) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(exercises, that.exercises) && Objects.equals(setsReps, that.setsReps) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, exercises, setsReps, creationDate);
    }
}
