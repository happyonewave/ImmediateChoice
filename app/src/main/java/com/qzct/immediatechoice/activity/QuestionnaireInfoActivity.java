package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.utils;

import java.util.ArrayList;
import java.util.List;

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
        setContentView(utils.getUsableView(this, R.layout.activity_questionnaire_info, "问卷详情"));
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
        tv_hint.setText(questionnaire.getHint());
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
                title.setText(position + 1 + "." + questionnaire.getEntities().get(position).getTitle());
                LinearLayout options = (LinearLayout) v.findViewById(R.id.options);
                for (String str : questionnaire.getEntities().get(position).getOptions()) {
                    TextView option = new TextView(QuestionnaireInfoActivity.this);
                    option.setTextColor(Color.GRAY);
                    option.setText(str);
                    options.addView(option);
                }
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
