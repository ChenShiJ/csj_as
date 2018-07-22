package com.hzyc.csj.demo_06;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
        弹出菜单
 */
public class Main3Activity extends AppCompatActivity {
    private Button toMain4,showMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toMain4 = (Button) findViewById(R.id.toMain4);
        showMenu = (Button)findViewById(R.id.showMenu);
        toMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                startActivity(intent);
            }
        });
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Main3Activity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.one,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.ms:
                                Toast.makeText(Main3Activity.this, "点击秒杀", Toast.LENGTH_SHORT).show();
                                break;
                            case  R.id.qc:
                                Toast.makeText(Main3Activity.this, "点击清除", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tj:
                                Toast.makeText(Main3Activity.this, "点击特价", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

}
