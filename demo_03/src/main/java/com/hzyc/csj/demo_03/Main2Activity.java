package com.hzyc.csj.demo_03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class Main2Activity extends AppCompatActivity {

    private GridView girdView;

    private int [] data={R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs, R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,
            R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,
            R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,
            R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs,
            R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs, R.drawable.tj,R.drawable.tjj,R.drawable.ms,R.drawable.xs};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        girdView = (GridView) findViewById(R.id.gridView);
        girdView.setAdapter(new MyAdapter());
    }
    //适配器 继承BaseAdapter
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            //第一次使用适配器时未创建convertView 进行判断 ==null时 new ImageView
            //往界面写东西有两种方法 ，第一种是在xml里写，第二种是在MainActivity里写 new XXXX(上下文对象)
            if(convertView==null){
                imageView = new ImageView(Main2Activity.this);
            }else{
                imageView=(ImageView)convertView;
            }
            //数据加载给控件
            imageView.setImageResource(data[position]);
            return imageView;
        }
    }
}
