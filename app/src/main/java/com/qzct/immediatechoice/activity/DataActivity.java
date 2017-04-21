package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

/**
 * Created by tsh2 on 2017/4/21.
 */

public class DataActivity extends Activity {
    private WebView data_wv1;
    private WebView data_wv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(utils.getUsableView(this, R.layout.activity_data, null));
        initView();
        initData();
    }

    private void initView() {
        data_wv1 = (WebView) findViewById(R.id.data_wv1);
        data_wv2 = (WebView) findViewById(R.id.data_wv2);
    }

    private void initData() {
        data_wv1.getSettings().setJavaScriptEnabled(true);
        data_wv2.getSettings().setJavaScriptEnabled(true);
        data_wv1.loadUrl("file:///android_asset/index.html");
        data_wv2.loadUrl("file:///android_asset/index.html");
    }
}
