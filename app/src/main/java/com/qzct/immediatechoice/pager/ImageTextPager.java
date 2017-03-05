package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.MainActivity;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.domain.question;
import com.qzct.immediatechoice.fragment.HomeFragment;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017-03-05.
 */
public class ImageTextPager extends BasePager {


    public ImageTextPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_image_text, null);
        return view;
    }

    //    @ViewInject(R.id.vp_home)
    private ListView lv_home;
    //    @ViewInject(R.id.home_swipe_refresh)
    private SwipeRefreshLayout home_swipe_refresh;

    //    int oldVisibleItem;
    @Override
    public void initData() {
        lv_home = (ListView) view.findViewById(R.id.lv_home);
        sendFabIsVisible(lv_home);
        home_swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        new ShowFromJsonArrayTask(context, lv_home, context.getString(R.string.url_image_text)).execute();
        home_swipe_refresh.setColorSchemeColors(Color.YELLOW, Color.BLUE);
        home_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(context, "正在刷新", Toast.LENGTH_SHORT).show();
                new ShowFromJsonArrayTask(context, lv_home, context.getString(R.string.url_image_text)).execute();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
                        home_swipe_refresh.setRefreshing(false);

                    }
                }, 1500);
            }
        });


    }


    class ShowFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {

        String spec;
        Context context;
        ListView listView;

        public ShowFromJsonArrayTask(Context context, ListView listView, String spec) {
            this.context = context;
            this.listView = listView;
            this.spec = spec;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            //返回获取的jasonArray
            return utils.GetJsonArray(spec);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            ArrayList<question> questionlist = null;
            try {
                //new一个info数组
                questionlist = new ArrayList<question>();
                //遍历传入的jsonArray
                for (int i = jsonArray.length() - 1; i > 0; i--) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    //读取相应内容
                    String question_content = temp.getString("question_content");
                    String image_left = temp.getString("image_left");
                    String image_right = temp.getString("image_right");
                    String quizzer_name = temp.getString("quizzer_name");
                    int share_count = temp.getInt("share_count");
                    int comment_count = temp.getInt("comment_count");
                    String comment = temp.getString("comment");
                    question question = new question(question_content, image_left, image_right, quizzer_name, share_count, comment_count, comment, null);
                    System.out.println(question.toString());
                    questionlist.add(question);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //设置适配器
            listView.setAdapter(new ImageTextAdpter(context, questionlist));
            super.onPostExecute(jsonArray);
        }
    }

}