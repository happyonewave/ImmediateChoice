package com.qzct.immediatechoice.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.GroupInfo;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;


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
    private WebView webview;
    PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    private PieChartDataProvider chart;
    private WebView webview1;
    private WebView webview2;
    private SwipeMenuListView lv;
    private Context context = this;
    private String[] mStrs = {"1", "2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        setContentView(utils.getUsableView(this, R.layout.activity_test, null));
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData();

    }


    private void initView() throws IOException {
        lv = (SwipeMenuListView) findViewById(R.id.lv);
//        chart = (PieChartDataProvider) findViewById(R.id.chart1);
//
//
//        //实例化WebView对象
////        webview = new WebView(this);
//        webview1 = (WebView) findViewById(R.id.webView1);
//        webview2 = (WebView) findViewById(R.id.webView2);
//        //设置WebView属性，能够执行Javascript脚本
//        webview1.getSettings().setJavaScriptEnabled(true);
//        webview2.getSettings().setJavaScriptEnabled(true);
//
//        new AsyncTask<String, String, String>() {
//
//            @Override
//            protected String doInBackground(String... params) {
//                FileOutputStream outputStream = null;
//                try {
//                    outputStream = new FileOutputStream("file:///android_asset/index1.html");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                try {
////        加载需要显示的网页
//                    String datas = "<!DOCTYPE html>\n" +
//                            "<html style=\"height: 100%; margin: 0;\">\n" +
//                            "\t<head>\n" +
//                            "\t\t<meta charset=\"utf-8\" />\n" +
//                            "\t\t<title></title>\n" +
//                            "\t\t<script src=\"js/echarts.js\" type=\"text/javascript\" charset=\"utf-8\"></script>\n" +
//                            "\t</head>\n" +
//                            "  <body style=\"height: 100%;margin: 0;\">\n" +
//                            "\t\t\n" +
//                            "    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->\n" +
//                            "    <div id=\"main\" style=\"width: 100%;height:100%;\"></div>\n" +
//                            "    <script type=\"text/javascript\">\n" +
//                            "        // 基于准备好的dom，初始化echarts实例\n" +
//                            "        var myChart = echarts.init(document.getElementById('main'));\n" +
//                            "\n" +
//                            "//         指定图表的配置项和数据\n" +
//                            "//      var option = {\n" +
//                            "//          title: {\n" +
//                            "//              text: 'ECharts 入门示例'\n" +
//                            "//          },\n" +
//                            "//          tooltip: {},\n" +
//                            "//          legend: {\n" +
//                            "//              data:['销量']\n" +
//                            "//          },\n" +
//                            "//          xAxis: {\n" +
//                            "//              data: [\"衬衫\",\"羊毛衫\",\"雪纺衫\",\"裤子\",\"高跟鞋\",\"袜子\"]\n" +
//                            "//          },\n" +
//                            "//          yAxis: {},\n" +
//                            "//          series: [{\n" +
//                            "//              name: '销量',\n" +
//                            "//              type: 'bar',\n" +
//                            "//              data: [5, 20, 36, 10, 10, 20]\n" +
//                            "//          }]\n" +
//                            "//      };\n" +
//                            "//      app.title = '环形图';\n" +
//                            "\n" +
//                            "   var option = {\n" +
//                            "    tooltip: {\n" +
//                            "        trigger: 'item',\n" +
//                            "        formatter: \"{a} <br/>{b}: {c} ({d}%)\"\n" +
//                            "    },\n" +
//                            "    legend: {\n" +
//                            "        orient: 'vertical',\n" +
//                            "        x: 'left',\n" +
//                            "        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']\n" +
//                            "    },\n" +
//                            "    series: [\n" +
//                            "        {\n" +
//                            "            name:'访问来源',\n" +
//                            "            type:'pie',\n" +
//                            "            radius: ['50%', '70%'],\n" +
//                            "            avoidLabelOverlap: false,\n" +
//                            "            label: {\n" +
//                            "                normal: {\n" +
//                            "                    show: false,\n" +
//                            "                    position: 'center'\n" +
//                            "                },\n" +
//                            "                emphasis: {\n" +
//                            "                    show: true,\n" +
//                            "                    textStyle: {\n" +
//                            "                        fontSize: '30',\n" +
//                            "                        fontWeight: 'bold'\n" +
//                            "                    }\n" +
//                            "                }\n" +
//                            "            },\n" +
//                            "            labelLine: {\n" +
//                            "                normal: {\n" +
//                            "                    show: false\n" +
//                            "                }\n" +
//                            "            },\n" +
//                            "            data:[\n" +
//                            "                {value:335, name:'直接访问'},       \n" +
//                            "                {value:310, name:'邮件营销'},\n" +
//                            "                {value:234, name:'联盟广告'},\n" +
//                            "                {value:135, name:'视频广告'},\n" +
//                            "                {value:1548, name:'搜索引擎'}\n" +
//                            "            ]\n" +
//                            "        }\n" +
//                            "    ]\n" +
//                            "};\n" +
//                            "\n" +
//                            "        \n" +
//                            "        \n" +
//                            "        // 使用刚指定的配置项和数据显示图表。\n" +
//                            "        myChart.setOption(option);\n" +
//                            "        window.onresize = myChart.resize;\n" +
//                            "    </script>\n" +
//                            "\t</body>\n" +
//                            "</html>\n";
//                    outputStream.write(datas.getBytes("GB2312"));
////                    utils.getTextFromStream()
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                webview1.loadUrl("file:///android_asset/index1.html");
//
//                super.onPostExecute(s);
//            }
//        }.execute();
////        webview1.loadUrl("javascript:refresh('" + "" + "')");
//        webview2.loadUrl("file:///android_asset/index.html");
//        webview1.addJavascriptInterface();
//        webview2.loadDataWithBaseURL(null, datas, "text/html", "utf-8",
//                null);
//        webview.loadUrl("http://www.51cto.com/");
        //设置Web视图
//        setContentView(webview);

    }

    private void initData() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(Color.GREEN));
                openItem.setWidth(utils.Dp2Px(context, 90));
                openItem.setTitle("打开");
                openItem.setTitleSize(20);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//
//                deleteItem.setWidth(180);
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lv.setMenuCreator(creator);
        // Right
        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
//                        Toast.makeText(context, "open", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // 监测用户在ListView的SwipeMenu侧滑事件。
        lv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int pos) {
                Log.d("位置:" + pos, "开始侧滑...");
            }

            @Override
            public void onSwipeEnd(int pos) {
                Log.d("位置:" + pos, "侧滑结束.");
            }
        });
        lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mStrs));
//        List<BubbleValue> values = new ArrayList<BubbleValue>();
//        values.add(new BubbleValue(0, 2, 2));
//        values.add(new BubbleValue(2, 1, 2));
//        values.add(new BubbleValue(3, 3, 3));
//        values.add(new BubbleValue(5, 5, 5));
//        values.add(new BubbleValue(7, 7, 1));
//        values.add(new BubbleValue(1, 3, 7));

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

    private void generateData() {
        int numValues = 6;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        data = new PieChartData(values);
//        data.setHasLabels(hasLabels);
//        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
//        data.setHasLabelsOutside(hasLabelsOutside);
//        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }
        chart.setPieChartData(data);
    }

//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
////            webview.goBack(); //goBack()表示返回WebView的上一页面
////            return true;
////        }
//        return false;
//    }
}
