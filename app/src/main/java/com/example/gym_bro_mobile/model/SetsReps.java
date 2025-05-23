package com.example.gym_bro_mobile.model;

public class SetsReps {
    private Integer sets;
    private Integer reps;

    public SetsReps(Integer sets, Integer reps) {
        this.sets = sets;
        this.reps = reps;
    }

    public SetsReps() {
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }
}
