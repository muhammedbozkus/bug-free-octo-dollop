package com.example.newmusclelog;

import com.example.newmusclelog.data.Exercise;

public class ExerciseElement {

    private String bodypart;
    private String title;

    public ExerciseElement() {

    }

    public ExerciseElement(String bodypart, String title) {
        this.bodypart = bodypart;
        this.title = title;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
