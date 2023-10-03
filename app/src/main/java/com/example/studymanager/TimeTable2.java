package com.example.studymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimeTable2 extends AppCompatActivity {

    Button addDate_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table2);

        addDate_button = (Button)findViewById(R.id.addDButton);
        addDate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TimeTable2.this, DialogActivity.class);
                startActivity(myIntent);
            }
        });
    }
}