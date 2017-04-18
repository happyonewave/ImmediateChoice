package com.qzct.immediatechoice.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.PushActivity;
import com.qzct.immediatechoice.domain.GroupInfo;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * 测试
 */
//@ContentView(R.layout.view_video_item)
public class TestActivity extends AppCompatActivity {
    private List<GroupInfo> groupInfoList;
    private List<List<User>> membersList;
    private ListView lv_group;
    private RadioButton choice_group;
    private List<String> groupIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        setContentView(utils.getHasTopView(this, R.layout.activity_test, null));
        initView();
        initData();

    }


    private void initView() {

    }

    private void initData() {
        List<BubbleValue> values = new ArrayList<BubbleValue>();
        values.add(new BubbleValue(0, 2, 2));
        values.add(new BubbleValue(2, 1, 2));
        values.add(new BubbleValue(3, 3, 3));
        values.add(new BubbleValue(5, 5, 5));
        values.add(new BubbleValue(7, 7, 1));
        values.add(new BubbleValue(1, 3, 7));

//In most cased you can call data model methods in builder-pattern-like manner.
//        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
//        List<Line> lines = new ArrayList<Line>();
//        lines.add(line);
//        BubbleChartData data = new BubbleChartData();
//        data.setValues(values);
////        LineChartView chart = new LineChartView(this);
////        LineChartView chart = (LineChartView) findViewById(R.id.chart);
//        BubbleChartView bubbleChartView = (BubbleChartView) findViewById(R.id.chart);
//        bubbleChartView.setBubbleChartData(data);
    }
}

