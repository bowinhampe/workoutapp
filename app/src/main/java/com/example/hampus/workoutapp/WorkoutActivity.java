package com.example.hampus.workoutapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.hampus.workoutapp.database.dao.WorkoutDataSource;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends ListActivity {
    private WorkoutDataSource workoutDataSrc;
    private String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                workoutName= null;
            } else {
                workoutName= extras.getString("WorkoutName");
            }
        } else {
            workoutName= (String) savedInstanceState.getSerializable("WorkoutName");
        }
        Log.d("DEBUG",workoutName);
        this.initiateDB();
    }

    private void initiateDB(){
        this.workoutDataSrc = new WorkoutDataSource(this);
        this.workoutDataSrc.open();

        Workout workout = this.workoutDataSrc.getWorkout(this.workoutName);

        List<String> exerciseNames = new ArrayList<>();
        for (Exercise e: workout.getExercises())
            exerciseNames.add(e.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
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