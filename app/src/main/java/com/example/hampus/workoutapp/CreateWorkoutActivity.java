package com.example.hampus.workoutapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hampus.workoutapp.database.dao.ExerciseDAO;
import com.example.hampus.workoutapp.database.dao.WorkoutDAO;
import com.example.hampus.workoutapp.entities.Exercise;
import com.example.hampus.workoutapp.entities.Workout;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkoutActivity extends AppCompatActivity {
    private RelativeLayout baseRL;
    private ScrollView svExercises;
    private RelativeLayout rlScrollView;
    private TextView workoutNameText;
    private TextView exerciseText;
    private EditText workoutNameEdit;
    private Button addExerciseBtn;
    private Button removeExerciseBtn;
    private Button doneCreatingBtn;
    private ArrayList<String> categorys;
    private ArrayList<String> exercises;
    private LinearLayout llScrollView;
    private ExerciseDAO exerciseDB;
    private WorkoutDAO workoutDB;
    private ScrollView mAddExercise;
    private Spinner mCategory;
    private Spinner mExerciseByCategory;
    private TextView mSpinnerHeaderExercise;
    private List<Exercise> mExercisesToAdd;
    private boolean firstLoad = true;
    private boolean mCreateWorkoutOK=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        initiateView();
        exerciseDB = new ExerciseDAO(this);
        exerciseDB.open();
        workoutDB = new WorkoutDAO(this);
        workoutDB.open();

        mExercisesToAdd = new ArrayList<>();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        exerciseDB.close();
        workoutDB.close();
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
                Toast toast = new Toast(this);
                toast.makeText(this, "Already in Create Workout!",Toast.LENGTH_LONG);
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

    private void initiateView() {
        baseRL = (RelativeLayout) findViewById(R.id.create_workout_base_rl);
        rlScrollView= (RelativeLayout) findViewById(R.id.relative_scroll_createworkout);
        workoutNameText = (TextView) findViewById(R.id.create_workout_textview_name);
        exerciseText = (TextView) findViewById(R.id.create_workout_textView_exercises);
        workoutNameEdit = (EditText) findViewById(R.id.create_workout_editText_name);
        addExerciseBtn = (Button) findViewById(R.id.create_workout_button_add);
        removeExerciseBtn = (Button) findViewById(R.id.create_workout_button_remove);
        doneCreatingBtn = (Button) findViewById(R.id.create_workout_button_done);

        doneCreatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create workout and exit mode
                if(mCreateWorkoutOK) {
                    Workout wkout = workoutDB.createWorkout(workoutNameEdit.getText().toString(), mExercisesToAdd);
                }
                Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                startActivity(intent);
            }
        });
        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseMode(v);
            }
        });
        svExercises= new ScrollView(this);
        svExercises.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        svExercises.addView(ll);

        for (int i = 0; i < 20; i++) {
            TextView b = new TextView(this);
            b.setText("Exercise nr " + i);
            ll.addView(b);
        }

        rlScrollView.addView(svExercises);

    }

    public void addExerciseMode(View view){
        // Hide all Views
        rlScrollView.setVisibility(View.INVISIBLE);
        workoutNameText.setVisibility(View.INVISIBLE);
        exerciseText.setVisibility(View.INVISIBLE);
        workoutNameEdit.setVisibility(View.INVISIBLE);
        addExerciseBtn.setVisibility(View.INVISIBLE);
        removeExerciseBtn.setVisibility(View.INVISIBLE);

        // Initiate DropDowns for each type of exercises
        mAddExercise = new ScrollView(this);
        mAddExercise.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        llScrollView = new LinearLayout(this);
        llScrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        llScrollView.setOrientation(LinearLayout.VERTICAL);
        llScrollView.setGravity(Gravity.CENTER);
        mAddExercise.addView(llScrollView);
        mSpinnerHeaderExercise = new TextView(this);

        categorys = exerciseDB.getAllCategories();
        mCategory = new Spinner(this);
        Log.d(categorys.size()+": SIZEOF","cat");
        TextView spinnerHeader = new TextView(this);
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (int i = 0; i < categorys.size(); i++) {

            spinnerArray.add(categorys.get(i));

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategory.setAdapter(spinnerArrayAdapter);
            mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    Log.d("DEBUG: ",id+"");
                    // TODO: Initiate Dropdown for exercises
                        llScrollView.removeView(mExerciseByCategory);
                        if(firstLoad) {
                            exerciseCategorySelected(categorys.get(position));
                            firstLoad = false;
                        }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            // Set spinner-name depending on category

        }
        // Category Header
        spinnerHeader.setText("Category");
        spinnerHeader.setTextSize(20);
        spinnerHeader.setGravity(Gravity.CENTER);
        llScrollView.addView(spinnerHeader);
        llScrollView.addView(mCategory);
        // Exercise Header
        mSpinnerHeaderExercise.setText("Exercise");
        mSpinnerHeaderExercise.setTextSize(20);
        mSpinnerHeaderExercise.setGravity(Gravity.CENTER);
        llScrollView.addView(mSpinnerHeaderExercise);
        baseRL.addView(mAddExercise);

    }
    public void exerciseCategorySelected(String category){
        // Initiate DropDowns for each type of exercises

        ArrayList<String> exercisesByCategorys =null;
        mExerciseByCategory = new Spinner(this);
        exercises = new ArrayList<>();

        // Get All exercises depending on category and add to category-defined spinner
        exercises = exerciseDB.getAllExerciseNamesByCategory(category);
        if(exercisesByCategorys!=null) {
            for (int k = 0; k < exercisesByCategorys.size(); k++) {
                    exercises.add(exercisesByCategorys.get(k));
                }
            }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exercises); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseByCategory.setAdapter(spinnerArrayAdapter);
        // TODO: LISTENER

        mExerciseByCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    Log.d("DEBUG: ",id+"");
                    if(!firstLoad) {
                        firstLoad = true;
                    }
                    else{
                        addExerciseModeComplete(exercises.get(position));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        // Set spinner-name depending on category
        llScrollView.addView(mExerciseByCategory);
    }
    public void addExerciseModeComplete(String exerciseToAdd){
        rlScrollView.setVisibility(View.VISIBLE);
        workoutNameText.setVisibility(View.VISIBLE);
        exerciseText.setVisibility(View.VISIBLE);
        workoutNameEdit.setVisibility(View.VISIBLE);
        addExerciseBtn.setVisibility(View.VISIBLE);
        removeExerciseBtn.setVisibility(View.VISIBLE);

        llScrollView.setVisibility(View.INVISIBLE);

        Exercise toAdd = exerciseDB.getExerciseByName(exerciseToAdd);
        mExercisesToAdd.add(toAdd);
        mCreateWorkoutOK=true;
        Log.d("test","WTF MANM");


    }
}
