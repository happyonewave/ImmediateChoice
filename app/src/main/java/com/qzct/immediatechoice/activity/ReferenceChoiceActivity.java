package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.SurveyedQuestionnaireAdpter;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;

import java.util.ArrayList;
import java.util.List;

public class ReferenceChoiceActivity extends AppCompatActivity {

    private ListView lv_questionnaire;
    private Questionnaire questionnaire;
    private List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
    private List<String> mtitleList = new ArrayList<String>();
    private int PUSH_QUESTIONNAIRE = 0;
    private SurveyedQuestionnaireAdpter adapter;
    private AppCompatActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_choice);
//        setContentView(Utils.getHasTopView(this, R.layout.activity_reference_choice, "参考问卷库"));
        initView();
        initData();
    }

    private void initView() {

        lv_questionnaire = (ListView) findViewById(R.id.lv_questionnaire);
        ImageView back = (ImageView) findViewById(R.id.top_back);
        TextView tv_title = (TextView) findViewById(R.id.top_title);
        TextView tv_key_word = (TextView) findViewById(R.id.tv_key_word);
        final SearchView search = (SearchView) findViewById(R.id.search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        tv_title.setText("参考问卷库");
        tv_key_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.onActionViewExpanded();
            }
        });
    }


    public void initData() {
        getData();
        adapter = new SurveyedQuestionnaireAdpter(context, mtitleList);
        lv_questionnaire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, QuestionnaireInfoActivity.class);
                intent.putExtra("questionnaire", questionnaireList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PUSH_QUESTIONNAIRE) {
                questionnaire = (Questionnaire) data.getSerializableExtra("questionnaire");
                questionnaireList.add(questionnaire);
                mtitleList.add(questionnaire.getTitle());
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void getData() {
        Service.getInstance().getQuestionnaire(new MyCallback.GetQuestionnaireCallback() {
            @Override
            public int getQuestionnaireId() {
                return 0;
            }

            @Override
            public int getUserId() {
                return MyApplication.user.getUser_id();
            }

            @Override
            public void onSuccess(Questionnaire questionnaire) {

            }

            @Override
            public void onSuccess(List<Questionnaire> list) {
                questionnaireList = list;
                for (Questionnaire questionnaire : questionnaireList) {
                    mtitleList.add(questionnaire.getTitle());
                }
                lv_questionnaire.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(context, "连接失败或你没有发布过问卷", Toast.LENGTH_SHORT).show();
            }

        });

    }


}
