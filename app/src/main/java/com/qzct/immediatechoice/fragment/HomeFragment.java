package com.qzct.immediatechoice.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CalendarActivity;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.activity.MainActivity;
import com.qzct.immediatechoice.activity.PushActivity;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.util.FabSpeedDial;
import com.qzct.immediatechoice.util.SimpleMenuListenerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


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
//    View home_attention_line;
    public final static String ACTION_SET_FAB_VISBILITY = "ACTION_SET_FAB_VISBILITY";
    private LinearLayout home_title;
    private FabSpeedDial fabSpeedDial;
    private List<Fragment> fragmentList;
    private View home_calendar;
    private View home_search;
    private QuestionFragment questionFragment;
    private View shade;
    private ImageView searchButton;

    /**
     * 填充view
     *
     * @return
     */
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        v = x.view().inject(this, inflater, container);
        vp_home = (ViewPager) v.findViewById(R.id.vp_home);
        home_image_text_line = v.findViewById(R.id.home_image_text_line);
        home_video_line = v.findViewById(R.id.home_video_line);
//        home_attention_line = v.findViewById(R.id.home_attention_line);
        home_calendar = v.findViewById(R.id.home_calendar);
        home_search = v.findViewById(R.id.home_search);
        shade = v.findViewById(R.id.shade);
        fabSpeedDial = (FabSpeedDial) v.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                shade.setAlpha(0);
                switch (menuItem.getItemId()) {
                    case R.id.fab_push:

                        Intent intent = new Intent(context, LoginActivity.class);
                        if (MyApplication.logined) {
                            intent = new Intent(context, PushActivity.class);
                            intent.putExtra("isImage", true);
                        }
                        startActivity(intent);
                        break;
                    case R.id.fab_question:
//                        intent = new Intent(context, QuestionnaireActivity.class);
//                        startActivity(intent);

                        Intent videoIntent = new Intent(context, LoginActivity.class);
                        if (MyApplication.logined) {
                            videoIntent = new Intent(context, PushActivity.class);
                            videoIntent.putExtra("isImage", false);
                        }
                        startActivity(videoIntent);

                        break;

                    default:

                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }

            @Override
            public void onOpenMenu() {
                shade.setAlpha(0.7f);
            }

            @Override
            public void onCloseMenu() {
                shade.setAlpha(0);

            }
        });
        home_title = (LinearLayout) v.findViewById(R.id.home_title);
        searchButton = (ImageView) home_search.findViewById(R.id.search_button);

        return v;
    }

    /**
     * 填充数据
     */
    @Override
    public void initData() {
        searchButton.setImageResource(R.mipmap.search);
        home_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.logined) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(context, CalendarActivity.class);
                startActivity(intent);
            }
        });
        vp_home.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFabVisibility(true);
                if (position == 2) {
                    if (!MyApplication.logined) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        context.finish();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SET_FAB_VISBILITY);

        //接收并处理是否显示悬浮按钮的广播
        BroadcastReceiver bordcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //信息处理

                Log.e("isvisible", String.valueOf(intent.getBooleanExtra("isvisible", true)));

                setFabVisibility(intent.getBooleanExtra("isvisible", true));


            }
        };
        broadcastManager.registerReceiver(bordcastReceiver, intentFilter);

//        pagers.add(new ImageTextPager(context));
//        pagers.add(new VideoPager(context));
////        pagers.add(new VideoPager(context));
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
//        vp_home.setAdapter(pagerAdapter);

        fragmentList = new ArrayList<Fragment>();
        questionFragment = new QuestionFragment();
//        fragmentList.add(new QuestionFragment());
        fragmentList.add(questionFragment);
        fragmentList.add(new VideoFragment());
//        fragmentList.add(new AttentionFragment());
        vp_home.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
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
//                    case ATTENTTION:
//                        home_attention_line.setVisibility(View.VISIBLE);
//                        break;


                    default:

                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /**
     * 设置悬浮按钮的可见属性
     *
     * @param isVisible 是否可见
     */
    public void setFabVisibility(boolean isVisible) {

        if (isVisible && home_title.getVisibility() == View.GONE) {
            fabSpeedDial.show();
//            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//            mShowAction.setDuration(500);
//            home_title.startAnimation(mShowAction);
            home_title.setVisibility(View.VISIBLE);
//            home_title.startAnimation(mShowAction);
            MainActivity.rg_nav.setVisibility(View.VISIBLE);
            Log.e("show", "show");
        } else if (!isVisible && home_title.getVisibility() != View.GONE) {
            fabSpeedDial.hide();
//            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                    -1.0f);
//            mHiddenAction.setDuration(500);
//            home_title.startAnimation(mHiddenAction);
            home_title.setVisibility(View.GONE);
//            MainActivity.rg_nav.startAnimation(mHiddenAction);
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
//    @Event({R.id.bt_home_title_image, R.id.bt_home_title_video, R.id.bt_home_title_attention})
    @Event({R.id.bt_home_title_image, R.id.bt_home_title_video})
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

//            case R.id.bt_home_title_attention:
//                vp_home.setCurrentItem(ATTENTTION);
//                home_attention_line.setVisibility(View.VISIBLE);
//                break;
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
//        home_attention_line.setVisibility(View.GONE);
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


}
