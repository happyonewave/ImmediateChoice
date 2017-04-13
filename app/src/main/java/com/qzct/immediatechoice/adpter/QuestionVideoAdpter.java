package com.qzct.immediatechoice.adpter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.activity.MainActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


/**
 * Created by qin on 2017/3/17.
 */

public class QuestionVideoAdpter extends BaseAdapter {


    Activity context;
    List<Question> questionList;
    private String CHOICE_ONE = "1";
//    View v;
//    private StandardGSYVideoPlayer gsyVideoPlayer_left;
//    private StandardGSYVideoPlayer gsyVideoPlayer_right;

    public QuestionVideoAdpter(Activity context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public void onDataChange(List<Question> questionList) {
        this.questionList = questionList;
        this.notifyDataSetChanged();

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

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.view_video_item, null);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);    //拿到相应的View对象
            holder.gsyVideoPlayer_left = (StandardGSYVideoPlayer) convertView.findViewById(R.id.view_video_item_left);
            holder.gsyVideoPlayer_right = (StandardGSYVideoPlayer) convertView.findViewById(R.id.view_video_item_right);
            holder.tpv_left = (ThumbUpView) convertView.findViewById(R.id.tpv_left);
            holder.tpv_right = (ThumbUpView) convertView.findViewById(R.id.tpv_right);
            holder.left_ProgressBar = (NumberProgressBar) convertView.findViewById(R.id.view_video_item_left_ProgressBar);
            holder.right_ProgressBar = (NumberProgressBar) convertView.findViewById(R.id.view_video_item_right_ProgressBar);
            holder.item_username = (TextView) convertView.findViewById(R.id.item_username);
            holder.item_portrait = (ImageView) convertView.findViewById(R.id.item_portrait);
            holder.comment_icon = (Button) convertView.findViewById(R.id.comment_icon);
            holder.share_icon = (Button) convertView.findViewById(R.id.share_icon);
            holder.item_comment = (TextView) convertView.findViewById(R.id.item_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//        v = null;
//
//        if (convertView == null) {
//            v = View.inflate(context, R.layout.view_video_item, null);//将fragment01_item填充成一个View
//        } else {
//            v = convertView;
//        }

//        TextView tv_question = (TextView) v.findViewById(R.id.tv_question);    //拿到相应的View对象
//        gsyVideoPlayer_left = (StandardGSYVideoPlayer) v.findViewById(R.id.view_video_item_left);
//        gsyVideoPlayer_right = (StandardGSYVideoPlayer) v.findViewById(R.id.view_video_item_right);
//        ThumbUpView tpv_left = (ThumbUpView) v.findViewById(R.id.tpv_left);
//        ThumbUpView tpv_right = (ThumbUpView) v.findViewById(R.id.tpv_right);


//        holder.gsyVideoPlayer_left.setThumbImageView(new View(context));
//        holder.gsyVideoPlayer_right.setThumbImageView(new View(context));
//        holder.tpv_left.setUnlike();
//        holder.tpv_right.setUnlike();
//        holder.left_ProgressBar.setMax(100);
//        holder.right_ProgressBar.setMax(100);
//        holder.left_ProgressBar.setVisibility(View.GONE);
//        holder.right_ProgressBar.setVisibility(View.GONE);
//        holder.left_ProgressBar.setProgress(0);
//        holder.right_ProgressBar.setProgress(0);


        Question i = questionList.get(position);                                    //拿到一个info对象
        initVideoPlayer(holder.gsyVideoPlayer_left, i.getLeft_url());
        initVideoPlayer(holder.gsyVideoPlayer_right, i.getRight_url());
//        holder.gsyVideoPlayer_left.setImageBitmap(utils.createVideoThumbnail(i.getLeft_url()));
//        holder.gsyVideoPlayer_right.setImageBitmap(utils.createVideoThumbnail(i.getLeft_url()));
        holder.tv_question.setText(i.getQuestion_content());


        if (i.getPortrait_url() != null) {
            holder.item_username.setText(i.getQuizzer_name());
            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setCircular(true);
            builder.setLoadingDrawableId(R.mipmap.default_portrait);//加载中默认显示图片
            builder.setFailureDrawableId(R.mipmap.default_portrait);
            builder.setSize(100, 100);
            ImageOptions options = builder.build();
            x.image().bind(holder.item_portrait, i.getPortrait_url(), options);
        }
        holder.comment_icon.setText(i.getComment_count() + "");
        holder.share_icon.setText(i.getShare_count() + "");
        holder.item_comment.setText(i.getComment());
        holder.tpv_left.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like) {
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
//                            showBar(percent, 100 - percent);
                                holder.left_ProgressBar.setVisibility(View.VISIBLE);
                                holder.right_ProgressBar.setVisibility(View.VISIBLE);
                                setProgress(holder.left_ProgressBar, percent);
                                setProgress(holder.right_ProgressBar, 100 - percent);
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
            }
        });
        holder.tpv_right.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like) {
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
                                        "right:" + (percent) + "%", Toast.LENGTH_SHORT).show();
