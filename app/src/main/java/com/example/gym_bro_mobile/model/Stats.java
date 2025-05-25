package com.example.gym_bro_mobile.model;

import java.util.List;
import java.util.Objects;

public class Stats {
    ExerciseSet mostRepsSet;
    ExerciseSet leastRepsSet;
    ExerciseSet mostWeightSet;
    ExerciseSet leastWeightSet;
    ExerciseSet newestSet;
    ExerciseSet oldestSet;
    List<ExerciseSet> plotData;

    public Stats(ExerciseSet mostRepsSet, ExerciseSet leastRepsSet, ExerciseSet mostWeightSet, ExerciseSet leastWeightSet, ExerciseSet newestSet, ExerciseSet oldestSet, List<ExerciseSet> plotData) {
        this.mostRepsSet = mostRepsSet;
        this.leastRepsSet = leastRepsSet;
        this.mostWeightSet = mostWeightSet;
        this.leastWeightSet = leastWeightSet;
        this.newestSet = newestSet;
        this.oldestSet = oldestSet;
        this.plotData = plotData;
    }

    public Stats() {
    }

    public ExerciseSet getMostRepsSet() {
        return mostRepsSet;
    }

    public void setMostRepsSet(ExerciseSet mostRepsSet) {
        this.mostRepsSet = mostRepsSet;
    }

    public ExerciseSet getLeastRepsSet() {
        return leastRepsSet;
    }

    public void setLeastRepsSet(ExerciseSet leastRepsSet) {
        this.leastRepsSet = leastRepsSet;
    }

    public ExerciseSet getMostWeightSet() {
        return mostWeightSet;
    }

    public void setMostWeightSet(ExerciseSet mostWeightSet) {
        this.mostWeightSet = mostWeightSet;
    }

    public ExerciseSet getLeastWeightSet() {
        return leastWeightSet;
    }

    public void setLeastWeightSet(ExerciseSet leastWeightSet) {
        this.leastWeightSet = leastWeightSet;
    }

    public ExerciseSet getNewestSet() {
        return newestSet;
    }

    public void setNewestSet(ExerciseSet newestSet) {
        this.newestSet = newestSet;
    }

    public ExerciseSet getOldestSet() {
        return oldestSet;
    }

    public void setOldestSet(ExerciseSet oldestSet) {
        this.oldestSet = oldestSet;
    }

    public List<ExerciseSet> getPlotData() {
        return plotData;
    }

    public void setPlotData(List<ExerciseSet> plotData) {
        this.plotData = plotData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return Objects.equals(mostRepsSet, stats.mostRepsSet) && Objects.equals(leastRepsSet, stats.leastRepsSet) && Objects.equals(mostWeightSet, stats.mostWeightSet) && Objects.equals(leastWeightSet, stats.leastWeightSet) && Objects.equals(newestSet, stats.newestSet) && Objects.equals(oldestSet, stats.oldestSet) && Objects.equals(plotData, stats.plotData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mostRepsSet, leastRepsSet, mostWeightSet, leastWeightSet, newestSet, oldestSet, plotData);
    }
}
