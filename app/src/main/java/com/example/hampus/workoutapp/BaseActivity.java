package com.example.hampus.workoutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.hampus.workoutapp.database.dao.ExerciseDAO;
import com.example.hampus.workoutapp.database.dao.WorkoutDAO;
import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private WorkoutDAO workoutDAO;
    private ExerciseDAO exerciseDAO;
    private LinearLayout layout;
    private ArrayList<Button> allBtn;
    private List<Workout> workoutNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initiateDB();
        initiateBtns();
    }

    private void initiateDB() {
        this.workoutDAO = new WorkoutDAO(this);
        this.workoutDAO.open();
        this.exerciseDAO = new ExerciseDAO(this);
        this.exerciseDAO.open();

        // Loading the database with data
        List<Exercise> exercises1 = new ArrayList<>();
        Exercise exercise;
        int i = 0;
        for (; i < 2; i++) {
            exercise = exerciseDAO.createExercise("Exercise" + Integer.toString(i), "Category" + Integer.toString(i), "MuscleGroup" + Integer.toString(i), "desc");
            if (exercise != null) {
                exercises1.add(exercise);
            }
        }

        List<Exercise> exercises2 = new ArrayList<>();
        for (int j = i; j < i + 2; j++) {
            exercise = exerciseDAO.createExercise("Exercise" + Integer.toString(j), "Category" + Integer.toString(j), "MuscleGroup" + Integer.toString(j), "desc");
            if (exercise != null) {
                exercises2.add(exercise);
            }
        }

        workoutDAO.createWorkout("Heavy liftin", exercises1);
        workoutDAO.createWorkout("Running", exercises2);

    }

    private void initiateBtns() {
        // Initiate Create Workout button
        Button createWorkout = (Button) findViewById(R.id.base_activity_createworkout_button);
        createWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateWorkoutActivity(v);
            }
        });

        // Initiate all workout buttons
        this.workoutNames = this.workoutDAO.getAllWorkoutNames();
        layout = (LinearLayout) findViewById(R.id.linear_layout_base);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        allBtn = new ArrayList<>();

        int k = 0;
        for (int i = 0; i < this.workoutNames.size(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add button in layout with correct parameters and set onClick to start new intent
            Button workoutBtn = new Button(this);
            workoutBtn.setLayoutParams(new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT));
            workoutBtn.setText(this.workoutNames.get(i).getName());
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
        long id = -1;
        for (Workout workout : this.workoutNames) {
            if (workout.getName() == btnName) {
                id = workout.getId();
            }
        }
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("WorkoutId", id);
        startActivity(intent);
    }

    public void startCreateWorkoutActivity(View view) {
        Intent intent = new Intent(this, CreateWorkoutActivity.class);
        startActivity(intent);
    }
}
