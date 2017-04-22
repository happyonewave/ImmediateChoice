package com.qzct.immediatechoice.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 问卷调查Activity
 */
public class PushQuestionnaireActivity extends AppCompatActivity {

    private EditText questionnaire_title;
    private EditText questionnaire_hint;
    private Button push;
    private Button add;
    private EditText et_title;
    private LinearLayout questions;
    Context context = this;
    private List<Questionnaire.Question> questionEntityList = new ArrayList<Questionnaire.Question>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_push_questionnaire, "发布问卷"));
        initView();
        initData();
    }

    private void initView() {
        questionnaire_title = (EditText) findViewById(R.id.questionnaire_title);
        questionnaire_hint = (EditText) findViewById(R.id.questionnaire_hint);
        add = (Button) findViewById(R.id.questionnaire_question_add);
        push = (Button) findViewById(R.id.questionnaire_question_push);
        questions = (LinearLayout) findViewById(R.id.questions);
    }

    private void initData() {
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionnaire_title.getText().toString().isEmpty()) {
                    String title = questionnaire_title.getText().toString();
                    String hint = questionnaire_hint.getText().toString();
                    Intent data = new Intent();
                    data.putExtra("questionnaire", new Questionnaire(title, hint, questionEntityList));
                    setResult(RESULT_OK, data);
                    finish();
                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "您未填写信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            public AlertDialog dialog;

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View view = View.inflate(context, R.layout.dialog_add_questionnaire_question, null);
                et_title = (EditText) view.findViewById(R.id.title);
                final LinearLayout options = (LinearLayout) view.findViewById(R.id.options);
                final Button add = (Button) view.findViewById(R.id.questionnaire_option_add);
                Button finish = (Button) view.findViewById(R.id.finish);
                final List<LinearLayout> optionList = new ArrayList<LinearLayout>();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout option = (LinearLayout) View.inflate(context, R.layout.questionnaire_question_option, null);
                        optionList.add(option);
                        options.addView(option);
                    }
                });
                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add(optionList);
                        dialog.cancel();
                    }
                });
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });
    }

    public void add(List<LinearLayout> optionList) {
        RadioGroup group = new RadioGroup(context);
        List<String> options = new ArrayList<String>();
        for (LinearLayout option : optionList) {
//            EditText et_title = (EditText) option.findViewById(R.id.title);
            EditText et_num = (EditText) option.findViewById(R.id.num);
            EditText et_content = (EditText) option.findViewById(R.id.content);
//            String title = et_title.getText().toString();
            String num = et_num.getText().toString();
            String content = et_content.getText().toString();
            options.add(content);
            RadioButton button = new RadioButton(context);
            button.setText(content);
            group.addView(button);
        }
        String title = et_title.getText().toString();
        TextView tv_title = new TextView(context);
        tv_title.setText(title);
        Questionnaire.Question questionEntity = new Questionnaire.Question(title, options);
        questionEntityList.add(questionEntity);
        questions.addView(tv_title);
        questions.addView(group);

    }
}


//public class QuestionnaireActivity extends AppCompatActivity {
//
//    @ViewInject(R.id.iv_questionnaire_back)
//    private ImageView iv_back;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_push_questionnaire);
//        //返回view
//        iv_back = (ImageView) findViewById(R.id.iv_questionnaire_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.finish();
//            }
//        });
//    }
//}