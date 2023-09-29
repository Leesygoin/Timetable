package com.example.studymanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TimeTableActivity extends AppCompatActivity {

    Button addDia_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        addDia_button = (Button)findViewById(R.id.addButton);

        addDia_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableActivity.this);

                final View innerView = getLayoutInflater().inflate(R.layout.activity_dialog, null);
                final EditText Cname = findViewById(R.id.className);

                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("추가");
                builder.setView(innerView);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = Cname.getText().toString();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

    }
}