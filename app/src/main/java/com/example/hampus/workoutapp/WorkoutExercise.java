package com.example.hampus.workoutapp;

/**
 * Created by hampus on 2017-04-08.
 */

public class WorkoutExercise {
    public class Workout {
        private long workOutID; // FK
        private long exerciseID; // FK

        public long getWorkoutID() {
            return workOutID;
        }

        public void setId(long workOutID) {
            this.workOutID = workOutID;
        }

        public long getExerciseID() {
            return exerciseID;
        }

        public void setName(long exerciseID) {
            this.exerciseID = exerciseID;
        }

    }
}
