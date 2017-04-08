package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hampus.workoutapp.Exercise;
import com.example.hampus.workoutapp.Workout;
import com.example.hampus.workoutapp.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by Daniel on 4/7/2017.
 */

public class WorkoutDataSource extends DataSource {

    private static final String WORKOUT_QUERY = "select " + DatabaseHandler.TABLE_WORKOUTS + ".*, "
            + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_NAME + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION
            + " from " + DatabaseHandler.TABLE_WORKOUTS
            + " inner join " + DatabaseHandler.TABLE_WORKOUTEXERCISES
            + " on " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID + " = " + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_WORKOUTID
            + " inner join " + DatabaseHandler.TABLE_EXERCISES
            + " on " + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID + " = " + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_ID;

    public WorkoutDataSource(Context context) {
        super(context);
        this.dbHandler = new DatabaseHandler(context);

        String[] columns = {DatabaseHandler.WORKOUTS_COLUMN_ID,
                DatabaseHandler.EXERCISES_COLUMN_NAME};
        this.setColumns(columns);
    }

    @Override
    public void open() throws SQLException {
        super.open();
        // Use with SD-Card db.openOrCreateDatabase
    }

    @Override
    public void close() {
        super.close();
    }

    public Workout createWorkout(String name, List<Exercise> exercises) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.WORKOUTS_COLUMN_NAME, name);

        long workoutId = this.getDb().insert(DatabaseHandler.TABLE_WORKOUTS, null,
                values);

        if (exercises != null) {
            for (Exercise exercise: exercises) {
                values = new ContentValues();
                values.put(DatabaseHandler.WORKOUTEXERCISES_COLUMN_WORKOUTID, workoutId);
                values.put(DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID, exercise.getId());
                this.getDb().insert(DatabaseHandler.TABLE_WORKOUTEXERCISES, null,
                        values);
            }
        }

        String query = WorkoutDataSource.WORKOUT_QUERY + " where " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID + " = '" + workoutId + "'";
        Cursor cursor = this.getDb().rawQuery(query, null);
        cursor.moveToFirst();
        Workout workout = this.cursorToWorkout(cursor);
        cursor.close();

        return workout;
    }

    public void deleteWorkout(Workout workout) {
        long id = workout.getId();
        this.getDb().delete(DatabaseHandler.TABLE_WORKOUTS, DatabaseHandler.WORKOUTS_COLUMN_ID
                + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();

        Cursor cursor = this.getDb().rawQuery(WorkoutDataSource.WORKOUT_QUERY, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return workouts;
    }

    public Workout getWorkout(String name) {
        String query = WorkoutDataSource.WORKOUT_QUERY + " where " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_NAME + " = '" + name + "'";
        Cursor cursor = this.getDb().rawQuery(query, null);
        cursor.moveToFirst();
        Workout workout = cursorToWorkout(cursor);
        // make sure to close the cursor
        cursor.close();

        return workout;
    }

    private Workout cursorToWorkout(Cursor cursor) {
        Workout workout = new Workout();
        workout.setId(cursor.getLong(0));
        workout.setName(cursor.getString(1));

        long id;
        List<Exercise> exercises = new ArrayList<>();
        do {
            id = cursor.getLong(0);
            Exercise exercise = new Exercise();
            exercise.setId(cursor.getLong(2));
            exercise.setName(cursor.getString(3));
            exercise.setDescription(cursor.getString(4));
            exercises.add(exercise);
            cursor.moveToNext();

        } while (!cursor.isAfterLast() && cursor.getLong(0) == id);
        cursor.moveToPrevious();

        workout.setExercises(exercises);

        return workout;
    }

}
