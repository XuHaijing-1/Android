package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG="Diandian";
    private Button registerButton;
    private Button loginButton;
    private Button experienceButton;
    private Button findpasswordButton;
    private ImageView qqButton;
    private ImageView wxButton;
    private UserLab lab =UserLab.getInstance();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (null!=msg){
                switch (msg.what){
                    case UserLab.USER_LOGIN_SUCCESS:
                        loginSucess();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //实现登录APP模块
        loginButton=findViewById(R.id.login_button);
        loginButton.setOnClickListener(v->{
            TextInputLayout username=findViewById(R.id.register_username);
            TextInputLayout password=findViewById(R.id.login_password);
            String u=username.getEditText().getText().toString();
            String p=password.getEditText().getText().toString();
            // 调用retrofit
            lab.login(u,p,handler);
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
            Log.e(TAG,"点击注册！前往注册页面");
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        // TODO 还未定时体验
        experienceButton=findViewById(R.id.experience_button);
        experienceButton.setOnClickListener(v->{
            Log.e(TAG,"体验失败！体验通道未开发");
            //TODO 暂时不跳转
            //Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            //startActivity(intent);
            Toast.makeText(LoginActivity.this,"暂时未开发体验通道！",Toast.LENGTH_LONG).show();
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
}
