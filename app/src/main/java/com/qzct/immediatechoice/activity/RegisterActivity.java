package com.qzct.immediatechoice.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.fragment.RegisterFirstFragment;


/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    FrameLayout fl_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fl_register = (FrameLayout) findViewById(R.id.fl_register);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_register_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RegisterFirstFragment fragment = new RegisterFirstFragment();
        transaction.add(R.id.fl_register, (Fragment) fragment);
        transaction.commit();
    }


    public void setTitleColor(int code) {
        TextView tv_fill_verification_code;
        switch (code) {
            case 0:
                TextView tv_fill_tel = (TextView) findViewById(R.id.tv_fill_tel);
                tv_fill_verification_code = (TextView) findViewById(R.id.tv_fill_verification_code);
                tv_fill_tel.setTextColor(getResources().getColor(R.color.gray));
                tv_fill_verification_code.setTextColor(getResources().getColor(R.color.apporange));
                break;
            case 1:
                tv_fill_verification_code = (TextView) findViewById(R.id.tv_fill_verification_code);
                tv_fill_verification_code.setTextColor(getResources().getColor(R.color.gray));
                TextView tv_set_password = (TextView) findViewById(R.id.tv_set_password);
                tv_set_password.setTextColor(getResources().getColor(R.color.apporange));
                break;
            case 2:

                break;

            default:

                break;
        }
    }


}