package com.hzyc.csj.demo_10;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog progressDialog;
    private static final String LISTPATH="http://192.168.1.166:8080/csj_web_android_001/ListGoodsServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        listView = (ListView) findViewById(R.id.listView);

        progressDialog = new ProgressDialog(Main3Activity.this);
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待。。。。。。");

        new MyTask().execute(LISTPATH);
    }

    class MyAdapter extends BaseAdapter{
        private List<Goods> list;
        //使用构造方法往里面传参
        public MyAdapter(List<Goods> list){
            this.list = list;
        }
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
            View view;
            if(convertView==null){
                view = LayoutInflater.from(Main3Activity.this).inflate(R.layout.img_text,null);
            }else{
                view = convertView;
            }
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView price = (TextView) view.findViewById(R.id.price);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            TextView shops = (TextView) view.findViewById(R.id.shops);
            ImageView imageView = (ImageView) view.findViewById(R.id.photo);
            name.setText(list.get(position).getName());
            price.setText(list.get(position).getPrice()+"");
            ratingBar.setRating((float) list.get(position).getRatingBar());
            shops.setText(list.get(position).getShops());

            //图片适配
            imageView.setTag(list.get(position).getPhoto());
            new MyTaskGetPhoto(imageView,list.get(position).getPhoto()).execute(list.get(position).getPhoto());
            return view;
        }
    }

    class MyTask extends AsyncTask<String,Void,List<Goods>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        public List<Goods> GetListGoods(String json){
         List<Goods> list = new ArrayList<Goods>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Goods goods = new Goods();
                    goods.setId(Integer.parseInt(jsonObject.getString("id")));
                    goods.setName(jsonObject.getString("name"));
                    goods.setPhoto(jsonObject.getString("photo"));
                    goods.setRatingBar(Float.parseFloat(jsonObject.getString("ratingBar")));
                    goods.setPrice(Double.parseDouble(jsonObject.getString("price")));
                    goods.setShops(jsonObject.getString("shops"));
                    list.add(goods);
                }
                Log.i("list",list.size()+"");
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected List<Goods> doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                return GetListGoods(sf.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Goods> list) {
            super.onPostExecute(list);
            progressDialog.dismiss();
            listView.setAdapter(new MyAdapter(list));
        }

    }

    //适配图片的异步任务
    class MyTaskGetPhoto extends AsyncTask<String,Void,Bitmap>{
        private ImageView imageView;
        private String tag;
        //构造方法没有返回值类型
        public MyTaskGetPhoto(ImageView imageView,String tag){
            this.imageView=imageView;
            this.tag=tag;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);
                Bitmap bitmap = BitmapFactory.decodeStream(hc.getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(tag.equals(imageView.getTag())){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
