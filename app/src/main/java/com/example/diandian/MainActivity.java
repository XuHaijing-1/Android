package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView channelRv;
    private ChannelRvAdapter rvAdapter;
    private ChannelLab lab=ChannelLab.getInstance();
    //线程通讯第1步，在主线程创建HandLer
    private Handler handler=new Handler(){
        //按快捷键ctrl o
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what==1){
                rvAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRv=findViewById(R.id.channel_rv);
        //lambda简化
        //适用handler，把适配器改为实例变量
        rvAdapter =new ChannelRvAdapter(MainActivity.this, p -> {
            //跳转到新界面，使用意图Intent
            Intent intent =new Intent(MainActivity.this,PlayerActivity.class);
            //TODO 传递用户选中的频道到下一个界面
            //通过位置p得到当前频道channel
            Channel c=lab.getChannel(p);
            intent.putExtra("channel",c);
            startActivity(intent);
        });

        this.channelRv.setAdapter(rvAdapter);
        this.channelRv.setLayoutManager(new LinearLayoutManager(this));

        Retrofit a=RetrofitClient.getInstance();
        // 标题栏居中显示
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_name);
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.display_title);
            textView.setText("微视频");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //把主线程的handler传递给子线程适用
        lab.getData(handler);
    }


}

