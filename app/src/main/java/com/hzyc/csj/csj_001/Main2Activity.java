package com.hzyc.csj.csj_001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    EditText username,password;
    Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        username = (EditText) findViewById(R.id.username);
        password = (EditText)findViewById(R.id.passwrod);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        login.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((username.getText().toString()).equals("admin")&&(password.getText().toString()).equals("admin")){
                    Toast.makeText(Main2Activity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Main2Activity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
