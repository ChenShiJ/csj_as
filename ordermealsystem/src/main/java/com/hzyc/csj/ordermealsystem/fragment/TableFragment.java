package com.hzyc.csj.ordermealsystem.fragment;

import android.annotation.TargetApi;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.hzyc.csj.oedermealsystem.R;
import com.hzyc.csj.ordermealsystem.model.Tables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小柿子 on 2018/8/3.
 */
public class TableFragment extends Fragment {
    private GridView gridView;
    private ProgressDialog progressDialog;
    private static final String LISTTABLEPATH="http://10.151.4.8:8080/csj_web_android_osystem/SearchAllTables.do";
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
        View view = inflater.inflate(R.layout.fragment_table,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("正在加载");
        progressDialog.setMessage("请稍后。。。");
        new MyTaskGetTable(inflater).execute(LISTTABLEPATH);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*
        异步任务 加载桌子
     */
    class MyTaskGetTable extends AsyncTask<String,Void,List<Tables>>{
        private LayoutInflater inflater;
        public MyTaskGetTable(LayoutInflater inflater){
            this.inflater = inflater;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List doInBackground(String... params) {
            try {
                Thread.sleep(3000);
                HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                hc.setRequestMethod("POST");
                hc.setReadTimeout(5000);
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer sf= new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                return getTableList(sf.toString()) ;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Tables> list) {
            super.onPostExecute(list);
            for(Tables t : list){
                    Log.i("tables",t.toString());
                }
            progressDialog.dismiss();
            gridView.setAdapter(new MyTableAdapter(list,inflater));
        }
        private List<Tables> getTableList(String json)  {
            List<Tables> list = new ArrayList<Tables>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Tables table = new Tables();
                    table.setId(Integer.parseInt(jsonObject.getString("id")));
                    table.setMax(jsonObject.getString("max"));
                    table.setNow(jsonObject.getString("now"));
                    table.setNumber(jsonObject.getString("number"));
                    table.setState(jsonObject.getString("state") );
                    list.add(table);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /*
    适配器
     */
    class MyTableAdapter extends BaseAdapter{
        private List<Tables> list;
        private LayoutInflater inflater;
        public MyTableAdapter( List<Tables> list,LayoutInflater inflater){
            this.list = list;
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
                view = inflater.inflate(R.layout.tableitem,null);
            }else{
                view = convertView;
            }
            TextView itemNum = (TextView) view.findViewById(R.id.itemNum);
            TextView itemState = (TextView) view.findViewById(R.id.itemState);
            TextView itemPeo = (TextView) view.findViewById(R.id.itemPeo);
            itemNum.setText(list.get(position).getNumber());
            itemState.setText(list.get(position).getState());
            itemPeo.setText(list.get(position).getNow()+"/"+list.get(position).getMax());
            return view;
        }
    }
}
