package com.example.hampus.workoutapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.hampus.workoutapp.database.dao.WorkoutDAO;
import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;
import com.example.hampus.workoutapp.entities.WorkoutSession;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends ListActivity {
    private WorkoutDAO workoutDAO;
    private long workoutId;
    private List<WorkoutSession> mWorkoutSessions;

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
        mWorkoutSessions = new ArrayList<>();
        this.initiateDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_create_workout:
                Intent intent = new Intent(this, CreateWorkoutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_developer_options:
                // TODO: add;
                return true;
            case R.id.menu_graphs:
                // TODO: add;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        this.workoutDAO.createNewSession(this.workoutId);
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