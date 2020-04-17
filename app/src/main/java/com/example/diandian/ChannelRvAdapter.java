package com.example.diandian;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelRvAdapter extends RecyclerView.Adapter<ChannelRvAdapter.ChannelRowHolder> {

    private ChannelLab lab=ChannelLab.getInstance();
    private ChannelClickListener listener;

    public ChannelRvAdapter(ChannelClickListener listener){
        this.listener=listener;
    }
    /**
     * 当需要新的一行，此方法负责创建一行对应的对象，既ChannelRowHolder对象
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ChannelRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row,parent,false);
        ChannelRowHolder holder =new ChannelRowHolder(rowView);
        return holder;
    }

    /**
     * 用于确定列表有几行（既多少ChannRowHolder对象）
     * @return
     */
    @Override
    public int getItemCount() {
        return lab.getSize();
    }

    /**
     * 用于确定每一行的内容是什么，及填充行中各个视图内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChannelRowHolder holder, int position) {
        Channel c=lab.getChannel(position);
        holder.bind(c);
    }

    //自定义新接口
    public interface ChannelClickListener{
        public void onChannelClick(int position);
    }

    /**
     * 单行布局对应的java控制类
     */
    public class ChannelRowHolder extends RecyclerView.ViewHolder{
        private TextView title; //频道标题 实例对象
        private TextView quality;//清晰度
        private ImageView cover;

        public ChannelRowHolder(@NonNull View row) {
            super(row);
            this.title=row.findViewById(R.id.channel_title);
            this.quality=row.findViewById(R.id.channel_quality);
            this.cover=row.findViewById(R.id.channel_cover);
            row.setOnClickListener((v)->{
                int position= getLayoutPosition();
                Log.d("DianDian", position+"行被点击啦！");
                    listener.onChannelClick(position);
            });
        }

        /**
         * 自定义方法，用于向内部的title提供数据
         * @param // title
         */
        public void bind(Channel c){
            this.title.setText(c.getTitle());
            this.quality.setText(c.getQuality());
            this.cover.setImageResource(c.getCover());
        }
    }

//    public interface ChannelClickListener{
//        public void onChannelClick();
//    }
}
