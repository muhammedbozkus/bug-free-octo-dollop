package com.example.newmusclelog.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistory {

    private static final String JSON_FILENAME = "workout_history.json";
    private File historyPath;
    private Gson gson;

    public WorkoutHistory(File directory) throws IOException {
        historyPath = new File(directory, JSON_FILENAME);

        if (!historyPath.isFile())
        {
            // create file if it doesn't exist
            FileWriter fileWriter = new FileWriter(historyPath);
        }
        gson = new Gson();
    }

    public void write(List<Workout> workoutList) throws IOException {
        FileWriter fileWriter = new FileWriter(historyPath);
            gson.toJson(workoutList, fileWriter);
    }

    public List<Workout> read() throws FileNotFoundException {

        FileReader fileReader = new FileReader(historyPath);
        Type listType = new TypeToken<List<Workout>>(){}.getType();

        return gson.fromJson(fileReader, listType);
    }

    public void addWorkout(Workout workoutToAdd) throws IOException {
        List<Workout> list = read();

        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(workoutToAdd);
        write(list);
    }

    public void updateWorkout(Workout workoutToUpdate) throws IOException {
        List<Workout> list = read();
        if (list == null)
        {
            addWorkout(workoutToUpdate);
            list = read();
        }

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getWorkoutName().equals(workoutToUpdate.getWorkoutName()))
            {
                list.set(i, workoutToUpdate);
                break;
            }
        }
        write(list);
    }

    public void removeWorkout(Workout workoutToRemove) throws IOException {
        List<Workout> list = read();

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getWorkoutName().equals(workoutToRemove.getWorkoutName()))
            {
                list.remove(i);
                break;
            }
        }
        write(list);
    }

    public List<Workout> getWorkouts() throws FileNotFoundException {
        return read();
    }

    public String[] getWorkoutNames() throws FileNotFoundException {
        List<Workout> workoutList = read();
        List<String> names = new ArrayList<>();

        for (Workout workout : workoutList)
        {
            names.add(workout.getWorkoutName());
        }
        return names.toArray(new String[0]);
    }
}
