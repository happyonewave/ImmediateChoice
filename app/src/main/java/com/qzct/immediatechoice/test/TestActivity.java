package com.qzct.immediatechoice.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.qzct.immediatechoice.R;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * 测试
 */
//@ContentView(R.layout.view_video_item)
public class TestActivity extends AppCompatActivity implements View.OnClickListener, RongIM.UserInfoProvider {

    private StandardGSYVideoPlayer gsyVideoPlayer;
    private OrientationUtils orientationUtils;
    public final static String TRANSITION = "TRANSITION";
    private boolean isTransition;
    private String token1 = "18LyaY+7DAct9px+BRJxCl3lOgyfFg2AjkViYtqQca3pbaogcPqTnfcI34AD9x2wXSWqwKWhvYM=";
    private String token2 = "G/i7WKl49H6dWLJIf31wg5K2kJJ49pN8xrdowMMx5ayEtxy460ZKgAMWNe5rqcc8kGacuUXe1r87hDlHGJQzBg==";
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private ViewPager vp_test;
    private List<Fragment> fragmentList;
    private List<UserInfo> UserInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
//        x.view().inject(this);
        UserInfoList = new ArrayList<UserInfo>();
        UserInfoList.add(new UserInfo("1", "小梨子", Uri.parse("http://123.207.31.213/ImmediateChoice_service/image/2.jpg")));
        UserInfoList.add(new UserInfo("2", "Qin", Uri.parse("http://123.207.31.213/ImmediateChoice_service/image/3.jpg")));
//        RongIM.getInstance().setCurrentUserInfo(new UserInfo("1", "小梨子", Uri.parse("http://123.207.31.213/ImmediateChoice_service/image/2.jpg")));
        RongIM.setUserInfoProvider(this, true);
        initView();
        initData();

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.activity_test, null);
        setContentView(view);
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(initConversationList());
        fragmentList.add(initConversationList());
        vp_test = (ViewPager) findViewById(R.id.vp_test);

        vp_test.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        bt_3 = (Button) findViewById(R.id.bt_3);
        bt_4 = (Button) findViewById(R.id.bt_4);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);

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


//        gsyVideoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.view_video_item_left);
//        //增加封面
//        final String url = "http://123.207.31.213:8080/ImmediateChoice_service/1.mp4";
//        ImageView imageView = new ImageView(this);
//        imageView.setImageBitmap(utils.createVideoThumbnail(url));
//        gsyVideoPlayer.setThumbImageView(imageView);
//        //url
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
////        gsyVideoPlayer.startWindowFullscreen(this,true,true);
//        //全屏首先横屏
////        gsyVideoPlayer.setLockLand(true);
//
//        //是否需要全屏动画效果
//        gsyVideoPlayer.setShowFullAnimation(false);
//        //设置全屏按键功能
//        orientationUtils = new OrientationUtils(TestActivity.this, gsyVideoPlayer);
//        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });
//        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gsyVideoPlayer.startWindowFullscreen(getApplication(), true, true);
//
//            }
//        });
        //立即播放
//        gsyVideoPlayer.startPlayLogic();


    }


    private Fragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri;
        uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                .build();


        listFragment.setUri(uri);
        return listFragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                RongIM.connect(token1, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("qin", "userid: " + s);
                        bt_1.setText("userid:" + s + "连接成功");

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.d("qin", "onError: " + errorCode.getMessage());
                    }

                    @Override
                    public void onTokenIncorrect() {

                    }
                });
                break;
            case R.id.bt_2:
                RongIM.getInstance().startPrivateChat(TestActivity.this, "2", "与userid：2的聊天");
                break;
            case R.id.bt_3:
                RongIM.connect(token2, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("qin", "userid: " + s);
                        bt_3.setText("userid:" + s + "连接成功");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.d("qin", "onError: " + errorCode.getMessage());
                    }

                    @Override
                    public void onTokenIncorrect() {

                    }
                });

                break;
            case R.id.bt_4:
                RongIM.getInstance().startPrivateChat(TestActivity.this, "1", "与userid：1的聊天");

                break;

            default:

                break;

        }

    }

    private void initData() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        //先返回正常状态
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            gsyVideoPlayer.getFullscreenButton().performClick();
//            return;
//        }
//        //释放所有
//        gsyVideoPlayer.setStandardVideoAllCallBack(null);
//        GSYVideoPlayer.releaseAllVideos();
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            super.onBackPressed();
//        } else {
//            finish();
//            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }
    }

    @Override
    public UserInfo getUserInfo(String s) {

        for (UserInfo user : UserInfoList) {
            if (user.getUserId().equals(s)) {
                return user;
            }
        }
        return null;
    }
}

