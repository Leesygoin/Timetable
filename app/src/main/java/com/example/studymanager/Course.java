package com.example.studymanager;

import com.google.firebase.firestore.Exclude;

public class Course {
    private String day="";
    private int sTime=0;
    private int eTime=0;
    private boolean applied = false;
    private String documentId="";

    private int color = 0;

    public Course() {
        //
    }

    public Course(String day, int sTime, int eTime, boolean applied, int color){
        this.day=day;
        this.eTime=eTime;
        this.sTime=sTime;
        this.applied = applied;
        this.color = color;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getsTime() {
        return sTime;
    }

    public void setsTime(int sTime) {
        this.sTime = sTime;
    }

    public int geteTime() {
        return eTime;
    }

    public void seteTime(int eTime) {
        this.eTime = eTime;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
