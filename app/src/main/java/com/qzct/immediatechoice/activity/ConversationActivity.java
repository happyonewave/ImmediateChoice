package com.qzct.immediatechoice.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.qzct.immediatechoice.R;

/**
 * Created by qin on 2017/3/25.
 */

public class ConversationActivity extends FragmentActivity {

    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initView();
        initData();
    }

    private void initView() {
         tv_name =  (TextView) findViewById(R.id.name);

    }

    private void initData() {
        //获取Id
         String tergetId= getIntent().getData().getQueryParameter("tergetId");
         String name = getIntent().getData().getQueryParameter("title");

        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
        } else {
            //id
            //TODO  拿到Id 去请求服务端

        }
    }


}
