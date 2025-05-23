package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ExerciseSet {
    private Long id;
    private Exercise exercise;
    private Long workoutId;
    private Long userId;
    private Double weight;
    private Integer reps;
    private LocalDateTime creationDate;

    public ExerciseSet() {
    }

    public ExerciseSet(Long id, Exercise exercise, Long workoutId, Long userId, Double weight, Integer reps, LocalDateTime creationDate) {
        this.id = id;
        this.exercise = exercise;
        this.workoutId = workoutId;
        this.userId = userId;
        this.weight = weight;
        this.reps = reps;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
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
        ExerciseSet that = (ExerciseSet) o;
        return Objects.equals(id, that.id) && Objects.equals(exercise, that.exercise) && Objects.equals(workoutId, that.workoutId) && Objects.equals(userId, that.userId) && Objects.equals(weight, that.weight) && Objects.equals(reps, that.reps) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, workoutId, userId, weight, reps, creationDate);
    }
}
