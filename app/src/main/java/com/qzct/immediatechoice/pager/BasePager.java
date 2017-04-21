package com.qzct.immediatechoice.pager;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;

import com.qzct.immediatechoice.fragment.HomeFragment;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcAbsListView;
import zrc.widget.ZrcListView;

/**
 * Created by Administrator on 2017-03-05.
 */

public abstract class BasePager {

    public Activity context;
    public View view;
    public LayoutInflater inflater;

    public BasePager(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = initView();
        initData();
    }

    /**
     * 构建ui
     */
    public abstract View initView();

    /**
     * 构建数据
     */
    public abstract void initData();

    /**
     * 返回ui效果
     */
    public View getRootView() {
        return view;
    }

    /**
     * 响应listview滑动事件 发送是否显示悬浮按钮广播
     */
    int oldVisibleItem;
    public void sendFabIsVisible(final ZrcListView listView) {

        listView.setOnScrollListener(new ZrcListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(ZrcAbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(ZrcAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Intent intent = new Intent(HomeFragment.ACTION_SET_FAB_VISBILITY);
                if (firstVisibleItem > oldVisibleItem) {
                    // 向上滑动
                    intent.putExtra("isvisible", false);
                    //开启加载更多
                    listView.startLoadMore();
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                if (firstVisibleItem < oldVisibleItem) {
                    // 向下滑动
                    intent.putExtra("isvisible", true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                oldVisibleItem = firstVisibleItem;
            }
        });

    }


    public void setLoad(ZrcListView listView) {

        // 设置下拉刷新的样式
        SimpleHeader header = new SimpleHeader(context);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);
        // 设置加载更多的样式
        SimpleFooter footer = new SimpleFooter(context);
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);
        // 设置列表项出现动画
//        lv_home.setItemAnimForTopIn(R.anim.topitem_in);
//        lv_home.setItemAnimForBottomIn(R.anim.bottomitem_in);
        //开启加载更多
        listView.startLoadMore();
        // 下拉刷新事件回调
        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });
        // 加载更多事件回调
        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });
    }

    public void loadMore() {

    }

    public void refresh() {

    }
}
