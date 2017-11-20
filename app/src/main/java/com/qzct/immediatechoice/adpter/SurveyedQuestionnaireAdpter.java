package com.qzct.immediatechoice.adpter;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qzct.immediatechoice.R;

import java.util.List;


/**
 * Created by qin on 2017/2/26.
 */

public class SurveyedQuestionnaireAdpter extends BaseAdapter {


    AppCompatActivity context;
    List<String> titleList;
    private String CHOICE_ONE = "1";
    View v;

    public SurveyedQuestionnaireAdpter(AppCompatActivity context, List<String> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    public void onDataChange(List<String> titleList) {
        this.titleList.clear();
        this.titleList.addAll(titleList);
        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }

    public String getStringFromItem(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = null;
        if (convertView == null) {
            v = View.inflate(context, R.layout.view_surveyed_questionnaire_item, null);//将fragment01_item填充成一个View
        } else {
            v = convertView;
        }

        TextView surveyed_title = (TextView) v.findViewById(R.id.surveyed_title);
        surveyed_title.setText(titleList.get(position));
        return v;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}