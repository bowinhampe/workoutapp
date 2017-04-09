package com.example.hampus.workoutapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.hampus.workoutapp.database.dao.WorkoutDAO;
import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends ListActivity {
    private WorkoutDAO workoutDAO;
    private long workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                workoutId = -1;
            } else {
                workoutId = extras.getLong("WorkoutId");
            }
        } else {
            workoutId = savedInstanceState.getLong("WorkoutId");
        }
        Log.d("DEBUG", Long.toString(workoutId));
        this.initiateDB();
    }

    private void initiateDB() {
        // TODO: Get Workout Exercises depending on workoutName
        this.workoutDAO = new WorkoutDAO(this);
        this.workoutDAO.open();

        Workout workout = this.workoutDAO.getWorkout(this.workoutId);

        List<String> exerciseNames = new ArrayList<>();
        for (Exercise e : workout.getExercises())
            exerciseNames.add(e.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        this.workoutDAO.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.workoutDAO.close();
        super.onPause();
    }
}