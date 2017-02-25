package com.qzct.immediatechoice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.itheima.immediatechoice.R;

public class QuestionnaireActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_questionnaire_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionnaireActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}