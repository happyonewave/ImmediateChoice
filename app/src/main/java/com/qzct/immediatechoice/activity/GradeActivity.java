package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

import org.w3c.dom.Text;

/**
 * Created by tsh2 on 2017/4/25.
 */

public class GradeActivity extends AppCompatActivity {
    private TextView tv_grade;
    private int userGrade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_grade, "积分商城"));
        SharedPreferences sharedPreferences = getSharedPreferences("grade", Context.MODE_PRIVATE);
        userGrade = sharedPreferences.getInt("grade", 1);
        initView();
        initData();
    }

    private void initView() {
        tv_grade = (TextView) findViewById(R.id.grade);

    }

    private void initData() {
        tv_grade.setText(userGrade + "分");
    }
}
