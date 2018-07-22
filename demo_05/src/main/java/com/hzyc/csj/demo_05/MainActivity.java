package com.hzyc.csj.demo_05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button toMain2;
    private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //onCreate 里的只加载一次
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        toMain2 = (Button) findViewById(R.id.toMain2);

        toMain2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unValue = username.getText().toString().trim();
                String pwValue = password.getText().toString().trim();
                /*
                    传值的方式
                    1.可以用意图Intent 进行传值 键值队 key-value
                    2.类似于Javabean模式  需要通过统一bean封装一些数据
                            Bundle
                            intent.putExtras
                 */
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",unValue);
                bundle.putString("password",pwValue);
                intent.putExtras(bundle);
                /*
                    跳转
                    两种  1 startActivity(intent)  非安全跳转
                          2 startActivityForResult(intent,0); //开始一个activity 并得到结果  激活一个函数 默认父类中自带的  主要的作用是接收 setResult的数据
                */

                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==200){
            String backValue = data.getStringExtra("backValue");
            Toast.makeText(MainActivity.this, backValue, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String unValue = username.getText().toString().trim();
        if("".equals(unValue)){
            Toast.makeText(MainActivity.this, "不需要保护", Toast.LENGTH_SHORT).show();
        }else{
            //存储到xml
            //手机应用内部（06） 是否存在data.xml  不存在创建data  存在会找到data获取
            //Android Device Monitor -->data-->data-->demo_05 shared_prefs--> data.xml -->pull a file
            SharedPreferences spf = getSharedPreferences("data",0);
            //得到这个data编辑权限
            SharedPreferences.Editor editer = spf.edit();
            editer.putString("username",unValue);
            boolean bol = editer.commit();
            Toast.makeText(MainActivity.this, "需要保护"+bol, Toast.LENGTH_SHORT).show();
        }
    }
}
