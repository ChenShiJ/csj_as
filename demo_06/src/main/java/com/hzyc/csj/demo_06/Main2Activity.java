package com.hzyc.csj.demo_06;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/*
    上下文菜单
        1.设计菜单xml
        2.实现创建菜单的方法
        3.实现菜单选择的方法
        4.不会自动显示的 注册（onCreate） 注册到 列表上（listView）
 */
public class Main2Activity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ActionMode actionMode;
    private Button toMain3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,getList());
        listView.setAdapter(arrayAdapter);
        //registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode!=null){
                    return false;
                }
                    actionMode = Main2Activity.this.startActionMode(callback);
                return true;
            }
        });
        toMain3 = (Button) findViewById(R.id.toMain3);
        toMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent);
            }
        });
    }
    public List<String> getList(){
        List<String> list = Arrays.asList("数据1","数据2","数据3","数据4","数据5");
        return list;
    }
//    //第一种
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.one,menu);
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.ms:
//                Toast.makeText(Main2Activity.this, "点击秒杀", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.qc:
//                Toast.makeText(Main2Activity.this, "点击清除", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.tj:
//                Toast.makeText(Main2Activity.this, "点击特价", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

    //第二种 ActionMode
    private ActionMode.Callback callback = new ActionMode.Callback(){

        //创建操作模式时调用
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.one,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.ms:
                    Toast.makeText(Main2Activity.this, "点击秒杀", Toast.LENGTH_SHORT).show();
                    break;
                case  R.id.qc:
                    Toast.makeText(Main2Activity.this, "点击清除", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tj:
                    Toast.makeText(Main2Activity.this, "点击特价", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode=null;
        }
    };
}
