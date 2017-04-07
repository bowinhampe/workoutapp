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
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String WORKOUTS_COLUMN_ID = "id";
    public static final String WORKOUTS_COLUMN_NAME = "name";
    public static final String WORKOUTS_COLUMN_DESCRIPTION = "description";

    // Sql statement used when creating database
    private static final String DATABASE_CREATE = "create table "
            + TABLE_WORKOUTS + "( "
            + WORKOUTS_COLUMN_ID + " integer primary key autoincrement, "
            + WORKOUTS_COLUMN_NAME + " text not null,"
            + WORKOUTS_COLUMN_DESCRIPTION + " text);";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_WORKOUTS);
        onCreate(db);
    }
}
