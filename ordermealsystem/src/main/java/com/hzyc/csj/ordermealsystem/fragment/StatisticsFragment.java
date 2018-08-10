package com.hzyc.csj.ordermealsystem.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.hzyc.csj.ordermealsystem.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小柿子 on 2018/8/7.
 */
public class StatisticsFragment extends Fragment {
    private ListView listView;
    private ProgressDialog progressDialog;
    private LayoutInflater inflater1;
    private List<Map<String,Object>> listm;
/*    private static final String TJPATH = "http://10.151.4.8:8080/csj_web_android_osystem/TongJi.do";
    private static final String STJPATH = "http://10.151.4.8:8080/csj_web_android_osystem/StrongTongJi.do";*/
    private static final String TJPATH = "http://192.168.1.166:8080/csj_web_android_osystem/TongJi.do";
    private static final String STJPATH = "http://192.168.1.166:8080/csj_web_android_osystem/StrongTongJi.do";
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
        View view = inflater.inflate(R.layout.frgment_statistics,container,false);
        inflater1 = inflater;
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("请等待。。。");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("正在加载");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dap = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String data = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                //Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                new MyTaskToTJ(data,listView,0).execute(TJPATH);
            }
        },year,month,day);
        dap.show();
        listView = (ListView) view.findViewById(R.id.listView4);
        listView.setOnItemClickListener(clickOne);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    //统计
    class MyTaskToTJ extends AsyncTask<String,Void,List<Map<String,Object>>> {
        private String data;
        private ListView listView1;
        private int kind;
        public MyTaskToTJ(String data,ListView listView1, int kind){
            this.data = data;
            this.listView1 = listView1;
            this.kind = kind;
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
                String values ="data="+data;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str = "";
                StringBuffer sf = new StringBuffer();
                while ((str=br.readLine())!=null){
                    sf.append(str);
                }
                if(kind==0){
                    return getDataAndSum(sf.toString());
                }else if(kind==1){
                    return getADataAndSum(sf.toString());
                }

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
        protected void onPostExecute(List<Map<String, Object>> listMap) {
            super.onPostExecute(listMap);
            progressDialog.dismiss();
            if(kind == 0){
                listm=listMap;
                listView1.setAdapter(new myAdapterToGeiTJ(listMap,inflater1));
            }else if(kind == 1){
                Log.i("size",listMap.size()+"");
                listView1.setAdapter(new myAdapterToGeiATJ(listMap,inflater1));
            }

        }
    }
    private List<Map<String,Object>> getDataAndSum(String s){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String [] a = s.split(";");
        for(int i = 0;i<a.length;i++){
            String [] b = a[i].split("@@");
            Map<String,Object> map = new HashMap<String,Object>();
                map.put("日期",b[0]);
                map.put("销售额",b[1]);
            list.add(map);
        }
        return list;
    }
    private List<Map<String,Object>> getADataAndSum(String s){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String [] a = s.split(";");
        for(int i = 0;i<a.length;i++){
            String [] b = a[i].split("@@");
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("日期",b[0]);
            map.put("销售额",b[1]);
            map.put("桌号",b[2]);
            list.add(map);
        }
        return list;
    }
    class myAdapterToGeiTJ extends BaseAdapter{
        private List<Map<String,Object>> list;
        private LayoutInflater inflater;
        public  myAdapterToGeiTJ(List<Map<String,Object>> list,LayoutInflater inflater){
            this.list = list;
            this.inflater=inflater;
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
                view = inflater.inflate(R.layout.statisticsitem,null);
            }else{
                view=convertView;
            }
            TextView tjData = (TextView) view.findViewById(R.id.data);
            TextView tjCount = (TextView) view.findViewById(R.id.count);
            tjData.setText(list.get(position).get("日期").toString());
            tjCount.setText(list.get(position).get("销售额").toString());
            return view;
        }
    }
    //适配器 日期 桌号 金额
    class myAdapterToGeiATJ extends BaseAdapter{
        private List<Map<String,Object>> list;
        private LayoutInflater inflater;
        public  myAdapterToGeiATJ(List<Map<String,Object>> list,LayoutInflater inflater){
            this.list = list;
            this.inflater=inflater;
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
                view = inflater.inflate(R.layout.statisticsitemmore,null);
            }else{
                view=convertView;
            }
            TextView tjData = (TextView) view.findViewById(R.id.data);
            TextView tjCount = (TextView) view.findViewById(R.id.count);
            TextView tjNumber = (TextView) view.findViewById(R.id.number);
            tjData.setText(list.get(position).get("日期").toString());
            tjCount.setText(list.get(position).get("销售额").toString());
            tjNumber.setText(list.get(position).get("桌号").toString());
            return view;
        }
    }
    private AdapterView.OnItemClickListener clickOne = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view2 = inflater1.inflate(R.layout.statisticsmore,null);
            ListView listView2 = (ListView) view2.findViewById(R.id.listView4);
            builder.setTitle("详细")
                    .setView(view2)
                    .setNeutralButton("返回",null)
                    .show();
            new MyTaskToTJ(listm.get(position).get("日期").toString(),listView2,1).execute(STJPATH);
        }
    };
}
