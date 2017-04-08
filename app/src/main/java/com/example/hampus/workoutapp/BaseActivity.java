package com.example.hampus.workoutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.hampus.workoutapp.database.dao.WorkoutDataSource;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private WorkoutDataSource workoutDataSrc;
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

        // Loading the database with data
        workoutDataSrc.createWorkout("Heavy liftin", "Flex them muscles.");
        workoutDataSrc.createWorkout("Running", "Run fast af.");

    }

    private void initiateBtns(){
        List<Workout> workouts = this.workoutDataSrc.getAllWorkouts();
        layout = (LinearLayout) findViewById(R.id.linear_layout_base);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        allBtn = new ArrayList<>();

        for (int i = 0; i<workouts.size(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add button in layout with correct parameters and set onClick to start new intent
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText(workouts.get(i).getName());
            btnTag.setId(i);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWorkoutActivity(v);
                }
            });
            allBtn.add(btnTag);
            row.addView(btnTag);

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
}
