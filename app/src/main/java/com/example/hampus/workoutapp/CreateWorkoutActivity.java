package com.example.hampus.workoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        initiateView();
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
        svExercisesDropDown= new ScrollView(this);
        svExercisesDropDown.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        svExercisesDropDown.addView(ll);


        for (int i = 0; i < 5; i++) {
            TextView spinnerHeader = new TextView(this);
            Spinner spinner = new Spinner(this);
            ArrayList<String> spinnerArray = new ArrayList<>();
            // TODO: Set this to get All exercises by group and add
            spinnerArray.add("Exercise 1");
            spinnerArray.add("Exercise 2");
            spinnerArray.add("Exercise 3");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO: Set on click listener on each item and return the value that was chosen to be added in scrollViewExercises
                }
            });
            // TODO: Make this set by Category
            spinnerHeader.setText("Category: "+i);
            spinnerHeader.setTextSize(20);
            ll.addView(spinnerHeader);
            ll.addView(spinner);
        }
        baseRL.addView(svExercisesDropDown);

    }
    public void addExerciseModeComplete(View view){
        // TODO: Make previous mode Invisible and return to base :D with exercise added to scrollview
    }
}
