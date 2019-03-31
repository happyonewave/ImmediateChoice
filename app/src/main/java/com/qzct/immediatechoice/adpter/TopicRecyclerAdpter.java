package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.TopicActivity;
import com.qzct.immediatechoice.domain.Topic;
import com.qzct.immediatechoice.util.GlideRoundTransform;

import java.util.List;

import static com.baidu.location.d.a.h;
import static com.baidu.location.d.a.i;

/**
 * Created by wuwenqin on 2019/3/31.
 */

public class TopicRecyclerAdpter extends RecyclerView.Adapter<TopicRecyclerAdpter.Holder> {

    private Context context;
    private List<Topic> topicList;
    private OnItemClickListener onItemClickListener;

    public TopicRecyclerAdpter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    //创建ViewHolder
    public static class Holder extends RecyclerView.ViewHolder {
        private ImageView view_class_img;
        private TextView view_class_text;

        public Holder(View v) {
            super(v);
            //获取item中的组件
            view_class_img = (ImageView) v.findViewById(R.id.view_topic_img);
            view_class_text = (TextView) v.findViewById(R.id.view_topic_text);
        }


        public void setData(Topic item, Context context) {
            view_class_text.setText(item.getTopic_title());
            Glide.with(context).load(item.getTopic_img_url()).bitmapTransform(new GlideRoundTransform(context, 5)).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(view_class_img);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = View.inflate(context, R.layout.view_topic_item, null);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int i) {
        final Topic topic = topicList.get(i);
        holder.setData(topic, context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(view,pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        return null != topicList ? topicList.size() : 0;
    }

    //定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}