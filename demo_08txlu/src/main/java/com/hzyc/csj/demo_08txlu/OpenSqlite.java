package com.hzyc.csj.demo_08txlu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 小柿子 on 2018/7/18.
 */
public class OpenSqlite extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "data.db";

    public OpenSqlite(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        boolean bol =false;
        db.execSQL("create table Persons(id integer primary key autoincrement,name varchar(10),phonenum varchar(20),note varchar(30),photo varchar(100))");
        bol=true;
        Log.i("数据库状态",bol+"");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
