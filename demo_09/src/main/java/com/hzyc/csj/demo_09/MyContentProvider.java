package com.hzyc.csj.demo_09;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by 小柿子 on 2018/7/19.
 */
public class MyContentProvider extends ContentProvider {
    private OpenSqlite openSqlite;
    //创建了当前的CP的 uri访问地址集合 默认里面没有任何地址
    private static final UriMatcher URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    //只要CP被注册 默认最先加载
    static {
/*        uri
        content:// 在设置URI的时候不需要加
        在使用cr访问cp的时候 必须压加载uri的字前面
        设置的URI    csj.hzyc.09.person / person /10
        访问的时候   content://csj.hzyc.009.person
        com.hzyc.csj.demo_09.MyContentProvider/通配的/通配的
        #  通配数字
                *  通配字符串*/
        //使用的时候要加content
        URIMATCHER.addURI("com.hzyc.csj.demo_09.MyContentProvider","persons",1);
        URIMATCHER.addURI("com.hzyc.csj.demo_09.MyContentProvider","persons/#",2);
        URIMATCHER.addURI("com.hzyc.csj.demo_09.MyContentProvider","persons/#/*",3);
        //使用的时候  content://com.hzyc.csj.demo_09.MyContentProvider/person   == 1 整个person表
        //使用的时候  content://com.hzyc.csj.demo_09.MyContentProvider/person/10   == 2 id == 10
        //使用的时候  content://com.hzyc.csj.demo_09.MyContentProvider/person/10/name   == 3 id == 10 name列
    }
    @Override
    public boolean onCreate() {
        openSqlite=new OpenSqlite(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int resultCode = URIMATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = openSqlite.getReadableDatabase();
        switch (resultCode){
            case 1:
                break;
            case 2:
                Cursor cursor = sqLiteDatabase.query("persons",projection,selection,selectionArgs,null,null,sortOrder);
               return cursor;
            case 3:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int resultCode = URIMATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = openSqlite.getReadableDatabase();
        Log.i("uri","@@@###$$$"+uri);
        Log.i("resultCode","@@@###$$$"+resultCode);
        switch (resultCode){
            case 1 :
                long i = sqLiteDatabase.insert("persons",null,values);
                Uri newUri = ContentUris.withAppendedId(uri,i);
                Log.i("newUri","@@##$$"+newUri);
                return  newUri;
            case  2 :
                break;
            case  3 :
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int resultCode = URIMATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = openSqlite.getReadableDatabase();
        switch (resultCode){
            case 1 :
                break;
            case 2 :
                int i = sqLiteDatabase.delete("persons",selection,selectionArgs);
                return i;
            case 3 :
                break;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int resultCode = URIMATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = openSqlite.getReadableDatabase();
        switch (resultCode){
            case 1 :
                break;
            case 2 :
                break;
            case 3 :
                int i = sqLiteDatabase.update("persons",values,selection,selectionArgs);
                return i;
        }
        return 0;
    }
}
