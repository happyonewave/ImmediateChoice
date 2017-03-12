package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Comment;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by qin on 2017/3/11.
 * comment适配器
 */

public class CommentAdpter extends BaseAdapter {
    Context context;
    List<Comment> commentList;

    /**
     * 构造方法
     * @param context  上下文
     * @param commentList Comment数据
     */
    public CommentAdpter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    /**
     * 获取item数量
     * @return
     */
    @Override
    public int getCount() {
        return commentList.size();
    }

    /**
     * 数据改变 刷新UI
     * @param commentList
     */
    public void onDataChange(List<Comment> commentList) {
        this.commentList = commentList;
        this.notifyDataSetChanged();

    }

    /**
     * 获取ItemView
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = null;
        //判断View是否存在 如果不存在 创建
        if (view == null) {
            v = View.inflate(context, R.layout.dialog_comment_item, null);//将fragment01_item填充成一个View
        } else {
            v = view;
        }
        //获取item中的view
        ImageView comment_portrait = (ImageView) v.findViewById(R.id.comment_portrait);
        TextView comment_name = (TextView) v.findViewById(R.id.comment_name);
        TextView comment_date = (TextView) v.findViewById(R.id.comment_date);
        TextView comment_content = (TextView) v.findViewById(R.id.comment_content);
        //取出单个comment
        Comment comment = commentList.get(i);
        //如果comment存在 填充数据
        if (comment != null) {
            ImageOptions options = new ImageOptions.Builder().setCircular(true).build();
            x.image().bind(comment_portrait, comment.getCommenter_portrait_url(), options);
            comment_name.setText(comment.getCommenter_name());
            comment_date.setText(comment.getComment_date());
            comment_content.setText(comment.getComment_content());
        }
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
