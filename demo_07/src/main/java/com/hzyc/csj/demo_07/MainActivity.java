package com.hzyc.csj.demo_07;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button save,read;
    private TextView username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save = (Button) findViewById(R.id.save);
        read = (Button) findViewById(R.id.read);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        //内存存储 xml
        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unV = username.getText().toString().trim();
                String pwV = password.getText().toString().trim();
                if(unV.equals("")||pwV.equals("")){
                    Toast.makeText(MainActivity.this, "您的信息不完善，请完善", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("data", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", unV);
                    editor.putString("password", pwV);
                    boolean bol = editor.commit();
                    Toast.makeText(MainActivity.this, "Save" + bol, Toast.LENGTH_SHORT).show();
                }
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data",0);
                String unV = sharedPreferences.getString("username","没有存储用户名");
                String pwV = sharedPreferences.getString("password","还没有存密码");
                Toast.makeText(MainActivity.this, unV+"!!!!"+pwV, Toast.LENGTH_SHORT).show();
            }
        });*/
        //IO流存储
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unV = username.getText().toString().trim();
                String pwV = password.getText().toString().trim();
                if(unV.equals("")||pwV.equals("")){
                    Toast.makeText(MainActivity.this, "您还有信息未完善，请完善", Toast.LENGTH_SHORT).show();
                }else{
                    FileOutputStream output = null;
                    try {
                        output = openFileOutput("data.txt",0);
                        String str = unV+"@@@@"+pwV;
                        output.write(str.getBytes());
                        Toast.makeText(MainActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream input = null;
                try {
                    input = openFileInput("data.txt");
                   InputStreamReader reader = new InputStreamReader(input);
                    BufferedReader buf = new BufferedReader(reader);
                    String str = null;
                    String message="";
                    while((str=buf.readLine())!=null){
                        message +=str;
                    }
                    String unV = message.split("@@@@")[0];
                    String pwV = message.split("@@@@")[1];

                    Toast.makeText(MainActivity.this, "用户名:"+unV+";密码:"+pwV, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
