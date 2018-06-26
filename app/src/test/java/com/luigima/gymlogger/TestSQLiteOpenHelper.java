package com.luigima.gymlogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestSQLiteOpenHelper extends SQLiteOpenHelper{
    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME = "gymlogger.db";// Database name
    private SQLiteDatabase mDataBase;

    public TestSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);// 1? its Database Version
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
