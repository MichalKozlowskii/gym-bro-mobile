package com.example.gym_bro_mobile.rv.exercise;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gym_bro_mobile.databinding.RvExerciseBinding;
import com.example.gym_bro_mobile.model.Exercise;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.time.format.DateTimeFormatter;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    private final RvExerciseBinding binding;
    private final OnExerciseClickListener listener;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ExerciseViewHolder(RvExerciseBinding binding, OnExerciseClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Exercise exercise) {
        binding.exerciseName.setText(exercise.getName());
        binding.creationDate.setText(exercise.getCreationDate().format(formatter));

        String videoId = extractYoutubeId(exercise.getDemonstrationUrl());
        if (videoId != null) {
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
            binding.youtubeThumbnail.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot().getContext())
                    .load(thumbnailUrl)
                    .into(binding.youtubeThumbnail);
        } else {
            binding.youtubeThumbnail.setVisibility(View.GONE);
        }

        // Set click listener for whole exercise item
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) listener.onExerciseClick(exercise);
        });

        // Set click listener for thumbnail only if videoId exists
        if (videoId != null) {
            binding.youtubeThumbnail.setOnClickListener(v -> {
                if (listener != null) listener.onThumbnailClick(exercise.getDemonstrationUrl());
            });
        } else {
            binding.youtubeThumbnail.setOnClickListener(null);
        }
    }

    private String extractYoutubeId(String url) {
        if (url == null || url.isEmpty()) return null;

        // Regex to grab video ID from various YouTube URL formats
        String pattern = "^(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([\\w-]{11}).*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}

