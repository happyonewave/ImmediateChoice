package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.PushActivity;
import com.qzct.immediatechoice.QuestionnaireActivity;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.domain.question;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends baseFragment implements View.OnClickListener {

    private View v;
    ListView lv_home;
    LinearLayout fab_push_vote__layout;
    LinearLayout fab_questionfragment_layout;

    @Override
    public View initview() {
        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        fab_push_vote__layout = (LinearLayout) v.findViewById(R.id.fab_push_vote__layout);
        fab_questionfragment_layout = (LinearLayout) v.findViewById(R.id.fab_questionfragment_layout);
        FloatingActionButton fab_home = (FloatingActionButton) v.findViewById(R.id.fab_home);
        FloatingActionButton fab_push = (FloatingActionButton) v.findViewById(R.id.fab_push);
        FloatingActionButton fab_questionfragment = (FloatingActionButton) v.findViewById(R.id.fab_questionfragment);
        fab_home.setOnClickListener(this);
        fab_push.setOnClickListener(this);
        fab_questionfragment.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.fab_home:

                if (fab_push_vote__layout.getVisibility() == View.GONE) {
                    fab_push_vote__layout.setVisibility(View.VISIBLE);
                    fab_questionfragment_layout.setVisibility(View.VISIBLE);
                } else {
                    fab_push_vote__layout.setVisibility(View.GONE);
                    fab_questionfragment_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.fab_push:
                Intent intent = new Intent(context, PushActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_questionfragment:
                intent = new Intent(context, QuestionnaireActivity.class);
                startActivity(intent);
                break;

            default:
                break;


        }
    }


    @Override
    public void initdata() {
        lv_home = (ListView) v.findViewById(R.id.lv_home);
        new ShowFromJsonArrayTask(context, lv_home, getString(R.string.url_image_text)).execute();

        final SwipeRefreshLayout home_swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.home_swipe_refresh);
        home_swipe_refresh.setColorSchemeColors(Color.YELLOW, Color.BLUE);
        home_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(context,"正在刷新",Toast.LENGTH_SHORT).show();
                new ShowFromJsonArrayTask(context, lv_home, getString(R.string.url_image_text)).execute();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context,"刷新完成",Toast.LENGTH_SHORT).show();
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    //读取相应内容
                    String question_content= temp.getString("question_content");
                    String image_left= temp.getString("image_left");
                    String image_right= temp.getString("image_right");
                    String quizzer_name= temp.getString("quizzer_name");
                    int share_count= temp.getInt("share_count");
                    int comment_count= temp.getInt("comment_count");
                    String comment= temp.getString("comment");
                    question question = new question(question_content,image_left,image_right,quizzer_name,share_count,comment_count,comment);
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
