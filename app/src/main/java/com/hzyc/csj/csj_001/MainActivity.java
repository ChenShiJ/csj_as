package com.hzyc.csj.csj_001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.button);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "hello word", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView imageView =(ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
    }
}
