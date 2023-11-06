package com.example.studymanager;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class TimeTable2 extends AppCompatActivity {

    Button addDate_button;
    int startT, endT, min, hour, clr;
    static int check[][] = new int[6][27];
    String cellH, cellM, day;
    Boolean ap;
    View.OnClickListener clickListener;
    private static final String KEY_DAY = "day";
    private static final String KEY_STARTTIME = "sTime";
    private static final String KEY_ENDTIME = "eTime";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseRef = db.document("User/Class").collection("cInfo");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table3);

        final Global global = (Global)getApplication();

        addDate_button = (Button)findViewById(R.id.addDButton);


        courseRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Course course = documentSnapshot.toObject(Course.class);
                    day = course.getDay();
                    startT = course.getsTime();
                    endT = course.geteTime();
                    ap = course.isApplied();
                    clr = course.getColor();

                    if (ap == true) {
                        GridLayout gridLayout2 = (GridLayout) findViewById(R.id.gridlayout_timetable);

                        for (int j = startT + 1; j < endT; ++j) {
                            hour = (j - 1) / 2 + 8;
                            min = ((j - 1) % 2);
                            if (hour < 10)
                                cellH = "0" + hour;
                            else cellH = "" + hour;
                            if (min == 0)
                                cellM = "00";
                            else cellM = "30";
                            String delete_cell_name = day + cellH + cellM;
                            //(TextView) findViewWithTag(delete_cell_name);
                            //GridLayout gridLayout2 = (GridLayout)findViewById(R.id.gridlayout_timetable);
                            TextView deleteCell = gridLayout2.findViewWithTag(delete_cell_name);
                            gridLayout2.removeView(deleteCell);
                            //gridLayout2.removeView(gridLayout2.findViewWithTag(delete_cell_name));
                        }

                        hour = (startT - 1) / 2 + 8;
                        min = ((startT - 1) % 2);
                        if (hour < 10)
                            cellH = "0" + hour;
                        else cellH = "" + hour;
                        if (min == 0)
                            cellM = "00";
                        else cellM = "30";
                        String IDofSpanCell = day + cellH + cellM;//span해야할 cell의 id
                        TextView spanCell = (TextView) gridLayout2.findViewWithTag(IDofSpanCell);//span cell
                        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) spanCell.getLayoutParams();
                        layoutParams.columnSpec = GridLayout.spec(dayToNum(day));
                        layoutParams.rowSpec = GridLayout.spec(startT, endT - startT);//병합할 셀 수 정함
                        spanCell.setLayoutParams(layoutParams);//적용
                        layoutParams.setGravity(Gravity.FILL);//gravity 설정
                        spanCell.setLayoutParams(layoutParams);//다시 적용

                        spanCell.setText(documentSnapshot.getId());
                        //spanCell.setTextColor(Color.WHITE);
                        //spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell, null));
                        spanCell.setOnClickListener(clickListener);

                        if(clr ==0)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_skyblue, null));
                        else if (clr == 1)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_green, null));
                        else if (clr == 2)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_yellow, null));
                        else if (clr == 3)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_orange, null));
                        else if (clr == 4)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_pink, null));
                        else if (clr == 5)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_purple, null));
                        else if (clr == 6)
                            spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_brown, null));

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TimeTable2.this, "No class exists", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        //timeClick()

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeTable2.this);
                //AlertDialog ad = builder.create();
                //ad.setIcon(R.mipmap.ic_launcher);
                //ad.setTitle("삭제하시겠습니까?");
                TextView tv = (TextView) v;
                builder.setTitle("delete?" + tv.getText());

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseRef.document(""+tv.getText()).delete();
                        //finish();
                        recreate();
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
        };

        addDate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TimeTable2.this, DialogActivity.class);
                //launcher.launch(myIntent);
                startActivity(myIntent);


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final Global global = (Global)getApplication();

        if(global.getFlag()==1)
        {

            //startT=global.getStartRow();
            //endT=global.getEndRow();
            //day=global.getColNum();
            courseRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Course course = documentSnapshot.toObject(Course.class);
                        day = course.getDay();
                        startT = course.getsTime();
                        endT = course.geteTime();
                        ap = course.isApplied();
                        clr = course.getColor();
                        if (ap == false) {
                            //course.setDocumentId(documentSnapshot.getId());

                            GridLayout gridLayout2 = (GridLayout) findViewById(R.id.gridlayout_timetable);

                            for (int j = startT + 1; j < endT; ++j) {
                                hour = (j - 1) / 2 + 8;
                                min = ((j - 1) % 2);
                                if (hour < 10)
                                    cellH = "0" + hour;
                                else cellH = "" + hour;
                                if (min == 0)
                                    cellM = "00";
                                else cellM = "30";
                                String delete_cell_name = day + cellH + cellM;
                                //(TextView) findViewWithTag(delete_cell_name);
                                //GridLayout gridLayout2 = (GridLayout)findViewById(R.id.gridlayout_timetable);
                                TextView deleteCell = gridLayout2.findViewWithTag(delete_cell_name);
                                gridLayout2.removeView(deleteCell);
                                //gridLayout2.removeView(gridLayout2.findViewWithTag(delete_cell_name));
                            }

                            hour = (startT - 1) / 2 + 8;
                            min = ((startT - 1) % 2);
                            if (hour < 10)
                                cellH = "0" + hour;
                            else cellH = "" + hour;
                            if (min == 0)
                                cellM = "00";
                            else cellM = "30";
                            String IDofSpanCell = day + cellH + cellM;//span해야할 cell의 id
                            TextView spanCell = (TextView) gridLayout2.findViewWithTag(IDofSpanCell);//span cell
                            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) spanCell.getLayoutParams();
                            layoutParams.columnSpec = GridLayout.spec(dayToNum(day));
                            layoutParams.rowSpec = GridLayout.spec(startT, endT - startT);//병합할 셀 수 정함
                            spanCell.setLayoutParams(layoutParams);//적용
                            layoutParams.setGravity(Gravity.FILL);//gravity 설정
                            spanCell.setLayoutParams(layoutParams);//다시 적용

                            spanCell.setText(documentSnapshot.getId());
                            //spanCell.setTextColor(Color.WHITE);
                            //spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell, null));
                            spanCell.setOnClickListener(clickListener);

                            if(clr ==0)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_skyblue, null));
                            else if (clr == 1)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_green, null));
                            else if (clr == 2)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_yellow, null));
                            else if (clr == 3)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_orange, null));
                            else if (clr == 4)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_pink, null));
                            else if (clr == 5)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_purple, null));
                            else if (clr == 6)
                                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell_brown, null));

                            course.setApplied(true);
                            courseRef.document(documentSnapshot.getId()).set(course)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "e.toString()");
                                        }
                                    });
                        }

                    }


                }
            });



            global.setFlag(0);
        }

        /*
        courseRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                for(QueryDocumentSnapshot documentSnapshot : value) {
                    Map<String, Object> course = new HashMap<>();
                    course = documentSnapshot.getData();
                    int startT = Integer.parseInt(String.valueOf(course.get(KEY_STARTTIME)));
                    int endT = Integer.parseInt(String.valueOf(course.get(KEY_ENDTIME)));
                    String day = documentSnapshot.getId();

                    GridLayout gridLayout2 = (GridLayout)findViewById(R.id.gridlayout_timetable);


                    for(int j = startT+1; j< endT; ++j)
                    {
                        hour=(j-1)/2+8;
                        min=((j-1)%2);
                        if(hour<10)
                            cellH="0"+hour;
                        else cellH=""+hour;
                        if(min==0)
                            cellM="00";
                        else cellM="30";
                        String delete_cell_name = day + cellH +cellM;
                        //(TextView) findViewWithTag(delete_cell_name);
                        //GridLayout gridLayout2 = (GridLayout)findViewById(R.id.gridlayout_timetable);
                        TextView deleteCell = gridLayout2.findViewWithTag(delete_cell_name);
                        gridLayout2.removeView(deleteCell);
                        //gridLayout2.removeView(gridLayout2.findViewWithTag(delete_cell_name));
                    }

                    hour=(startT-1)/2+8;
                    min=((startT-1)%2);
                    if(hour<10)
                        cellH="0"+hour;
                    else cellH=""+hour;
                    if(min==0)
                        cellM="00";
                    else cellM="30";
                    String IDofSpanCell = day + cellH +cellM;//span해야할 cell의 id
                    TextView spanCell = (TextView) gridLayout2.findViewWithTag(IDofSpanCell);//span cell
                    GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)spanCell.getLayoutParams();
                    layoutParams.columnSpec = GridLayout.spec(dayToNum(day));
                    layoutParams.rowSpec = GridLayout.spec(startT,endT-startT);//병합할 셀 수 정함
                    spanCell.setLayoutParams(layoutParams);//적용
                    layoutParams.setGravity(Gravity.FILL);//gravity 설정
                    spanCell.setLayoutParams(layoutParams);//다시 적용

                }
            }
        });*/
    }

    /*
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult data)
                {
                    final Global global = (Global)getApplication();
                    Log.d("TAG", "data : " + data);
                    if (data.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent intent = data.getData();
                        result = intent.getStringExtra ("result");
                        startT=global.getStartRow();
                        endT=global.getEndRow();

                        /*
                        for(int j = startT; j< endT; ++j)
                        {
                            hour=(j-1)/2+8;
                            min=((j-1)%2);
                            if(hour<10)
                                cellH="0"+hour;
                            else cellH=""+hour;
                            if(min==0)
                                cellM="00";
                            else cellM="30";
                            String delete_cell_name = result + cellH +cellM;
                            TextView deleteCell = returnTV(delete_cell_name);
                            GridLayout gridLayout2 = (GridLayout)view.findViewById(R.id.gridlayout_timetable);
                            gridLayout2.removeView(deleteCell);
                        }


                    }
                }
            });*/

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