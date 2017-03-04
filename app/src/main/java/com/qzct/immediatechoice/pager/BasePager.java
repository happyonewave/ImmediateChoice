package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017-03-05.
 */

public abstract class BasePager {

    public Context context;
    public  View view;

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
}
