package com.hzyc.csj.demo_08txlu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView addperson,personphoto;
    private TextView name,phonenum,note,ssperson;
    private ListView listView;
    private Button searchByName;
    private List<Map<String,Object>> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenSqlite os = new OpenSqlite(MainActivity.this);
        final SQLiteDatabase sqLiteDatabase = os.getReadableDatabase();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter());
        //添加用户图片
        addperson = (ImageView) findViewById(R.id.addperson);
        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.addperson,null);
                builder.setTitle("添加联系人")
                        .setView(view1)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                name = (TextView) view1.findViewById(R.id.name);
                                phonenum = (TextView) view1.findViewById(R.id.phonenum);
                                note = (TextView) view1.findViewById(R.id.note);
                                String nameV = name.getText().toString().trim();
                                String phonenumV = phonenum.getText().toString().trim();
                                String noteV = note.getText().toString().trim();
                                sqLiteDatabase.execSQL("insert into Persons(name,phonenum,note) values (?,?,?)",new String[]{nameV,phonenumV,noteV});
                                listView.setAdapter(new MyAdapter());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                personphoto = (ImageView)view1.findViewById(R.id.personphoto);
                personphoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChoosePicDialog();
                    }
                });

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.onemore,null);
                TextView name = (TextView) view2.findViewById(R.id.onename);
                TextView phone = (TextView) view2.findViewById(R.id.onephone);
                ImageView call = (ImageView) view2.findViewById(R.id.call);
                ImageView message = (ImageView) view2.findViewById(R.id.message);
                name.setText(getList().get(position).get("name").toString());
                phone.setText(getList().get(position).get("phonenum").toString());
                final String phonenumV = getList().get(position).get("phonenum").toString();
                builder.setView(view2)
                        .setTitle("联系人信息")
                        .setNeutralButton("返回",null)
                        .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                final View view3 = LayoutInflater.from(MainActivity.this).inflate(R.layout.addperson,null);
                                final TextView uname = (TextView) view3.findViewById(R.id.name);
                                final TextView uphonenum = (TextView) view3.findViewById(R.id.phonenum);
                                final TextView unote = (TextView) view3.findViewById(R.id.note);
                                uname.setText(getList().get(position).get("name").toString());
                                uphonenum.setText(getList().get(position).get("phonenum").toString());
                                unote.setText(getList().get(position).get("note").toString());
                                builder1.setView(view3)
                                        .setTitle("修改信息")
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String nameV = uname.getText().toString().trim();
                                                String phonenumV = uphonenum.getText().toString().trim();
                                                String noteV = unote.getText().toString().trim();
                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("name",nameV);
                                                contentValues.put("phonenum",phonenumV);
                                                contentValues.put("note",noteV);
                                                int i = sqLiteDatabase.update("Persons",contentValues,"id=?",new String[]{getList().get(position).get("id").toString()});
                                                if(i==1){
                                                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNeutralButton("返回", null)
                                        .show();
                            }
                        })
                        .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = getList().get(position).get("id").toString();
                                int i = sqLiteDatabase.delete("Persons","id=?",new String[]{id});
                                if(i==1){
                                    Toast.makeText(MainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                }
                                listView.setAdapter(new MyAdapter());
                            }
                        })
                        .show();
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phonenumV));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    }
                });
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" +phonenumV ));
                        // 如果需要将内容传过去增加如下代码
                        //intent.putExtra("sms_body","哈哈哈");
                        startActivity(intent);
                    }
                });
                return true;
            }
        });
        ssperson = (TextView) findViewById(R.id.ssperson);
        searchByName = (Button) findViewById(R.id.seachByName);
        searchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameV = "%"+ssperson.getText().toString().trim()+"%";
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Persons where name like ?",new String[]{nameV});
                list1 = getListByCursor(cursor);
                Log.i("测试",list1.size()+"");
                listView.setAdapter(new MyAdapterTwo());
            }
        });

    }
    public List<Map<String,Object>> getListByCursor(Cursor cursor){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while (cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            int columnum = cursor.getColumnCount();
            for (int i =0;i<columnum;i++){
                map.put(cursor.getColumnName(i),cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i))));
            }
            list.add(map);
        }
        Log.i("测试List",list.size()+"");
        return  list;
    }
    public class MyAdapterTwo extends BaseAdapter{

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Object getItem(int position) {
            return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView==null){
                view = MainActivity.this.getLayoutInflater().inflate(R.layout.oneperson,null);
            }else{
                view = convertView;
            }
            ImageView oneImage = (ImageView)view.findViewById(R.id.imageView);
            TextView oneName = (TextView) view.findViewById(R.id.name);
            Object photo = list1.get(position).get("photo");
            String name = list1.get(position).get("name").toString();
            oneName.setText(name);
            if (photo==null){

            }else{

            }
            return view;
        }
    }
    public List<Map<String,Object>> getList(){
        OpenSqlite os = new OpenSqlite(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = os.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("Persons",new String[]{"id","name","phonenum","note","photo"},null,null,null,null,null);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while (cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            int columnum = cursor.getColumnCount();
                for (int i =0;i<columnum;i++){
                    map.put(cursor.getColumnName(i),cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i))));
                }
            list.add(map);
        }
        return list;
    }
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return getList().size();
        }

        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView==null){
                view = MainActivity.this.getLayoutInflater().inflate(R.layout.oneperson,null);
            }else{
                view = convertView;
            }
            ImageView oneImage = (ImageView)view.findViewById(R.id.imageView);
            TextView oneName = (TextView) view.findViewById(R.id.name);
            Object photo = getList().get(position).get("photo");
            String name = getList().get(position).get("name").toString();
            oneName.setText(name);
            if (photo==null){

            }else{

            }
            return view;
        }
    }
    //选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.one,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //单机事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.psetting:
                Toast.makeText(MainActivity.this, "点击联系人设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.csetting:
                Toast.makeText(MainActivity.this, "点击通话设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pdelete:
                Toast.makeText(MainActivity.this, "点击删除联系人", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //实现弹出窗口选择照片来源
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    //接收来源得到并显示图片
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    //十字框裁剪图片
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    //裁剪完成保存图片到ImageView中圆形显示
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            personphoto.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        Log.i("文件路径1","@@@@@@"+Environment.getExternalStorageDirectory().getAbsolutePath());//获得文件路径
        String imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("文件路径2", "@@@@@@@"+imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }
    }
}
