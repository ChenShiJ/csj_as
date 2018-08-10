package com.hzyc.csj.ordermealsystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hzyc.csj.ordermealsystem.fragment.DeliciousFragment;
import com.hzyc.csj.ordermealsystem.fragment.StatisticsFragment;
import com.hzyc.csj.ordermealsystem.fragment.TableFragment;

public class Main4Activity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {}
        });
    }
    public void check(View v){
        int id = v.getId();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id){
            case R.id.looktable :
                TableFragment tableFragement = new TableFragment();
                fragmentTransaction.replace(R.id.topfragment,tableFragement);
                fragmentTransaction.addToBackStack("tableFragment");
                break;
            case R.id.listdelicious :
                DeliciousFragment deliciousFragment = new DeliciousFragment();
                fragmentTransaction.replace(R.id.topfragment,deliciousFragment);
                fragmentTransaction.addToBackStack("deliciousFragment");
                break;
            case R.id.out :
                finish();
                break;
            case R.id.tongji:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                fragmentTransaction.replace(R.id.topfragment,statisticsFragment);
                fragmentTransaction.addToBackStack("statisticsFragment");
                break;
        }
        fragmentTransaction.commit();
    }

}
