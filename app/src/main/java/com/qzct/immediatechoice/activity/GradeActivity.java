package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

/**
 * Created by tsh2 on 2017/4/25.
 */

public class GradeActivity extends AppCompatActivity {
    private TextView tv_grade;
    private int userGrade;
    private LinearLayout grade_introduce;

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
        grade_introduce = (LinearLayout) findViewById(R.id.grade_introduce);

    }

    private void initData() {
        tv_grade.setText(userGrade + "");
        grade_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =   new AlertDialog.Builder(GradeActivity.this);
                builder.setTitle("如何赚积分");
                String msg = "1.用户均可使用积分进行礼品兑换\n2.用户可以进行每日签到活动和填写得到相应积分\n3.积分不可转送或出售，如发现并证实，积分予以作废处理。";
                builder.setMessage(msg);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
                builder.create().show();
            }
        });

    }
}
