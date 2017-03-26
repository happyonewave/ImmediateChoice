package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.adpter.QuestionVideoAdpter;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zrc.widget.ZrcListView;


/**
 * Created by Administrator on 2017-03-05.
 */
public class VideoPager extends BasePager implements ZrcListView.OnItemClickListener {


    private static String GET_MAX_ID = "0";
    private static final String GET_QUESTION = "1";
    private static final String REFRESH_QUESTION = "2";
    private static final String TAG = "qin";
    private static final String url = Config.url_image_text;
    private ZrcListView lv_home_video;
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private JSONArray jsonArray;
    private String request;
    private QuestionVideoAdpter adpter;
    private boolean isFirst;
    private String maxPostTime;
    private String minPostTime;


    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        view = inflater.inflate(R.layout.view_video, null);
        return view;
    }


    @Override
    public void initData() {
        lv_home_video = (ZrcListView) view.findViewById(R.id.lv_home_video);
//        sendFabIsVisible(lv_home_video);
            setLoad(lv_home_video);
        lv_home_video.setOnItemClickListener(this);
        FirstLoadMoreTask firstLoadMoreTask = new FirstLoadMoreTask();
        firstLoadMoreTask.execute();

    }


    /**
     * 下拉刷新
     */
    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshTask refreshTask = new RefreshTask();
                refreshTask.execute();
                lv_home_video.setRefreshSuccess("刷新成功");
            }
        }, 2000);

    }

    /**
     * 上拉加载
     */
    private void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadMoreTask loadMoreTask = new LoadMoreTask();
                loadMoreTask.execute();
            }
        }, 2000);


    }


    /**
     * ListVItem点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(ZrcListView adapterView, View view, int i, long l) {
        Question question = adpter.getQuestionFromItem(i);
        MyApplication.question = question;
        MyApplication.isQuestion = false;
        Intent intent = new Intent(context, CommentActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, "点击了item" + i, Toast.LENGTH_LONG).show();
    }

    /**
     * 第一次加载
     */
    class FirstLoadMoreTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray 2017-03-20 21:25:53.0
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String startTime = format.format(now);
            request = getQuestionJson(GET_QUESTION, startTime, Config.unixTime_min);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (request != null) {
                if (!request.equals("-1")) {
                    adpter = new QuestionVideoAdpter(context, questionList);
                    isFirst = true;
                    refreshquestionList(GET_QUESTION);
                    lv_home_video.setAdapter(adpter);
                } else {
                    Toast.makeText(context, "已刷新为最新数据", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }


    /**
     * 刷新数据
     */
    class RefreshTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray 2017-03-20 21:25:53.0
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String startTime = format.format(now);
            request = getQuestionJson(REFRESH_QUESTION, startTime, maxPostTime);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (request != null) {
                if (!request.equals("-1")) {
                    refreshquestionList(REFRESH_QUESTION);
                } else {
                    Toast.makeText(context, "已刷新为最新数据", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    /**
     * 加载数据
     */
    class LoadMoreTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray 北京时间1970年01月01日08时00分00秒
            request = getQuestionJson(GET_QUESTION, minPostTime, Config.unixTime_min);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (request != null) {
                if (!request.equals("-1")) {
                    refreshquestionList(GET_QUESTION);
                    lv_home_video.setLoadMoreSuccess();
                } else {
                    lv_home_video.stopLoadMore();

                    Toast.makeText(context, "已加载所有数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 请求数据
     *
     * @param startTime
     * @return
     */
    private String getQuestionJson(String msg, String startTime, String endTime) {
        HttpClient hc = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            BasicNameValuePair pair1 =
                    new BasicNameValuePair("msg", msg);
            BasicNameValuePair pair2 =
                    new BasicNameValuePair("type", "video");
            BasicNameValuePair pair3 =
                    new BasicNameValuePair("startTime", startTime);
            BasicNameValuePair pair4 =
                    new BasicNameValuePair("endTime", endTime);
            parameters.add(pair1);
            parameters.add(pair2);
            parameters.add(pair3);
            parameters.add(pair4);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse hr = hc.execute(httpPost);
            if (hr.getStatusLine().getStatusCode() == 200) {
                InputStream is = hr.getEntity().getContent();
                String request = utils.getTextFromStream(is);
                return request;
            } else {
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 刷新questionList
     */
    private void refreshquestionList(String msg) {
        try {
            jsonArray = new JSONArray(request);
            ArrayList<Question> tempList = new ArrayList<Question>();
            //遍历传入的jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                //读取相应内容
                int question_id = temp.getInt("question_id");
                String post_time = temp.getString("post_time");

                //设置值
                if (msg.equals(GET_QUESTION)) {
                    if (i == jsonArray.length() - 1) {
                        minPostTime = post_time;
                        Log.d("qin", "minPostTime = post_time执行");
                    }
                    if (isFirst) {
                        if (i == 0) {
                            maxPostTime = post_time;
                            Log.d("qin", "maxPostTime = post_time执行");
                        }
                        isFirst = false;
                    }
                } else {
                    if (i == 0) {
                        maxPostTime = post_time;
                        Log.d("qin", "maxPostTime = post_time执行");
                    }
                }
                String question_content = temp.getString("question_content");
                String left_url = temp.getString("left_url");
                String right_url = temp.getString("right_url");
                String quizzer_name = temp.getString("quizzer_name");
                String portrait_url = temp.getString("portrait_url");
                int share_count = temp.getInt("share_count");
                int comment_count = temp.getInt("comment_count");
                String comment = temp.getString("comment");
                Question Question = new Question(question_id, question_content,
                        left_url, right_url, quizzer_name,
                        portrait_url, share_count,
                        comment_count, comment, null, null);
                tempList.add(Question);
            }
            if (msg == REFRESH_QUESTION) {
                questionList.addAll(0, tempList);
            } else {
                questionList.addAll(tempList);
            }
            adpter.onDataChange(questionList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
