package com.hzyc.csj.ordermealsystem.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzyc.csj.ordermealsystem.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小柿子 on 2018/8/6.
 */
public class PayFragment extends Fragment {
    private TextView tNum;
    private Button confirm;
    private LayoutInflater inflater1;
    private ProgressDialog progressDialog;
   /* private static final String LISTDDBYTID="http://10.151.4.8:8080/csj_web_android_osystem/LookTableByNum.do";
    private static final String QDPATH="http://10.151.4.8:8080/csj_web_android_osystem/QueDing.do";*/
    private static final String LISTDDBYTID="http://192.168.1.166:8080/csj_web_android_osystem/LookTableByNum.do";
    private static final String QDPATH="http://192.168.1.166:8080/csj_web_android_osystem/QueDing.do";
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragmet_pay,container,false);
        inflater1 = inflater;
        tNum = (TextView) view.findViewById(R.id.tnum);
        confirm = (Button) view.findViewById(R.id.confirm);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("正在加载");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("请等待");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tNumV = tNum.getText().toString().trim();
                new myTaskToMD(tNumV,inflater).execute(LISTDDBYTID);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }
    //买单异步线程
    class myTaskToMD extends AsyncTask<String,Void,List<Map<String, Object>>>{
        private String tNum;
        private LayoutInflater inflater;
        public myTaskToMD(String tNum,LayoutInflater inflater){
            this.tNum = tNum;
            this.inflater = inflater;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);

                OutputStream output = hc.getOutputStream();
                String values ="tNum="+tNum;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str = "";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                if(sf.toString().equals("桌号有误")||sf.toString().equals("该桌已付款")){
                    return null;
                }else{
                    return getXiangXiDD(sf.toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            progressDialog.dismiss();
            if(maps==null){
                Toast.makeText(getContext(),"您输入的桌号有误，或该桌已付款", Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view3 = inflater.inflate(R.layout.dingdan,null);
                final TextView tablenum = (TextView) view3.findViewById(R.id.tablenum);
                TextView priceNum = (TextView) view3.findViewById(R.id.priceNum);
                tablenum.setText(maps.get(maps.size()-1).get("桌号").toString());
                priceNum.setText(maps.get(maps.size()-1).get("消费共计").toString());
                ListView listView2= (ListView) view3.findViewById(R.id.listView2);
                listView2.setAdapter(new MyAdapterToGetDD(maps,inflater));
                builder.setView(view3)
                        .setTitle("订单详细")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyTaskToQD(tablenum.getText().toString()).execute(QDPATH);
                            }
                        })
                        .show();
            }
        }
    }
    class MyAdapterToGetDD extends BaseAdapter {
        private List<Map<String,Object>> list;
        private LayoutInflater inflater;
        public  MyAdapterToGetDD(List<Map<String,Object>> list,LayoutInflater inflater){
            this.list = list;
            this.inflater=inflater;
        }
        @Override
        public int getCount() {
            return list.size()-1;
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
                view = inflater.inflate(R.layout.dingdanitem,null);
            }else{
                view=convertView;
            }
            TextView dmname = (TextView) view.findViewById(R.id.dmname);
            TextView dmprice = (TextView) view.findViewById(R.id.dmprice);
            TextView dmnum = (TextView) view.findViewById(R.id.dmnum);
            dmname.setText(list.get(position).get("名称").toString());
            dmprice.setText(list.get(position).get("单价").toString());
            dmnum.setText(list.get(position).get("数量").toString());
            return view;
        }
    }

    //处理后台传递过来的方法
    private List<Map<String,Object>> getXiangXiDD(String result){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String [] a = result.split(";");
        for(int i = 0;i<a.length;i++){
            String [] b;
            Map<String,Object> map = new HashMap<String,Object>();
            if(i==a.length-1){
                b=a[i].split("@@@");
                map.put("桌号",b[0]);
                map.put("消费共计",b[1]);
            }else{
                b=a[i].split("@@");
                map.put("名称",b[1]);
                map.put("单价",b[2]);
                map.put("数量",b[0]);
            }
            list.add(map);
        }
        return list;
    }
    class MyTaskToQD extends  AsyncTask<String,Void,String>{
        private String tId;
        public MyTaskToQD(String tId){
            this.tId = tId;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);

                OutputStream output = hc.getOutputStream();
                String values = "tId="+tId;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str= "";
                StringBuffer sf = new StringBuffer();
                if((str=br.readLine())!=null){
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
            Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();
            onResume();
        }
    }
}
