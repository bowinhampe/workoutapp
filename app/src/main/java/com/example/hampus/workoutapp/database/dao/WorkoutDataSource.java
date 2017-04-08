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

/**
 * Created by Daniel on 4/7/2017.
 */

public class WorkoutDataSource extends DataSource {

    public WorkoutDataSource(Context context) {
        super(context);
        this.dbHandler = new DatabaseHandler(context);

        String[] columns = { DatabaseHandler.WORKOUTS_COLUMN_ID,
                DatabaseHandler.EXERCISES_COLUMN_NAME };
        this.setColumns(columns);
    }
    public void open() throws SQLException {
        this.db = this.dbHandler.getWritableDatabase();
        // Use with SD-Card db.openOrCreateDatabase
    }

    public Workout createWorkout(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.WORKOUTS_COLUMN_NAME, name);

        long insertId = this.getDb().insert(DatabaseHandler.TABLE_WORKOUTS, null,
                values);

        Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_WORKOUTS,
                this.getColumns(), DatabaseHandler.WORKOUTS_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Workout newWorkout = cursorToWorkout(cursor);
        cursor.close();
        return newWorkout;
    }

    public void deleteWorkout(Workout workout) {
        long id = workout.getId();
        this.getDb().delete(DatabaseHandler.TABLE_WORKOUTS, DatabaseHandler.WORKOUTS_COLUMN_ID
                + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();

        Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_WORKOUTS,
                this.getColumns(), null, null, null, null, null);

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

    private Workout cursorToWorkout(Cursor cursor) {
        Workout workout = new Workout();
        workout.setId(cursor.getLong(0));
        workout.setName(cursor.getString(1));
        return workout;
    }
}
