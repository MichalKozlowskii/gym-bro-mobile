package com.example.gym_bro_mobile.rv;

import com.example.gym_bro_mobile.model.Exercise;

public interface OnExerciseClickListener {
    void onExerciseClick(Exercise exercise);
    void onThumbnailClick(String videoUrl);
}
