package com.qzct.immediatechoice.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.fragment.DiscoveryFragment;
import com.qzct.immediatechoice.fragment.HomeFragment;
import com.qzct.immediatechoice.fragment.UserFragment;
import com.qzct.immediatechoice.fragment.baseFragment;
import com.qzct.immediatechoice.util.utils;

/**
 * 主
 */
public class MainActivity extends AppCompatActivity {

    private FrameLayout fl;
    public static Activity mainActivity;
    public static RadioGroup rg_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_main));
        rg_nav = (RadioGroup) findViewById(R.id.rg_nav);
        //用来放fragment的帧布局
        fl = (FrameLayout) findViewById(R.id.fl);
        //初始化Fragment为首页
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, 0);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
        mainActivity = this;
    }

    /**
     * 退出提醒
     *
     * @param keyCode
     * @param event
     * @return
     */
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "快速点两次就退出了哦！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    public void click(View v) {
        int id = v.getId();
        int index = 0;
        switch (id) {
            //首页
            case R.id.bt_home:
                index = 0;
                break;
            //发现
            case R.id.bt_discovery:
                index = 1;
                break;
            //我的
            case R.id.bt_user:
                index = 2;
                break;

            default:
                break;
        }
        //Fragment切换
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, index);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
    }

    /**
     * Fragment适配器
     */
    FragmentStatePagerAdapter Adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int arg0) {
            baseFragment baseFragment = null;
            switch (arg0) {
                case 0:
                    baseFragment = new HomeFragment();
                    break;
                case 1:
                    baseFragment = new DiscoveryFragment();
                    break;
                case 2:
                    baseFragment = new UserFragment();
                    break;

                default:
                    break;
            }
            return baseFragment;
        }
    };
}