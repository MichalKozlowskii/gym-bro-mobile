package com.example.gym_bro_mobile.gson;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.google.gson.*;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExerciseSet;
import com.example.gym_bro_mobile.model.Workout;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class WorkoutDeserializer implements JsonDeserializer<Workout> {

    @Override
    public Workout deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        Workout workout = new Workout();

        workout.setId(root.get("id").getAsLong());

        String creationDateStr = root.get("creationDate").getAsString();
        workout.setCreationDate(LocalDateTime.parse(creationDateStr));

        JsonElement workoutPlanElem = root.get("workoutPlanDTO");
        if (workoutPlanElem != null && !workoutPlanElem.isJsonNull()) {
            workout.setWorkoutPlan(context.deserialize(workoutPlanElem, WorkoutPlan.class));
        }

        JsonObject mapObj = root.getAsJsonObject("exerciseSetMap");
        if (mapObj != null) {
            Map<Exercise, List<ExerciseSet>> exerciseSetMap = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : mapObj.entrySet()) {
                String keyString = entry.getKey();
                Exercise exercise = parseExerciseFromString(keyString);

                JsonArray setsArray = entry.getValue().getAsJsonArray();
                List<ExerciseSet> sets = new ArrayList<>();
                for (JsonElement setElem : setsArray) {
                    ExerciseSet set = context.deserialize(setElem, ExerciseSet.class);
                    sets.add(set);
                }

                if (exercise != null) {
                    exerciseSetMap.put(exercise, sets);
                }
            }
            workout.setExerciseSetMap(exerciseSetMap);
        }

        return workout;
    }

    private Exercise parseExerciseFromString(String s) {
        Exercise ex = new Exercise();

        try {
            String idStr = extractValue(s, "id=");
            if (idStr != null) ex.setId(Long.parseLong(idStr));

            String name = extractValueString(s, "name=");
            ex.setName(name);

            String url = extractValueString(s, "demonstrationUrl=");
            ex.setDemonstrationUrl("null".equals(url) ? null : url);

            String creationDateStr = extractValueString(s, "creationDate=");
            if (creationDateStr != null) {
                ex.setCreationDate(LocalDateTime.parse(creationDateStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ex;
    }

    private String extractValue(String s, String key) {
        int start = s.indexOf(key);
        if (start == -1) return null;
        start += key.length();

        int end = s.indexOf(',', start);
        if (end == -1) end = s.indexOf(')', start);
        if (end == -1) return null;

        return s.substring(start, end).trim();
    }

    private String extractValueString(String s, String key) {
        // same as extractValue but for strings with no commas inside (no quotes)
        return extractValue(s, key);
    }
}

