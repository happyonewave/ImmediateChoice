package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.Utils;

/**
 * Created by tsh2 on 2017/4/13.
 */

public class SplashActivity extends Activity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Utils.getUsableView(this, R.layout.activty_splash, null));
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                boolean b = sharedPreferences.getBoolean("is_first", true);
                if (b) {
                    sharedPreferences.edit().putBoolean("is_first", false).commit();
                    Toast.makeText(SplashActivity.this, "您是初次使用，谢谢您的支持！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }.sendEmptyMessageDelayed(0, 3000);
    }
}
