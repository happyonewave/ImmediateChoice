package com.qzct.immediatechoice.adpter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


/**
 * Created by qin on 2017/2/26.
 */

public class ImageTextAdpter extends BaseAdapter {


    Activity context;
    List<Question> questionList;
    private String CHOICE_ONE = "1";
    SmartImageView image_text_item_img_left;
    SmartImageView image_text_item_img_right;
    View v;

    public ImageTextAdpter(Activity context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public void onDataChange(List<Question> questionList) {
        this.questionList.clear();
        this.questionList.addAll(questionList);
        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }

    public Question getQuestionFromItem(int position) {
        return questionList.get(position);
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = null;
        if (convertView == null) {
            v = View.inflate(context, R.layout.view_image_text_item, null);//将fragment01_item填充成一个View
        } else {
            v = convertView;
        }

        TextView tv_question = (TextView) v.findViewById(R.id.tv_question);    //拿到相应的View对象
        image_text_item_img_left = (SmartImageView) v.findViewById(R.id.image_text_item_img_left);
        image_text_item_img_right = (SmartImageView) v.findViewById(R.id.image_text_item_img_right);
        image_text_item_img_left.setVisibility(View.VISIBLE);
        image_text_item_img_right.setVisibility(View.VISIBLE);
        final NumberProgressBar left_ProgressBar = (NumberProgressBar) v.findViewById(R.id.image_text_item_left_ProgressBar);
        final NumberProgressBar right_ProgressBar = (NumberProgressBar) v.findViewById(R.id.image_text_item_right_ProgressBar);
        left_ProgressBar.setMax(100);
        right_ProgressBar.setMax(100);
        left_ProgressBar.setVisibility(View.GONE);
        right_ProgressBar.setVisibility(View.GONE);
        left_ProgressBar.setProgress(0);
        right_ProgressBar.setProgress(0);
//        image_text_item_img_left.setImageBitmap(null);
//        image_text_item_img_right.setImageBitmap(null);
        TextView item_username = (TextView) v.findViewById(R.id.item_username);
        ImageView item_portrait = (ImageView) v.findViewById(R.id.item_portrait);
        Button comment_icon = (Button) v.findViewById(R.id.comment_icon);
        Button share_icon = (Button) v.findViewById(R.id.share_icon);
        TextView item_comment = (TextView) v.findViewById(R.id.item_comment);


        Question i = questionList.get(position);                                    //拿到一个info对象

        tv_question.setText(i.getQuestion_content());
//        image_text_item_img_left.setImageUrl(i.getLeft_url());
        Glide.with(context).load(i.getLeft_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_left);


        if (i.getPortrait_url() != null) {
            image_text_item_img_right.setImageUrl(i.getRight_url());                                    //设置相应的信息
            Glide.with(context).load(i.getRight_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_right);
            item_username.setText(i.getQuizzer_name());
//            ImageOptions.Builder builder = new ImageOptions.Builder();
//            builder.setCircular(true);
//            builder.setLoadingDrawableId(R.mipmap.default_portrait);//加载中默认显示图片
//            builder.setFailureDrawableId(R.mipmap.default_portrait);
//            builder.setSize(100, 100);
//            ImageOptions options = builder.build();
//            x.image().bind(item_portrait, i.getPortrait_url(), options);
            Glide.with(context).load(i.getPortrait_url()).placeholder(R.mipmap.default_portrait).error(R.mipmap.default_portrait).into(item_portrait);
        }
        comment_icon.setText(i.getComment_count() + "");
        share_icon.setText(i.getShare_count() + "");
        item_comment.setText(i.getComment());
        System.out.println(i.getLeft_url());
        System.out.println(i.getRight_url());
        //left点击事件监听
        image_text_item_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams entity = new RequestParams(Config.url_comment);
                entity.addBodyParameter("msg", CHOICE_ONE);
                entity.addBodyParameter("question_id", questionList.get(position).getQuestion_id() + "");
                entity.addBodyParameter("user_id",
                        String.valueOf(MyApplication.user.getUser_id()));

                entity.addBodyParameter("left_or_right", "left");
                x.http().post(entity, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            int percent = Integer.parseInt(result);
                            Toast.makeText(context, "left:" + percent + "%," +
                                    "right:" + (100 - percent) + "%", Toast.LENGTH_SHORT).show();
                            left_ProgressBar.setVisibility(View.VISIBLE);
                            right_ProgressBar.setVisibility(View.VISIBLE);
                            setProgress(left_ProgressBar, percent);
                            setProgress(right_ProgressBar, 100 - percent);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }


        });
        //right点击事件监听
        image_text_item_img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_text_item_img_left.setBackgroundColor(Color.parseColor("#60000000"));
//                Glide.with(context).load(image_text_item_img_left.getResources())
//                        .bitmapTransform(new BlurTransformation(context, 25), new CenterCrop(context))
//                        .into(image_text_item_img_left);
//
//                Glide.with(context).load(image_text_item_img_right.getResources())
//                        .bitmapTransform(new BlurTransformation(context, 25), new CenterCrop(context))
//                        .into(image_text_item_img_right);

                RequestParams entity = new RequestParams(Config.url_comment);
                entity.addBodyParameter("msg", CHOICE_ONE);
                entity.addBodyParameter("question_id", questionList.get(position).getQuestion_id() + "");
                entity.addBodyParameter("user_id",
                        String.valueOf(MyApplication.user.getUser_id()));

                entity.addBodyParameter("left_or_right", "right");
                x.http().post(entity, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            int percent = Integer.parseInt(result);
                            Toast.makeText(context, "left:" + (100 - percent) + "%," +
                                    "right:" + percent + "%", Toast.LENGTH_SHORT).show();
                            left_ProgressBar.setVisibility(View.VISIBLE);
                            right_ProgressBar.setVisibility(View.VISIBLE);
                            setProgress(left_ProgressBar, 100 - percent);
                            setProgress(right_ProgressBar, percent);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
        return v;
    }

    /**
     * 显示投票情况
     */
    public void setProgress(final NumberProgressBar numberProgressBar, final int num) {

        final Handler handler = new Handler();
        numberProgressBar.setProgress(0);
        Runnable runnable = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                numberProgressBar.setProgress(counter);
                if (counter == num) {
                    counter = 0;
                } else {
                    handler.postDelayed(this, 50);
                }
            }
        };
        handler.postDelayed(runnable, 50);
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