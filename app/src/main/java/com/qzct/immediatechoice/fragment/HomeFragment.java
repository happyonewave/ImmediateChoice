package com.qzct.immediatechoice.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.MainActivity;
import com.qzct.immediatechoice.activity.PushActivity;
import com.qzct.immediatechoice.activity.QuestionnaireActivity;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.pager.ImageTextPager;
import com.qzct.immediatechoice.pager.VideoPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

@ContentView(R.layout.fragment_home)
public class HomeFragment extends baseFragment implements View.OnClickListener {

    private static final int IMAGE_TEXT = 0;
    private static final int VIDEO = 1;
    private static final int ATTENTTION = 2;
    private View v;
    private ViewPager vp_home;
    private PagerAdapter pagerAdapter = null;
    private List<BasePager> pagers = new ArrayList<BasePager>();
    View home_image_text_line;
    View home_video_line;
    View home_attention_line;
    public final static String ACTION_SET_FAB_VISBILITY = "ACTION_SET_FAB_VISBILITY";
    private LinearLayout home_title;
    private FabSpeedDial fabSpeedDial;

    /**
     * 填充view
     *
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        v = x.view().inject(this, inflater, container);
        vp_home = (ViewPager) v.findViewById(R.id.vp_home);
        vp_home.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        });
        vp_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        home_image_text_line = v.findViewById(R.id.home_image_text_line);
        home_video_line = v.findViewById(R.id.home_video_line);
        home_attention_line = v.findViewById(R.id.home_attention_line);

        fabSpeedDial = (FabSpeedDial) v.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.fab_push:
                        Intent intent = new Intent(context, PushActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.fab_question:
                        intent = new Intent(context, QuestionnaireActivity.class);
                        startActivity(intent);
                        break;

                    default:

                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
        home_title = (LinearLayout) v.findViewById(R.id.home_title);
        return v;
    }

    /**
     * 设置悬浮按钮的可见属性
     *
     * @param isvisible 是否可见
     */
    public void setFabvisibility(boolean isvisible) {

        if (isvisible) {
            fabSpeedDial.show();
            home_title.setVisibility(View.VISIBLE);
            MainActivity.rg_nav.setVisibility(View.VISIBLE);
            Log.e("show", "show");
        } else {
            fabSpeedDial.hide();
            home_title.setVisibility(View.GONE);
            MainActivity.rg_nav.setVisibility(View.GONE);
            Log.e("hide", "hide");


        }


    }

    /**
     * title按钮的点击事件
     * 切换到对应的View
     *
     * @param v 点击后返回的对应的对象
     */
    @Event({R.id.bt_home_title_image, R.id.bt_home_title_video, R.id.bt_home_title_attention})
    private void titleOnClick(View v) {
        reSetLine();
        switch (v.getId()) {

            case R.id.bt_home_title_image:
                vp_home.setCurrentItem(IMAGE_TEXT);
                home_image_text_line.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_home_title_video:
                vp_home.setCurrentItem(VIDEO);
                home_video_line.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_home_title_attention:
                vp_home.setCurrentItem(ATTENTTION);
                home_attention_line.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

    }

    /**
     * 设置所有的Line不可见
     */
    private void reSetLine() {

        home_image_text_line.setVisibility(View.GONE);
        home_video_line.setVisibility(View.GONE);
        home_attention_line.setVisibility(View.GONE);
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab_push:

                break;

            case R.id.fab_question:

                break;

            default:
                break;


        }
    }

    /**
     * 填充数据
     */
    @Override
    public void initdata() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SET_FAB_VISBILITY);

        //接收并处理是否显示悬浮按钮的广播
        BroadcastReceiver bordcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //信息处理

                Log.e("isvisible", String.valueOf(intent.getBooleanExtra("isvisible", true)));

                setFabvisibility(intent.getBooleanExtra("isvisible", true));


            }
        };
        broadcastManager.registerReceiver(bordcastReceiver, intentFilter);

        pagers.add(new ImageTextPager(context));
        pagers.add(new VideoPager(context));
        pagers.add(new VideoPager(context));
//        pagers.add(new AttentionPager(context));

        /**
         * viewPager适配器
         */
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pagers.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(pagers.get(position).getRootView());


                return pagers.get(position).getRootView();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pagers.get(position).getRootView());
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

        };
        vp_home.setAdapter(pagerAdapter);
        vp_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reSetLine();
                switch (position) {
                    case IMAGE_TEXT:
                        home_image_text_line.setVisibility(View.VISIBLE);
                        break;
                    case VIDEO:
                        home_video_line.setVisibility(View.VISIBLE);
                        break;
                    case ATTENTTION:
                        home_attention_line.setVisibility(View.VISIBLE);
                        break;


                    default:

                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
