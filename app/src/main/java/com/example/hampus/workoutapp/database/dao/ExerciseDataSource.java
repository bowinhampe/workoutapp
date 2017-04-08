package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.hampus.workoutapp.database.DatabaseHandler;
import com.example.hampus.workoutapp.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/7/2017.
 */

public class ExerciseDataSource extends DataSource{

    public ExerciseDataSource(Context context) {
        super(context);

        String[] columns = { DatabaseHandler.EXERCISES_COLUMN_ID,
                DatabaseHandler.EXERCISES_COLUMN_NAME,
                DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION };
        this.setColumns(columns);
    }

    public Exercise createExercise(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.EXERCISES_COLUMN_NAME, name);
        values.put(DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION, description);

        long insertId = this.getDb().insert(DatabaseHandler.TABLE_EXERCISES, null,
                values);

        Exercise newExercise = null;
        if (insertId != -1) {
            Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_EXERCISES,
                    this.getColumns(), DatabaseHandler.EXERCISES_COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newExercise = cursorToExercise(cursor);
            cursor.close();
        }

        return newExercise;
    }

    public void deleteExercise(Exercise exercise) {
        long id = exercise.getId();
        this.getDb().delete(DatabaseHandler.TABLE_EXERCISES, DatabaseHandler.EXERCISES_COLUMN_ID
                + " = " + id, null);
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();

        Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_EXERCISES,
                this.getColumns(), null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return exercises;
    }

    private Exercise cursorToExercise(Cursor cursor) {
        Exercise exercise = new Exercise();
        exercise.setId(cursor.getLong(0));
        exercise.setName(cursor.getString(1));
        exercise.setDescription(cursor.getString(2));
        return exercise;
    }
}
