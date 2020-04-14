package com.example.diandian;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ChannelLab {
    private  List<Channel>data;

    public ChannelLab(){
        //TODO 把下面的代码换成从网络获取数据
        test();
        getData();
    }
    /**
     * 生成测试数据
     */
    public void test() {
        data = new ArrayList<>();
        Channel c = new Channel();
        c.setTitle("CCTV-1 综合");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a1);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-2 财经");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a2);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv2hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-3 综艺");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a3);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-4 中文国际");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a4);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv4hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-5 体育");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a5);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-6 电影");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a6);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-7 军事农业");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a7);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv7hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-8 电视剧");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a8);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv8hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-9 记录");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a9);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv9hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-10 科教");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a10);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv10hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-11 戏曲");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a11);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv11.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-12 社会与法");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a12);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv12hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-13 新闻");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a13);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv13.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-14 少儿");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a14);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv14hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-15 音乐");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a15);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv15.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV-17 农业农村");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a17);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv17hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("CCTV 新科动漫");
        c.setQuality("4k 超高清");
        c.setCover(R.drawable.a16);
        c.setUrl("http://ivi.bupt.edu.cn/hls/btv10.m3u8");
        data.add(c);
    }
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
     */
    private void getData(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://47.115.94.109:8005")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<List<Channel>> call=api.getAllChannels();
        call.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                if (null != response && null != response.body()) {
                    Log.d("Diandian", "从阿里云得到数据是：");
                    Log.d("Diandian", response.body().toString());
                } else {
                    Log.w("Diandian", "responew没有数据!");
                }
            }
            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                    Log.e("Diandian","访问网络失败",t);

            }
        });

    }
}
