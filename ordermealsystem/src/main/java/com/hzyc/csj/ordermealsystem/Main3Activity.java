package com.hzyc.csj.ordermealsystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hzyc.csj.oedermealsystem.R;
import com.hzyc.csj.ordermealsystem.fragment.OrderFragment;
import com.hzyc.csj.ordermealsystem.fragment.TableFragment;

public class Main3Activity extends AppCompatActivity {
    private Button orderfood,pay,back,looktable;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        orderfood = (Button) findViewById(R.id.orderFood);
        pay = (Button) findViewById(R.id.pay);
        back = (Button) findViewById(R.id.back);
        looktable = (Button) findViewById(R.id.looktable);
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
                OrderFragment orderFragement = new OrderFragment();
                fragmentTransaction.replace(R.id.rightfragment,orderFragement);
                fragmentTransaction.addToBackStack("orderFragement");
                break;
            case R.id.pay :
                break;
            case R.id.back :
                fragmentManager.popBackStack();
                break;
            case R.id.looktable :
                TableFragment tableFragement = new TableFragment();
                fragmentTransaction.replace(R.id.rightfragment,tableFragement);
                fragmentTransaction.addToBackStack("tableFragment");
                break;
        }
        fragmentTransaction.commit();
    }
}
