package com.hzyc.csj.oedermealsystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {
    private Button orderfood,pay;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        orderfood = (Button) findViewById(R.id.orderFood);
        pay = (Button) findViewById(R.id.pay);
        fragmentManager = getFragmentManager();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });
    }
    public void check(View v){
        int id = v.getId();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id){
            case R.id.orderFood :
                OrderFragement orderFragement = new OrderFragement();
                fragmentTransaction.replace(R.id.rightfragment,orderFragement);
                fragmentTransaction.addToBackStack("orderFragement");
                break;
            case R.id.pay :
                break;
        }
        fragmentTransaction.commit();
    }
}
