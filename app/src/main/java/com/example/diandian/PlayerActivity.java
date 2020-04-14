package com.example.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;

public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Channel currentChannel;
    private TextView tvName,tvQuality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //init();
        Serializable s=getIntent().getSerializableExtra("channel");
        Log.d("Diandian","取得当前频道对象是："+s);
        if (s!=null&&s instanceof Channel){
            currentChannel=(Channel) s;
        }
        updateUI();
    }

    private void updateUI(){
        tvName=findViewById(R.id.tv_name);
        tvQuality=findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());
    }

    //快捷键ctrl O
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    protected void onStart(){
        super.onStart();
        init();
        if (playerView!=null){
            playerView.onResume();
        }
    }

    protected void onStop(){
        super.onStop();
        if (playerView!=null){
                playerView.onPause();
        }
        clean();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (player==null){
            init();
            if (playerView!=null){
                playerView.onResume();
            }
        }
    }
    /**
     * 自定义方法，初始化播放器
     */
    private void init(){
        player= ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面查找视图
        playerView=findViewById(R.id.tv_player);
        //关联视图与播放器
        playerView.setPlayer(player);

        //准备播放器的媒体
        Uri videoUrl=Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1.m3u8");
        if (null != currentChannel){
            //使用当前频道的网址
            videoUrl=Uri.parse(currentChannel.getUrl());
        }
        DataSource.Factory factory=
                new DefaultDataSourceFactory(this,"Diandian");
        MediaSource videoSource=new HlsMediaSource.Factory(factory).createMediaSource(videoUrl);
        player.prepare(videoSource);
    }

    /**
     * 自定义方法，清理不用资源
     */
    private void clean(){
        if (player!=null){
            player.release();
            player=null;
        }
    }
}