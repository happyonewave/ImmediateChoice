package com.qzct.immediatechoice.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.util.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class CalendarActivity extends AppCompatActivity {
    private ListView lv_calendar;
    private OneCalendarView calendar;
    //    private List<Questionnaire> questionnaireList;
    private Questionnaire questionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_calendar, "每日一问"));
        initView();
        initData();
    }

    private void initView() {
        lv_calendar = (ListView) findViewById(R.id.lv_calendar);
        calendar = new OneCalendarView(this);
        calendar = (OneCalendarView) View.inflate(CalendarActivity.this, R.layout.calendar, null);
        calendar.setVisibility(View.VISIBLE);
//        calendar = (OneCalendarView) findViewById(R.id.calendar);
    }

    private void initData() {
        List<String> options1 = new ArrayList<String>();
        options1.add("男");
        options1.add("女");
        List<String> options2 = new ArrayList<String>();
        options2.add("1000及以下");
        options2.add("1000~1500");
        options2.add("1500以上");
        List<String> options3 = new ArrayList<String>();
        options3.add("水果店");
        options3.add("网上");
        options3.add("超市");
        Questionnaire.Entity entity = new Questionnaire.Entity("您的性别", options1);
        Questionnaire.Entity entity1 = new Questionnaire.Entity("您每月的生活费", options2);
        Questionnaire.Entity entity2 = new Questionnaire.Entity("您平时常在哪些地方购买水果", options3);
        List<Questionnaire.Entity> entities = new ArrayList<Questionnaire.Entity>();
        entities.add(entity);
        entities.add(entity1);
        entities.add(entity2);
        questionnaire = new Questionnaire("关于学校水果店的调查问卷", entities);
        lv_calendar.setAdapter(new BaseAdapter() {
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
                View v = View.inflate(CalendarActivity.this, R.layout.activity_calendar_item, null);
                TextView title = (TextView) v.findViewById(R.id.calendar_title);
                title.setText(position + 1 + "." + questionnaire.getEntities().get(position).getTitle());
                RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
                for (String str : questionnaire.getEntities().get(position).getOptions()) {
                    RadioButton option = new RadioButton(CalendarActivity.this);
                    option.setTextColor(Color.GRAY);
                    option.setText(str);
                    radioGroup.addView(option);
                }
                return v;
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
                Toast.makeText(CalendarActivity.this, "点击了" + day.getDate(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dateOnLongClick(Day day, int position)   {
                Toast.makeText(CalendarActivity.this, "长按了" + day.getDate(), Toast.LENGTH_SHORT).show();
            }
        });
//        calendar.setVisibility(View.VISIBLE);
        lv_calendar.addHeaderView(calendar);
        Button submit = new Button(CalendarActivity.this);
        submit.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal));
        submit.setPadding(0, 5, 0, 5);
        submit.setWidth(50);
        submit.setText("提交");
        lv_calendar.addFooterView(submit);
    }
}
