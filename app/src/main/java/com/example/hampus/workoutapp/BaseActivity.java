package com.example.hampus.workoutapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.hampus.workoutapp.database.dao.ExerciseDataSource;
import com.example.hampus.workoutapp.database.dao.WorkoutDataSource;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private WorkoutDataSource workoutDataSrc;
    private ExerciseDataSource exerciseDataSrc;
    private LinearLayout layout;
    private ArrayList<Button> allBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initiateDB();
        initiateBtns();
    }
    private void initiateDB(){
        this.workoutDataSrc = new WorkoutDataSource(this);
        this.workoutDataSrc.open();
        this.exerciseDataSrc = new ExerciseDataSource(this);
        this.exerciseDataSrc.open();

        // Loading the database with data
        List<Exercise> exercises1 = new ArrayList<>();
        Exercise exercise;
        int i = 0;
        for(; i < 2; i++){
            exercise = exerciseDataSrc.createExercise("Exercise" + Integer.toString(i), "desc");
            if(exercise != null){
                exercises1.add(exercise);
            }
        }

        List<Exercise> exercises2 = new ArrayList<>();
        for(int j = i; j < i+2; j++){
            exercise = exerciseDataSrc.createExercise("Exercise" + Integer.toString(j), "desc");
            if(exercise != null){
                exercises2.add(exercise);
            }
        }

        workoutDataSrc.createWorkout("Heavy liftin", exercises1);
        workoutDataSrc.createWorkout("Running", exercises2);

    }

    private void initiateBtns(){
        // Initiate Create Workout button
        Button createWorkout = (Button) findViewById(R.id.base_activity_createworkout_button);
        createWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateWorkoutActivity(v);
            }
        });

        // Initiate all workout buttons
        List<Workout> workouts = this.workoutDataSrc.getAllWorkouts();
        layout = (LinearLayout) findViewById(R.id.linear_layout_base);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        allBtn = new ArrayList<>();

        int k=0;
        for (int i = 0; i < workouts.size(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add button in layout with correct parameters and set onClick to start new intent
            Button workoutBtn = new Button(this);
            workoutBtn.setLayoutParams(new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT));
            workoutBtn.setText(workouts.get(i).getName());
            workoutBtn.setId(i);
            workoutBtn.setAllCaps(false);
            workoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWorkoutActivity(v);
                }
            });
            Button editBtn = new Button(this);
            editBtn.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.WRAP_CONTENT));
            editBtn.setBackgroundResource(R.mipmap.settings);
            editBtn.setId(k++);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Start Editing of Workout
                }
            });
            Button removeBtn = new Button(this);
            removeBtn.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.WRAP_CONTENT));
            removeBtn.setBackgroundResource(R.mipmap.delete);
            removeBtn.setId(k++);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Start Removing of Workout
                }
            });

            allBtn.add(workoutBtn);
            row.addView(workoutBtn);
            row.addView(editBtn);
            row.addView(removeBtn);

            layout.addView(row);
        }


    }
    public void startWorkoutActivity(View view) {
        int btnID = view.getId();
        String btnName = allBtn.get(btnID).getText().toString();
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("WorkoutName",btnName);
        startActivity(intent);
    }
    public void startCreateWorkoutActivity(View view){
        Intent intent = new Intent(this, CreateWorkoutActivity.class);
        startActivity(intent);
    }
}
