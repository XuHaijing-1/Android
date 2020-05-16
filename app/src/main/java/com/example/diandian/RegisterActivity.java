package com.example.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private final static String TAG="Diandian";
    private ImageView useravatar;
    TextInputLayout birthdayInput;
    private Date birthday=new Date();
    private UserLab lab =UserLab.getInstance();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (null!=msg){
                switch (msg.what){
                    case UserLab.USER_REGISTER_SUCCESS:
                        ReqisterSucess();
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

    private void ReqisterFailed(){
        Toast.makeText(RegisterActivity.this,"服务器错误，请稍后再试！",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        birthdayInput=findViewById(R.id.register_birthday);
        //注册按钮
        registerButton=findViewById(R.id.register_button);
        registerButton.setOnClickListener(v->{
            register();
        });

        //选择头像图片
        //TODO 暂时不能设置用户头像
        useravatar=findViewById(R.id.register_useravatar);
        useravatar.setOnClickListener(v->{
            Log.e(TAG,"不能设置头像！头像上传通道未开发");
            Toast.makeText(RegisterActivity.this,"暂时未开发头像上传通道！",Toast.LENGTH_LONG).show();
        });

        /**
         * 点击选择生日
         */
        //新建一个Builder
        MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.datePicker();
        //告诉bulider我们想要的
        builder.setTitleText(R.string.birthday_title);
        MaterialDatePicker<Long>picker=builder.build();

        //告诉bulider，开工
        picker.addOnPositiveButtonClickListener(s->{
            Log.d(TAG,"日历的结果是："+s);
            Log.d(TAG,"标题是："+picker.getHeaderText());
            birthday.setTime(s);
            birthdayInput.getEditText().setText(picker.getHeaderText());
        });

        //点击图标弹出日历
        birthdayInput.setEndIconOnClickListener(v->{
            Log.d(TAG,"你的生日图标被点击了");
            picker.show(getSupportFragmentManager(),picker.toString());
        });

    }

    /**
     * 标题栏处理
     */
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

    //获取登录信息
    private void register(){
        User u =new User();
        boolean error=false;
        String errorMessage;

        //头像

        //用户名
        TextInputLayout usernameInput =findViewById(R.id.register_username);
        Editable username =usernameInput.getEditText().getText();
        u.setUsername(username!=null?username.toString():"");

        //判断密码并获取
        TextInputLayout passwordInput =findViewById(R.id.register_password);
        TextInputLayout confirmpasswordInput =findViewById(R.id.register_confirmpassword);
        Editable password =passwordInput.getEditText().getText();
        Editable confirmpassword =confirmpasswordInput.getEditText().getText();
        if (password!=null&&confirmpassword!=null){
            if (!confirmpassword.toString().equals(password.toString())){
                error =true;
                errorMessage="两次输入密码不一样！";
                Toast.makeText(RegisterActivity.this,"信息不完整，请重新输入！",Toast.LENGTH_LONG).show();
            }else {
                u.setPassword(password.toString());
            }
        }

        //手机号
        TextInputLayout phoneInput =findViewById(R.id.register_phone);
        Editable phone =phoneInput.getEditText().getText();
        u.setPhone(phone!=null?phone.toString():"");

        //生日
        u.setBirthday(birthday);

        //性别
        RadioGroup genderGroup =findViewById(R.id.register_sex);
        int gender =genderGroup.getCheckedRadioButtonId();
        switch (gender){
            case R.id.register_male:
                u.setGender("男");
                break;
            case R.id.register_femle:
                u.setGender("女");
                break;
            default:
                u.setGender("保密");
        }

        //上传服务器
        lab.register(u, handler);
    }
}
