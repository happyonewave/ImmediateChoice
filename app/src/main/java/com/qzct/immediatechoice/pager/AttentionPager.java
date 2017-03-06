package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.domain.question;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017-03-05.
 */
public class AttentionPager extends BasePager {


    public AttentionPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_attention, null);
        return view;
    }

    //    @ViewInject(R.id.vp_home)
    private ListView lv_home_attention;
    //    @ViewInject(R.id.home_swipe_refresh)
    private SwipeRefreshLayout home_attention_swipe_refresh;

    @Override
    public void initData() {

        lv_home_attention = (ListView) view.findViewById(R.id.lv_home_attention);
        sendFabIsVisible(lv_home_attention);
        home_attention_swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.home_attention_swipe_refresh);
        new ShowFromJsonArrayTask(context, lv_home_attention, MyApplication.url_image_text).execute();
        home_attention_swipe_refresh.setColorSchemeColors(Color.YELLOW, Color.BLUE);
        home_attention_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(context, "正在刷新", Toast.LENGTH_SHORT).show();
                new ShowFromJsonArrayTask(context, lv_home_attention, MyApplication.url_image_text).execute();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
                        home_attention_swipe_refresh.setRefreshing(false);

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
                    String quizzer_portrait = temp.getString("quizzer_portrait");
                    int share_count = temp.getInt("share_count");
                    int comment_count = temp.getInt("comment_count");
                    String comment = temp.getString("comment");
                    question question = new question(question_content, image_left, image_right, quizzer_name,quizzer_portrait, share_count, comment_count, comment, null);
                    System.out.println(question.getQuizzer_name());
                    questionlist.add(question);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("questionlist",questionlist.toString());
            //设置适配器
            listView.setAdapter(new ImageTextAdpter(context, questionlist));
            super.onPostExecute(jsonArray);
        }
    }

}
