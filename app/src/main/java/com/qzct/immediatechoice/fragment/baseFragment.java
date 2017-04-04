package com.qzct.immediatechoice.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzct.immediatechoice.application.MyApplication;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;

public abstract class baseFragment extends Fragment {
    Activity context;
    MyApplication myApplication;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        myApplication = (MyApplication) context.getApplication();
        super.onCreate(savedInstanceState);
    }

    public abstract View initview(LayoutInflater inflater, ViewGroup container);

    public abstract void initdata();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = initview(inflater, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initdata();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (getView() != null) {
            getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
        super.setMenuVisibility(menuVisible);
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
