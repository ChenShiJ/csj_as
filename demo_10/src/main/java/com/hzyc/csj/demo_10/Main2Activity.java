package com.hzyc.csj.demo_10;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    private Button connectTomcat;
    private ProgressDialog progressDialog;
    private static final String PATH="http://192.168.1.166:8080/csj_web_android_001/TestServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        connectTomcat = (Button) findViewById(R.id.connectTomcat);
        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待");
        connectTomcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new myTask().execute(PATH);
            }
        });
    }
    class myTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                //建立web端的连接
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("GET");
                hc.setReadTimeout(5000);
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
            Toast.makeText(Main2Activity.this, s, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
