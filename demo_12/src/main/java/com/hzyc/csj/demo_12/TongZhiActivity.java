package com.hzyc.csj.demo_12;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TongZhiActivity extends AppCompatActivity {
    private Button send,cancel;
    private NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_zhi);
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        //通知发送到系统中
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        send.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Notification.Builder builder = new Notification.Builder(TongZhiActivity.this);
                PendingIntent intent = PendingIntent.getActivity(TongZhiActivity.this,200,new Intent(TongZhiActivity.this,MainActivity.class),PendingIntent.FLAG_ONE_SHOT);
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("QQ音乐")
                        .setContentText("您需要好的歌单清凉一夏，打开QQ音乐，了解一下")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);
                //点击跳转
                notificationManager.notify(1,builder.build());//id通知的编号
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(1);
            }
        });
    }
}
