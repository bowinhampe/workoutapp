package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.hampus.workoutapp.database.DatabaseHandler;
import com.example.hampus.workoutapp.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/7/2017.
 */

public class ExerciseDAO extends DAO {

    public ExerciseDAO(Context context) {
        super(context);

        String[] columns = {DatabaseHandler.EXERCISES_COLUMN_ID,
                DatabaseHandler.EXERCISES_COLUMN_NAME,
                DatabaseHandler.EXERCISES_COLUMN_CATEGORY,
                DatabaseHandler.EXERCISES_COLUMN_MUSCLEGROUP,
                DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION};
        this.setColumns(columns);
    }

    public Exercise createExercise(String name, String category, String muscleGroup, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.EXERCISES_COLUMN_NAME, name);
        values.put(DatabaseHandler.EXERCISES_COLUMN_CATEGORY, category);
        values.put(DatabaseHandler.EXERCISES_COLUMN_MUSCLEGROUP, muscleGroup);
        values.put(DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION, description);

        long insertId = this.getDb().insert(DatabaseHandler.TABLE_EXERCISES, null,
                values);

        Exercise exercise = null;
        if (insertId != -1) {
            Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_EXERCISES,
                    this.getColumns(), DatabaseHandler.EXERCISES_COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                exercise = cursorToExercise(cursor);
            }
            cursor.close();
        }

        return exercise;
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
        cursor.close();

        return exercises;
    }

    public Exercise getExerciseByName(String name) {
        Exercise exercise = null;

        Cursor cursor = this.getDb().rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_EXERCISES + " WHERE " + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_NAME + " = '" + name + "'", null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            exercise = cursorToExercise(cursor);
        }

        return exercise;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT DISTINCT " + DatabaseHandler.EXERCISES_COLUMN_CATEGORY + " FROM " + DatabaseHandler.TABLE_EXERCISES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(cursor.getString(cursor.getColumnIndex(DatabaseHandler.EXERCISES_COLUMN_CATEGORY)));
            cursor.moveToNext();
        }
        cursor.close();

        return categories;
    }

    public ArrayList<String> getAllExerciseNamesByCategory(String category) {
        ArrayList<String> exerciseNames = new ArrayList<>();
        String query = "SELECT " + DatabaseHandler.EXERCISES_COLUMN_NAME + " FROM " + DatabaseHandler.TABLE_EXERCISES + " WHERE " + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_CATEGORY + " = '" + category + "'";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // TODO: return Exercise instead to show description etc?
            exerciseNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHandler.EXERCISES_COLUMN_NAME)));
            cursor.moveToNext();
        }
        cursor.close();

        return exerciseNames;
    }

    private Exercise cursorToExercise(Cursor cursor) {
        Exercise exercise = new Exercise();
        exercise.setId(cursor.getLong(0));
        exercise.setName(cursor.getString(1));
        exercise.setCategory(cursor.getString(2));
        exercise.setMuscleGroup(cursor.getString(3));
        exercise.setDescription(cursor.getString(4));

        return exercise;
    }
}
