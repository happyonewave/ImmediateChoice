package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Question;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by qin on 2017/2/26.
 */

public class ImageTextAdpter extends BaseAdapter {


    Context context;
    List<Question> questionlist;

    public ImageTextAdpter(Context context, List<Question> questionlist) {
        this.context = context;
        this.questionlist = questionlist;
    }

    public void onDataChange(List<Question> questionlist) {
        this.questionlist =questionlist;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return questionlist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView == null) {
            v = View.inflate(context, R.layout.fragment_image_text_item, null);//将fragment01_item填充成一个View
        } else {
            v = convertView;
        }


        TextView tv_question = (TextView) v.findViewById(R.id.tv_question);    //拿到相应的View对象
        SmartImageView image_text_item_img_left = (SmartImageView) v.findViewById(R.id.image_text_item_img_left);
        SmartImageView image_text_item_img_right = (SmartImageView) v.findViewById(R.id.image_text_item_img_right);
        TextView item_username = (TextView) v.findViewById(R.id.item_username);
        ImageView item_portrait = (ImageView) v.findViewById(R.id.item_portrait);
        Button comment_icon = (Button) v.findViewById(R.id.comment_icon);
        Button share_icon = (Button) v.findViewById(R.id.share_icon);
        TextView item_comment = (TextView) v.findViewById(R.id.item_comment);


        Question i = questionlist.get(position);                                    //拿到一个info对象

        tv_question.setText(i.getQuestion_content());
        image_text_item_img_left.setImageUrl(i.getImage_left());


        if (i.getQuizzer_portrait() != null) {
            image_text_item_img_right.setImageUrl(i.getImage_right());                                    //设置相应的信息
            item_username.setText(i.getQuizzer_name());
            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setCircular(true);
            builder.setLoadingDrawableId(R.mipmap.default_portrait);//加载中默认显示图片
            builder.setFailureDrawableId(R.mipmap.default_portrait);
            builder.setSize(100, 100);
            ImageOptions options = builder.build();
            x.image().bind(item_portrait, i.getQuizzer_portrait(), options);
        }
        comment_icon.setText(i.getComment_count() + "");
        share_icon.setText(i.getShare_count() + "");
        item_comment.setText(i.getComment());
        System.out.println(i.getImage_left());
        System.out.println(i.getImage_right());
        return v;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}