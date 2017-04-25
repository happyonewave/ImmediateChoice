package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

/**
 * Created by tsh2 on 2017/4/21.
 */

public class DataActivity extends Activity {
    private WebView data_wv1;
    private WebView data_wv2;
    private Questionnaire.Question questionEntity;
    private TextView data_title;
    private LinearLayout data_options;
    private android.content.Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionEntity = (Questionnaire.Question) getIntent().getSerializableExtra("questionEntity");
        setContentView(utils.getUsableView(this, R.layout.activity_data, "问题详情"));
        initView();
        initData();
    }

    private void initView() {
        data_wv1 = (WebView) findViewById(R.id.data_wv1);
        data_title = (TextView) findViewById(R.id.data_title);
        data_options = (LinearLayout) findViewById(R.id.data_options);

//        data_wv2 = (WebView) findViewById(R.id.data_wv2);
    }

    private void initData() {
        data_title.setText(questionEntity.getTitle());
        for (Questionnaire.Question.Option option : questionEntity.getOptions()) {
            TextView textView = new TextView(context);
            if (option.getNum() != null) {
                textView.setText(option.getNum() + "." + option.getContent());
            }
            data_options.addView(textView);
        }
        data_wv1.getSettings().setJavaScriptEnabled(true);
//        data_wv2.getSettings().setJavaScriptEnabled(true);
//        data_wv1.loadUrl("file:///android_asset/index.html");
        data_wv1.loadUrl(Config.url_chart + "showData.html");
        final int questionnaire_question_id = questionEntity.getQuestionnaire_question_id();
        Log.d("qin", "questionnaire_question_id: " + questionnaire_question_id);
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                view.loadUrl("javascript:refrech(" + questionnaire_question_id + ")");

            }
        };
        data_wv1.setWebViewClient(client);

//        data_wv2.loadUrl("file:///android_asset/index.html");
    }
}
