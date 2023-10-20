package com.example.studymanager;

import android.app.Application;

public class Global extends Application {

    private static String tmp, colNum, className;
    private static int startRow, endRow, flag=0, c;

    /*
    @Override
    public void onCreate() {
        super.onCreate();
        tmp="";
        colNum="";
        startRow=0;
        endRow=0;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //instance =null;
    }

    public void Init() {
        tmp="";
        colNum="";
        startRow=0;
        endRow=0;
    }*/

    public static void setTmp(String tmp) {
        Global.tmp = tmp;
    }

    public static String getTmp() {
        return tmp;
    }

    public static void setColNum(String colNum) {
        Global.colNum = colNum;
    }

    public static String getColNum() {
        return colNum;
    }

    public static void setStartRow(int startRow) {
        Global.startRow = startRow;
    }

    public static int getStartRow() {
        return startRow;
    }

    public static void setEndRow(int endRow) {
        Global.endRow = endRow;
    }

    public static int getEndRow() {
        return endRow;
    }

    public static void setFlag(int flag) {
        Global.flag = flag;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setClassName(String className) {
        Global.className = className;
    }

    public static String getClassName() {
        return className;
    }

    public static int getC() {
        return c;
    }

    public static void setC(int c) {
        Global.c = c;
    }
}
