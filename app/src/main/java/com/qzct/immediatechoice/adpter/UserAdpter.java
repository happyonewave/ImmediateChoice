package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.GlideRoundTransform;
import com.qzct.immediatechoice.util.Utils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        Question question = questionList.get(position);
        if (convertView == null) {
            v = View.inflate(context, R.layout.fragment_user_item, null);
            System.out.println("调用：" + position);
        } else {
            v = convertView;
        }

        ImageView user_item_img_left = (ImageView) v.findViewById(R.id.user_item_img_left);
        ImageView user_item_img_right = (ImageView) v.findViewById(R.id.user_item_img_right);
        TextView user_tv_question = (TextView) v.findViewById(R.id.user_tv_question);
        //拿到一个info对象

        user_tv_question.setText(question.getQuestion_content());
        System.out.println(question.getQuestion_content());
//        Log.d("qin", ".equals(question.getLeft_url()): " + "video".equals(question.getLeft_url()));
        if (question.getLeft_url().contains("video")) {
            SetThumbImageTask setThumbImageTask = new SetThumbImageTask(user_item_img_left, question.getLeft_url());
            setThumbImageTask.execute();
            SetThumbImageTask setRightThumbImageTask = new SetThumbImageTask(user_item_img_right, question.getRight_url());
            setRightThumbImageTask.execute();
            return v;
        }
        Glide.with(context).load(question.getLeft_url()).bitmapTransform(new GlideRoundTransform(context, 5)).into(user_item_img_left);
        Glide.with(context).load(question.getRight_url()).bitmapTransform(new GlideRoundTransform(context, 5)).into(user_item_img_right);
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

    private class SetThumbImageTask extends AsyncTask<String, String, Bitmap> {
        ImageView imageView;
        String url;

        public SetThumbImageTask(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return Utils.createVideoThumbnail(url);
        }

        @Override
        protected void onPostExecute(Bitmap videoThumbnail) {
            if (videoThumbnail != null) {
                Bitmap mBitmap = Bitmap.createBitmap(videoThumbnail);
//                if (imageView.getWidth() > 0) {
//                    mBitmap = Bitmap.createScaledBitmap(videoThumbnail, imageView.getWidth(), imageView.getHeight(), true);
//                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                Glide.with(context.getApplicationContext()).load(bytes).bitmapTransform(new GlideRoundTransform(context, 20)).into(imageView);
//                imageView.setImageBitmap(mBitmap);
            }
        }
    }
}