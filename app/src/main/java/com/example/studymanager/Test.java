package com.example.studymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Test extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseRef = db.document("User/Class").collection("cInfo");
    int startT, endT;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final Global global = (Global) getApplication();

        Button testButton = findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent myIntent = new Intent(MainActivity.this, TimeTable2.class);
                Intent myIntent = new Intent(Test.this, DialogActivity.class);
                startActivity(myIntent);
                //finish();
            }
        });

        if (global.getFlag() == 1) {
            //startT=global.getStartRow();
            //endT=global.getEndRow();
            //day=global.getColNum();

            courseRef.document("math").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                /*Map<String, Object> course = new HashMap<>();
                                course = documentSnapshot.getData();
                                startT = Integer.parseInt(String.valueOf(course.get(KEY_STARTTIME)));
                                endT = Integer.parseInt(String.valueOf(course.get(KEY_ENDTIME)));*/

                                Course course = documentSnapshot.toObject(Course.class);
                                day = course.getDay();
                                startT = course.getsTime();
                                endT = course.geteTime();


                                TextView dText = findViewById(R.id.day_tv);
                                TextView sTimeText = findViewById(R.id.sTime_tv);
                                TextView eTimeText = findViewById(R.id.eTime_tv);

                                dText.setText(day);
                                sTimeText.setText(""+startT);
                                eTimeText.setText(""+endT);
                            } else {
                                Toast.makeText(Test.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Test.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }
}