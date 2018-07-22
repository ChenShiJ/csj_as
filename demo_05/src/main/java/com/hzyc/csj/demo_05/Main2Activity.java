package com.hzyc.csj.demo_05;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    private EditText backValue;
    private Button toMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        backValue = (EditText) findViewById(R.id.backValue);
        toMain = (Button) findViewById(R.id.toMain);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        backValue.setText(bundle.getString("username")+"@@@@@"+bundle.getString("password"));
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backValues = backValue.getText().toString().trim();
                Intent intent1 = new Intent(Main2Activity.this,MainActivity.class);
                intent1.putExtra("backValue",backValues);
                setResult(200,intent1);
                finish();

            }
        });
    }
}
