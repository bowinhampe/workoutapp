package com.example.hampus.workoutapp.entities;

/**
 * Created by hampus on 2017-04-11.
 */

public class WorkoutSession {
    private long id;
    private String date;
    private int sets;
    private int reps;
    private int weight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }
}
