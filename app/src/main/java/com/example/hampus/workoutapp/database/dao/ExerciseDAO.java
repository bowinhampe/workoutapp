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
        Exercise exercise = null;

        if (!cursor.isAfterLast()) {
            exercise = new Exercise();
            exercise.setId(cursor.getLong(0));
            exercise.setName(cursor.getString(1));
            exercise.setCategory(cursor.getString(2));
            exercise.setMuscleGroup(cursor.getString(3));
            exercise.setDescription(cursor.getString(4));
        }

        return exercise;
    }

    public ArrayList<String> getAllCategorys(){
        Cursor c = db.rawQuery("SELECT DISTINCT "+DatabaseHandler.EXERCISES_COLUMN_CATEGORY+" FROM "+DatabaseHandler.TABLE_EXERCISES, null);
        ArrayList<String> allValues = new ArrayList<>();
        try {
            while(c.moveToNext()){
                allValues.add(c.getString(c.getColumnIndex(DatabaseHandler.EXERCISES_COLUMN_CATEGORY)));
            }
        } finally {
            c.close();
        }
        return allValues;
    }
    public ArrayList<String> getAllExerciseByCategory(String category){
        String query = "SELECT "+DatabaseHandler.EXERCISES_COLUMN_NAME+" FROM "+DatabaseHandler.TABLE_EXERCISES+" WHERE "+DatabaseHandler.TABLE_EXERCISES+"."+DatabaseHandler.EXERCISES_COLUMN_CATEGORY + " = "+"\""+category+"\"";
        Cursor c = db.rawQuery(query, null);
        ArrayList<String> allValues = new ArrayList<>();
        try {
            while(c.moveToNext()){
                // TODO: return Exercise instead to show description etc?
                allValues.add(c.getString(c.getColumnIndex(DatabaseHandler.EXERCISES_COLUMN_NAME)));
            }
        } finally {
            c.close();
        }
        return allValues;

    }
}
