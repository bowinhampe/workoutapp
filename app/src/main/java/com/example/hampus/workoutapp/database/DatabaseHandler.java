package com.example.hampus.workoutapp.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 4/7/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";

    // Tables
    public static final String TABLE_WORKOUTS = "Workouts";
    public static final String WORKOUTS_COLUMN_ID = "id";
    public static final String WORKOUTS_COLUMN_NAME = "name";

    public static final String TABLE_EXERCISES = "Exercises";
    public static final String EXERCISES_COLUMN_ID = "id";
    public static final String EXERCISES_COLUMN_NAME = "name";
    public static final String EXERCISES_COLUMN_CATEGORY = "category";
    public static final String EXERCISES_COLUMN_MUSCLEGROUP = "muscleGroup";
    public static final String EXERCISES_COLUMN_DESCRIPTION = "description";

    public static final String TABLE_WORKOUTEXERCISES = "WorkoutExercises";
    public static final String WORKOUTEXERCISES_COLUMN_ID = "id";
    public static final String WORKOUTEXERCISES_COLUMN_WORKOUTID = "workoutId";
    public static final String WORKOUTEXERCISES_COLUMN_EXERCISEID = "exerciseId";

    public static final String TABLE_WORKOUTSESSION = "WorkoutSession";
    public static final String WORKOUTSESSION_COLUMN_ID ="id";
    public static final String WORKOUTSESSION_COLUMN_WORKOUTEXERCISES_ID="WorkoutExercisesId";
    public static final String WORKOUTSESSION_COLUMN_DATE="Date";
    public static final String WORKOUTSESSION_COLUMN_SETS="Sets";
    public static final String WORKOUTSESSION_COLUMN_REPS="Reps";
    public static final String WORKOUTSESSION_COLUMN_WEIGHT="Weight";

    // Sql statement used when creating database
    private static final String CREATE_TABLE_WORKOUTS = "create table " + TABLE_WORKOUTS + "("
            + WORKOUTS_COLUMN_ID + " integer primary key autoincrement, "
            + WORKOUTS_COLUMN_NAME + " text    not null"
            + ");";

    private static final String CREATE_TABLE_EXERCISES = "create table " + TABLE_EXERCISES + "("
            + EXERCISES_COLUMN_ID + " integer primary key autoincrement, "
            + EXERCISES_COLUMN_NAME + " text    not null    unique, "
            + EXERCISES_COLUMN_CATEGORY + " text    not null, "
            + EXERCISES_COLUMN_MUSCLEGROUP + " text, "
            + EXERCISES_COLUMN_DESCRIPTION + " text"
            + ");";

    private static final String CREATE_TABLE_WORKOUTEXERCISES = "create table " + TABLE_WORKOUTEXERCISES + "("
            + WORKOUTEXERCISES_COLUMN_ID + " integer primary key autoincrement, "
            + WORKOUTEXERCISES_COLUMN_WORKOUTID + " integer not null, "
            + WORKOUTEXERCISES_COLUMN_EXERCISEID + " integer not null, "
            + "foreign key(" + WORKOUTEXERCISES_COLUMN_WORKOUTID + ") " + "references " + TABLE_WORKOUTS + "(" + WORKOUTS_COLUMN_ID + "), "
            + "foreign key(" + WORKOUTEXERCISES_COLUMN_EXERCISEID + ") " + "references " + TABLE_EXERCISES + "(" + EXERCISES_COLUMN_ID + ")"
            + ");";

    private static final String CREATE_TABLE_WORKOUTSESSION = "create table "+ TABLE_WORKOUTSESSION + "("
            +WORKOUTSESSION_COLUMN_ID+ " integer primary key autoincrement, "
            +"foreign key(" +WORKOUTSESSION_COLUMN_WORKOUTEXERCISES_ID +") references "+TABLE_WORKOUTEXERCISES+ "("+ WORKOUTEXERCISES_COLUMN_ID+"), "
            +WORKOUTSESSION_COLUMN_DATE +" text not null, "
            +WORKOUTSESSION_COLUMN_SETS +" integer, "
            +WORKOUTSESSION_COLUMN_REPS + " integer , "
            +WORKOUTSESSION_COLUMN_WEIGHT +" integer"
            +");";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_EXERCISES);
        db.execSQL(CREATE_TABLE_WORKOUTEXERCISES);
        db.execSQL(CREATE_TABLE_WORKOUTSESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_WORKOUTS);
        db.execSQL("drop table if exists " + TABLE_EXERCISES);
        db.execSQL("drop table if exists " + TABLE_WORKOUTEXERCISES);
        db.execSQL("drop table if exists " + TABLE_WORKOUTSESSION);
        onCreate(db);
    }
}
