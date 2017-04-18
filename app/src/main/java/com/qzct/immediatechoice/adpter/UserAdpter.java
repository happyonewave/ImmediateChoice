package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Question;

import java.util.List;

/**
 * Created by qin on 2017/2/21.
 */

public class UserAdpter extends BaseAdapter {

    List<Question> questionList;
    Context context;

    public UserAdpter(Context centext, List<Question> questionList) {
        this.questionList = questionList;
        this.context = centext;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        Log.i("now", "getView");
        Question Question = questionList.get(position);
        if (convertView == null) {
            v = v.inflate(context, R.layout.fragment_user_item, null);
            System.out.println("调用：" + position);
        } else {
            v = convertView;
        }

        ImageView user_item_img_left = (ImageView) v.findViewById(R.id.user_item_img_left);
        ImageView user_item_img_right = (ImageView) v.findViewById(R.id.user_item_img_right);
        TextView user_tv_question = (TextView) v.findViewById(R.id.user_tv_question);
        //拿到一个info对象

        user_tv_question.setText(Question.getQuestion_content());
        System.out.println(Question.getQuestion_content());
        Glide.with(context).load(Question.getLeft_url()).into(user_item_img_left);
        Glide.with(context).load(Question.getRight_url()).into(user_item_img_right);
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