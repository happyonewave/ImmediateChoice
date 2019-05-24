package com.qzct.immediatechoice.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.PushQuestionnaireActivity;
import com.qzct.immediatechoice.activity.QuestionnaireInfoActivity;
import com.qzct.immediatechoice.adpter.SurveyingQuestionnaireAdpter;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tsh2 on 2017/4/25.
 */
public class QuestionnaireSurveyingFragment extends BaseFragment {

    private ListView lv_questionnaire;
    private Questionnaire questionnaire;
    private List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
    private List<String> mtitleList = new ArrayList<String>();
    private ImageView push__questionnaire;
    private SurveyingQuestionnaireAdpter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_questionnaire_surveying, null);
        lv_questionnaire = (ListView) v.findViewById(R.id.lv_questionnaire);
        push__questionnaire = (ImageView) v.findViewById(R.id.push__questionnaire);
        return v;
    }

    public void initData() {
        getData();
        adapter = new SurveyingQuestionnaireAdpter(context,mtitleList);
        lv_questionnaire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, QuestionnaireInfoActivity.class);
                intent.putExtra("questionnaire", questionnaireList.get(position));
                startActivity(intent);
            }
        });
        push__questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PushQuestionnaireActivity.class);
                startActivityForResult(intent, PUSH_QUESTIONNAIRE_DOING);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PUSH_QUESTIONNAIRE_DOING) {
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
//        List<String> options1 = new ArrayList<String>();
//        options1.add("男");
//        options1.add("女");
//        List<String> options2 = new ArrayList<String>();
//        options2.add("1000及以下");
//        options2.add("1000~1500");
//        options2.add("1500以上");
//        List<String> options3 = new ArrayList<String>();
//        options3.add("水果店");
//        options3.add("网上");
//        options3.add("超市");
//        Questionnaire.Question entity = new Questionnaire.Question("您的性别", options1);
//        Questionnaire.Question entity1 = new Questionnaire.Question("您每月的生活费", options2);
//        Questionnaire.Question entity2 = new Questionnaire.Question("您平时常在哪些地方购买水果", options3);
//        List<Questionnaire.Question> entities = new ArrayList<Questionnaire.Question>();
//        entities.add(entity);
//        entities.add(entity1);
//        entities.add(entity2);
//        questionnaire = new Questionnaire("关于学校水果店的调查问卷", "谢谢您的调查", entities);
//        questionnaireList.add(questionnaire);
//        for (Questionnaire questionnaire : questionnaireList) {
//            mtitleList.add(questionnaire.getTitle());
//        }

    }
}
