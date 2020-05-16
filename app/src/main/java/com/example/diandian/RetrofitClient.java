package com.example.diandian;

import com.squareup.moshi.Moshi;

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
            INSTANCE=new Retrofit.Builder()
                    .baseUrl("http://www.weitv.icu:8005")
//                    .baseUrl("http://47.115.94.109:8005")
//                    .baseUrl("http://47.115.34.11:8080")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }
        return INSTANCE;
    }
}
