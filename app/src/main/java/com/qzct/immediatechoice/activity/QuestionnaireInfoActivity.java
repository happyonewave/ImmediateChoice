package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.Utils;

/**
 * Created by tsh2 on 2017/4/22.
 */

public class QuestionnaireInfoActivity extends Activity {
    private Questionnaire questionnaire;
    private ListView lv_show_questionnaire;
    private Context context = this;
    private TextView tv_title;
    private TextView tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Utils.getUsableView(this, R.layout.activity_questionnaire_info, "问卷详情"));
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        initView();
        initData();
    }

    private void initView() {
        lv_show_questionnaire = (ListView) findViewById(R.id.lv_show_questionnaire);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
    }

    private void initData() {
        tv_title.setText(questionnaire.getTitle());
        tv_hint.setText("  " + questionnaire.getHint());
        lv_show_questionnaire.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return questionnaire.getEntities().size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = View.inflate(QuestionnaireInfoActivity.this, R.layout.activity_questionnaire_info_item, null);
                TextView title = (TextView) v.findViewById(R.id.questionnaire_info_title);
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                title.setText(position + 1 + "." + questionnaire.getEntities().get(position).getTitle());
                LinearLayout options = (LinearLayout) v.findViewById(R.id.options);
                for (Questionnaire.Question.Option option : questionnaire.getEntities().get(position).getOptions()) {
                    TextView tv_option = new TextView(QuestionnaireInfoActivity.this);
                    tv_option.setTextColor(Color.GRAY);
                    tv_option.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    tv_option.setText(option.getNum() + "." + option.getContent());
                    options.addView(tv_option);
                }
//                WebView webView = new WebView(context);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("file:///android_asset/index.html");
//                final int questionnaire_question_id = questionnaire.getEntities().get(position).getQuestionnaire_question_id();
//                Log.d("qin", "questionnaire_question_id: " + questionnaire_question_id);
//                WebViewClient client = new WebViewClient() {
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
////                super.onPageFinished(view, url);
//                        view.loadUrl("javascript:refrech(" + questionnaire_question_id + ")");
//
//                    }
//                };
//                webView.setWebViewClient(client);
//                options.addView(webView);
                return v;
            }
        });
//        lv_show_questionnaire.addHeaderView(calendar);
        lv_show_questionnaire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DataActivity.class);
                intent.putExtra("questionEntity", questionnaire.getEntities().get(position));
                startActivity(intent);
            }
        });

    }
}
