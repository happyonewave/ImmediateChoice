package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.application.MyApplication;
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


    Context context;
    List<Question> questionlist;
    private String CHOICE_ONE = "1";
    SmartImageView image_text_item_img_left;
    SmartImageView image_text_item_img_right;
    View v;
//    MagicProgressCircle left_ProgressBar;
//    MagicProgressCircle right_ProgressBar;

    public ImageTextAdpter(Context context, List<Question> questionlist) {
        this.context = context;
        this.questionlist = questionlist;
    }

    public void onDataChange(List<Question> questionlist) {
        this.questionlist = questionlist;
        this.notifyDataSetChanged();

    }

    public Question getQuestionFromItem(int position) {
        return questionlist.get(position);
    }

    @Override
    public int getCount() {
        return questionlist.size();
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
        final ProgressBar left_ProgressBar = (ProgressBar) v.findViewById(R.id.image_text_item_left_ProgressBar);
        final ProgressBar right_ProgressBar = (ProgressBar) v.findViewById(R.id.image_text_item_right_ProgressBar);
        left_ProgressBar.setMax(100);
        right_ProgressBar.setMax(100);
        left_ProgressBar.setVisibility(View.GONE);
        right_ProgressBar.setVisibility(View.GONE);
        left_ProgressBar.setProgress(0);
        right_ProgressBar.setProgress(0);
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
        //left点击事件监听
        image_text_item_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams entity = new RequestParams(Config.url_comment);
                entity.addBodyParameter("msg", CHOICE_ONE);
                entity.addBodyParameter("question_id", questionlist.get(position).getQuestion_id() + "");
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
//                            showBar(percent, 100 - percent);
                            left_ProgressBar.setVisibility(View.VISIBLE);
                            right_ProgressBar.setVisibility(View.VISIBLE);
                            left_ProgressBar.setProgress(percent);
                            right_ProgressBar.setProgress(100 - percent);
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
                RequestParams entity = new RequestParams(Config.url_comment);
                entity.addBodyParameter("msg", CHOICE_ONE);
                entity.addBodyParameter("question_id", questionlist.get(position).getQuestion_id() + "");
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
//                            showBar(100 - percent, percent);
                            left_ProgressBar.setVisibility(View.VISIBLE);
                            right_ProgressBar.setVisibility(View.VISIBLE);
                            left_ProgressBar.setProgress(100 - percent);
                            right_ProgressBar.setProgress(percent);
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
    private void showBar(int count_left, int count_right) {
//        left_ProgressBar.setVisibility(View.VISIBLE);
//        right_ProgressBar.setVisibility(View.VISIBLE);
//        left_ProgressBar.setPercent(count_left / 100);
//        right_ProgressBar.setPercent(count_right / 100);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {


        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public void onClick(View view) {
//        RequestParams entity = new RequestParams(MyApplication.url_comment);
//        entity.addBodyParameter("msg", CHOICE_ONE);
////        entity.addBodyParameter("question_id", questionlist.get(position).);
//        entity.addBodyParameter("user_id",
//                String.valueOf(MyApplication.user.getUser_id()));
//        switch (view.getId()) {
//            case R.id.image_text_item_img_left:
//                entity.addBodyParameter("left_or_right", "left");
//                x.http().post(entity, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        if (result != null) {
//                            int percent = Integer.parseInt(result);
//                            Toast.makeText(context, "left:" + percent + "%," +
//                                    "right:" + (100 - percent) + "%", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
//                break;
//            case image_text_item_img_right:
//                entity.addBodyParameter("left_or_right", "right");
//                x.http().post(entity, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        if (result != null) {
//                            int percent = Integer.parseInt(result);
//                            Toast.makeText(context, "right:" + percent + "%," +
//                                    "right:" + (100 - percent) + "%", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
//
//                break;
//            default:
//
//                break;
//        }
//
//    }
}