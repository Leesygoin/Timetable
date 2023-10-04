package com.example.studymanager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

public class TimeTable2 extends AppCompatActivity {

    Button addDate_button;
    int startT, endT, min, hour;
    static int check[][] = new int[6][27];
    String cellH, cellM, day;
    View.OnClickListener clickListener;
    //View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table3);

        final Global global = (Global)getApplication();

        addDate_button = (Button)findViewById(R.id.addDButton);

        if(global.getFlag()==1)
        {
            //Toast.makeText(getApplicationContext(), "FlagSuccess", Toast.LENGTH_LONG).show();
            startT=global.getStartRow();
            endT=global.getEndRow();
            day=global.getColNum();

            int tmp=0;
            for(int j=startT; j<=endT; ++j)
            {
                if(check[dayToNum(day)][j]>0)
                    ++tmp;
            }
            if(tmp>0)
            {
                Toast.makeText(getApplicationContext(), "시간 중복", Toast.LENGTH_LONG).show();
                return;
            }

            else
            {
                for(int j=startT; j<=endT; ++j)
                {
                    ++check[dayToNum(day)][j];

                }
            }



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

                spanCell.setText(global.getClassName());
                spanCell.setTextColor(Color.WHITE);
                spanCell.setBackground(getResources().getDrawable(R.drawable.fill_cell));
                spanCell.setOnClickListener(clickListener);

                global.setFlag(0);
        }

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeTable2.this);
                AlertDialog ad = builder.create();
                //final View innerView = getLayoutInflater().inflate(R.layout.activity_add_day_view, null);
                //final EditText Cname = findViewById(R.id.className);

                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("삭제하시겠습니까?");
                //ad.setView(innerView);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                        startActivity(new Intent(TimeTable2.this, TimeTable2.class));
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
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