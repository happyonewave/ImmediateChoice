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
 */

public class CommentAdpter extends BaseAdapter {
    Context context;
    List<Comment> commentList;

    public CommentAdpter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }


    public void onDataChange(List<Comment> commentList) {
        this.commentList = commentList;
        this.notifyDataSetChanged();

    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = null;
        if (view == null) {
            v = View.inflate(context, R.layout.dialog_comment_item, null);//将fragment01_item填充成一个View
        } else {
            v = view;
        }

        ImageView comment_portrait = (ImageView) v.findViewById(R.id.comment_portrait);
        TextView comment_name = (TextView) v.findViewById(R.id.comment_name);
        TextView comment_date = (TextView) v.findViewById(R.id.comment_date);
        TextView comment_content = (TextView) v.findViewById(R.id.comment_content);

        Comment comment = commentList.get(i);
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