//                            showBar(percent, 100 - percent);
                                holder.left_ProgressBar.setVisibility(View.VISIBLE);
                                holder.right_ProgressBar.setVisibility(View.VISIBLE);
                                setProgress(holder.left_ProgressBar, 100 - percent);
                                setProgress(holder.right_ProgressBar, percent);
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
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tv_question;
        StandardGSYVideoPlayer gsyVideoPlayer_left;
        StandardGSYVideoPlayer gsyVideoPlayer_right;
        ThumbUpView tpv_left;
        ThumbUpView tpv_right;
        NumberProgressBar left_ProgressBar;
        NumberProgressBar right_ProgressBar;
        TextView item_username;
        ImageView item_portrait;
        Button comment_icon;
        Button share_icon;
        TextView item_comment;
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

    private class SetThumbImageTask extends AsyncTask<String, String, Bitmap> {
        StandardGSYVideoPlayer gsyVideoPlayer;
        String url;

        public SetThumbImageTask(StandardGSYVideoPlayer gsyVideoPlayer, String url) {
            this.gsyVideoPlayer = gsyVideoPlayer;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap videoThumbnail = utils.createVideoThumbnail(url);
            return videoThumbnail;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Bitmap videoThumbnail) {
            ImageView imageView = new ImageView(context);
//            Bitmap mBitmap = Bitmap.createBitmap(gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight(), Bitmap.Config.ARGB_8888);
            if (videoThumbnail != null) {
                //增加封面
                Bitmap mBitmap = videoThumbnail.copy(Bitmap.Config.ARGB_8888, true);
//                mBitmap = Bitmap.createBitmap(videoThumbnail, 0, 0, gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight());
                zoomImg(mBitmap, gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight());
//            mBitmap.setWidth(gsyVideoPlayer.getWidth());
//            mBitmap.setHeight(gsyVideoPlayer.getHeight());
                imageView.setImageBitmap(mBitmap);
                gsyVideoPlayer.setThumbImageView(imageView);
            }
        }
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private void initVideoPlayer(final StandardGSYVideoPlayer gsyVideoPlayer, String url) {
        //增加封面
        SetThumbImageTask setThumbImageTask = new SetThumbImageTask(gsyVideoPlayer, url);
        setThumbImageTask.execute();
        //url
        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
        gsyVideoPlayer.setUp(url, true, null, "");
//        非全屏下，不显示title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//        非全屏下不显示返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//        打开非全屏下触摸效果
        gsyVideoPlayer.setIsTouchWiget(true);
//        开启自动旋转
        gsyVideoPlayer.setRotateViewAuto(false);
//        全屏首先横屏
//        gsyVideoPlayer.setLockLand(true);
        //是否需要全屏动画效果
        gsyVideoPlayer.setShowFullAnimation(false);
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(MainActivity.mainActivity, true, true);
            }
        });
        gsyVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先返回正常状态
                OrientationUtils orientationUtils = new OrientationUtils(LoginActivity.loginActivity, gsyVideoPlayer);
                if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                    gsyVideoPlayer.getFullscreenButton().performClick();
                    gsyVideoPlayer.clearFullscreenLayout();
                    return;
                }
            }
        });


//
//        gsyVideoPlayer.setStandardVideoAllCallBack(new StandardVideoAllCallBack() {
//            @Override
//            public void onClickStartThumb(String s, Object... objects) {
//                Toast.makeText(context, "onClickStartThumb", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onClickBlank(String s, Object... objects) {
//                Toast.makeText(context, "onClickBlank", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickBlankFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onClickBlankFullscreen", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPrepared(String s, Object... objects) {
//                Toast.makeText(context, "onPrepared", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onClickStartIcon(String s, Object... objects) {
//                Toast.makeText(context, "onClickStartIcon", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickStartError(String s, Object... objects) {
//                Toast.makeText(context, "onClickStartError", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickStop(String s, Object... objects) {
//                Toast.makeText(context, "onClickStop", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickStopFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onClickStopFullscreen", Toast.LENGTH_SHORT).show();
//                gsyVideoPlayer.backFromWindowFull(LoginActivity.loginActivity);
//            }
//
//            @Override
//            public void onClickResume(String s, Object... objects) {
//                Toast.makeText(context, "onClickResume", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickResumeFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onClickResumeFullscreen", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickSeekbar(String s, Object... objects) {
//                Toast.makeText(context, "onClickSeekbar", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClickSeekbarFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onClickSeekbarFullscreen", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onAutoComplete(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onEnterFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onEnterFullscreen", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onQuitFullscreen(String s, Object... objects) {
//                Toast.makeText(context, "onQuitFullscreen", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onQuitSmallWidget(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onEnterSmallWidget(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onTouchScreenSeekVolume(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onTouchScreenSeekPosition(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onTouchScreenSeekLight(String s, Object... objects) {
//
//            }
//
//            @Override
//            public void onPlayError(String s, Object... objects) {
//                Toast.makeText(context, "onPlayError", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        //立即播放
//        gsyVideoPlayer.startPlayLogic();
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
////        entity.addBodyParameter("question_id", questionList.get(position).);
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