package com.hzyc.csj.demo_06;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Main4Activity extends AppCompatActivity {

    private ImageView imageView,loginimg;
    private Button bAlpha,bScale,bTranslate,bRoate,bStart,bStop,ToMain5;
    private Animation myAnimation;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ToMain5 = (Button) findViewById(R.id.toMain5);
        ToMain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this,Main5Activity.class);
                startActivity(intent);
            }
        });

        imageView = (ImageView) findViewById(R.id.dog);
        bAlpha = (Button) findViewById(R.id.Alpha);
        bScale = (Button) findViewById(R.id.Scale);
        bTranslate = (Button) findViewById(R.id.Translate);
        bRoate = (Button) findViewById(R.id.Rotate);
        bStart = (Button) findViewById(R.id.start);
        bStop = (Button) findViewById(R.id.stop);
        loginimg = (ImageView) findViewById(R.id.imageView);
        loginimg.setImageResource(R.drawable.login_anim);
        animationDrawable = (AnimationDrawable) loginimg.getDrawable();

    }

    //单机事件
    public void check(View v){
        int id = v.getId();
        switch (id){
            case R.id.Alpha:
                myAnimation = AnimationUtils.loadAnimation(Main4Activity.this,R.anim.my_alpha);
                imageView.startAnimation(myAnimation);
                break;
            case  R.id.Scale:
                myAnimation = AnimationUtils.loadAnimation(Main4Activity.this,R.anim.my_scale);
                imageView.startAnimation(myAnimation);
                break;
            case R.id.Translate:
                myAnimation = AnimationUtils.loadAnimation(Main4Activity.this,R.anim.my_translate);
                imageView.startAnimation(myAnimation);
                break;
            case R.id.Rotate:
                myAnimation = AnimationUtils.loadAnimation(Main4Activity.this,R.anim.my_rotate);
                imageView.startAnimation(myAnimation);
                break;
        }
    }
    public void checkone(View v){
        int id = v.getId();
        switch (id){
            case R.id.start:
                animationDrawable.start();
                break;
            case  R.id.stop:
                animationDrawable.stop();
                break;
        }
    }

}
