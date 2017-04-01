package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.pager.TopicPager;
import com.qzct.immediatechoice.util.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;


@ContentView(R.layout.fragment_discovery)
public class DiscoveryFragment extends baseFragment {

    private View v;
    @ViewInject(R.id.discovery_banner)
    private BGABanner banner;
    @ViewInject(R.id.discovery_search)
    private EditText discovery_search;
    @ViewInject(R.id.discovery_scan)
    private ImageView discovery_scan;
    @ViewInject(R.id.magic_indicator)
    private MagicIndicator magic_indicator;
    private List<String> mTitleDataList;
    @ViewInject(R.id.vp_discovery)
    private ViewPager vp_discovery;
    private List<BasePager> pagerList;

    /**
     * 初始化UI
     *
     * @param inflater  布局填充器
     * @param container
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        v = x.view().inject(this, inflater, container);
        return v;
    }

    /**
     * 填充数据
     */
    @Override
    public void initdata() {
        setCarousel();
        setTopic();
    }

    /**
     * 设置Topic
     */
    private void setTopic() {
        pagerList = new ArrayList<BasePager>();
        pagerList.add(new TopicPager(context));
        pagerList.add(new TopicPager(context));
        pagerList.add(new TopicPager(context));
        pagerList.add(new TopicPager(context));
        pagerList.add(new TopicPager(context));
        pagerList.add(new TopicPager(context));
        vp_discovery.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pagerList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(pagerList.get(position).getRootView());


                return pagerList.get(position).getRootView();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pagerList.get(position).getRootView());
            }


            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mTitleDataList = new ArrayList<String>();
        mTitleDataList.add("热门");
        mTitleDataList.add("视频");
        mTitleDataList.add("逗萌");
        mTitleDataList.add("娱乐");
        mTitleDataList.add("爱好");
        mTitleDataList.add("同学");
        magic_indicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleDataList.get(index));
                simplePagerTitleView.setTextSize(22);
                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp_discovery.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setYOffset(UIUtil.dip2px(context, 39));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                indicator.setColors(Color.parseColor("#f57c00"));
                return indicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magic_indicator, vp_discovery);

    }

    /**
     * 初始化轮播图
     */
    private void setCarousel() {
        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_1));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_2));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_3));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_4));
        banner.setData(views);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Event(R.id.discovery_scan)
    private void click(View v) {
        Toast.makeText(context, "点击了discovery_scan", Toast.LENGTH_SHORT).show();
    }

}

