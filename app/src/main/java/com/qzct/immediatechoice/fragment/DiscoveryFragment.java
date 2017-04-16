package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.pager.TopicPager;
import com.qzct.immediatechoice.util.Config;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
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
    //    @ViewInject(R.id.discovery_search)
    private SearchView discovery_search;
    @ViewInject(R.id.discovery_scan)
    private ImageView discovery_scan;
    @ViewInject(R.id.magic_indicator)
    private MagicIndicator magic_indicator;
    private List<String> mTitleDataList;
    @ViewInject(R.id.vp_discovery)
    private ViewPager vp_discovery;
    @ViewInject(R.id.lv_search)
    private ListView lv_search;
    private List<BasePager> pagerList;
    private LinearLayout ll_layout;
    private List<Question> questionList;
    private ArrayAdapter<String> adapter;
    private List<String> mStrList = new ArrayList<String>();


    /**
     * 初始化UI
     *
     * @param inflater  布局填充器
     * @param container
     * @return
     */
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        v = x.view().inject(this, inflater, container);
        discovery_search = (SearchView) v.findViewById(R.id.discovery_search);
        ll_layout = (LinearLayout) v.findViewById(R.id.ll_layout);
        return v;
    }

    /**
     * 填充数据
     */
    @Override
    public void initData() {
        mStrList.add("无此项");
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mStrList);
        lv_search.setAdapter(adapter);
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionList.get(position);
                MyApplication.question = question;
                if (!questionList.isEmpty()) {
                    if (question.getLeft_url().contains("image")) {
                        MyApplication.isQuestion = true;
                    } else {
                        MyApplication.isQuestion = false;
                    }
                    Intent intent = new Intent(context, CommentActivity.class);
                    context.startActivity(intent);
                }

            }
        });
//        lv_search.setTextFilterEnabled(true);
        discovery_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lv_search.setVisibility(View.VISIBLE);
                ll_layout.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    RequestParams entity = new RequestParams(Config.url_search);
                    entity.addBodyParameter("keyword", newText);
                    x.http().post(entity, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (result != null && !"[]".equals(result)) {
                                Log.d("qin", "result:   " + result);
                                try {
                                    questionList = new ArrayList<Question>();
                                    mStrList.clear();
                                    JSONArray array = new JSONArray(result);
                                    for (int i = 0; i < array.length(); i++) {
                                        Question question = Question.jsonObjectToQuestion(array.getJSONObject(i));
                                        mStrList.add(question.getQuestion_content());
                                        questionList.add(question);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(context, "连接搜索服务错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {
                            adapter.notifyDataSetChanged();
                        }
                    });
//                    lv_search.setFilterText(newText);
                } else {
//                    lv_search.clearTextFilter();
                }
                return false;
            }
        });
        discovery_search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lv_search.setVisibility(View.GONE);
                ll_layout.setVisibility(View.VISIBLE);
                return false;
            }
        });
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
                simplePagerTitleView.setTextSize(18);
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

