package com.example.studymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddTimeView extends AppCompatActivity {

    TextView dayText, Time1Text, Time2Text;
    View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_view);

        dayText = findViewById(R.id.day_textview);
        Time1Text = findViewById(R.id.time1_textview);
        Time2Text = findViewById(R.id.time2_textview);

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){



                    case R.id.day_textview:
                        break;
                    case R.id.time1_textview:
                        TimePickerDialog.OnTimeSetListener listener1 = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    Time1Text.setText(hourOfDay+":"+minute);
                                }
                            }
                        };
                        TimePickerDialog dialog1 = new TimePickerDialog(AddTimeView.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener1, 15, 30, false);
                        dialog1.setTitle("시작 시간");
                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog1.show();
                        break;
                    case R.id.time2_textview:
                        TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    Time2Text.setText(hourOfDay+":"+minute);
                                }
                            }
                        };
                        TimePickerDialog dialog2 = new TimePickerDialog(AddTimeView.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener2, 15, 30, false);
                        dialog2.setTitle("종료 시간");
                        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog2.show();
                        break;
                }

            }
        };



        dayText.setOnClickListener(clickListener);
        Time1Text.setOnClickListener(clickListener);
        Time2Text.setOnClickListener(clickListener);

    }


}