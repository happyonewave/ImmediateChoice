package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.qzct.immediatechoice.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.domain.Question;
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
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017-03-05.
 */
public class VideoPager extends BasePager {


    private static String GET_MAX_ID = "0";
    private static final String GET_QUESTION = "1";
    private static final String REFRESH_QUESTION = "2";
    private static final String TAG = "VideoPager";
    private static final String url = MyApplication.url_image_text;
    private ListView lv_home_video;
    private TwinklingRefreshLayout home_video_refreshLayout;
    private ArrayList<Question> questionlist = new ArrayList<Question>();
    private ImageTextAdpter adpter;
    private int questiobId;
    private JSONArray jsonArray;
    private int maxId;
    private String request;


    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_video, null);
        return view;
    }


    @Override
    public void initData() {
        lv_home_video = (ListView) view.findViewById(R.id.lv_home_video);
        sendFabIsVisible(lv_home_video);
//        lv_home_video.setOnItemClickListener(this);
        home_video_refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.home_video_refreshLayout);
        SinaRefreshView sinaRefreshView = new SinaRefreshView(context);
        home_video_refreshLayout.setHeaderView(sinaRefreshView);
        //监听下拉，上拉事件
        home_video_refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {

            //下拉刷新事件
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "刷新时maxId: " + maxId);
                        RefreshFromJsonArrayTask refreshFromJsonArrayTask =
                                new RefreshFromJsonArrayTask(context, url, maxId);
                        refreshFromJsonArrayTask.execute();
                        home_video_refreshLayout.finishRefreshing();
                    }
                }, 2000);


            }

            //上拉加载事件
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateFromJsonArrayTask updateFromJsonArrayTask =
                                new UpdateFromJsonArrayTask(context, url, questiobId);
                        updateFromJsonArrayTask.execute();
                        home_video_refreshLayout.finishLoadmore();
                    }
                }, 2000);


            }
        });
        GetMaxIdTask getMaxIdTask = new GetMaxIdTask(url);
        getMaxIdTask.execute();


    }

    /**
     * 拿到最大的Id
     */
    private void getMaxId() {
        RequestParams entity = new RequestParams(url);
        entity.addBodyParameter("msg", GET_MAX_ID);
        x.http().post(entity, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                maxId = Integer.parseInt(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }


        });
    }

    /**
     * 拿到最大的Id并第一次请求
     */
    class GetMaxIdTask extends AsyncTask<String, String, String> {

        String url;

        public GetMaxIdTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            try {
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                BasicNameValuePair pair1 =
                        new BasicNameValuePair("msg", GET_MAX_ID);
                parameters.add(pair1);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                httpPost.setEntity(entity);
                HttpResponse hr = hc.execute(httpPost);
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    String id = utils.getTextFromStream(is);
                    return id;
                } else {
                    return "0";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String id) {
            if (id != "0") {
                Log.d(TAG, "获取到的maxId：" + id);
                questiobId = Integer.parseInt(id);
                maxId = questiobId;
                ShowFromJsonArrayTask showFromJsonArrayTask =
                        new ShowFromJsonArrayTask(context, lv_home_video, url, questiobId);
                showFromJsonArrayTask.execute();


            } else {
            }

        }
    }

    /**
     * 获取数据
     */
    class ShowFromJsonArrayTask extends AsyncTask<String, String, String> {

        String spec;
        Context context;
        ListView listView;
        int startId;

        public ShowFromJsonArrayTask(Context context, ListView listView, String spec, int startId) {
            this.context = context;
            this.listView = listView;
            this.spec = spec;
            this.startId = startId;
        }

        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray
            request = getQuestionJson(GET_QUESTION, startId);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (request != null) {

                if (!request.equals("-1")) {
                    Log.d(TAG, "第一次获取的的jsonArray: " + jsonArray);
                    adpter = new ImageTextAdpter(context, questionlist);
                    refreshQuestionList(GET_QUESTION);
                    lv_home_video.setAdapter(adpter);
                } else {
                    Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    /**
     * 刷新数据
     */
    class RefreshFromJsonArrayTask extends AsyncTask<String, String, String> {

        String spec;
        Context context;
        int startId;

        public RefreshFromJsonArrayTask(Context context, String spec, int startId) {
            this.context = context;
            this.spec = spec;
            this.startId = startId;
            Log.d(TAG, "刷新的startId: " + startId);
        }

        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray
            request = getQuestionJson(REFRESH_QUESTION, startId);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (jsonArray != null) {
                if (!request.equals("-1")) {
                    Log.d(TAG, "刷新的jsonArroy: " + jsonArray);
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
    class UpdateFromJsonArrayTask extends AsyncTask<String, String, String> {

        String spec;
        Context context;
        int startId;

        public UpdateFromJsonArrayTask(Context context, String spec, int startId) {
            this.context = context;
            this.spec = spec;
            this.startId = startId;
            Log.d(TAG, "更新的startId: " + startId);
        }

        @Override
        protected String doInBackground(String... params) {
            //返回获取的jasonArray
            request = getQuestionJson(GET_QUESTION, startId);
            return request;
        }

        @Override
        protected void onPostExecute(String request) {
            if (request != null) {
                if (!request.equals("-1")) {
                    Log.d(TAG, "更新的jsonArroy: " + jsonArray);
                    refreshQuestionList(GET_QUESTION);
                } else {
                    Toast.makeText(context, "已加载所有数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 刷新QuestionList
     */
    private void refreshQuestionList(String msg) {
        try {
            jsonArray = new JSONArray(request);
            //遍历传入的jsonArray
            for (int i = jsonArray.length() - 1; i > -1; i--) {
                JSONObject temp = jsonArray.getJSONObject(i);
                //读取相应内容
                //设置值
                if (msg == REFRESH_QUESTION) {
                    if (i == jsonArray.length() - 1) {
                        maxId = temp.getInt("id");
                        Log.d(TAG, "maxId: " + maxId);
                    }
                } else {
                    if (i == 0) {
                        questiobId = temp.getInt("id") - 1;
                    }
                }
                String question_content = temp.getString("question_content");
                String image_left = temp.getString("image_left");
                String image_right = temp.getString("image_right");
                String quizzer_name = temp.getString("quizzer_name");
                String quizzer_portrait = temp.getString("quizzer_portrait");
                int share_count = temp.getInt("share_count");
                int comment_count = temp.getInt("comment_count");
                String comment = temp.getString("comment");
                Question Question = new Question(question_content, image_left, image_right, quizzer_name, quizzer_portrait, share_count, comment_count, comment, null);
                if (msg == REFRESH_QUESTION) {
                    questionlist.add(0, Question);
                } else {
                    questionlist.add(Question);
                }
            }
            adpter.onDataChange(questionlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求数据
     *
     * @param startId
     * @return
     */
    private String getQuestionJson(String msg, int startId) {
        HttpClient hc = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            BasicNameValuePair pair1 =
                    new BasicNameValuePair("msg", msg);
            BasicNameValuePair pair2 =
                    new BasicNameValuePair("startId", String.valueOf(startId));
            parameters.add(pair1);
            parameters.add(pair2);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse hr = hc.execute(httpPost);
            if (hr.getStatusLine().getStatusCode() == 200) {
                InputStream is = hr.getEntity().getContent();
                String request = utils.getTextFromStream(is);
                return request;
            } else {
//                    return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
