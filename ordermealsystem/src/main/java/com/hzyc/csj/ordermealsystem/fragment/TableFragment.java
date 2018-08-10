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
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzyc.csj.ordermealsystem.R;
import com.hzyc.csj.ordermealsystem.model.Tables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by 小柿子 on 2018/8/3.
 */
public class TableFragment extends Fragment {
    private GridView gridView;
    private ProgressDialog progressDialog;
    private LayoutInflater inflater1;
    private Button addTable;
    private List<Tables> listt;
/*    private static final String LISTDDBYTID="http://10.151.4.8:8080/csj_web_android_osystem/ListDDByTId.do";
    private static final String LISTTABLEPATH="http://10.151.4.8:8080/csj_web_android_osystem/SearchAllTables.do";
    private static final String QDPATH="http://10.151.4.8:8080/csj_web_android_osystem/QueDing.do";
    private static final String KZPATH="http://10.151.4.8:8080/csj_web_android_osystem/KaiZhuo.do";
    private static final String ZTPATH="http://10.151.4.8:8080/csj_web_android_osystem/ZhuanTai.do";
    private static final String BTPATH="http://10.151.4.8:8080/csj_web_android_osystem/BingTai.do";
    private static final String ATPATH="http://10.151.4.8:8080/csj_web_android_osystem/AddTable.do";
    private static final String DTPATH="http://10.151.4.8:8080/csj_web_android_osystem/DeleteTable.do";
    private static final String UTPATH="http://10.151.4.8:8080/csj_web_android_osystem/UpdateTable.do";*/
    private static final String LISTDDBYTID="http://192.168.1.166:8080/csj_web_android_osystem/ListDDByTId.do";
    private static final String LISTTABLEPATH="http://192.168.1.166:8080/csj_web_android_osystem/SearchAllTables.do";
    private static final String QDPATH="http://192.168.1.166:8080/csj_web_android_osystem/QueDing.do";
    private static final String KZPATH="http://192.168.1.166:8080/csj_web_android_osystem/KaiZhuo.do";
    private static final String ZTPATH="http://192.168.1.166:8080/csj_web_android_osystem/ZhuanTai.do";
    private static final String BTPATH="http://192.168.1.166:8080/csj_web_android_osystem/BingTai.do";
    private static final String ATPATH="http://192.168.1.166:8080/csj_web_android_osystem/AddTable.do";
    private static final String DTPATH="http://192.168.1.166:8080/csj_web_android_osystem/DeleteTable.do";
    private static final String UTPATH="http://192.168.1.166:8080/csj_web_android_osystem/UpdateTable.do";
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
        inflater1 = inflater;
        View view = inflater.inflate(R.layout.fragment_table,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        addTable = (Button) view.findViewById(R.id.addtable);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("正在加载");
        progressDialog.setMessage("请稍后。。。");
        new MyTaskGetTable(inflater).execute(LISTTABLEPATH);
        gridView.setOnItemClickListener(tableclick);
        addTable.setOnClickListener(clickAddTable);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyTaskGetTable(inflater1).execute(LISTTABLEPATH);
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
            /*for(Tables t : list){
                    Log.i("tables",t.toString());
                }*/
            listt = list;
            progressDialog.dismiss();
            gridView.setAdapter(new MyTableAdapter(list,inflater));
        }
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
    /*
    桌子  适配器
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
    private AdapterView.OnItemClickListener tableclick  = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            PopupMenu popupMenu = new PopupMenu(getContext(),view);
            popupMenu.getMenuInflater().inflate(R.menu.two,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final String[] tables = getTableNum(listt);
                    switch (id){
                        case R.id.maidan :
                            if(listt.get(position).getState().equals("已付款")){
                                Toast.makeText(getContext(), "该桌已付款", Toast.LENGTH_SHORT).show();
                            }else if(listt.get(position).getState().equals("空闲")){
                                Toast.makeText(getContext(), "亲，还没有客人呢", Toast.LENGTH_SHORT).show();
                            }else if(listt.get(position).getState().equals("用餐")){
                                new MyTaskGetDD(0,listt.get(position).getId(),inflater1).execute(LISTDDBYTID);
                            }
                            break;
                        case R.id.bingtai :
                            builder.setTitle("请选择要并的桌号")
                                    .setSingleChoiceItems(tables, position, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            new MyTaskToGetBT((listt.get(position).getId())+"",(which+1)+"").execute(BTPATH);
                                        }
                                    })
                                    .show();
                            break;
                        case R.id.zhuantai :
                            builder.setTitle("请选择新的桌号")
                                    .setSingleChoiceItems(tables, position, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            new MyTaskToZT((listt.get(position).getId())+"",(listt.get(which).getId())+"").execute(ZTPATH);
                                        }
                                    })
                                    .show();
                            break;
                        case R.id.kaizhuo :
                            if(listt.get(position).getState().equals("空闲")){
                                Toast.makeText(getContext(), "该桌已经开桌", Toast.LENGTH_SHORT).show();
                            }else  if(listt.get(position).getState().equals("用餐")){
                                Toast.makeText(getContext(), "该桌正在用餐", Toast.LENGTH_SHORT).show();
                            }else if (listt.get(position).getState().equals("已付款")){
                                new MyTaskToKZ((listt.get(position).getId())+"").execute(KZPATH);
                            }
                            break;
                        case  R.id.updatetable :
                            clickXG(position);
                            break;
                        case R.id.deletetable :
                            new MyTaskToDT(listt.get(position).getNumber()).execute(DTPATH);
                            break;
                        case R.id.chakan :
                            if(listt.get(position).getState().equals("空闲")){
                                Toast.makeText(getContext(), "该桌目前没有客人", Toast.LENGTH_SHORT).show();
                            }else{
                                new MyTaskGetDD(1,listt.get(position).getId(),inflater1).execute(LISTDDBYTID);
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    };
    //获取table的桌号
    private String[] getTableNum(List<Tables> list){
        String [] result = new String[list.size()];
        for(int i =0;i<list.size();i++){
            result[i]= list.get(i).getNumber()+"------"+list.get(i).getState();
        }
        return result;
    }
    //买单异步线程
    class MyTaskGetDD extends AsyncTask<String,Void,List<Map<String,Object>>>{
        private int kind;
        private int tid;
        private LayoutInflater inflater;
        public MyTaskGetDD(int kind,int tid,LayoutInflater inflater){
            this.tid = tid;
            this.inflater = inflater;
            this.kind = kind;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.i("!!!!!!tId",tid+"");
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
                String values ="tId="+tid+"&kind="+kind;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str = "";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                return getXiangXiDD(sf.toString());

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
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            progressDialog.dismiss();
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
                            if(kind==0){
                                new MyTaskToQD(tablenum.getText().toString()).execute(QDPATH);
                            }else if(kind==1){

                            }

                        }
                    })
                    .show();
        }


        //处理listView2的适配器
        class MyAdapterToGetDD extends BaseAdapter{
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



        //确定异步任务
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

    //开桌异步任务
    class MyTaskToKZ extends AsyncTask<String,Void,String>{
        private String tId;
        public MyTaskToKZ(String tId){
            this.tId=tId;
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
                String values = "tId="+tId;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
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
            Toast.makeText(getContext(), "开桌成功", Toast.LENGTH_SHORT).show();
            onResume();
        }
    }

    //转台异步任务
    class MyTaskToZT extends AsyncTask<String,Void,String>{
        private String tId;
        private String newTId;
        public MyTaskToZT(String tId,String newTId){
            this.tId = tId;
            this.newTId = newTId;
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
                String values = "tId="+tId+"&newTId="+newTId;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                return sf.toString();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            onResume();
        }
    }
    //并台异步任务
    class MyTaskToGetBT extends AsyncTask<String,Void,String>{
        private String tId;
        private String newTId;
        public MyTaskToGetBT(String tId,String newTId){
            this.tId = tId;
            this.newTId = newTId;
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
                String values="tId="+tId+"&btId="+newTId;
                output.write(values.getBytes());
                output.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String str="";
                StringBuffer sf = new StringBuffer();
                while((str=br.readLine())!=null){
                    sf.append(str);
                }
                return sf.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            onResume();

        }
    }
    private View.OnClickListener clickAddTable = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view5 = inflater1.inflate(R.layout.table_add,null);
            final TextView adTnum = (TextView) view5.findViewById(R.id.atNum);
            final TextView adPeo = (TextView) view5.findViewById(R.id.adPeo);
            builder.setTitle("新增餐桌")
                    .setView(view5)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String adTnumV = adTnum.getText().toString().trim();
                            String adPeoV =adPeo.getText().toString().trim();
                            if("".equals(adTnumV)){
                                Toast.makeText(getContext(), "对不起，您还没有输入餐桌号", Toast.LENGTH_SHORT).show();
                            }else if("".equals(adPeoV)){
                                Toast.makeText(getContext(), "对不起，您还没有输入可以容纳的人数", Toast.LENGTH_SHORT).show();
                            }else {
                                new MyTaskToAT(adTnumV,adPeoV).execute(ATPATH);
                            }
                        }
                    })
                    .setNegativeButton("取消",null)
                    .show();
        }
    };
    //添加餐桌异步任务
    class MyTaskToAT extends AsyncTask<String,Void,String>{
        private String tNum;
        private String tPeo;
        public MyTaskToAT(String tNum,String tPeo){
            this.tNum = tNum;
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
                String values ="tNum="+tNum+"&tPeo="+tPeo;
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
            if(s.equals("0")){
                Toast.makeText(getContext(), "对不起，您输入的桌号已存在", Toast.LENGTH_SHORT).show();
            }else if(s.equals("1")){
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                onResume();
            }
        }
    }
    class MyTaskToDT extends AsyncTask<String,Void,String>{
        private String tNum;
        public MyTaskToDT(String tNum){
            this.tNum = tNum;
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
                String values ="tNum="+tNum;
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
            if(s.equals("1")){
                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void clickXG(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view6 = inflater1.inflate(R.layout.table_update,null);
        final TextView tNum = (TextView) view6.findViewById(R.id.uTnum);
        final TextView tMax = (TextView) view6.findViewById(R.id.uTMax);
        final TextView tNow = (TextView) view6.findViewById(R.id.uTNow);
        final TextView tState = (TextView) view6.findViewById(R.id.uTstate);
        tNum.setText(listt.get(position).getNumber());
        tMax.setText(listt.get(position).getMax());
        tNow.setText(listt.get(position).getNow());
        tState.setText(listt.get(position).getState());
        builder.setTitle("修改餐桌")
                .setView(view6)
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tNumV = tNum.getText().toString().trim();
                        String tMaxV = tMax.getText().toString().trim();
                        String tNowV = tNow.getText().toString().trim();
                        String tStateV = tState.getText().toString().trim();
                        String tId = listt.get(position).getId()+"";
                        new MyTaskToUT(tNumV,tMaxV,tStateV,tNowV,tId,position).execute(UTPATH);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
    //更新餐桌异步任务
    class MyTaskToUT extends AsyncTask<String,Void,String>{
        private int position;
        private  String tId;
        private String tNum;
        private String tMax;
        private String tState;
        private String tNow;
        public MyTaskToUT(String tNum,String tMax,String tState,String tNow,String tId,int position){
            this.tNum = tNum;
            this.tMax = tMax;
            this.tState = tState;
            this.tNow = tNow;
            this.tId = tId;
            this.position=position;
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
                boolean bol = false;
                for(int i =0;i<listt.size();i++){
                    if(tNum.equals(listt.get(i).getNumber())){
                        if(i==position){
                            bol = false;
                        }else{
                            bol = true;
                            break;
                        }
                    }
                }
                if(bol){
                    return 0+"";
                }else if("".equals(tNum)){
                    return 2+"";
                }else if("".equals(tState)){
                    return 3+"";
                }else if("".equals(tMax)||"0".equals(tMax)) {
                    return 4 + "";
                }else{
                        HttpURLConnection hc = (HttpURLConnection) new URL(params[0]).openConnection();
                        hc.setRequestMethod("POST");
                        hc.setReadTimeout(5000);
                        OutputStream output = hc.getOutputStream();
                        String values ="id="+tId+"&number=" + tNum + "&max=" + tMax + "&state=" + tState + "&now=" + tNow;
                        output.write(values.getBytes());
                        output.close();

                        BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                        String str = "";
                        StringBuffer sf = new StringBuffer();
                        while ((str = br.readLine()) != null) {
                            sf.append(str);
                        }
                        return sf.toString();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("0")){
                Toast.makeText(getContext(), "对不起，您输入的桌号已存在", Toast.LENGTH_SHORT).show();
            }else if(s.equals("1")){
                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                onResume();
            }else if(s.equals("2")){
                Toast.makeText(getContext(), "对不起，您还没有输入桌号", Toast.LENGTH_SHORT).show();
            }else if(s.equals("3")){
                Toast.makeText(getContext(), "对不起，您还没有输入餐桌状态", Toast.LENGTH_SHORT).show();
            }else if(s.equals("4")){
                Toast.makeText(getContext(), "对不起，您还没有输入餐桌最大容客量", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
