package com.example.newmusclelog.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Parcelable {

    private List<Exercise> exercises = new ArrayList<>();
    private String workoutName;

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public Workout(String workoutName) {
        exercises = new ArrayList<>();
        this.workoutName = workoutName;
    }

    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(exercises);
        dest.writeString(workoutName);
    }

    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    private Workout(Parcel in)
    {
        in.readList(exercises, getClass().getClassLoader());
        workoutName = in.readString();
    }
}
