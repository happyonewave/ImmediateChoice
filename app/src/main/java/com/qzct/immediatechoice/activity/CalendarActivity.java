package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;
import com.qzct.immediatechoice.util.utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.qzct.immediatechoice.domain.Questionnaire.*;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class CalendarActivity extends AppCompatActivity {
    private ListView lv_calendar;
    private OneCalendarView calendar;
    //    private List<Questionnaire> questionnaireList;
    private Questionnaire questionnaire;
    private ListAdapter adapter;
    private List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    private List<Questionnaire.Choice> choiceList = new ArrayList<Questionnaire.Choice>();
    private TextView tv_questionnaire_title;
    private LinearLayout headerView;
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_calendar, "每日一问"));
        initView();
        initData();
    }

    private void initView() {
        lv_calendar = (ListView) findViewById(R.id.lv_calendar);
//        calendar = new OneCalendarView(this);
        calendar = (OneCalendarView) View.inflate(CalendarActivity.this, R.layout.calendar, null);
//         headerView = (LinearLayout) View.inflate(CalendarActivity.this, R.layout.calendar, null);
//        calendar = (OneCalendarView) headerView.findViewById(R.id.calendar);
//        tv_questionnaire_title = (TextView) headerView.findViewById(R.id.questionnaire_title);
//        calendar.setVisibility(View.VISIBLE);
//        calendar = (OneCalendarView) findViewById(R.id.calendar);
    }

    private void initData() {
        adapter = new BaseAdapter() {
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
                View v = null;
//                if (convertView != null) {
//                    v = convertView;
//                } else {
                v = View.inflate(CalendarActivity.this, R.layout.activity_calendar_item, null);
//                }
                TextView title = (TextView) v.findViewById(R.id.calendar_title);
                title.setText(position + 1 + "." + questionnaire.getEntities().get(position).getTitle());
//                RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
                LinearLayout checkGroup = (LinearLayout) v.findViewById(R.id.checkGroup);
                for (Question.Option option : questionnaire.getEntities().get(position).getOptions()) {
                    CheckBox cb_option = new CheckBox(CalendarActivity.this);
                    cb_option.setTextColor(Color.GRAY);
                    cb_option.setText(option.getNum() + "." + option.getContent());
                    cb_option.setTag(R.id.tag_num, option.getNum());
                    cb_option.setTag(R.id.tag_questionnaire_question_id, questionnaire.getEntities().get(position).getQuestionnaire_question_id());
                    cb_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            String num = (String) buttonView.getTag(R.id.tag_num);
                            int questionnaire_question_id = (int) buttonView.getTag(R.id.tag_questionnaire_question_id);
                            if (isChecked) {
                                Toast.makeText(CalendarActivity.this, "选择了questionnaire_question_id:   " + questionnaire_question_id + "num:   " + num, Toast.LENGTH_SHORT).show();
                                Questionnaire.Choice choice = new Questionnaire.Choice(num, MyApplication.user.getUser_id(), questionnaire_question_id);
                                choiceList.add(choice);
                            } else {
                                for (Choice choice : choiceList) {
                                    if (choice.getQuestionnaire_question_id() == questionnaire_question_id && num.equals(choice.getNum())) {
                                        choiceList.remove(choice);
                                        Toast.makeText(CalendarActivity.this, "移除了questionnaire_question_id:   " + questionnaire_question_id + "num:   " + num, Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }
                        }
                    });
//                    radioButtonList.add(rb_option);
//                    radioGroup.addView(rb_option);
                    checkGroup.addView(cb_option);
                }
                return v;
            }
        };
        Service.getInstance().getQuestionnaireIds(new MyCallback.GetQuestionnaireIdsCallback() {
            @Override
            public void onSuccess(final List<Integer> idList) {
//                int randNumber = new Random().nextInt(MAX - MIN + 1) + MIN;
                final int randNumber = new Random().nextInt(idList.size());
                Log.d("qin", "count: " + (idList.size() - 1));
                Log.d("qin", "randNumber: " + randNumber);
                Service.getInstance().getQuestionnaire(new MyCallback.GetQuestionnaireCallback() {
                    @Override
                    public int getQuestionnaireId() {
                        return idList.get(randNumber);
                    }

                    @Override
                    public int getUserId() {
                        return 0;
                    }

                    @Override
                    public void onSuccess(Questionnaire questionnaire) {
                        CalendarActivity.this.questionnaire = questionnaire;
                        lv_calendar.setAdapter(adapter);
                    }

                    @Override
                    public void onSuccess(List<Questionnaire> list) {

                    }

                    @Override
                    public void onError(Throwable ex) {

                    }
                });
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(CalendarActivity.this, "获取问卷数量失败", Toast.LENGTH_SHORT).show();
            }
        });
//        List<Questionnaire.Question.Option> options1 = new ArrayList<Questionnaire.Question.Option>();
//        options1.add(new Questionnaire.Question.Option("1", "男"));
//        options1.add(new Questionnaire.Question.Option("2", "女"));
//        List<Questionnaire.Question.Option> options2 = new ArrayList<Questionnaire.Question.Option>();
//        options2.add(new Questionnaire.Question.Option("1", "1000及以下"));
//        options2.add(new Questionnaire.Question.Option("2", "1000~1500"));
//        options2.add(new Questionnaire.Question.Option("3", "1500以上"));
//        List<Questionnaire.Question.Option> options3 = new ArrayList<Questionnaire.Question.Option>();
//        options3.add(new Questionnaire.Question.Option("1", "水果店"));
//        options3.add(new Questionnaire.Question.Option("2", "网上"));
//        options3.add(new Questionnaire.Question.Option("3", "超市"));
//        Questionnaire.Question entity = new Questionnaire.Question("您的性别", options1);
//        Questionnaire.Question entity1 = new Questionnaire.Question("您每月的生活费", options2);
//        Questionnaire.Question entity2 = new Questionnaire.Question("您平时常在哪些地方购买水果", options3);
//        List<Questionnaire.Question> entities = new ArrayList<Questionnaire.Question>();
//        entities.add(entity);
//        entities.add(entity1);
//        entities.add(entity2);
//        questionnaire = new Questionnaire("关于学校水果店的调查问卷", entities);
        calendar.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {

            @Override
            public void prevMonth() {

            }

            @Override
            public void nextMonth() {

            }
        });

        calendar.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {

            @Override
            public void dateOnClick(Day day, int position) {
                Toast.makeText(CalendarActivity.this, "点击了" + day.getDate(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dateOnLongClick(Day day, int position) {
                Toast.makeText(CalendarActivity.this, "长按了" + day.getDate(), Toast.LENGTH_SHORT).show();
            }
        });
//        calendar.setVisibility(View.VISIBLE);
        lv_calendar.addHeaderView(calendar);
        Button submit = new Button(CalendarActivity.this);
        submit.setBackgroundResource(R.drawable.btn_bg_normal);
//        submit.setPadding(100, 5, 100, 5);
        submit.setWidth(50);
        submit.setText("提交");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choiceList.size() == questionnaire.getEntities().size()) {

                    Service.getInstance().chooseQuestionOption(choiceList, new MyCallback.ChooseQuestionOptionCallback() {
                        @Override
                        public void onSuccess() {
                            Service.getInstance().gradeAdd(context, 5);
                            Toast.makeText(CalendarActivity.this, "提交成功,您的积分已+5", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex) {
                            Toast.makeText(CalendarActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(CalendarActivity.this, "请您选择所有的问题!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv_calendar.addFooterView(submit);
    }
}
