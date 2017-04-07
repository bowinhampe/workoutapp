package com.example.hampus.workoutapp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.hampus.workoutapp.database.dao.WorkoutDataSource;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends ListActivity {
    private WorkoutDataSource workoutDataSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        this.workoutDataSrc = new WorkoutDataSource(this);
        this.workoutDataSrc.open();

        // Loading the database with data
        workoutDataSrc.createWorkout("Heavy liftin", "Flex them muscles.");
        workoutDataSrc.createWorkout("Running", "Run fast af.");

        List<Workout> workouts = this.workoutDataSrc.getAllWorkouts();

        List<String> strings = new ArrayList<>();
        for(Workout workout: workouts){
            strings.add(workout.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        this.workoutDataSrc.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.workoutDataSrc.close();
        super.onPause();
    }
}
