package com.hzyc.csj.demo_09;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 小柿子 on 2018/7/19.
 */
public class OpenSqlite extends SQLiteOpenHelper {
    private static final String NAME = "data.db";
    private static final int VERSION = 1;
    public OpenSqlite(Context context) {
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        boolean bol = false;
        db.execSQL("create table persons(id integer primary key autoincrement,name varchar(20),age varchar(10)) ");
        bol=true;
        Log.i("数据库状态","创建"+bol);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
