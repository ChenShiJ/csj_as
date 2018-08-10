package com.hzyc.csj.ordermealsystem.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hzyc.csj.ordermealsystem.R;
import com.hzyc.csj.ordermealsystem.model.Delicious;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小柿子 on 2018/8/6.
 */
public class DeliciousFragment extends Fragment {
    private ListView listView;
    private LayoutInflater inflater1;
    private ProgressDialog progressDialog;
    private Button adddelicious,all,huncai,sucai,zhushi;
    private List<Delicious> listd;
    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private ImageView imageView;
    /*private static final String LISTPATH="http://10.151.4.8:8080/csj_web_android_osystem/ListDelicious.do";
    private static final String DDTPATH="http://10.151.4.8:8080/csj_web_android_osystem/DeleteDelicious.do";
    private static final String ADTPATH="http://10.151.4.8:8080/csj_web_android_osystem/AddDelicious.do";
    private static final String UDTPATH="http://10.151.4.8:8080/csj_web_android_osystem/UpdateDelicious.do";*/
    private static final String LISTPATH="http://192.168.1.166:8080/csj_web_android_osystem/ListDelicious.do";
    private static final String DDTPATH="http://192.168.1.166:8080/csj_web_android_osystem/DeleteDelicious.do";
    private static final String ADTPATH="http://192.168.1.166:8080/csj_web_android_osystem/AddDelicious.do";
    private static final String UDTPATH="http://192.168.1.166:8080/csj_web_android_osystem/UpdateDelicious.do";
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
        View view = inflater.inflate(R.layout.fragment_delicious,container,false);
        inflater1 = inflater;
        listView = (ListView) view.findViewById(R.id.listView3);
        adddelicious = (Button) view.findViewById(R.id.adddelicious);
        all = (Button) view.findViewById(R.id.all);
        huncai = (Button) view.findViewById(R.id.huncai);
        sucai = (Button) view.findViewById(R.id.sucai);
        zhushi = (Button) view.findViewById(R.id.zhushi);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待");

        new MyTaskGetDelicious(inflater,"全部").execute(LISTPATH);

        listView.setOnItemClickListener(itemclick);
        adddelicious.setOnClickListener(clickAD);
        all.setOnClickListener(call);
        sucai.setOnClickListener(csucai);
        huncai.setOnClickListener(chuncai);
        zhushi.setOnClickListener(czhushi);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyTaskGetDelicious(inflater1,"全部").execute(LISTPATH);
    }

    //***************
    //适配器
    class MyAdapter extends BaseAdapter {
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
    class MyTaskGetDelicious extends AsyncTask<String,Void,List<Delicious>> {
        private String kind;
        private LayoutInflater inflater;
        public MyTaskGetDelicious(LayoutInflater inflater,String kind){
            this.inflater=inflater;
            this.kind=kind;
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
                OutputStream output = hc.getOutputStream();
                String values = "kind="+kind;
                output.write(values.getBytes());
                output.close();
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
            listd = deliciouses;
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
    public AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            PopupMenu popupMenu = new PopupMenu(getContext(),view);
            popupMenu.getMenuInflater().inflate(R.menu.three,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id){
                        case R.id.updatedelicious:
                            clickUD(position);
                            break;
                        case R.id.deletedelicious :
                            new MyTaskToDD(listd.get(position).getId()+"").execute(DDTPATH);
                            break;
                    }
                    return true;
                }
            });
        }
    };
    //删除
    class MyTaskToDD extends AsyncTask<String,Void,String>{
        private String id;
        public MyTaskToDD( String id){
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);
                OutputStream output = hc.getOutputStream();
                String values ="dId="+id;
                output.write(values.getBytes());
                output.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str = "";
                StringBuffer sf = new StringBuffer();
                while ((str=br.readLine())!=null){
                    sf.append(str);
                }
                return sf.toString();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if("1".equals(s)){
                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                onResume();
            }else{
                Toast.makeText(getContext(), "系统忙，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //点击更新菜谱
    @TargetApi(Build.VERSION_CODES.M)
    private void clickUD(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = inflater1.inflate(R.layout.delicious_updateoradd,null);
        TextView udName = (TextView) view1.findViewById(R.id.udName);
        TextView udPrice = (TextView) view1.findViewById(R.id.udPrice);
        RatingBar udTj = (RatingBar) view1.findViewById(R.id.udTj);
        ImageView udPhoto = (ImageView) view1.findViewById(R.id.udPhoto);
        imageView = udPhoto;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PHOTO_FROM_GALLERY);
            }
        });
        udName.setText(listd.get(position).getName());
        udPrice.setText(listd.get(position).getPrice());
        udTj.setRating(listd.get(position).getTuij());
        builder.setTitle("更新菜品")
                .setView(view1)
                .setPositiveButton("更新",null)
                .setNegativeButton("取消",null)
                .show();
        new MyTaskToUDQ(udPhoto).execute(listd.get(position).getPhoto());
    }
    //更新前显示
    class MyTaskToUDQ extends AsyncTask<String,Void,Bitmap>{
        ImageView udPhoto;
        public MyTaskToUDQ(ImageView udPhoto){
            this.udPhoto = udPhoto;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                    Thread.sleep(2000);
                    HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                    hc.setRequestMethod("POST");
                    hc.setReadTimeout(5000);
                    Bitmap bitmap = BitmapFactory.decodeStream(hc.getInputStream());
                    return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressDialog.dismiss();

            udPhoto.setImageBitmap(bitmap);
        }
    }
    //新增菜品
    private View.OnClickListener clickAD = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = inflater1.inflate(R.layout.delicious_add,null);
            WebView webView= (WebView) view.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("http://192.168.1.166:8080/csj_web_android_osystem/addDelicious.jsp");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view,url);
                }
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
            });
             builder .setTitle("新增菜品")
                     .setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("取消",null)
                    .show();
            /*View view1 = inflater1.inflate(R.layout.delicious_updateoradd,null);
            TextView udName = (TextView) view1.findViewById(R.id.udName);
            TextView udPrice = (TextView) view1.findViewById(R.id.udPrice);
            RatingBar udTj = (RatingBar) view1.findViewById(R.id.udTj);
            ImageView udPhoto = (ImageView) view1.findViewById(R.id.udPhoto);
            imageView = udPhoto;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PHOTO_FROM_GALLERY);
                }
            });
            builder .setTitle("新增菜品")
                    .setView(view1)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("取消",null)
                    .show();*/
        }
    };

    private View.OnClickListener call = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onResume();
        }
    };
    private View.OnClickListener czhushi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MyTaskGetDelicious(inflater1,"主食").execute(LISTPATH);
        }
    };
    private View.OnClickListener csucai = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MyTaskGetDelicious(inflater1,"素菜").execute(LISTPATH);
        }
    };
    private View.OnClickListener chuncai = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MyTaskGetDelicious(inflater1,"荤菜").execute(LISTPATH);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //第一层switch
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                //第二层switch
                switch (resultCode) {
                    case -1:
                        if (data != null) {
                            Uri uri = data.getData();
                            imageView.setImageURI(uri);
                        }
                        break;
                    case 0:
                        break;
                }
                break;
            case PHOTO_FROM_CAMERA:
                if (resultCode == -1) {
                } else {
                    Log.e("result", "is not ok" + resultCode);
                }
                break;
            default:
                break;
        }
    }

}
