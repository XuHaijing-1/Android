package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity{

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Channel currentChannel;
    private TextView tvName,tvQuality,tvLike;
    private ImageButton sendButton;
    private ChannelLab lab=ChannelLab.getInstance();
    private final static String TAG="Diandian";
    private final DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 完成接收数据后
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case ChannelLab.MSG_HOT_COMMENTS:
                    if (msg.obj!=null){
                    List <Comment> hotComments =(List<Comment>)msg.obj;
                    updateHotComments(hotComments);
                }
                    break;
                case ChannelLab.MSG_ADD_COMMENT:
                    Toast.makeText(PlayerActivity.this,"感谢你的评论！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
                case ChannelLab.MSG_FAILURE:
                    Toast.makeText(PlayerActivity.this,"评论失败，请稍后重试！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //init();
        Serializable s=getIntent().getSerializableExtra("channel");
        Log.d(TAG,"取得当前频道对象是："+s);
        initWindow();
        if (s!=null&&s instanceof Channel){
            currentChannel=(Channel) s;

            sendButton=findViewById(R.id.send);
            sendButton.setOnClickListener(v->{
                EditText t=findViewById(R.id.comment);
                Comment c=new Comment();
                c.setAuthor(LoginActivity.name);
                c.setContent(t.getText().toString());
                //随机点赞0-100
                Random random=new Random();
                c.setStar(random.nextInt(100));
                //调用retrofit上传评论
                if(TextUtils.isEmpty(LoginActivity.name)){
                    Log.d(TAG,"跳转登录界面！");
                    Toast.makeText(PlayerActivity.this,"请登录后评论！",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(PlayerActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else if (TextUtils.isEmpty(t.getText().toString())){
                    Toast.makeText(PlayerActivity.this,"请输入后评论！",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Log.d(TAG,"输入当前评论是："+c);
                    lab.addComment(currentChannel.getId(), c, handler);
                }
            });
        }
        updateUI();
    }

    private void updateUI(){
        tvName=findViewById(R.id.tv_name);
        tvQuality=findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());
        tvLike=findViewById(R.id.tv_like);
        tvLike.setText(currentChannel.getLike()+"");
    }

    private  void updateHotComments(List<Comment> hotComments){
        //更新界面
        if (hotComments.size()>0){
            Comment c1=hotComments.get(0);
            TextView username1 =findViewById(R.id.username1);
            username1.setText(c1.getAuthor());
            TextView date1=findViewById(R.id.date1);
            date1.setText(dateFormat.format(c1.getDt()));
            TextView content1 =findViewById(R.id.conten1);
            content1.setText(c1.getContent());
            TextView star1 =findViewById(R.id.thumbup_count1);
            star1.setText(c1.getStar()+"");
        }
        if (hotComments.size()>1){
            Comment c2=hotComments.get(1);
            TextView username2 =findViewById(R.id.username2);
            username2.setText(c2.getAuthor());
            TextView date2=findViewById(R.id.date2);
            date2.setText(dateFormat.format(c2.getDt()));
            TextView content2 =findViewById(R.id.conten2);
            content2.setText(c2.getContent());
            TextView star2 =findViewById(R.id.thumbup_count2);
            star2.setText(c2.getStar()+"");
        }
        if (hotComments.size()>2){
            Comment c3=hotComments.get(2);
            TextView username3 =findViewById(R.id.username3);
            username3.setText(c3.getAuthor());
            TextView date3=findViewById(R.id.date3);
            date3.setText(dateFormat.format(c3.getDt()));
            TextView content3 =findViewById(R.id.conten3);
            content3.setText(c3.getContent());
            TextView star3 =findViewById(R.id.thumbup_count3);
            star3.setText(c3.getStar()+"");
        }
        if (hotComments.size()>3){
            Comment c4=hotComments.get(3);
            TextView username4 =findViewById(R.id.username4);
            username4.setText(c4.getAuthor());
            TextView date4=findViewById(R.id.date4);
            date4.setText(dateFormat.format(c4.getDt()));
            TextView content4 =findViewById(R.id.conten4);
            content4.setText(c4.getContent());
            TextView star4 =findViewById(R.id.thumbup_count4);
            star4.setText(c4.getStar()+"");
        }
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

        //获取最新热门评论
        lab.getHotComments(currentChannel.getId(),handler);

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
                new DefaultDataSourceFactory(this,TAG);
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

    //评论点赞


    //频道关注度

}



