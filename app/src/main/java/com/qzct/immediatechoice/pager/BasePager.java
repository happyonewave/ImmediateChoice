package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;

import com.qzct.immediatechoice.fragment.HomeFragment;

import zrc.widget.ZrcAbsListView;
import zrc.widget.ZrcListView;

/**
 * Created by Administrator on 2017-03-05.
 */

public abstract class BasePager {

    public Context context;
    public View view;
    public LayoutInflater inflater;
    int oldVisibleItem;

    public BasePager(Context context) {
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

    ;
}
