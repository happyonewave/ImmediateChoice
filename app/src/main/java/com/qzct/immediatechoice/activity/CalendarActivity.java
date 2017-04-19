package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.QuestionnaireItem;
import com.qzct.immediatechoice.test.TestView;
import com.qzct.immediatechoice.util.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class CalendarActivity extends AppCompatActivity {
    private ListView lv_calendar;
    private OneCalendarView calendar;
    private List<QuestionnaireItem> questionnaire;

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
        calendar = (OneCalendarView) findViewById(R.id.calendar);
    }

    private void initData() {
        questionnaire = new ArrayList<QuestionnaireItem>();
        questionnaire.add(new QuestionnaireItem("您的性别", "男", "女", null, null));
        questionnaire.add(new QuestionnaireItem("您每月得让", "男", "女", null, null));
        questionnaire.add(new QuestionnaireItem("您的性别", "男", "女", null, null));
        lv_calendar.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return questionnaire.size();
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
                LinearLayout view = new LinearLayout(CalendarActivity.this);
                view.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(CalendarActivity.this);
                RadioGroup radioGroup = new RadioGroup(CalendarActivity.this);
                title.setText(questionnaire.get(position).getTitle());

                RadioButton a = new RadioButton(CalendarActivity.this);
                a.setText(questionnaire.get(position).getA());
                RadioButton b = new RadioButton(CalendarActivity.this);
                b.setText(questionnaire.get(position).getB());
                view.addView(title);
                radioGroup.addView(a);
                radioGroup.addView(b);
                if (questionnaire.get(position).getC() != null) {
                    RadioButton c = new RadioButton(CalendarActivity.this);
                    c.setText(questionnaire.get(position).getC());
                    radioGroup.addView(c);
                }
                if (questionnaire.get(position).getD() != null) {
                    RadioButton d = new RadioButton(CalendarActivity.this);
                    d.setText(questionnaire.get(position).getD());
                    radioGroup.addView(d);
                }
                view.addView(radioGroup);
//                view.addView(a);
//                view.addView(b);
//                view.addView(c);
//                view.addView(d);
                return view;
            }
        });
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
                Toast.makeText(CalendarActivity.this, "点击了" + day.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dateOnLongClick(Day day, int position) {
                Toast.makeText(CalendarActivity.this, "长按了" + day.toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        calendar.setVisibility(View.VISIBLE);
//        lv_calendar.addHeaderView(calendar);
    }
}
