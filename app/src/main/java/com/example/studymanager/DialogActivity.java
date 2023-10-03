package com.example.studymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DialogActivity extends AppCompatActivity {

    Button Tadd_button, Confirm_Button;
    TextView dayText, Time1Text, Time2Text, MonText, TueText, WedText, ThuText, FriText;
    View.OnClickListener clickListener, onClickListener;
    private final int VIEW_ID = 0x8000;
    private final int BUTTON_ID = 0x9000;
    int dayID;
    int day;
    private Integer viewNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        final Global global = (Global)getApplication();
        global.Init();

        Confirm_Button = (Button)findViewById(R.id.confirmButton);
        Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setColNum(day);
            }
        });

        Tadd_button = (Button)findViewById(R.id.TaddButton);
        Tadd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewNum<1) {
                    ++viewNum;

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    LinearLayout conLinear = (LinearLayout) findViewById(R.id.addview_container);
                    //conLinear.setId(VIEW_ID+viewNum);
                    View layout = inflater.inflate(R.layout.activity_add_time_view, conLinear);

                    dayText = findViewById(R.id.day_textview);
                    Time1Text = findViewById(R.id.time1_textview);
                    Time2Text = findViewById(R.id.time2_textview);


                    clickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            switch (view.getId()) {

                                case R.id.day_textview:


                                    AlertDialog.Builder builder = new AlertDialog.Builder(DialogActivity.this);
                                    AlertDialog ad = builder.create();
                                    final View innerView = getLayoutInflater().inflate(R.layout.activity_add_day_view, null);
                                    //final EditText Cname = findViewById(R.id.className);

                                    ad.setIcon(R.mipmap.ic_launcher);
                                    ad.setTitle("요일 선택");
                                    ad.setView(innerView);

                                    MonText = innerView.findViewById(R.id.Monday);
                                    TueText = innerView.findViewById(R.id.Tuesday);
                                    WedText = innerView.findViewById(R.id.Wednesday);
                                    ThuText = innerView.findViewById(R.id.Thursday);
                                    FriText = innerView.findViewById(R.id.Friday);

                                    onClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            switch(dayID = v.getId())
                                            {

                                                case R.id.Monday:
                                                    dayText.setText("월요일");
                                                    day=1;
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Tuesday:
                                                    dayText.setText("화요일");
                                                    day=2;
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Wednesday:
                                                    dayText.setText("수요일");
                                                    day=3;
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Thursday:
                                                    dayText.setText("목요일");
                                                    day=4;
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Friday:
                                                    dayText.setText("금요일");
                                                    day=5;
                                                    ad.dismiss();
                                                    break;
                                            }
                                        }
                                    };

                                    MonText.setOnClickListener(onClickListener);
                                    TueText.setOnClickListener(onClickListener);
                                    WedText.setOnClickListener(onClickListener);
                                    ThuText.setOnClickListener(onClickListener);
                                    FriText.setOnClickListener(onClickListener);

                                    /*
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String result = Cname.getText().toString();
                                            dayText.setText(result);
                                            dialog.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });*/
                                    ad.show();
                                    break;
                                case R.id.time1_textview:
                                    TimePickerDialog.OnTimeSetListener listener1 = new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                            if (view.isShown()) {
                                                Time1Text.setText(hourOfDay + ":" + minute);
                                            }
                                        }
                                    };
                                    TimePickerDialog dialog1 = new TimePickerDialog(DialogActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener1, 15, 30, true);
                                    dialog1.setTitle("시작 시간");
                                    dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog1.show();
                                    break;
                                case R.id.time2_textview:
                                    TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                            if (view.isShown()) {
                                                Time2Text.setText(hourOfDay + ":" + minute);
                                            }
                                        }
                                    };
                                    TimePickerDialog dialog2 = new TimePickerDialog(DialogActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener2, 15, 30, true);
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

                else {
                    Toast.makeText(getApplicationContext(), "아직 한 타임만 지원 가능", Toast.LENGTH_SHORT).show();
                }
                //Button delButton = new Button(conLinear.getContext()); // 새로운 버튼 생성
                //delButton.setId(BUTTON_ID + viewNum);
                //delButton.setText("삭제 " + viewNum); // 버튼의 이름를 버튼의 개수로 표시
                //conLinear.addView(delButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                //        LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        });
    }

    private void pushButton() {

        Button delButton = new Button(this); // 새로운 버튼 생성
        delButton.setText("삭제 " + viewNum); // 버튼의 이름를 버튼의 개수로 표시

        //아까 만든 공간에 크기에 맞는 버튼을 생성함.

    }

}