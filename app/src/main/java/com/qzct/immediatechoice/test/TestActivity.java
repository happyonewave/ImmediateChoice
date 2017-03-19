package com.qzct.immediatechoice.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.PushActivity;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;


/**
 * 评论
 */
//@ContentView(R.layout.view_video_item)
public class TestActivity extends AppCompatActivity {


    private StandardGSYVideoPlayer gsyVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.view_video_item, null);
        setContentView(view);
//        x.view().inject(this);
        initView();
        initData();

    }

    private void initView() {
        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .doH264Compress(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                )
                .setMediaBitrateConfig(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                )
                .smallVideoWidth(480)
                .smallVideoHeight(360)
                .recordTimeMax(6 * 1000)
                .maxFrameRate(20)
                .minFrameRate(8)
                .captureThumbnailsTime(1)
                .recordTimeMin((int) (1.5 * 1000))
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(this, PushActivity.class.getName(), config);


//        //增加封面
//        gsyVideoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.view_video_item_left);
////        gsyVideoPlayer.setThumbImageView(holder.imageView);
//        //url
//        final String url = "http://123.207.31.213:8080/ImmediateChoice_service/1.mp4";
//        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
//        gsyVideoPlayer.setUp(url, true, null, "这是title");
//        //非全屏下，不显示title
//        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//        //非全屏下不显示返回键
//        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//        //打开非全屏下触摸效果
//        gsyVideoPlayer.setIsTouchWiget(true);
//        //开启自动旋转
//        gsyVideoPlayer.setRotateViewAuto(false);
//
//        gsyVideoPlayer.startWindowFullscreen(this,true,true);
//        //全屏首先横屏
//        gsyVideoPlayer.setLockLand(true);
//
//        //是否需要全屏动画效果
//        gsyVideoPlayer.setShowFullAnimation(false);
//
//        //立即播放
//        gsyVideoPlayer.startPlayLogic();


    }

    private void initData() {
    }

}

