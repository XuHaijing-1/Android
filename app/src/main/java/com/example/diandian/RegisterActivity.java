package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private UserLab lab =UserLab.getInstance();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (null!=msg){
                switch (msg.what){
                    case UserLab.USER_REGISTER_SUCCESS:
                        ReqisterSucess();
                        break;
                    case UserLab.USER_REGISTER_ERROR:
                        ReqisterError();
                        break;
                    case UserLab.USER_REGISTER_NET_ERROR:
                        ReqisterFailed();
                        break;
                }
            }
        }
    };

    private void ReqisterSucess(){
        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
        //跳转登录界面
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    private void ReqisterError(){
        Toast.makeText(RegisterActivity.this,"信息不完整，请重新输入！",Toast.LENGTH_LONG).show();
    }

    private void ReqisterFailed(){
        Toast.makeText(RegisterActivity.this,"服务器错误，请稍后再试！",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton=findViewById(R.id.register_button);
        registerButton.setOnClickListener(v->{
//            TextInputLayout username=findViewById(R.id.register_username);
//            TextInputLayout password=findViewById(R.id.register_password);
//            TextInputLayout confirmpassword=findViewById(R.id.register_confirmpassword);
//            TextInputLayout phone=findViewById(R.id.register_phone);
//            TextInputLayout birthbay=findViewById(R.id.register_birthday);
//            //TODO 性别未获取
//            String us=username.getEditText().getText().toString();
//            String pa=password.getEditText().getText().toString();
//            String co=confirmpassword.getEditText().getText().toString();
//            String ph=phone.getEditText().getText().toString();
//            String bi=birthbay.getEditText().getText().toString();
//            // 调用retrofit
//            lab.register(us,pa,co,ph,bi,handler);
        });

    }

    private void initWindow() {
        // 标题栏居中显示
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_name_register);
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.display_title_register);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
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
}
