package com.hzyc.csj.demo_10;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    private Button register,login;
    private ProgressDialog progressDialog;
    private TextView username,password;
    private static final String RPATH="http://192.168.1.166:8080/csj_web_android_001/RegisterServlet";
    private static final String LPATH="http://192.168.1.166:8080/csj_web_android_001/LoginServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);

        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setTitle("加载中");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待。。。");

        //注册
        register.setOnClickListener(registerClick);
        //登录
        login.setOnClickListener(loginClick);
    }
    /*
        第一个参数   表示送进任务的中的请求地址和数据  可以是一个或多个
        第二个参数   表示可更新进度 进度使用  数据去控制  进度条是水平的 如果进度条是 圆形的第二个参数没有用Void...
        第三个参数   表示异步任务在web端获取的数据类型  获取的是一个字符串  用String来接受
     */
    class myTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                //建立web端的连接
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                /*
                    GET 相当于是超链接传值，传汉字时会乱码
                    POST 方式 几种格式来的？  1.key-value  2.流模式  3.文本模式  enctype
                    建立之后进行post传值 建立之前get传值
                 */
                hc.setReadTimeout(5000);
                OutputStream output = hc.getOutputStream();
                String values = "username="+params[1]+"&password="+params[2];
                output.write(values.getBytes());
                output.close();
                //接收web端数据
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer stringBuffer = new StringBuffer();
                while((str=br.readLine())!=null){
                    stringBuffer.append(str);
                }
                return stringBuffer.toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("Gson",s);
            if(s.endsWith("注册")) {
                if (s.startsWith("1")) {
                    Toast.makeText(Main2Activity.this,"注册成功", Toast.LENGTH_SHORT).show();
                }else if(s.startsWith("0")){
                    Toast.makeText(Main2Activity.this,"对不起，该帐号已存在，请重新注册", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Main2Activity.this, "对不起，系统忙，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }else if(s.endsWith("登录")){
                if(s.startsWith("1")){
                    Toast.makeText(Main2Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right,R.anim.left);
                }else if(s.startsWith("0")){
                    Toast.makeText(Main2Activity.this, "对不起,您的用户名或者密码不正确，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();
        }
    }
    private View.OnClickListener registerClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            final View view1 = LayoutInflater.from(Main2Activity.this).inflate(R.layout.register,null);
            builder.setTitle("注册页面")
                    .setIcon(R.mipmap.ic_launcher)
                    .setView(view1)
                    .setPositiveButton("注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextView un = (TextView)view1.findViewById(R.id.username);
                            String unV = un.getText().toString().trim();
                            TextView pw = (TextView) view1.findViewById(R.id.password);
                            TextView apw = (TextView) view1.findViewById(R.id.apassword);
                            String pwV = pw.getText().toString().trim();
                            String apwV = apw.getText().toString().trim();
                            if(unV.length()<=0){
                                Toast.makeText(Main2Activity.this, "对不起，用户名长度不能为空", Toast.LENGTH_SHORT).show();
                            }else if(pwV.equals("")){
                                Toast.makeText(Main2Activity.this, "对不起，您的密码不能为空", Toast.LENGTH_SHORT).show();
                            }else if(!apwV.equals(pwV)){
                                Toast.makeText(Main2Activity.this, "对不起，两次密码不一致", Toast.LENGTH_SHORT).show();
                            }else{
                               new myTask().execute(RPATH,unV,pwV);
                            }
                        }
                    })
                    .setNegativeButton("取消",null)
                    .show();
        }
    };
    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            username = (TextView) findViewById(R.id.username);
            password = (TextView) findViewById(R.id.password);
            String unV = username.getText().toString().trim();
            String pwV = password.getText().toString().trim();
            if(unV.equals("")){
                Toast.makeText(Main2Activity.this, "对不起，您还没有输入用户名", Toast.LENGTH_SHORT).show();
            }else if(pwV.equals("")){
                Toast.makeText(Main2Activity.this, "对不起，您还没有输入密码", Toast.LENGTH_SHORT).show();
            }else{
                new myTask().execute(LPATH,unV,pwV);
            }
        }
    };

}
