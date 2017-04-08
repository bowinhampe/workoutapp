package com.example.hampus.workoutapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.hampus.workoutapp.database.dao.ExerciseDataSource;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends ListActivity {
    private ExerciseDataSource exerciseDataSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        this.exerciseDataSrc = new ExerciseDataSource(this);
        this.exerciseDataSrc.open();

        // Loading the database with data
        exerciseDataSrc.createExercise("Heavy liftin", "Flex them muscles.");
        exerciseDataSrc.createExercise("Running", "Run fast af.");

        List<Exercise> exercises = this.exerciseDataSrc.getAllExercises();

        List<String> strings = new ArrayList<>();
        for(Exercise exercise : exercises){
            strings.add(exercise.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        this.exerciseDataSrc.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.exerciseDataSrc.close();
        super.onPause();
    }
}
