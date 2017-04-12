package com.qzct.immediatechoice.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * 设置
 */
@ContentView(R.layout.setting)
public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xutils初始化
        x.view().inject(this);
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Event({R.id.seting_back, R.id.logout})
    private void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.seting_back:
                SettingActivity.this.finish();

                break;
            //退出
            case R.id.logout:
                MyApplication.user = null;
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.mainActivity.finish();
                SettingActivity.this.finish();

                break;

        }

    }

}