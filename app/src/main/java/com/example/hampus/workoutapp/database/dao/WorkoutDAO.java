package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.ContactsContract;

import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;
import com.example.hampus.workoutapp.database.DatabaseHandler;
import com.example.hampus.workoutapp.entities.WorkoutSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 4/7/2017.
 */

public class WorkoutDAO extends DAO {

    private static final String QUERY_WORKOUT = "select " + DatabaseHandler.TABLE_WORKOUTS + ".*, "
            + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_NAME + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_CATEGORY + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_MUSCLEGROUP + ", "
            + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_DESCRIPTION
            + " from " + DatabaseHandler.TABLE_WORKOUTS
            + " inner join " + DatabaseHandler.TABLE_WORKOUTEXERCISES
            + " on " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID + " = " + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_WORKOUTID
            + " inner join " + DatabaseHandler.TABLE_EXERCISES
            + " on " + DatabaseHandler.TABLE_WORKOUTEXERCISES + "." + DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID + " = " + DatabaseHandler.TABLE_EXERCISES + "." + DatabaseHandler.EXERCISES_COLUMN_ID;

    public WorkoutDAO(Context context) {
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
            for (Exercise exercise : exercises) {
                values = new ContentValues();
                values.put(DatabaseHandler.WORKOUTEXERCISES_COLUMN_WORKOUTID, workoutId);
                values.put(DatabaseHandler.WORKOUTEXERCISES_COLUMN_EXERCISEID, exercise.getId());
                this.getDb().insert(DatabaseHandler.TABLE_WORKOUTEXERCISES, null,
                        values);
            }
        }

        String query = WorkoutDAO.QUERY_WORKOUT + " where " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID + " = '" + workoutId + "'";
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

        Cursor cursor = this.getDb().rawQuery(WorkoutDAO.QUERY_WORKOUT, null);

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

    public List<Workout> getAllWorkoutNames() {
        List<Workout> workouts = new ArrayList<>();

        Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_WORKOUTS,
                this.getColumns(), null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkoutOnlyNames(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return workouts;
    }

    public Workout getWorkout(long id) {
        String query = WorkoutDAO.QUERY_WORKOUT + " where " + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID + " = '" + id + "'";
        Cursor cursor = this.getDb().rawQuery(query, null);
        cursor.moveToFirst();
        Workout workout = cursorToWorkout(cursor);
        // make sure to close the cursor
        cursor.close();

        return workout;
    }

    public List<WorkoutSession> createNewSession(long workoutId){
        List<WorkoutSession> sessionArray = new ArrayList<>();
        WorkoutSession toAdd = null;
        // TODO fetch all WorkoutExercise ids that belongs to WorkoutId
        String query = "SELECT "+DatabaseHandler.TABLE_WORKOUTEXERCISES+"."+DatabaseHandler.WORKOUTEXERCISES_COLUMN_ID+" FROM "
                + DatabaseHandler.TABLE_WORKOUTEXERCISES + " INNER JOIN "
                + DatabaseHandler.TABLE_WORKOUTS +" ON "
                + DatabaseHandler.TABLE_WORKOUTS + "." + DatabaseHandler.WORKOUTS_COLUMN_ID +" = "+ Long.toString(workoutId);
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMDD:hh:mm");
        Date dateOfDay = new Date();
        String dateToSet = date.format(dateOfDay);
        Cursor cursor = this.getDb().rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            toAdd = new WorkoutSession();
            ContentValues values = new ContentValues();
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_WORKOUTEXERCISES_ID, cursor.getLong(0));
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_DATE, dateToSet);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_REPS, 0);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_SETS, 0);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_WEIGHT,0);
            long workoutSessionID = this.getDb().insert(DatabaseHandler.WORKOUTSESSION_COLUMN_ID, null,
                    values);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_ID, workoutSessionID);
            cursor.moveToNext();
            toAdd.setId(workoutSessionID);
            toAdd.setDate(dateToSet);
            toAdd.setReps(0);
            toAdd.setSets(0);
            toAdd.setWeight(0);
            sessionArray.add(toAdd);
        }
        return sessionArray;

    }

    private Workout cursorToWorkout(Cursor cursor) {
        Workout workout = null;

        if (!cursor.isAfterLast()) {
            workout = new Workout();

            workout.setId(cursor.getLong(0));
            workout.setName(cursor.getString(1));

            long id;
            List<Exercise> exercises = new ArrayList<>();
            do {
                id = cursor.getLong(0);
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getLong(2));
                exercise.setName(cursor.getString(3));
                exercise.setCategory(cursor.getString(4));
                exercise.setMuscleGroup(cursor.getString(5));
                exercise.setDescription(cursor.getString(6));
                exercises.add(exercise);
                cursor.moveToNext();

            } while (!cursor.isAfterLast() && cursor.getLong(0) == id);
            cursor.moveToPrevious();

            workout.setExercises(exercises);
        }

        return workout;
    }

    private Workout cursorToWorkoutOnlyNames(Cursor cursor) {
        Workout workout = null;

        if (!cursor.isAfterLast()) {
            workout = new Workout();
            workout.setId(cursor.getLong(0));
            workout.setName(cursor.getString(1));
        }

        return workout;
    }
}
