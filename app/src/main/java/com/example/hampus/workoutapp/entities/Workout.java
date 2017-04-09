package com.example.hampus.workoutapp.entities;

import java.util.List;

/**
 * Created by Daniel on 4/8/2017.
 */

public class Workout {
    private long id;
    private String name;
    private List<Exercise> exercises;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
