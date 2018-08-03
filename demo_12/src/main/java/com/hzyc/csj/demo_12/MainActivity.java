package com.hzyc.csj.demo_12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = textView.getText().toString().trim();
                Bitmap log = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                Bitmap bitmap = EncodingUtils.createQRCode(value,200,200,log);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
