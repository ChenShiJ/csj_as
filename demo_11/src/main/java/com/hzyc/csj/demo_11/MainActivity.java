package com.hzyc.csj.demo_11;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer = null;
    private Button start,restart,end,stop;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        restart = (Button) findViewById(R.id.restart);
        end = (Button) findViewById(R.id.end);
        stop = (Button) findViewById(R.id.stop);
    }
    public void check(View v){
        int id = v.getId();
        switch (id){
            case R.id.start:
                start();
                break;
            case R.id.stop:
                stop();
                break;
            case R.id.restart:
                restart();
                break;
            case R.id.end:
                end();
                break;
        }
    }
    public void start(){
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.jingyu);
        mediaPlayer.start();
        start.setEnabled(false);
    }
    public void end(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.release();
            mediaPlayer=null;
            start.setEnabled(true);
        }
    }
    public void restart(){
        end();
        start();
    }
    public void stop(){
        mediaPlayer.pause();
        if(i%2==0){
            i++;
            mediaPlayer.pause();
            stop.setText("继续");
        }else{
            i++;
            mediaPlayer.start();
            stop.setText("暂停");
        }
    }
}
