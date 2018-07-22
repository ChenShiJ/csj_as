package com.hzyc.csj.demo_07;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteActicity extends AppCompatActivity {
    private Button insert,update,delete,query,selId,cursorToList,toContentProvider,read09;
    private TextView name,price,number,code,deleteId,sel_code,u_name,u_code,u_price,u_num;
    private ListView listView;
    private ArrayAdapter<List<Map<String,Object>>> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_acticity);
        insert = (Button) findViewById(R.id.insert);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        query = (Button) findViewById(R.id.query);
        cursorToList = (Button) findViewById(R.id.cursorToList);
        toContentProvider = (Button) findViewById(R.id.toContentProvider);

        insert.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SqliteActicity.this);
                final View view1 = LayoutInflater.from(SqliteActicity.this).inflate(R.layout.activity_insert, null);
                builder.setTitle("添加商品")
                        .setView(view1)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //想要获取自定义布局中的控件必须调用你所定义的View的findViewById方法，而不能像获得其他控件一样直接调用findViewById方法
                                //！！！！！
                                name = (TextView) view1.findViewById(R.id.name);
                                code = (TextView)  view1.findViewById(R.id.code);
                                number = (TextView) view1.findViewById(R.id.number);
                                price = (TextView)  view1.findViewById(R.id.price);

                                String codeV = code.getText().toString().trim();
                                String nameV = name.getText().toString().trim();
                                String numberV = number.getText().toString().trim();
                                String priceV = price.getText().toString().trim();
                                //Toast.makeText(SqliteActicity.this, codeV, Toast.LENGTH_SHORT).show();

                                OpenSqlite os =new OpenSqlite(SqliteActicity.this);
                                SQLiteDatabase sqliteDatebase = os.getReadableDatabase();
                                sqliteDatebase.execSQL("insert into Goods(code,name,number,price) values(?,?,?,?)",new String[]{codeV,nameV,numberV,priceV});
                            }
                        })
                        .show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(SqliteActicity.this);
                final View view2 = LayoutInflater.from(SqliteActicity.this).inflate(R.layout.delete,null);
                builder.setTitle("删除操作")
                        .setView(view2)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteId = (TextView) view2.findViewById(R.id.deleteId);
                                String gid = deleteId.getText().toString().trim();
                                //Toast.makeText(SqliteActicity.this, ""+gid, Toast.LENGTH_SHORT).show();
                                OpenSqlite os = new OpenSqlite(SqliteActicity.this);
                                SQLiteDatabase sqLiteDatabase = os.getReadableDatabase();
                                sqLiteDatabase.execSQL("delete from Goods where code = ?",new String[]{gid});
                            }
                        })
                        .show();
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SqliteActicity.this);
                final View view3 =LayoutInflater.from(SqliteActicity.this).inflate(R.layout.select,null);
                listView = (ListView) view3.findViewById(R.id.listView);
                listView.setAdapter(new MyAdapter());
                builder.setTitle("商品信息")
                        .setView(view3)
                        .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSqlite os = new OpenSqlite(SqliteActicity.this);
                final SQLiteDatabase sqLiteDatabase = os.getReadableDatabase();
                AlertDialog.Builder builder = new AlertDialog.Builder(SqliteActicity.this);
                final View view4 =LayoutInflater.from(SqliteActicity.this).inflate(R.layout.update,null);
                builder.setTitle("修改信息")
                        .setView(view4);
                selId = (Button) view4.findViewById(R.id.selById);
                u_name = (TextView) view4.findViewById(R.id.u_name);
                u_code = (TextView) view4.findViewById(R.id.u_code);
                u_num = (TextView) view4.findViewById(R.id.u_num);
                u_price = (TextView) view4.findViewById(R.id.u_price);
                sel_code = (TextView) view4.findViewById(R.id.sel_code);
                selId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sel_codeV = sel_code.getText().toString().trim();
                        //Toast.makeText(SqliteActicity.this, sel_codeV, Toast.LENGTH_SHORT).show();
                        Cursor cursor = sqLiteDatabase.rawQuery("select * from Goods where code = ?",new String[]{sel_codeV});
                        while(cursor.moveToNext()) {
                            u_num.setText(cursor.getString(cursor.getColumnIndex("number")));
                            u_name.setText(cursor.getString(cursor.getColumnIndex("name")));
                            u_code.setText(cursor.getString(cursor.getColumnIndex("code")));
                            u_price.setText(cursor.getString(cursor.getColumnIndex("price")));
                        }
                    }
                });
                builder.setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String u_numV = u_num.getText().toString().trim();
                        String u_codeV = u_code.getText().toString().trim();
                        String u_priceV = u_price.getText().toString().trim();
                        String u_nameV =u_name .getText().toString().trim();
                        //Toast.makeText(SqliteActicity.this, u_numV, Toast.LENGTH_SHORT).show();
                        sqLiteDatabase.execSQL("update Goods set code=?,name=?,price=?,number=? where code = ?",new String[]{u_codeV,u_nameV,u_priceV,u_numV,sel_code.getText().toString().trim()});
                    }
                })
                        .show();
            }
        });
        cursorToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = new OpenSqlite(SqliteActicity.this).getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query("Goods",new String[]{"id","name","code","number","price"},null,null,null,null,null);
                List<Map<String,Object>> list  = getList(cursor);
               Log.i("记录数",list.size()+"");
                Log.i("第一条记录的name",list.get(0).get("name").toString());
            }
        });


        //获取通讯录
        toContentProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver cr = getContentResolver();
                Cursor cursor1 = cr.query(ContactsContract.Contacts.CONTENT_URI,new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME},null,null,null);
                Cursor cursor =cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null);
                /*while (cursor.moveToNext()&&cursor1.moveToNext()) {
                    Log.i("联系人", cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID)) + "@@@@" + cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Log.i("联系人",cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+"@@@@"+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                }*/
                List<Map<String,Object>> list = getContractList(cursor,cursor1);
                Log.i("List大小",list.size()+"");
                Log.i("测试",list.get(0).get(ContactsContract.Contacts.DISPLAY_NAME).toString()+"@@@@"+list.get(0).get(ContactsContract.CommonDataKinds.Phone.NUMBER).toString());
            }
        });
        read09 = (Button) findViewById(R.id.read09);
        read09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "content://com.hzyc.csj.demo_09.MyContentProvider/persons/16";
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(Uri.parse(uri),new String[]{"name","age"},"id=?",new String[]{"10"},null);
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String age = cursor.getString(cursor.getColumnIndex("age"));
                    Log.i("查询信息",name+"@@"+age);
                }
            }
        });
    }
    //整合Name和Phonenum
    public List<Map<String,Object>> getList(Cursor cursor){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()){
            int colnum = cursor.getColumnCount();
            //Log.i("列数",colnum+"");
            //Log.i("列索引",cursor.getColumnIndex("name")+"");
            Map<String,Object> map = new HashMap<String,Object>();
            for(int i=0;i<colnum;i++){
                map.put(cursor.getColumnName(i),cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i))));
                //Log.i("列名",cursor.getColumnName(i));
            }
            list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getContractList(Cursor cursor,Cursor cursor1){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        while(cursor.moveToNext()&&cursor1.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put(cursor1.getColumnName(cursor1.getColumnIndex(ContactsContract.Contacts._ID)),cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID)));
            map.put(cursor1.getColumnName(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            map.put(cursor.getColumnName(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getCursor(){
        OpenSqlite os = new OpenSqlite(SqliteActicity.this);
        SQLiteDatabase sqliteDatebase = os.getReadableDatabase();
        Cursor cursor = sqliteDatebase.rawQuery("select * from Goods",null);
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("id","编号");
        map1.put("name","商品名称");
        map1.put("code","商品编号");
        map1.put("price","单价");
        map1.put("number","数量");
        list.add(map1);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",id);
            map.put("name",name);
            map.put("code",code);
            map.put("price",price);
            map.put("number",number);
            list.add(map);
        }
        return list;
    }
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return getCursor().size();
        }

        @Override
        public Object getItem(int position) {
            return getCursor().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView==null){
                view = SqliteActicity.this.getLayoutInflater().inflate(R.layout.one,null);
            }else{
                view = convertView;
            }
            TextView s_id = (TextView) view.findViewById(R.id.s_id);
            TextView s_code = (TextView) view.findViewById(R.id.s_code);
            TextView s_name = (TextView) view.findViewById(R.id.s_name);
            TextView s_number = (TextView) view.findViewById(R.id.s_number);
            TextView s_price = (TextView) view.findViewById(R.id.s_price);
            s_id.setText(getCursor().get(position).get("id").toString());
            s_code.setText(getCursor().get(position).get("code").toString());
            s_name.setText(getCursor().get(position).get("name").toString());
            s_number.setText(getCursor().get(position).get("number").toString());
            s_price.setText(getCursor().get(position).get("price").toString());
            return view;
        }
    }

}
