package com.hzyc.csj.csj_001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new myAdapter());
    }
    public List<Map<String,Object>> getList(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<10;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("photo",R.drawable.hello);
            map.put("name","hello"+i);
            if(i%5!=0) {
                map.put("ratingBar",i%5 );
            }else{
                map.put("ratingBar",5);
            }
            map.put("price","118.00");
            map.put("resource","小米(MI)");
            list.add(map);
           // Log.i("调试信息",list.get(i).toString());
        }
        return list;
    }
    public class myAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return getList().size();
        }

        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Log.i("调试",position+"");
            //从适配器取出image_text.xml获取这个布局文件
            //从该文件取出空间
            View view;//iamge_text.xml
            if(convertView==null){
                view = Main3Activity.this.getLayoutInflater().inflate(R.layout.image_text,null);
            }else{
                view = convertView;
            }

            //取控件
            ImageView imageView = (ImageView)view. findViewById(R.id.photo);
            TextView textView = (TextView) view.findViewById(R.id.name);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            TextView textView1 = (TextView) view.findViewById(R.id.price);
            TextView textView2 = (TextView) view.findViewById(R.id.resource);


            imageView.setImageResource(Integer.parseInt(getList().get(position).get("photo").toString()));
            textView.setText(getList().get(position).get("name").toString());
            textView1.setText(getList().get(position).get("price").toString());
            textView2.setText(getList().get(position).get("resource").toString());
            ratingBar.setRating(Float.parseFloat(getList().get(position).get("ratingBar").toString()));
            return view;
        }
    }
}
