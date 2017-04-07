package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.hampus.workoutapp.database.DatabaseHandler;
import com.example.hampus.workoutapp.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/7/2017.
 */

public class WorkoutDataSource {
    private SQLiteDatabase db;
    private DatabaseHandler dbHelper;
    private String[] columns = { DatabaseHandler.WORKOUTS_COLUMN_ID,
            DatabaseHandler.WORKOUTS_COLUMN_NAME,
            DatabaseHandler.WORKOUTS_COLUMN_DESCRIPTION };

    public WorkoutDataSource(Context context) {
        this.dbHelper = new DatabaseHandler(context);
    }

    public void open() throws SQLException {
        this.db = this.dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public Workout createWorkout(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.WORKOUTS_COLUMN_NAME, name);
        values.put(DatabaseHandler.WORKOUTS_COLUMN_DESCRIPTION, description);

        long insertId = this.db.insert(DatabaseHandler.TABLE_WORKOUTS, null,
                values);

        Cursor cursor = this.db.query(DatabaseHandler.TABLE_WORKOUTS,
                this.columns, DatabaseHandler.WORKOUTS_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Workout newWorkout = cursorToWorkout(cursor);
        cursor.close();
        return newWorkout;
    }

    public void deleteWorkout(Workout workout) {
        long id = workout.getId();
        this.db.delete(DatabaseHandler.TABLE_WORKOUTS, DatabaseHandler.WORKOUTS_COLUMN_ID
                + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = this.db.query(DatabaseHandler.TABLE_WORKOUTS,
                this.columns, null, null, null, null, null);

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
        Workout workout= new Workout();
        workout.setId(cursor.getLong(0));
        workout.setName(cursor.getString(1));
        workout.setDescription(cursor.getString(2));
        return workout;
    }
}
