package com.hzyc.csj.demo_10;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("正在加载");
        progressDialog.setMessage("请等待......");
/*        异步线程任务原则
        不要阻塞UI线程。
        不要在UI线程之外访问Andoid的UI组件包。  不要在ui线程意外的线程去更改控件*/
        //1.使用android异步任务  自动有子和主的交接  （公共）
        //2.每个控件都带了一个  异步任务方法  （只属于控件私有  获取数据的时候使用异步任务 更新控件的时候回到UI线程）（私有的）
        //给每一个控件的异步任务方法
/*        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.dog);
                    }
                });
            }
        });*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute("url1","url2");
            }
        });
    }
    class MyTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        //该方法引发的时异步线程
        //先执行这个方法，异步线程在获取图片
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dog);
            Log.i("参数",params[0]);
            Log.i("参数",params[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    }
}
