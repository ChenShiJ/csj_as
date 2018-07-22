package com.hzyc.csj.demo_06;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button toMain2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toMain2 = (Button) findViewById(R.id.toMain2);
        toMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right,R.anim.left);
            }
        });
    }
    //创建一个选项菜单
    //不需注册（默认显示）

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();//菜单填充器
        menuInflater.inflate(R.menu.one,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //创建单击事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.ms:
                Toast.makeText(MainActivity.this, "点击了秒杀", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.qc:
                Toast.makeText(MainActivity.this, "点击了清除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tj:
                Toast.makeText(MainActivity.this, "点击了特价", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
