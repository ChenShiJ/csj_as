package com.hzyc.csj.demo_04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView mytb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new myAdapter());
        mytb = (ImageView) findViewById(R.id.mytb);
        mytb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

    }
    public List<Map<String,Object>> getFirstList(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map0 = new HashMap<String,Object>();
        map0.put("tp",R.drawable.dy);
        map0.put("nr","2016秋装大码条绒上衣韩版宽松灯芯风衣中长...");
        map0.put("price","￥99.99");
        map0.put("number","1333人付款");
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("tp",R.drawable.gc);
        map1.put("nr","纯手工 水果茶片 水果茶 花果果干茶 果味养生...");
        map1.put("price","￥29.10");
        map1.put("number","134人付款");
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("tp",R.drawable.zdy);
        map2.put("nr","青蔷薇 冬季加棉加厚呢外套 韩版学生中长款...");
        map2.put("price","￥128.45");
        map2.put("number","678人付款");
        Map<String,Object> map3 = new HashMap<String,Object>();
        map3.put("tp",R.drawable.ddx);
        map3.put("nr","天天特价 豆豆鞋女棉加绒 冬季皮毛一体雪地鞋...");
        map3.put("price","￥138");
        map3.put("number","269人付款");
        list.add(map0);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        return list;
    }

    public class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return getFirstList().size();
        }

        @Override
        public Object getItem(int position) {
            return getFirstList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;//text_image_2.xml
            if(convertView==null){
                view = MainActivity.this.getLayoutInflater().inflate(R.layout.text_image_2,null);
            }else{
                view = convertView;
            }
            ImageView imageView = (ImageView)view. findViewById(R.id.tp);
            TextView textView = (TextView) view.findViewById(R.id.nr);
            TextView textView1 = (TextView) view.findViewById(R.id.price);
            TextView textView2 = (TextView) view.findViewById(R.id.number);

            imageView.setImageResource(Integer.parseInt(getFirstList().get(position).get("tp").toString()));
            textView.setText(getFirstList().get(position).get("nr").toString());
            textView1.setText(getFirstList().get(position).get("price").toString());
            textView2.setText(getFirstList().get(position).get("number").toString());

            return view;
        }
    }
}
