package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
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

        SmartImageView user_item_img_left = (SmartImageView) v.findViewById(R.id.user_item_img_left);
        SmartImageView user_item_img_right = (SmartImageView) v.findViewById(R.id.user_item_img_right);
        TextView user_tv_question = (TextView) v.findViewById(R.id.user_tv_question);
        //拿到一个info对象

        user_tv_question.setText(Question.getQuestion_content());
        System.out.println(Question.getQuestion_content());
        user_item_img_left.setImageUrl(Question.getLeft_url());
        user_item_img_right.setImageUrl(Question.getRight_url());
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