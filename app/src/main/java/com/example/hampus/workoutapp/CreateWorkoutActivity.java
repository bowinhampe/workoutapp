package com.example.hampus.workoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreateWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        initiateScrollViewExercises();
    }

    private void initiateScrollViewExercises(){
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.relative_scroll_createworkout);

        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        for(int i = 0; i < 20; i++)
        {
            TextView b = new TextView(this);
            b.setText("Exercise nr "+i);
            ll.addView(b);
        }

        rl.addView(sv);

    }
}
