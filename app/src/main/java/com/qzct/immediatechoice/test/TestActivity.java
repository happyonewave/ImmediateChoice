package com.qzct.immediatechoice.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.qzct.immediatechoice.R;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import io.rong.imlib.model.UserInfo;


/**
 * 测试
 */
//@ContentView(R.layout.view_video_item)
public class TestActivity extends AppCompatActivity {

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
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();

    }

    private void initView() {
        final NumberProgressBar bnp = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        counter = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bnp.incrementProgressBy(1);
                counter++;
                if (counter == 110) {
                    bnp.setProgress(0);
                    counter = 0;
                }

            }
        },2000);

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bnp.incrementProgressBy(1);
//                        counter++;
//                        if (counter == 110) {
//                            bnp.setProgress(0);
//                            counter = 0;
//                        }
//                    }
//                });
//            }
//        }, 1000, 100);
    }


    private void initData() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}

