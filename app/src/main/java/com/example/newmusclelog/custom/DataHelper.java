package com.example.newmusclelog.custom;

import android.content.Context;

import com.example.newmusclelog.ExercisesFragment;
import com.example.newmusclelog.data.Exercise;
import com.example.newmusclelog.data.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataHelper {

    public static ArrayList<Exercise> loadExercise(ExercisesFragment context) throws IOException, JSONException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String json = "";

        InputStream is = context.getActivity().getAssets().open("data.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        JSONObject obj = new JSONObject(json);
        JSONArray jsonArray = obj.getJSONArray("exerciseList");

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Exercise exercise = new Exercise();
            exercise.setName(jsonObject.getString("title"));

            exercises.add(exercise);
        }
        return exercises;
    }
}
