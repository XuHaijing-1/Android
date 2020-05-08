package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
            if (msg.what==ChannelLab.MSG_CHANNELS){
                rvAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initWindow();
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


    }

    @Override
    protected void onResume(){
        super.onResume();
        //把主线程的handler传递给子线程适用
        lab.getData(handler);
    }

    /**
     * 实现返回键按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // 返回按钮
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWindow() {//初始化，将状态栏和标题栏设为透明或者隐藏
//        getSupportActionBar().hide();//隐藏标题栏
        //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明标题栏
//        }
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.channel_row);

        // 标题栏居中显示
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_name);
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.display_title);
            //标题名称
//            textView.setText("微视频");
            //返回箭头
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }
}

