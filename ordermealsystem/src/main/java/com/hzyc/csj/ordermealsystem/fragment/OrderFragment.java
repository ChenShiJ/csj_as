package com.hzyc.csj.ordermealsystem.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.hzyc.csj.ordermealsystem.model.DingDan;

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
 * Created by 小柿子 on 2018/8/2.
 */
public class OrderFragment extends Fragment {
    private ListView listView;
    private String dd="";
    private Button xiadan,all,huncai,sucai,zhushi;
    private LayoutInflater inflater1;
    private List<DingDan> listdd = new ArrayList<DingDan>();
    private ProgressDialog progressDialog;
    private List<Delicious> listd;
   /* private static final String LISTPATH="http://10.151.4.8:8080/csj_web_android_osystem/ListDelicious.do";
    private static final String XDPATH="http://10.151.4.8:8080/csj_web_android_osystem/XiaDan.do";*/
    private static final String LISTPATH="http://192.168.1.166:8080/csj_web_android_osystem/ListDelicious.do";
    private static final String XDPATH="http://192.168.1.166:8080/csj_web_android_osystem/XiaDan.do";
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
        inflater1 = inflater;
        listView = (ListView) view.findViewById(R.id.listView);
        xiadan = (Button) view.findViewById(R.id.xiadan);
        all = (Button) view.findViewById(R.id.all);
        huncai = (Button) view.findViewById(R.id.huncai);
        sucai = (Button) view.findViewById(R.id.sucai);
        zhushi = (Button) view.findViewById(R.id.zhushi);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待");
        new MyTaskGetDelicious(inflater,"全部").execute(LISTPATH);
        listView.setOnItemClickListener(orderMealClick);
        xiadan.setOnClickListener(xd);
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
        private String kind;
        public MyTaskGetDelicious(LayoutInflater inflater,String kind){
            this.inflater=inflater;
            this.kind = kind;
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

    //ListView 的onclick
   private AdapterView.OnItemClickListener orderMealClick = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            final TextView textView = (TextView)view.findViewById(R.id.itemNum);
            PopupMenu popupMenu = new PopupMenu(getContext(),view);
            popupMenu.getMenuInflater().inflate(R.menu.one,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int num = Integer.parseInt(textView.getText().toString());
                    int id = item.getItemId();
                    switch (id){
                        case R.id.jia:
                            num++;
                            textView.setText(num+"");
                            break;
                        case R.id.jian :
                            if(num>0){
                                num--;
                                textView.setText(num+"");
                            }
                            break;
                    }
                    DingDan ddan = new DingDan();
                    int mnum = Integer.parseInt(textView.getText().toString());
                    ddan.setMnum(mnum);
                    ddan.setmId(listd.get(position).getId());
                        if(listdd.isEmpty()){
                            listdd.add(ddan);
                        }else{
                            for(int i = 0;i<listdd.size();i++){
                                if(listd.get(position).getId()==listdd.get(i).getmId()){
                                    if(mnum!=listdd.get(i).getMnum()){
                                        if(mnum==0){
                                            listdd.remove(i);
                                        }else{
                                            listdd.set(i,ddan);
                                        }
                                        break;
                                    }
                                }else if(i== (listdd.size()-1)){
                                    listdd.add(ddan);
                                    break;
                                }
                            }
                        }
                       /* for(DingDan d : listdd){
                            Log.i("订单",d.toString());
                        }*/
                    return true;
                }
            });

        }
    };

    //下单的onclick
    private View.OnClickListener xd = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            if (!(listdd.isEmpty())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view2 = LayoutInflater.from(getContext()).inflate(R.layout.xiadan, null);
                final TextView beizhu = (TextView) view2.findViewById(R.id.beizhu);
                final TextView tnumber = (TextView) view2.findViewById(R.id.tnumber);
                final TextView tpeo = (TextView) view2.findViewById(R.id.tPeo);
                builder.setView(view2)
                        .setTitle("下单")
                        .setPositiveButton("下单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bzV = beizhu.getText().toString().trim();
                                String tnumV = tnumber.getText().toString().trim();
                                String tpeoV = tpeo.getText().toString().trim();
                                if (tnumV.equals("")) {
                                    Toast.makeText(getContext(), "对不起，您还没有输入餐桌号", Toast.LENGTH_SHORT).show();
                                } else {
                                    new MyTaskToOrder(tnumV, bzV, listdd,tpeoV).execute(XDPATH);
                                }
                            }
                        })
                        .show();
            }else{
                Toast.makeText(getContext(), "没有点餐，怎么可以下单呢", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //下单异步任务
    class MyTaskToOrder extends AsyncTask<String,Void,String>{
        private String tNumber;
        private String beizhu;
        private String tPeo;
        private List<DingDan> listdd;
        public MyTaskToOrder( String tNumber,String beizhu,List<DingDan> listdd,String tPeo){
            this.tNumber = tNumber;
            this.beizhu = beizhu;
            this.listdd = listdd;
            this.tPeo = tPeo;
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
                String values = "dingdan="+getDD(listdd)+"&tNumber="+tNumber+"&beizhu="+beizhu+"&tPeo="+tPeo;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str = "";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
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
            if(s.equals("下单成功")){
                Toast.makeText(getContext(), "下单成功", Toast.LENGTH_SHORT).show();
                onResume();
            }else{
                Toast.makeText(getContext(), "信息有误，请重试", Toast.LENGTH_SHORT).show();
            }
        }
        public String getDD(List<DingDan> listdd){
            String dingd="";
            for(int i = 0 ;i<listdd.size();i++){
                if(i!=(listdd.size()-1)){
                    dingd+=listdd.get(i).getmId()+"@@"+listdd.get(i).getMnum()+";";
                }else{
                    dingd+=listdd.get(i).getmId()+"@@"+listdd.get(i).getMnum();
                }
            }
            return dingd;
        }
    }
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
}
