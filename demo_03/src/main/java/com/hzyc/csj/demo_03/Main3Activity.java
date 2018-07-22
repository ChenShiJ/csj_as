package com.hzyc.csj.demo_03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main3Activity extends AppCompatActivity {

    private GridView girdView;
    private List<HashMap<String,Object>> list;
    public List<HashMap<String,Object>> setList(){
        for(int i=0;i<10;i++){
            HashMap<String,Object> hashMap = new HashMap<String,Object>();
            hashMap.put("picture",R.drawable.tj);
            hashMap.put("number",i);
            list.add(hashMap);
        }
        return list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
    public class myAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return null;
        }
    }
}
