package com.hzyc.csj.demo_05;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    private Button toMain4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toMain4 = (Button) findViewById(R.id.toMain4);
        toMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right,R.anim.left);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.one,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.save :
                Toast.makeText(Main3Activity.this, "保存", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.delete :
                Toast.makeText(Main3Activity.this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sao :
                Toast.makeText(Main3Activity.this, "扫一扫", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
