package com.hzyc.csj.oedermealsystem;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by 小柿子 on 2018/8/2.
 */
public class OrderFragement extends Fragment {
    private ListView listView;
    private ProgressDialog progressDialog;
    private static final String LISTPATH="http://10.151.4.8:8080/csj_web_android_osystem/ListDelicious.do";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderfood,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待");
        new MyTaskGetDelicious(inflater).execute(LISTPATH);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //***************
    //适配器
    class MyAdapter extends BaseAdapter{
        private List<Delicious> list;
        private LayoutInflater inflater;
        public MyAdapter(List<Delicious> list, LayoutInflater inflater){
            this.list=list;
            this.inflater = inflater;
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
                view = inflater.inflate(R.layout.orderitem,null);
            }else{
                view = convertView;
            }
            TextView itemName = (TextView) view.findViewById(R.id.itemName);
            TextView itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            ImageView itemPhoto = (ImageView) view.findViewById(R.id.itemPhoto);
            TextView itemNum = (TextView) view.findViewById(R.id.itemNum);

            itemName.setText(list.get(position).getName());
            itemPrice.setText("￥"+list.get(position).getPrice());
            ratingBar.setRating((float)list.get(position).getTuij());
            //Toast.makeText(getContext(), itemName.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            itemPhoto.setTag(list.get(position).getPhoto());
            new MyTaskGetPhoto(itemPhoto,list.get(position).getPhoto()).execute(list.get(position).getPhoto());

            return view;
        }
    }

    //***************
    //获取delicious的异步任务
    class MyTaskGetDelicious extends AsyncTask<String,Void,List<Delicious>>{
        private LayoutInflater inflater;
        public MyTaskGetDelicious(LayoutInflater inflater){
            this.inflater=inflater;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Delicious> doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);
                BufferedReader bf = new BufferedReader( new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer sf = new StringBuffer();
                while((str=bf.readLine())!=null){
                    sf.append(str);
                }
                return GetListDelicious(sf.toString()) ;
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
        protected void onPostExecute(List<Delicious> deliciouses) {
            super.onPostExecute(deliciouses);
            progressDialog.dismiss();
           listView.setAdapter( new MyAdapter(deliciouses,inflater));
        }
        private List<Delicious> GetListDelicious(String json){
            List<Delicious> list = new ArrayList<Delicious>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Delicious delicious = new Delicious();
                    delicious.setId(Integer.parseInt(jsonObject.getString("id")));
                    delicious.setName(jsonObject.getString("name"));
                    delicious.setPrice(jsonObject.getString("price"));
                    delicious.setTuij(Float.parseFloat(jsonObject.getString("tuij")));
                    delicious.setPhoto(jsonObject.getString("photo"));
                    list.add(delicious);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //***************
    //获取delicious photo的异步任务
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
            //Log.i("!!!!","运行过这里");
            if(tag.equals(imageView.getTag())){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
