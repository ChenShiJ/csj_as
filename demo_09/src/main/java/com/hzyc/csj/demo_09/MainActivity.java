package com.hzyc.csj.demo_09;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button getContracts,initSqlite,addByCp,searchByCp,updateByCp,deleteByCp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenSqlite os = new OpenSqlite(MainActivity.this);
        final SQLiteDatabase sqLiteDatabase = os.getReadableDatabase();
        getContracts = (Button) findViewById(R.id.getContracts);
        //获取通讯录
        getContracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME},null,null,null);
                Cursor cursor1 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null);
                List<Map<String,Object>> list = getContractList(cursor,cursor1);
                for(Map map : list){
                    Log.i("联系人",map.get(ContactsContract.Contacts.DISPLAY_NAME).toString()+"@@@@"+map.get(ContactsContract.CommonDataKinds.Phone.NUMBER).toString());
                }
            }
        });
        initSqlite = (Button) findViewById(R.id.initSqlite);
        initSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase.execSQL("insert into persons (name,age) values (?,?)",new String[]{"张三","20"});
                sqLiteDatabase.execSQL("insert into persons (name,age) values (?,?)",new String[]{"李四","40"});
                sqLiteDatabase.execSQL("insert into persons (name,age) values (?,?)",new String[]{"王五","30"});
            }
        });
        addByCp = (Button) findViewById(R.id.addByCp);
        addByCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "content://com.hzyc.csj.demo_09.MyContentProvider/persons";
                ContentResolver cr = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put("name","小明");
                cv.put("age","18");
                Uri uri1 = cr.insert(Uri.parse(uri),cv);
                Log.i("uri1","@#$"+uri1);
            }
        });
        searchByCp = (Button) findViewById(R.id.searchByCp);
        searchByCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "content://com.hzyc.csj.demo_09.MyContentProvider/persons/16";
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(Uri.parse(uri),new String[]{"id","name","age"},"id=?",new String[]{"16"},null);
                while (cursor.moveToNext()){
                    Log.i("16行记录",cursor.getString(cursor.getColumnIndex("id"))+"@@"+cursor.getString(cursor.getColumnIndex("name"))+"##"+cursor.getString(cursor.getColumnIndex("age")));
                }
            }
        });
        updateByCp = (Button) findViewById(R.id.updateByCp);
        updateByCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "content://com.hzyc.csj.demo_09.MyContentProvider/persons/15/name";
                ContentValues contentValues = new ContentValues();
                contentValues.put("name","大明");
                ContentResolver cr = getContentResolver();
                int i = cr.update(Uri.parse(uri),contentValues,"id=?",new String[]{"15"});
                Log.i("修改影响行数",i+"");
            }
        });
        deleteByCp = (Button) findViewById(R.id.deleteByCp);
        deleteByCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "content://com.hzyc.csj.demo_09.MyContentProvider/persons/14";
                ContentResolver contentResolver = getContentResolver();
                int i = contentResolver.delete(Uri.parse(uri),"id=?",new String[]{"14"});
                if(i==1){
                    Log.i("删除操作","成功");
                }
            }
        });
    }
    //整合Name+Phonenum
    public List<Map<String,Object>> getContractList(Cursor cursor,Cursor cursor1){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while(cursor.moveToNext()&&cursor1.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put(cursor.getColumnName(cursor.getColumnIndex(ContactsContract.Contacts._ID)),cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
            map.put(cursor.getColumnName(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            map.put(cursor1.getColumnName(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            list.add(map);
        }
        return list;
    }
}
