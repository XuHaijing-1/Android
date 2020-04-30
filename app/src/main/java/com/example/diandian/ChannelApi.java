package com.example.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChannelApi {
        /**
         * 获取收有频道
         * @return
         */
        @GET("/channel")
        Call<List<Channel>> getAllChannels();

        /**
         * 获取热门评论
         * @param channenId 频道编号
         * @return 热门评论的列表
         */
        @GET("/channel/{channelId}/hotcomments")
        Call<List<Comment>>getHotComments(@Path("channelId") String channenId);

}
