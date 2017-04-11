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

import java.util.ArrayList;

public class CreateWorkoutActivity extends AppCompatActivity {
    private RelativeLayout baseRL;
    private ScrollView svExercises;
    private RelativeLayout rlScrollView;
    private TextView workoutNameText;
    private TextView exerciseText;
    private EditText workoutNameEdit;
    private Button addExerciseBtn;
    private Button removeExerciseBtn;
    private ScrollView svExercisesDropDown;

    private ExerciseDAO exerciseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        initiateView();
        exerciseDB = new ExerciseDAO(this);
        exerciseDB.open();
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
        ScrollView svExercisesCategoryDropDown = new ScrollView(this);
        svExercisesCategoryDropDown.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout llCat = new LinearLayout(this);
        llCat.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        llCat.setOrientation(LinearLayout.VERTICAL);
        llCat.setGravity(Gravity.CENTER);
        svExercisesCategoryDropDown.addView(llCat);

        ArrayList<String> categorys = exerciseDB.getAllCategorys();
        for (int i = 0; i < categorys.size(); i++) {
            TextView spinnerHeader = new TextView(this);
            Spinner spinner = new Spinner(this);
            ArrayList<String> spinnerArray = new ArrayList<>();

            spinnerArray.add(categorys.get(i));

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArrayAdapter.
            spinner.setAdapter(spinnerArrayAdapter);
            // TODO: LISTENER

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    Log.d("DEBUG: ",id+"");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            // Set spinner-name depending on category
            spinnerHeader.setText("Category");
            spinnerHeader.setTextSize(20);
            spinnerHeader.setGravity(Gravity.CENTER);
            llCat.addView(spinnerHeader);
            llCat.addView(spinner);
        }
        baseRL.addView(svExercisesDropDown);

    }
    public void exerciseCategorySelected(){
        // Initiate DropDowns for each type of exercises
        svExercisesDropDown= new ScrollView(this);
        svExercisesDropDown.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        svExercisesDropDown.addView(ll);

        ArrayList<String> categorys = exerciseDB.getAllCategorys();
        ArrayList<String> exercisesByCategorys =null;
        for (int i = 0; i < categorys.size(); i++) {
            TextView spinnerHeader = new TextView(this);
            Spinner spinner = new Spinner(this);
            ArrayList<String> spinnerArray = new ArrayList<>();

            // Get All exercises depending on category and add to category-defined spinner
            exercisesByCategorys = exerciseDB.getAllExerciseByCategory(categorys.get(i));
            if(exercisesByCategorys!=null) {
                for (int k = 0; k < exercisesByCategorys.size(); k++) {
                    spinnerArray.add(exercisesByCategorys.get(k));
                }
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            // TODO: LISTENER

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    Log.d("DEBUG: ",id+"");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            // Set spinner-name depending on category
            spinnerHeader.setText(categorys.get(i));
            spinnerHeader.setTextSize(20);
            spinnerHeader.setGravity(Gravity.CENTER);
            ll.addView(spinnerHeader);
            ll.addView(spinner);
        }
        baseRL.addView(svExercisesDropDown);
    }
    public void addExerciseModeComplete(View view){
        // TODO: Make previous mode Invisible and return to base :D with exercise added to scrollview
    }
}
