package com.example.newmusclelog.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Workout implements Parcelable {

    private List<Exercise> exercises = new ArrayList<>();
    private String workoutName;
    private String userID;
    private String first;

    public Workout() {

    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getFirst() {
        return first;
    }

    public String getUserID() {
        return userID;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Workout(String workoutName) {
        exercises = new ArrayList<>();
        this.workoutName = workoutName;
    }

    public Workout(String workoutName, String userID, List<Exercise> exercises) {
        this.exercises = exercises;
        this.workoutName = workoutName;
        this.userID = userID;
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

    @NonNull
    @Override
    public String toString() {
        return workoutName + exercises.toString();
    }
}
