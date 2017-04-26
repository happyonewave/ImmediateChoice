package com.qzct.immediatechoice.adpter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * Created by qin on 2017/2/26.
 */

public class ImageTextAdpter extends BaseAdapter {


    AppCompatActivity context;
    List<Question> questionList;
    private String CHOICE_ONE = "1";
    View v;

    public ImageTextAdpter(AppCompatActivity context, List<Question> questionList) {
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
        final ImageView image_text_item_img_left = (ImageView) v.findViewById(R.id.image_text_item_img_left);
        final ImageView image_text_item_img_right = (ImageView) v.findViewById(R.id.image_text_item_img_right);
        image_text_item_img_left.setVisibility(View.VISIBLE);
        image_text_item_img_right.setVisibility(View.VISIBLE);
        image_text_item_img_left.clearColorFilter();
        image_text_item_img_right.clearColorFilter();
        final NumberProgressBar left_ProgressBar = (NumberProgressBar) v.findViewById(R.id.image_text_item_left_ProgressBar);
        final NumberProgressBar right_ProgressBar = (NumberProgressBar) v.findViewById(R.id.image_text_item_right_ProgressBar);
        left_ProgressBar.setMax(100);
//        right_ProgressBar.setMax(100);
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
        Glide.with(context).load(i.getLeft_url()).into(image_text_item_img_left);
//        Glide.with(context).load(i.getLeft_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_left);


        if (i.getPortrait_url() != null) {                                   //设置相应的信息
            Glide.with(context).load(i.getRight_url()).into(image_text_item_img_right);
//            Glide.with(context).load(i.getRight_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_right);
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
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheet.createBuilder(context, context.getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("微信好友", "朋友圈", "QQ空间")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
//                                Toast.makeText(context, "dismissed isCancle = " + isCancel, Toast.LENGTH_SHORT).show();
                                if (isCancel) {
                                    actionSheet.dismiss();
                                }
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                                Toast.makeText(context, "click item index = " + index,
//                                        Toast.LENGTH_SHORT).show();
                                switch (index) {
                                    case 0:
                                        Toast.makeText(context, "微信好友", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(context, "朋友圈", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(context, "QQ空间", Toast.LENGTH_SHORT).show();
                                        break;

                                }

                            }
                        }).show();

            }
        });
        comment_icon.setTag(questionList.get(position));
        comment_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = (Question) v.getTag();
                MyApplication.isQuestion = true;
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("question", question);
                context.startActivity(intent);
            }
        });
        item_comment.setText(i.getComment());
        System.out.println(i.getLeft_url());
        System.out.println(i.getRight_url());
        //left点击事件监听
        image_text_item_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ImageView imageView = (ImageView) view;
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
//                            Toast.makeText(context, "left:" + percent + "%," +
//                                    "right:" + (100 - percent) + "%", Toast.LENGTH_SHORT).show();
                            imageView.setColorFilter(Color.parseColor("#99000000"));
                            image_text_item_img_right.setColorFilter(Color.parseColor("#99000000"));
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
            public void onClick(final View view) {
                final ImageView imageView = (ImageView) view;
//                image_text_item_img_left.setBackgroundColor(Color.parseColor("#60000000"));
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
//                            Toast.makeText(context, "left:" + (100 - percent) + "%," +
//                                    "right:" + percent + "%", Toast.LENGTH_SHORT).show();
                            image_text_item_img_left.setColorFilter(Color.parseColor("#99000000"));
                            imageView.setColorFilter(Color.parseColor("#99000000"));
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
//        numberProgressBar.setCircleColor();
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