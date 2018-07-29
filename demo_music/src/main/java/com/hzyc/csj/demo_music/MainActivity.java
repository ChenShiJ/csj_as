package com.hzyc.csj.demo_music;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter(getMusicList()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = getMusicList().get(position).getUrl();
                String title = getMusicList().get(position).getTitle();
                String artist = getMusicList().get(position).getArtist();
                String duration = getMusicList().get(position).getTime();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                intent.putExtra("artist",artist);
                intent.putExtra("duration",duration);
                startActivity(intent);
            }
        });
    }
    public List<MusicMedia> getMusicList(){
        List<MusicMedia> list = new ArrayList<MusicMedia>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        assert cursor != null;

        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast())){   //返回游标是否指向第最后一行的位置
                //歌曲的id
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲的标题
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                MusicMedia musicMedia = new MusicMedia();
                musicMedia.setId(id);
                musicMedia.setAlbum(album);
                musicMedia.setAlbumId(albumId);
                musicMedia.setArtist(artist);
                musicMedia.setTitle(title);
                musicMedia.setUrl(url);
                musicMedia.setSize(size);
                musicMedia.setTime(duration);
                list.add(musicMedia);
                cursor.moveToNext();
            }
        }
        return list;
    }
    class MyAdapter extends BaseAdapter{
        private List<MusicMedia> list;
        public MyAdapter(List<MusicMedia> list){
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
            TextView textView;
            if(convertView==null){
                textView = new TextView(MainActivity.this);
                textView.setPadding(8,8,8,8);
                textView.setTextSize(20);
            }else{
                textView = (TextView) convertView;
            }
            textView.setText(list.get(position).getTitle());
            return textView;
        }
    }
}
