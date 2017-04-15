package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;
import com.tuyenmonkey.mkloader.MKLoader;

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
 * Created by qin on 2017/4/2.
 */

public class CircleFragment extends baseFragment implements ZrcListView.OnItemClickListener {
    private View view;
    private ZrcListView lv_circle;
    private MKLoader loader;

    private static final String GET_QUESTION = "1";
    private static final String REFRESH_QUESTION = "2";
    private static final String url = Config.url_image_text;
    private ArrayList<Question> questionlist = new ArrayList<Question>();
    private ImageTextAdpter adpter;
    private String minPostTime;
    private JSONArray jsonArray;
    private String request;
    private String maxPostTime;
    private boolean isFirst;


    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_circle, null);
        return view;
    }

    @Override
    public void initdata() {
        lv_circle = (ZrcListView) view.findViewById(R.id.lv_circle);
        loader = (MKLoader) view.findViewById(R.id.loader);
        lv_circle.setOnItemClickListener(this);
        setLoad(lv_circle);
        FirstLoadMoreTask firstLoadMoreTask = new FirstLoadMoreTask();
        firstLoadMoreTask.execute();

    }


    /**
     * 下拉刷新
     */
    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshTask refreshTask = new RefreshTask();
                refreshTask.execute();
                lv_circle.setRefreshSuccess("刷新成功");
            }
        }, 2000);

    }

    /**
     * 上拉加载
     */
    public void loadMore() {
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
        MyApplication.isQuestion = true;
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
                    adpter = new ImageTextAdpter(context, questionlist);
                    isFirst = true;
                    refreshQuestionList(GET_QUESTION);
                    lv_circle.setAdapter(adpter);

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
                    refreshQuestionList(REFRESH_QUESTION);
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
                    refreshQuestionList(GET_QUESTION);
                    lv_circle.setLoadMoreSuccess();
                } else {
                    lv_circle.stopLoadMore();

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
                    new BasicNameValuePair("type", "image");
            BasicNameValuePair pair3 =
                    new BasicNameValuePair("user_id", MyApplication.user.getUser_id() + "");
            BasicNameValuePair pair4 =
                    new BasicNameValuePair("startTime", startTime);
            BasicNameValuePair pair5 =
                    new BasicNameValuePair("endTime", endTime);
            parameters.add(pair1);
            parameters.add(pair2);
            parameters.add(pair3);
            parameters.add(pair4);
            parameters.add(pair5);
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
     * 刷新QuestionList
     */
    private void refreshQuestionList(String msg) {
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
                questionlist.addAll(0, tempList);
            } else {
                questionlist.addAll(tempList);
            }
            adpter.onDataChange(questionlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (lv_circle.getVisibility() == View.GONE) {
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    loader.setVisibility(View.GONE);
                    lv_circle.setVisibility(View.VISIBLE);
                }
            }.sendEmptyMessageDelayed(0, 2000);
        }
    }


}
