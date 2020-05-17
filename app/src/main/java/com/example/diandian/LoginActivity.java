package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    public static String name;
    private final static String TAG="Diandian";
    private Button registerButton;
    private Button loginButton;
    private Button experienceButton;
    private Button findpasswordButton;
    private ImageView qqButton;
    private ImageView wxButton;
    final boolean[] tiyan = {true};
    private UserLab lab =UserLab.getInstance();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (null!=msg){
                switch (msg.what){
                    case UserLab.USER_LOGIN_SUCCESS:
                        loginSucess();
                        TextInputLayout username=findViewById(R.id.register_username);
                        name=username.getEditText().getText().toString();
                        break;
                    case UserLab.USER_LOGIN_PASSWORD_ERROR:
                        loginPasswordError();
                        break;
                    case UserLab.USER_LOGIN_NET_ERROR:
                        loginFailed();
                        break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    //使程序休眠5分钟
                    sleep(300000);
                    //启动LonginActivity
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "5分钟体验已结束，请登录！", Toast.LENGTH_LONG).show();
                    Looper.loop();
                } catch (Exception e) {}
            }
        };

        //实现登录APP模块
        loginButton=findViewById(R.id.login_button);
        loginButton.setOnClickListener(v->{
            TextInputLayout username=findViewById(R.id.register_username);
            TextInputLayout password=findViewById(R.id.login_password);
            String u=username.getEditText().getText().toString();
            String p=password.getEditText().getText().toString();

            //判断
            if (TextUtils.isEmpty(u)){
                Toast.makeText(LoginActivity.this,"请输入用户名！",Toast.LENGTH_LONG).show();
                return;
            }else if (TextUtils.isEmpty(p)){
                Toast.makeText(LoginActivity.this,"请输入密码！",Toast.LENGTH_LONG).show();
                return;
            }else {
                // 调用retrofit
                lab.login(u, p, handler);
            }
        });

        // TODO 还未实现微信注册登录
        wxButton=findViewById(R.id.login_wx_button);
        wxButton.setOnClickListener(v->{
            Log.e(TAG,"登录失败！微信通道未开发");
            Toast.makeText(LoginActivity.this,"暂时未开发微信注册登录通道！",Toast.LENGTH_LONG).show();
        });

        // TODO 还未实现QQ注册登录
        qqButton=findViewById(R.id.login_qq_button);
        qqButton.setOnClickListener(v->{
            Log.e(TAG,"登录失败！QQ通道未开发");
            Toast.makeText(LoginActivity.this,"暂时未开发QQ注册登录通道！",Toast.LENGTH_LONG).show();
        });

        //点击立即注册
        registerButton=findViewById(R.id.register_button);
        registerButton.setOnClickListener(v->{
            Log.d(TAG,"点击注册！前往注册页面");
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        //TODO 可定时体验，但又bug可反复体验
        //5分钟定时体验
        experienceButton=findViewById(R.id.experience_button);
        experienceButton.setOnClickListener(v-> {
            if (tiyan[0]) {
                Log.d(TAG, "开始体验5分钟");
                //体验跳转
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                tiyan[0] = false;
                myThread.start();//启动线程
            }else {
                Log.d(TAG, "体验失败！你已体验5分钟");
                Toast.makeText(LoginActivity.this, "你已体验5分钟，不能在体验，请登录！", Toast.LENGTH_LONG).show();
            }
        });

        // TODO 还未实现找回密码，目前不能找回密码
        findpasswordButton=findViewById(R.id.find_password_button);
        findpasswordButton.setOnClickListener(v->{
            Log.e(TAG,"找回失败！密码找回通道未开发");
            //TODO 暂时不跳转
            //Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            //startActivity(intent);
            Toast.makeText(LoginActivity.this,"暂时未开发找回密码通道！",Toast.LENGTH_LONG).show();
        });
    }

    private void loginSucess(){
        Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void loginPasswordError(){
        Toast.makeText(LoginActivity.this,"密码错误，请重新输入！",Toast.LENGTH_LONG).show();
    }

    private void loginFailed(){
        Toast.makeText(LoginActivity.this,"服务器错误，请稍后再试！",Toast.LENGTH_LONG).show();
    }
}
