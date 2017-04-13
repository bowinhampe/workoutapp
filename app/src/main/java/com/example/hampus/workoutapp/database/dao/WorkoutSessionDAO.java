package com.example.hampus.workoutapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.example.hampus.workoutapp.database.DatabaseHandler;
import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;
import com.example.hampus.workoutapp.entities.WorkoutSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hampus on 2017-04-11.
 */

public class WorkoutSessionDAO extends DAO {

        public WorkoutSessionDAO(Context context) {
            super(context);
            this.dbHandler = new DatabaseHandler(context);

            String[] columns = {DatabaseHandler.WORKOUTSESSION_COLUMN_ID,
                    DatabaseHandler.WORKOUTSESSION_COLUMN_WORKOUTEXERCISES_ID,
                    DatabaseHandler.WORKOUTSESSION_COLUMN_DATE,
                    DatabaseHandler.WORKOUTSESSION_COLUMN_SETS,
                    DatabaseHandler.WORKOUTSESSION_COLUMN_REPS,
                    DatabaseHandler.WORKOUTSESSION_COLUMN_WEIGHT};
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

        public WorkoutSession createWorkoutSession(String date,long fk, int set, int rep, int weight) {
            ContentValues values = new ContentValues();

            long workoutSessionId = this.getDb().insert(DatabaseHandler.TABLE_WORKOUTSESSION, null,
                    values);

            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_WORKOUTEXERCISES_ID,fk);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_DATE, date);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_SETS, set);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_REPS, rep);
            values.put(DatabaseHandler.WORKOUTSESSION_COLUMN_WEIGHT, weight);

            WorkoutSession toAdd=null;
            if (workoutSessionId != -1) {
                Cursor cursor = this.getDb().query(DatabaseHandler.TABLE_WORKOUTSESSION,
                        this.getColumns(), DatabaseHandler.WORKOUTSESSION_COLUMN_ID + " = " + workoutSessionId, null,
                        null, null, null);
                cursor.moveToFirst();
                toAdd = cursorToWorkoutSession(cursor);
                cursor.close();
            }
            return toAdd;
        }



        private WorkoutSession cursorToWorkoutSession(Cursor cursor) {
            WorkoutSession wSession = null;

            if (!cursor.isAfterLast()) {
                wSession = new WorkoutSession();

                wSession.setId(cursor.getLong(0));
                wSession.setDate(cursor.getString(1));
                wSession.setSets(cursor.getInt(2));
                wSession.setReps(cursor.getInt(3));
                wSession.setWeight(cursor.getInt(4));
            }

            return wSession;
        }

    }
