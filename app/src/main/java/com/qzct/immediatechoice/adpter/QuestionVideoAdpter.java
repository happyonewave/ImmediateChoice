package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.domain.QuestionVideo;
import com.qzct.immediatechoice.util.utils;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


/**
 * Created by qin on 2017/3/17.
 */

public class QuestionVideoAdpter extends BaseAdapter {


    Context context;
    List<QuestionVideo> questionVideoList;
    private String CHOICE_ONE = "1";
    //    IjkPlayerView view_video_item_left;
//    IjkPlayerView view_video_item_right;
    View v;
    private StandardGSYVideoPlayer gsyVideoPlayer_left;
    private StandardGSYVideoPlayer gsyVideoPlayer_right;
//    MagicProgressCircle left_ProgressBar;
//    MagicProgressCircle right_ProgressBar;

    public QuestionVideoAdpter(Context context, List<QuestionVideo> questionVideoList) {
        this.context = context;
        this.questionVideoList = questionVideoList;
    }

    public void onDataChange(List<QuestionVideo> questionVideoList) {
        this.questionVideoList = questionVideoList;
        this.notifyDataSetChanged();

    }

    public QuestionVideo getQuestionVideoFromItem(int position) {
        return questionVideoList.get(position);
    }

    @Override
    public int getCount() {
        return questionVideoList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = null;
        if (convertView == null) {
            v = View.inflate(context, R.layout.view_video_item, null);//将fragment01_item填充成一个View
        } else {
            v = convertView;
        }

        TextView tv_question = (TextView) v.findViewById(R.id.tv_question);    //拿到相应的View对象
//        view_video_item_left = (IjkPlayerView) v.findViewById(R.id.view_video_item_left);
//        view_video_item_right = (IjkPlayerView) v.findViewById(R.id.view_video_item_right);
//        view_video_item_left.setVisibility(View.VISIBLE);
        gsyVideoPlayer_left = (StandardGSYVideoPlayer) v.findViewById(R.id.view_video_item_left);
        gsyVideoPlayer_right = (StandardGSYVideoPlayer) v.findViewById(R.id.view_video_item_right);
//
        gsyVideoPlayer_left.setThumbImageView(new View(context));
        gsyVideoPlayer_right.setThumbImageView(new View(context));
//  view_video_item_right.setVisibility(View.VISIBLE);
//        final ProgressBar left_ProgressBar = (ProgressBar) v.findViewById(R.id.view_video_item_left_ProgressBar);
//        final ProgressBar right_ProgressBar = (ProgressBar) v.findViewById(R.id.view_video_item_right_ProgressBar);
//        left_ProgressBar.setMax(100);
//        right_ProgressBar.setMax(100);
//        left_ProgressBar.setVisibility(View.GONE);
//        right_ProgressBar.setVisibility(View.GONE);
//        left_ProgressBar.setProgress(0);
//        right_ProgressBar.setProgress(0);

        TextView item_username = (TextView) v.findViewById(R.id.item_username);
        ImageView item_portrait = (ImageView) v.findViewById(R.id.item_portrait);
        Button comment_icon = (Button) v.findViewById(R.id.comment_icon);
        Button share_icon = (Button) v.findViewById(R.id.share_icon);
        TextView item_comment = (TextView) v.findViewById(R.id.item_comment);


        QuestionVideo i = questionVideoList.get(position);                                    //拿到一个info对象
        initVideoPlayer(gsyVideoPlayer_left, i.getVideo_left());
        initVideoPlayer(gsyVideoPlayer_right, i.getVideo_right());
        tv_question.setText(i.getQuestion_video_content());
//        image_text_item_img_left.setImageUrl(i.getVideo_left());


        if (i.getQuizzer_portrait() != null) {
//            image_text_item_img_right.setImageUrl(i.getVideo_right());                                    //设置相应的信息
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
        System.out.println(i.getVideo_left());
        System.out.println(i.getVideo_right());


//        //left点击事件监听
//        image_text_item_img_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RequestParams entity = new RequestParams(MyApplication.url_comment);
//                entity.addBodyParameter("msg", CHOICE_ONE);
//                entity.addBodyParameter("question_id", questionVideoList.get(position).getQuestion_video_id() + "");
//                entity.addBodyParameter("user_id",
//                        String.valueOf(MyApplication.user.getUser_id()));
//
//                entity.addBodyParameter("left_or_right", "left");
//                x.http().post(entity, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        if (result != null) {
//                            int percent = Integer.parseInt(result);
//                            Toast.makeText(context, "left:" + percent + "%," +
//                                    "right:" + (100 - percent) + "%", Toast.LENGTH_SHORT).show();
////                            showBar(percent, 100 - percent);
//                            left_ProgressBar.setVisibility(View.VISIBLE);
//                            right_ProgressBar.setVisibility(View.VISIBLE);
//                            left_ProgressBar.setProgress(percent);
//                            right_ProgressBar.setProgress(100 - percent);
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
//            }
//
//
//        });
//        //right点击事件监听
//        image_text_item_img_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RequestParams entity = new RequestParams(MyApplication.url_comment);
//                entity.addBodyParameter("msg", CHOICE_ONE);
//                entity.addBodyParameter("question_id", questionVideoList.get(position).getQuestion_video_id() + "");
//                entity.addBodyParameter("user_id",
//                        String.valueOf(MyApplication.user.getUser_id()));
//
//                entity.addBodyParameter("left_or_right", "right");
//                x.http().post(entity, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        if (result != null) {
//                            int percent = Integer.parseInt(result);
//                            Toast.makeText(context, "left:" + (100 - percent) + "%," +
//                                    "right:" + percent + "%", Toast.LENGTH_SHORT).show();
////                            showBar(100 - percent, percent);
//                            left_ProgressBar.setVisibility(View.VISIBLE);
//                            right_ProgressBar.setVisibility(View.VISIBLE);
//                            left_ProgressBar.setProgress(100 - percent);
//                            right_ProgressBar.setProgress(percent);
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
//            }
//        });
        return v;
    }

    private void initVideoPlayer(final StandardGSYVideoPlayer gsyVideoPlayer, String url) {

        //增加封面
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(utils.createVideoThumbnail(url));
        gsyVideoPlayer.setThumbImageView(imageView);
        //url
        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
        gsyVideoPlayer.setUp(url, true, null, "这是title");
        //非全屏下，不显示title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //非全屏下不显示返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //打开非全屏下触摸效果
        gsyVideoPlayer.setIsTouchWiget(true);
        //开启自动旋转
        gsyVideoPlayer.setRotateViewAuto(false);
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(LoginActivity.loginActivity, true, true);

            }
        });
        gsyVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先返回正常状态
                OrientationUtils orientationUtils = new OrientationUtils(LoginActivity.loginActivity, gsyVideoPlayer);
                if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    gsyVideoPlayer.getFullscreenButton().performClick();
                    return;
                }
            }
        });

        //全屏首先横屏
        gsyVideoPlayer.setLockLand(true);
        gsyVideoPlayer.setStandardVideoAllCallBack(new StandardVideoAllCallBack() {
            @Override
            public void onClickStartThumb(String s, Object... objects) {
                Toast.makeText(context, "onClickStartThumb", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickBlank(String s, Object... objects) {
                Toast.makeText(context, "onClickBlank", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickBlankFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onClickBlankFullscreen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepared(String s, Object... objects) {
                Toast.makeText(context, "onPrepared", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickStartIcon(String s, Object... objects) {
                Toast.makeText(context, "onClickStartIcon", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickStartError(String s, Object... objects) {
                Toast.makeText(context, "onClickStartError", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickStop(String s, Object... objects) {
                Toast.makeText(context, "onClickStop", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickStopFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onClickStopFullscreen", Toast.LENGTH_SHORT).show();
                gsyVideoPlayer.backFromWindowFull(LoginActivity.loginActivity);
            }

            @Override
            public void onClickResume(String s, Object... objects) {
                Toast.makeText(context, "onClickResume", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickResumeFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onClickResumeFullscreen", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickSeekbar(String s, Object... objects) {
                Toast.makeText(context, "onClickSeekbar", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClickSeekbarFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onClickSeekbarFullscreen", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAutoComplete(String s, Object... objects) {

            }

            @Override
            public void onEnterFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onEnterFullscreen", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onQuitFullscreen(String s, Object... objects) {
                Toast.makeText(context, "onQuitFullscreen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQuitSmallWidget(String s, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String s, Object... objects) {

            }

            @Override
            public void onPlayError(String s, Object... objects) {
                Toast.makeText(context, "onPlayError", Toast.LENGTH_SHORT).show();

            }
        });
        //是否需要全屏动画效果
        gsyVideoPlayer.setShowFullAnimation(false);

        //立即播放
//        gsyVideoPlayer.startPlayLogic();
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
////        entity.addBodyParameter("question_id", questionVideoList.get(position).);
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