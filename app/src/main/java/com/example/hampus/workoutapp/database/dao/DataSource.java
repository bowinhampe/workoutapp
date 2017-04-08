package com.example.hampus.workoutapp.database.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.hampus.workoutapp.database.DatabaseHandler;

/**
 * Created by Daniel on 4/8/2017.
 */

public abstract class DataSource {
    private SQLiteDatabase db;
    private DatabaseHandler dbHandler;
    private String[] columns;

    public DataSource(Context context) {
        this.dbHandler = new DatabaseHandler(context);
    }

    public void open() throws SQLException {
        this.db = this.dbHandler.getWritableDatabase();
    }

    public void close() {
        this.dbHandler.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }
}
