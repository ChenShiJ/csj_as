package com.hzyc.csj.demo_12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

public class SaoActivity extends AppCompatActivity {
    private Button saoyisao;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sao);
        saoyisao = (Button) findViewById(R.id.saoyisao);
        result = (TextView) findViewById(R.id.result);

        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaoActivity.this, CaptureActivity.class);
                startActivityForResult(intent,200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==200){
            String value = data.getStringExtra("result");
            result.setText(value);
        }
    }
}
