package com.hzyc.csj.demo_07;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 小柿子 on 2018/7/15.
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
        db.execSQL("create table Goods (id integer primary key autoincrement,code varchar(10),name varchar(10),number int(10),price double)");
        bol = true;
        Log.i("数据库状态","Create"+bol);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
