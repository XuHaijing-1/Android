package com.example.diandian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RecyclerView channelRv;
    private ChannelLab lab=new ChannelLab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRv=findViewById(R.id.channel_rv);
        //lambda简化
        ChannelRvAdapter rvAdapter=new ChannelRvAdapter(p -> {
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
    }

}

