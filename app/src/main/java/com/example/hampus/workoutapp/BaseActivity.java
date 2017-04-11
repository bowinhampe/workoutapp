package com.example.hampus.workoutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_create_workout:
                Intent intent = new Intent(this, CreateWorkoutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_developer_options:
                // TODO: add;
                return true;
            case R.id.menu_graphs:
                // TODO: add;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateDB() {
        this.workoutDAO = new WorkoutDAO(this);
        this.workoutDAO.open();
        this.exerciseDAO = new ExerciseDAO(this);
        this.exerciseDAO.open();

        if(workoutDAO.getAllWorkouts().size()==0) {
            // Loading the database with data
            List<Exercise> exercises = staticDBinitiateChest();
            workoutDAO.createWorkout("National Chestday", exercises);

            exercises = staticDBinitiateBack();
            workoutDAO.createWorkout("Back-day", exercises);

            exercises = staticDBinitiateLegs();
            workoutDAO.createWorkout("LEGDAY!", exercises);
        }

    }

    private List<Exercise> staticDBinitiateChest(){
        List<Exercise> boobs = new ArrayList<>();
        ArrayList<Exercise> allExercise = new ArrayList<>();
        allExercise.add(exerciseDAO.createExercise("Benchpress", "Chest", "Chest/Triceps", "desc"));
        allExercise.add(exerciseDAO.createExercise("Dumbbellpress", "Chest", "Chest/Triceps", "desc"));
        allExercise.add(exerciseDAO.createExercise("Flyers", "Chest", "Chest", "desc"));
        allExercise.add(exerciseDAO.createExercise("Push-ups", "Chest", "Chest/Triceps", "desc"));

        for(int i=0;i<allExercise.size();i++) {
            if (allExercise.get(i)!= null) {
                boobs.add(allExercise.get(i));
            }
        }
        return boobs;
    }

    private List<Exercise> staticDBinitiateBack(){
        List<Exercise> back = new ArrayList<>();
        ArrayList<Exercise> allExercise = new ArrayList<>();
        allExercise.add(exerciseDAO.createExercise("Deadlift", "Back", "Full body", "desc"));
        allExercise.add(exerciseDAO.createExercise("Row", "Back", "Back", "desc"));
        allExercise.add(exerciseDAO.createExercise("Pull-ups", "Back", "Back/Biceps", "desc"));
        allExercise.add(exerciseDAO.createExercise("Dumbbellrow", "Back", "Back", "desc"));

        for(int i=0;i<allExercise.size();i++) {
            if (allExercise.get(i)!= null) {
                back.add(allExercise.get(i));
            }
        }
        return back;
    }

    private List<Exercise> staticDBinitiateLegs(){
        List<Exercise> legs = new ArrayList<>();
        ArrayList<Exercise> allExercise = new ArrayList<>();
        allExercise.add(exerciseDAO.createExercise("Squat", "Legs", "Full body", "desc"));
        allExercise.add(exerciseDAO.createExercise("Legpress", "Legs", "Legs", "desc"));
        allExercise.add(exerciseDAO.createExercise("Legcurl", "Legs", "Backside Legs", "desc"));
        allExercise.add(exerciseDAO.createExercise("Legextension", "Legs", "Frontside Legs", "desc"));

        for(int i=0;i<allExercise.size();i++) {
            if (allExercise.get(i)!= null) {
                legs.add(allExercise.get(i));
            }
        }
        return legs;
    }

    private void initiateBtns() {

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

}
