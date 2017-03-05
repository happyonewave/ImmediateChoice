package com.qzct.immediatechoice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qzct.immediatechoice.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.setting)
public class SettingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
//        setContentView(R.layout.setting);
//        ImageView iv_back = (ImageView) findViewById(R.id.seting_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Event({R.id.seting_back, R.id.logout})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.seting_back:
                SettingActivity.this.finish();

                break;
            case R.id.logout:
                MyApplication.user = null;
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();

                break;

        }

    }

}