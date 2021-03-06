package com.example.diandian;

import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * 使用单例模式创建Retrofit对象
 */
public class RetrofitClient {
    private static Retrofit INSTANCE=null;
    public static Retrofit getInstance(){
        if (INSTANCE==null){
            Moshi moshi = new Moshi.Builder()
                    .add(new MyDateAdapter())
                    .build();

            //准备拦截器
            OkHttpClient okClient=new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .build();

            INSTANCE=new Retrofit.Builder()
                    .baseUrl("http://www.weitv.icu:8888") //域名访问阿里云服务器
//                    .baseUrl("http://47.115.94.109:8005") //IP访问阿里云服务器
//                    .baseUrl("http://47.115.34.11:8080")  //IP访问严张凌服务器
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .callFactory(okClient) //注入自定义的OKhttp
                    .build();
        }
        return INSTANCE;
    }
}
