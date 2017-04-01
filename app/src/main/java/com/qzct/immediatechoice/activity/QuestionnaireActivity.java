package com.qzct.immediatechoice.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qzct.immediatechoice.R;

import org.xutils.view.annotation.ViewInject;

/**
 * 问卷调查Activity
 */
public class QuestionnaireActivity extends AppCompatActivity {

    @ViewInject(R.id.iv_questionnaire_back)
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        //返回view
        iv_back = (ImageView) findViewById(R.id.iv_questionnaire_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionnaireActivity.this.finish();
            }
        });
    }
}