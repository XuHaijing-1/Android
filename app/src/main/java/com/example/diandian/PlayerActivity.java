package com.example.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
        initWindow();
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

    private void initWindow() {//初始化，将状态栏和标题栏设为透明或者隐藏
        getSupportActionBar().hide();//隐藏标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明标题栏

//            setTranslucentStatus(true);
//            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//            mTintManager.setStatusBarTintEnabled(true);
//            mTintManager.setStatusBarTintResource(R.color.colorTop);//通知栏所需颜色

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorTop));//通知栏所需颜色

        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_player);
    }

//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }
}
