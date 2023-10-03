package com.example.studymanager;

import android.app.Application;

public class Global extends Application {

    private static String tmp;
    private static int colNum;

    @Override
    public void onCreate() {
        super.onCreate();
        tmp="";
        colNum=0;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //instance =null;
    }

    public void Init() {
        tmp="";
        colNum=0;
    }

    public static void setTmp(String tmp) {
        Global.tmp = tmp;
    }

    public static String getTmp() {
        return tmp;
    }

    public static void setColNum(int colNum) {
        Global.colNum = colNum;
    }

    public static int getColNum() {
        return colNum;
    }
}
