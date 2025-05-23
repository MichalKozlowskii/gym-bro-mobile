package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Workout {
    private Long id;
    private WorkoutPlan workoutPlan;
    private Map<Exercise, List<ExerciseSet>> exerciseSetMap;
    private LocalDateTime creationDate;

    public Workout(Long id, WorkoutPlan workoutPlan, Map<Exercise, List<ExerciseSet>> exerciseSetMap, LocalDateTime creationDate) {
        this.id = id;
        this.workoutPlan = workoutPlan;
        this.exerciseSetMap = exerciseSetMap;
        this.creationDate = creationDate;
    }

    public Workout() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public Map<Exercise, List<ExerciseSet>> getExerciseSetMap() {
        return exerciseSetMap;
    }

    public void setExerciseSetMap(Map<Exercise, List<ExerciseSet>> exerciseSetMap) {
        this.exerciseSetMap = exerciseSetMap;
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
        Workout workout = (Workout) o;
        return Objects.equals(id, workout.id) && Objects.equals(workoutPlan, workout.workoutPlan) && Objects.equals(exerciseSetMap, workout.exerciseSetMap) && Objects.equals(creationDate, workout.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workoutPlan, exerciseSetMap, creationDate);
    }
}
