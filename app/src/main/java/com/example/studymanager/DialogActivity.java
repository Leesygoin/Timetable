package com.example.studymanager;

import static androidx.constraintlayout.widget.StateSet.TAG;

import static io.grpc.okhttp.internal.Platform.get;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogActivity extends AppCompatActivity {

    Button Tadd_button, Confirm_Button;
    EditText className_edittext;
    TextView dayText, Time1Text, Time2Text, MonText, TueText, WedText, ThuText, FriText;
    View.OnClickListener clickListener, onClickListener;
    private final int VIEW_ID = 0x8000;
    private final int BUTTON_ID = 0x9000;
    private static final String KEY_CLASSNAME = "cName";
    private static final String KEY_DAY = "day";
    private static final String KEY_STARTTIME = "sTime";
    private static final String KEY_ENDTIME = "eTime";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseRef = db.document("User/Class").collection("cInfo");
    private DocumentReference colorRef = db.document("User/Class");
    //private CollectionReference timeRef = db.document("User/Class").collection("tInfo");
    int dayID;
    String day = "monday";//, hour1="15", hour2="15", min1="30", min2="30";
    int hour1=15, hour2=15, min1=30, min2=30;
    private Integer viewNum = 0;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        final Global global = (Global)getApplication();


        Confirm_Button = (Button)findViewById(R.id.confirmButton);
        className_edittext = (EditText)findViewById(R.id.className);
        Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((hour1-8)*2 + 1 + min1/30)>=((hour2-8)*2 + 1 + min2/30))
                    Toast.makeText(getApplicationContext(), "시간 설정 오류", Toast.LENGTH_SHORT).show();


                else {
                    Task task1 = courseRef.whereGreaterThanOrEqualTo("sTime", (hour1-8)*2 + 1 + min1/30)
                                    .whereLessThan("sTime", (hour2-8)*2 + 1 + min2/30)
                                            .whereEqualTo("day", day)
                                                    .get();
                    Task task2 = courseRef.whereGreaterThan("eTime", (hour1-8)*2 + 1 + min1/30)
                            .whereLessThanOrEqualTo("eTime", (hour2-8)*2 + 1 + min2/30)
                            .whereEqualTo("day", day)
                            .get();
                    //Task allTasks = Tasks.whenAllSuccess(task1, task2)
                    Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task1, task2);
                    allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                        @Override
                        public void onSuccess(List<QuerySnapshot> querySnapshots) {
                            int tcf=0;
                            for(QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    ++tcf;
                                }
                            }
                            if(tcf>0)
                            {
                                Toast.makeText(DialogActivity.this, "시간 중복", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                global.setColNum(day);
                                global.setStartRow((hour1-8)*2 + 1 + min1/30);
                                global.setEndRow((hour2-8)*2 + 1 + min2/30);
                                global.setClassName(className_edittext.getText().toString());
                                global.setFlag(1);

                                colorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()) {
                                            Color color = documentSnapshot.toObject(Color.class);
                                            c = color.getColor();
                                            ++c;
                                            global.setC(c%7);
                                            color.setColor(c%7);
                                            colorRef.set(color).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(DialogActivity.this, "New color", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, e.toString());
                                                }
                                            });

                                        }

                                        else {
                                            global.setC(0);
                                            Color color = new Color(global.getC());
                                            colorRef.set(color).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(DialogActivity.this, "Added new color", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, e.toString());
                                                }
                                            });
                                        }

                                        Course course = new Course(day, (hour1-8)*2 + 1 + min1/30, (hour2-8)*2 + 1 + min2/30, false, global.getC());

                                        courseRef.document(global.getClassName()).set(course)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(DialogActivity.this, "강의 추가 성공", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(DialogActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, e.toString());
                                                    }
                                                });
                                        Intent intent = new Intent(DialogActivity.this, TimeTable2.class);
                                        //Intent intent = new Intent(DialogActivity.this, Test.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, e.toString());
                                    }
                                });

                                /*
                                Course course = new Course(day, (hour1-8)*2 + 1 + min1/30, (hour2-8)*2 + 1 + min2/30, false, global.getC());

                                courseRef.document(global.getClassName()).set(course)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(DialogActivity.this, "강의 추가 성공", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DialogActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, e.toString());
                                            }
                                        });
                                Intent intent = new Intent(DialogActivity.this, TimeTable2.class);
                                //Intent intent = new Intent(DialogActivity.this, Test.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                */
                            }
                        }
                    });


                }

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

                                    //ad.setIcon(R.mipmap.ic_launcher);
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
                                                    day="monday";
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Tuesday:
                                                    dayText.setText("화요일");
                                                    day="tuesday";
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Wednesday:
                                                    dayText.setText("수요일");
                                                    day="wednesday";
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Thursday:
                                                    dayText.setText("목요일");
                                                    day="thursday";
                                                    ad.dismiss();
                                                    break;
                                                case R.id.Friday:
                                                    dayText.setText("금요일");
                                                    day="friday";
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

                                    ad.show();
                                    break;
                                case R.id.time1_textview:
                                    TimePickerDialog.OnTimeSetListener listener1 = new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                            if (view.isShown()) {
                                                if(hourOfDay<8)
                                                    hour1=8;
                                                else if (hourOfDay>20) {
                                                    if(hourOfDay==21 && minute<30)
                                                        hour1=21;
                                                    else
                                                        hour1=20;
                                                }
                                                else hour1 = hourOfDay;
                                                if(minute<30)
                                                    min1=0;
                                                else
                                                    min1=30;
                                                Time1Text.setText(""+hour1/10 + hour1%10 + ":" + min1/10 + "0");
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
                                                if(hourOfDay<8)
                                                    hour2=8;
                                                else if (hourOfDay>20) {
                                                    if(hourOfDay==21 && minute<30)
                                                        hour2=21;
                                                    else
                                                        hour2=20;
                                                }
                                                else hour2 = hourOfDay;
                                                if(minute<30)
                                                    min2=0;
                                                else
                                                    min2=30;
                                                Time2Text.setText(""+hour2/10 + hour2%10 + ":" + min2/10 + "0");
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

    public int dayToNum(String day)
    {
        if (day.equals("monday"))
            return 1;
        else if (day.equals("tuesday"))
            return 2;
        else if (day.equals("wednesday"))
            return 3;
        else if (day.equals("thursday"))
            return 4;
        else if (day.equals("friday"))
            return 5;
        else if (day.equals("saturday"))
            return 6;
        else if (day.equals("sunday"))
            return 7;
        else
            return 0;
    }

}