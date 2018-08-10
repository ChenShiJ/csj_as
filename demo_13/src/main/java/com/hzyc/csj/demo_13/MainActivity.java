package com.hzyc.csj.demo_13;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private ImageView imageView;
    private File appDir;
    private Uri uriForCamera;
    private Date date;
    private String str = "";
    //private SharedPreferences sharePreference;
   // private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://10.151.4.8:8080/csj_web_android_osystem/addDelicious.jsp");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view,url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });*/
        //sharePreference = getPreferences(MODE_PRIVATE);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    //从相册取图片
    public void gallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //第一层switch
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                //第二层switch
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            Uri uri = data.getData();
                            imageView.setImageURI(uri);
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case PHOTO_FROM_CAMERA:
 /*               if (resultCode == RESULT_OK) {
                    //Uri uri = Uri.parse(sharePreference.getString("uri",""));
//                    updateDCIM(uri);
//                    try {
//                        //把URI转换为Bitmap，并将bitmap压缩，防止OOM(out of memory)
//                        Bitmap bitmap = ImageTools.getBitmapFromUri(uri, this);
//                        imageView.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    //removeCache("uri");
                } else {
                    Log.e("result", "is not ok" + resultCode);
                }*/
                break;
            default:
                break;
        }
    }

    /**
     * 设置相片存放路径，先将照片存放到SD卡中，再操作
     *
     * @return
     */
    private File createImageStoragePath() {
        if (hasSdcard()) {
            appDir = new File("/sdcard/testImage/");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            date = new Date();
            str = simpleDateFormat.format(date);
            String fileName = str + ".jpg";
            File file = new File(appDir, fileName);
            return file;
        } else {
            Log.e("sd", "is not load");
            return null;
        }
    }

    /**
     * 将照片插入系统相册，提醒相册更新
     *
     * @param uri
     */
    private void updateDCIM(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        this.sendBroadcast(intent);

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
    }

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除缓存
     *
     * @param cache
     */
   /* private void removeCache(String cache) {
        File file= new File("/data/data/"+getPackageName().toString()+"/shared_prefs","Activity.xml");
        if(file.exists()){
            file.delete();
        }

    }*/

}
