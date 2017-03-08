package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.fragment.HomeFragment;

/**
 * Created by Administrator on 2017-03-05.
 */

public abstract class BasePager {

    public Context context;
    public View view;
    int oldVisibleItem;
    public BasePager(Context context) {
        this.context = context;
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
    public abstract void  initData();
    /**
     * 返回ui效果
     */
    public View getRootView(){
     return view;
    }

    /**
     * 响应listview滑动事件 发送是否显示悬浮按钮广播
     */
    public void sendFabIsVisible(ListView listView){

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Intent intent = new Intent(HomeFragment.ACTION_SET_FAB_VISBILITY);
                if (firstVisibleItem > oldVisibleItem) {
                    // 向上滑动
                    intent.putExtra("isvisible",false);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Toast.makeText(context, "向上滑动", Toast.LENGTH_SHORT).show();
                }
                if (firstVisibleItem < oldVisibleItem) {
                    // 向下滑动
                    intent.putExtra("isvisible",true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Toast.makeText(context, "向下滑动", Toast.LENGTH_SHORT).show();
                }
                oldVisibleItem = firstVisibleItem;
            }
        });

    };
}
