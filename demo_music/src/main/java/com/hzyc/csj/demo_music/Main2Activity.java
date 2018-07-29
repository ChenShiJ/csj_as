package com.hzyc.csj.demo_music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
    private Button stop,pause,replay,play;
    private ImageView back;
    private TextView name,songer,currentTime,durationTime;
    private String url,artist,title,duration,now;
    private SeekBar seekBar;
    private boolean bol =false,stop_status = false;
    private MediaPlayer mediaPlayer = null;
    private int i = 0;
    private Timer timer;
    MusicMedia mm = new MusicMedia();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        currentTime = (TextView) findViewById(R.id.currentTime);
        durationTime= (TextView) findViewById(R.id.durationTime);
        back = (ImageView) findViewById(R.id.back);
        stop = (Button) findViewById(R.id.stop);
        pause = (Button) findViewById(R.id.pause);
        replay = (Button) findViewById(R.id.replay);
        play = (Button) findViewById(R.id.start);
        name = (TextView) findViewById(R.id.name);
        songer = (TextView) findViewById(R.id.songer);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                bol =true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());//设置播放直接到seekBar指定位置
                bol = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        artist = intent.getStringExtra("artist");
        title = intent.getStringExtra("title");
        duration = intent.getStringExtra("duration");
        name.setText(title);
        songer.setText(artist);
        durationTime.setText(duration);
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    public void check(View v){
        int id = v.getId();
        switch (id){
            case R.id.stop :
                stop();
                break;
            case R.id.back :
                back();
                break;
            case R.id.pause :
                pause();
                break;
            case R.id.replay :
                replay();
                break;
            case R.id.start :
                start();
                break;
        }
    }
    public void start(){
        stop_status=false;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play.setEnabled(false);

        seekBar.setMax(mediaPlayer.getDuration());//设置总进度
        //调度任务 不需要手动控制
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(bol){
                    return;
                }
                seekBar.setProgress(mediaPlayer.getCurrentPosition());//播放位置设置到进度条中
                //Log.i("当前时间",mm.getCurrentTime(mediaPlayer.getCurrentPosition()));
                now = mm.getCurrentTime(mediaPlayer.getCurrentPosition());
                //设置是否完成播放的监听事件
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        replay();
                    }
                });
                new MyTimeTask().execute();
            }
        };
        //开始调度任务
        timer.schedule(timerTask,0,500);
    }
    public void pause(){
        stop_status=false;
        if(i%2==0){
            mediaPlayer.pause();
            i++;
        }else if(i%2==1){
            mediaPlayer.start();
            i++;
        }
    }
    public void stop(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            timer.cancel();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            seekBar.setProgress(0);
            play.setEnabled(true);
            currentTime.setText("00:00");
            stop_status = true;
        }
    }
    public void replay(){
        stop();
        start();
    }
    public void back(){
        stop();
        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
        startActivity(intent);
    }
    //获取当前播放时长
    class MyTimeTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(500);
                if(stop_status){
                    return "00:00";
                }
                return now;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTime.setText(s);
        }
    }
}
