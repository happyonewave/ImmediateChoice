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

    String ListviewName;
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
//				v =  new LinearLayout(context);
//				v.setOrientation(LinearLayout.VERTICAL);
//            if(conversation.getAddresser().equals( "小梨子")){
//                v =  v.inflate(context, R.layout.fragment_user_item_lift, null);
//            }else{
//                v =  v.inflate(context, R.layout.ragment_userf_item_right, null);
//            }
            v = v.inflate(context, R.layout.fragment_user_item, null);
            System.out.println("调用：" + position);
        } else {
            v = convertView;
        }


//			SmartImageView siv = new SmartImageView(context);
//			TextView tv_content = new TextView(context);
//			TextView tv_addresser = new TextView(context);
//			TextView tv_addressee = new TextView(context);
//			siv.setRight(0);

        SmartImageView user_item_img_left = (SmartImageView) v.findViewById(R.id.user_item_img_left);
        SmartImageView user_item_img_right = (SmartImageView) v.findViewById(R.id.user_item_img_right);
        TextView user_tv_question = (TextView) v.findViewById(R.id.user_tv_question);
        //拿到一个info对象

        user_tv_question.setText(Question.getQuestion_content());
        System.out.println(Question.getQuestion_content());
        user_item_img_left.setImageUrl(Question.getImage_left());
//        ImageOptions options = new ImageOptions.Builder().build();
//        x.image().bind(user_item_img_left,Question.getImage_left(),options);
        user_item_img_right.setImageUrl(Question.getImage_right());
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