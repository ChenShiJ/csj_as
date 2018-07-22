package com.hzyc.csj.demo_06;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Main5Activity extends AppCompatActivity {

    private Button alertDialog,dataPickDialog,timePickDialog,progressDialog;
    private String [] data ={"长春市","吉林市","四平市"};
    private String [] sex = {"男","女"};
    private DialogInterface.OnClickListener click =  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case -1:
                        Toast.makeText(Main5Activity.this, "确定"+which, Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(Main5Activity.this, "否定"+which, Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(Main5Activity.this, "中立"+which, Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        alertDialog = (Button) findViewById(R.id.alertDialog);
        dataPickDialog = (Button) findViewById(R.id.DataPickDialog);
        timePickDialog = (Button) findViewById(R.id.TimePickDialog);
        progressDialog = (Button) findViewById(R.id.progressDialog);

        alertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                //多选弹出框
              /*  builder.setIcon(R.drawable.login_anim)
                        .setTitle("消息提示")
                        .setMultiChoiceItems(data,new boolean[]{true,false,false}, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Toast.makeText(Main5Activity.this, which+"", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();*/
                //普通三按钮弹出框
                /*
                builder.setIcon(R.drawable.login_anim)
                        .setTitle("消息提示")
                        .setMessage("*********")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Main5Activity.this, "确定"+which, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("否定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Main5Activity.this, "否定"+which, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("中立", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Main5Activity.this, "中立"+which, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                        */
                //加强版 普通三按钮
                /*
                builder.setIcon(R.drawable.login_anim)
                        .setTitle("提示信息")
                        .setMessage("***********")
                        .setPositiveButton("确定",click)
                        .setNegativeButton("否定",click)
                        .setNeutralButton("中立",click)
                        .show();
                */
                //弹出层单选
                builder.setIcon(R.drawable.login_anim)
                        .setTitle("请选择性别")
                        .setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Main5Activity.this, sex[which], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

            }
        });
        dataPickDialog.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dap = new DatePickerDialog(Main5Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Toast.makeText(Main5Activity.this, "点击了"+year+"-"+(monthOfYear+1)+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                    }
                },year,month,day);
                        dap.show();
            }
        });
        timePickDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(Main5Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(Main5Activity.this, "时间为"+hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
                    }
                },hour,minute,true);
                tpd.show();
            }
        });
        progressDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final  ProgressDialog pd = new ProgressDialog(Main5Activity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setIcon(R.drawable.login_anim);
                pd.setTitle("请等待");
                pd.setMessage("正在加载");
                pd.show();
                pd.setMax(100);

                class myThread extends Thread{
                    public void run(){
                        int i = 0;
                        while(true){
                            pd.setProgress(i+=10);
                            if (i>100){
                                pd.cancel();
                                break;
                            }
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                new myThread().start();
            }
        });
    }
}
