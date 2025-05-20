package com.example.gym_bro_mobile.model;

import java.time.LocalDateTime;
import java.util.List;

public class WorkoutPlan {
    private Long id;
    private String name;
    private List<Exercise> exercises;
    private List<SetsReps> setsReps;
    private LocalDateTime creationDate;
}
