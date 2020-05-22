package com.example.diandian;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 频道数据源
 * 使用了单例模式保证此类仅有一个对象
 */
public class ChannelLab {
    //单例第1步
    private static ChannelLab INSTANCE=null;

    private  List<Channel>data;

    private final static String TAG="Diandian";
    public final static int MSG_CHANNELS=1;
    public final static int MSG_HOT_COMMENTS=2;
    public final static int MSG_ADD_COMMENT=3;
    public final static int MSG_FAILURE=4;

    //单例第2步
    private ChannelLab(){
        //把下面的代码换成从网络获取数据
        data = new ArrayList<>();
//        test();
//        getData();
    }

    //单例第3步
    public static ChannelLab getInstance(){
        if (INSTANCE==null){
            INSTANCE=new ChannelLab();
        }
        return INSTANCE;
    }
    /**
     * 生成测试数据
     */
//    public void test() {
//        data = new ArrayList<>();
//        Channel c = new Channel();
//        c.setTitle("CCTV-1 综合");
//        c.setQuality("4k 超高清");
//        c.setCover(R.drawable.a1);
//        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
//        data.add(c);
//    }
    /**
     * 返回数据总数量
     * @return
     */
    public int getSize(){
        return data.size();
    }

    /**
     * 返回指定位置的频道信息
     * @param position 数据编号，从0开始
     * @return position 对应的频道数量
     */
    public Channel getChannel(int position){
        return this.data.get(position);
    }

    /**
     * 访问网络数据得到真实数据,代替以前的test（）方法
     * @param handler
     */
    public void getData(Handler handler){
        //调用单例
        Retrofit retrofit=RetrofitClient.getInstance();

        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Result<List<Channel>>> call=api.getAllChannels();
        //enqueue会自己生成子线程，去执行后溪代码
        call.enqueue(new Callback<Result<List<Channel>>>() {
            @Override
            public void onResponse(Call<Result<List<Channel>>> call, Response<Result<List<Channel>>> response) {
                if (response.code()==403){
                    Log.w(TAG,"被禁止访问服务器");
                    Message msg=new Message();
                    msg.what=MSG_FAILURE;
                    handler.sendMessage(msg);
                }else if (null != response && null != response.body()) {
                    Log.d(TAG, "从阿里云得到数据是：");
                    Log.d(TAG, response.body().toString());
                    Result<List<Channel>> result=response.body();
                    data=result.getData();
                    //发出通知
                    Message msg=new Message();
                    msg.what=MSG_CHANNELS;
                    handler.sendMessage(msg);
                } else {
                    Log.w(TAG, "responew没有数据!");
                }
            }
            @Override
            public void onFailure(Call<Result<List<Channel>>> call, Throwable t) {
                    Log.e(TAG,"访问网络失败",t);

            }
        });

    }

    public void getHotComments(String channelId, Handler handler){
        //调用单例
        Retrofit retrofit=RetrofitClient.getInstance();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Result<List<Comment>>> call = api.getHotComments(channelId);
        call.enqueue(new Callback<Result<List<Comment>>> () {
            @Override
            public void onResponse(Call<Result<List<Comment>>>  call, Response<Result<List<Comment>>>  response) {
                if (response.code()==403) {
                    Log.w(TAG, "禁止访问！");
                    Message msg = new Message();
                    msg.what = MSG_FAILURE;
                    handler.sendMessage(msg);
                }else if(null!=response&&null!=response.body()){
                    Log.d(TAG, "从阿里云得到评论数据是：");
                    Log.d(TAG, response.body().toString());
                    Result<List<Comment>> result=response.body();
                    List<Comment> comments =result.getData();
                    //发出通知
                    Message msg=new Message();
                    msg.what=MSG_HOT_COMMENTS; //自己规定1代表从阿里云获取数据完毕
                    msg.obj=comments;
                    handler.sendMessage(msg);
                } else {
                    Log.w(TAG, "responew没有评论数据!");
                }
            }
            @Override
            public void onFailure(Call<Result<List<Comment>>>  call, Throwable t) {
                Log.e(TAG,"访问网络失败",t);
            }
        });
    }

    /**
     * 添加新评论
     * @param channelId 频道编号
     * @param comment 评论对象
     * @param handler 主线程需要提供一个通讯录hansler
     */
    public void addComment(String channelId,Comment comment,Handler handler){
        Retrofit retrofit =RetrofitClient.getInstance();
        ChannelApi api=retrofit.create(ChannelApi.class);
        Call<Channel> call=api.addComment(channelId,comment);
        call.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                Log.d(TAG,"新增评论后服务器返回数据：");
                Log.d(TAG,response.body().toString());
                Message msg=new Message();
                msg.what=MSG_ADD_COMMENT;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.e(TAG,"访问网络失败！",t);
                Message msg=new Message();
                msg.what=MSG_FAILURE;
                handler.sendMessage(msg);
            }
        });
    }

}


