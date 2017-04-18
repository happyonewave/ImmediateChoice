package com.qzct.immediatechoice.adpter;

/**
 * Created by qin on 2017/3/12.
 */


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.domain.Topic;

import java.util.List;


/**
 * Topic适配器
 */
public class TopicAdpter extends BaseAdapter {
    private Context context;
    private List<Topic> topicList;

    /**
     * 数据改变 刷新Ui显示
     */
    public void onDataChange(List<Topic> topicList) {
        this.topicList = topicList;
        this.notifyDataSetChanged();

    }

    /**
     * 构造方法
     *
     * @param context   上下文
     * @param topicList topic数据
     */
    public TopicAdpter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    /**
     * 获取item数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return topicList.size();
    }

    /**
     * 获得itemView
     *
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        //判断View是否存在，不存在就创建
        if (view == null) {
            v = view.inflate(context, R.layout.view_topic_item, null);
        } else {
            v = view;
        }
        //获取item中的组件
        ImageView view_class_img = (ImageView) v.findViewById(R.id.view_topic_img);
        TextView view_class_text = (TextView) v.findViewById(R.id.view_topic_text);
        final Topic topic = topicList.get(i);

//        view_class_img.setImageUrl(topic.getTopic_img_url());
        Glide.with(context).load(topic.getTopic_img_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(view_class_img);

        view_class_text.setText(topic.getTopic_title());

        return v;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}

