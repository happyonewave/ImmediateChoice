package com.qzct.immediatechoice.test;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


/**
 * 评论
 */
//@ContentView(R.layout.view_video_item)
public class TestActivity extends AppCompatActivity {

    private StandardGSYVideoPlayer gsyVideoPlayer;
    private OrientationUtils orientationUtils;
    public final static String TRANSITION = "TRANSITION";
    private boolean isTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.view_video_item, null);
        setContentView(view);
//        x.view().inject(this);
        initView();
        initData();

    }

    private void initView() {
//        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
//                .doH264Compress(new AutoVBRMode()
////                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
//                )
//                .setMediaBitrateConfig(new AutoVBRMode()
////                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
//                )
//                .smallVideoWidth(480)
//                .smallVideoHeight(360)
//                .recordTimeMax(6 * 1000)
//                .maxFrameRate(20)
//                .minFrameRate(8)
//                .captureThumbnailsTime(1)
//                .recordTimeMin((int) (1.5 * 1000))
//                .build();
//        MediaRecorderActivity.goSmallVideoRecorder(this, PushActivity.class.getName(), config);


        gsyVideoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.view_video_item_left);
        //增加封面
        final String url = "http://123.207.31.213:8080/ImmediateChoice_service/1.mp4";
        ImageView imageView = new ImageView(this);
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

//        gsyVideoPlayer.startWindowFullscreen(this,true,true);
        //全屏首先横屏
//        gsyVideoPlayer.setLockLand(true);

        //是否需要全屏动画效果
        gsyVideoPlayer.setShowFullAnimation(false);
        //设置全屏按键功能
        orientationUtils = new OrientationUtils(TestActivity.this, gsyVideoPlayer);
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(getApplication(), true, true);

            }
        });
        //立即播放
//        gsyVideoPlayer.startPlayLogic();


    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            gsyVideoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        gsyVideoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }

    private void initData() {
    }

}

