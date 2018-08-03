package com.hzyc.csj.oedermealsystem;

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
    //private static final String REGPATH = "http://192.168.1.166:8080/csj_web_android_osystem/Register.do";
    private static final String REGPATH = "http://10.151.4.8:8080/csj_web_android_osystem/Register.do";
    //private static final String LOGINPATH="http://192.168.1.166:8080/csj_web_android_osystem/Login.do";
    private static final String LOGINPATH = "http://10.151.4.8:8080/csj_web_android_osystem/Login.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请稍候");

        login.setOnClickListener(logining);
        register.setOnClickListener(reg);
    }
    //注册
    private View.OnClickListener reg = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            final View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.register,null);
            builder.setTitle("注册信息")
                    .setView(view)
                    .setPositiveButton("注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String username = ((TextView) view.findViewById(R.id.username)).getText().toString().trim();
                            String password = ((TextView) view.findViewById(R.id.password)).getText().toString().trim();
                            String apassword = ((TextView) view.findViewById(R.id.apassword)).getText().toString().trim();
                            String personnum = ((TextView) view.findViewById(R.id.phonenum)).getText().toString().trim();

                            if(username.equals("")){
                                Toast.makeText(Main2Activity.this, "对不起，用户名不能为空", Toast.LENGTH_SHORT).show();
                            } else if(password.equals("")){
                                Toast.makeText(Main2Activity.this, "对不起，密码不能为空", Toast.LENGTH_SHORT).show();
                            }else if(!(password.equals(apassword))){
                                Toast.makeText(Main2Activity.this, "对不起，两次密码不一致", Toast.LENGTH_SHORT).show();
                            } else if(!personnum.matches(StringRegexTools.phone_regexp)){
                                Toast.makeText(Main2Activity.this, "对不起，联系方式不正确", Toast.LENGTH_SHORT).show();
                            }else{
                                new MyTaskToLoginOrRegister().execute(REGPATH,username,password,personnum);
                            }
                        }
                    })
                    .setNegativeButton("取消",null)
                    .show();

        }
    };
    //登录
    private View.OnClickListener logining = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = ((TextView)findViewById(R.id.username)).getText().toString().trim();
            String password = ((TextView)findViewById(R.id.password)).getText().toString().trim();
            if(username.equals("")){
                Toast.makeText(Main2Activity.this, "对不起，您还没有输入用户名", Toast.LENGTH_SHORT).show();
            }else if(password.equals("")){
                Toast.makeText(Main2Activity.this, "对不起，您还没有输入密码", Toast.LENGTH_SHORT).show();
            }else{
                new MyTaskToLoginOrRegister().execute(LOGINPATH,username,password);
            }

        }
    };
    //登录注册
    class MyTaskToLoginOrRegister extends AsyncTask<String,Void,String>{
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
                hc.setReadTimeout(5000);
                OutputStream output = hc.getOutputStream();


                String values="";
                if(params.length==3){
                    values = "username="+params[1]+"&password="+params[2];
                }else if(params.length==4){
                    values = "username="+params[1]+"&password="+params[2]+"&phonenum="+params[3];
                }
                //Log.i("values",values);
                output.write(values.getBytes());
                output.close();
                //接收
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer stringBuffer = new StringBuffer();
                while ((str=br.readLine())!=null){
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
            if(s.startsWith("注册")) {
                if (s.endsWith("成功")) {
                    Toast.makeText(Main2Activity.this,"注册成功", Toast.LENGTH_SHORT).show();
                }else if(s.endsWith("存在")){
                    Toast.makeText(Main2Activity.this,"对不起，该帐号已存在，请重新注册", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Main2Activity.this, "对不起，系统忙，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }else if(s.startsWith("登录")){
                if(s.endsWith("成功")){
                    Toast.makeText(Main2Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right,R.anim.left);
                }else if(s.endsWith("0")){
                    Toast.makeText(Main2Activity.this, "对不起,该帐号不存在，请注册", Toast.LENGTH_SHORT).show();
                }else if(s.endsWith("1")){
                    Toast.makeText(Main2Activity.this, "对不起,您的密码错误，请重新", Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();
        }
    }
}
