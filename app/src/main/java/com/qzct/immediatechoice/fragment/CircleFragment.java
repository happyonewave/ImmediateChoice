package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.adpter.CircleAdpter;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;

import java.util.ArrayList;

import zrc.widget.ZrcListView;


/**
 * Created by qin on 2017/4/2.
 */

public class CircleFragment extends BaseFragment implements ZrcListView.OnItemClickListener {

    //    private static final String GET_QUESTION = "1";
//    private static final String REFRESH_QUESTION = "2";
//    private String minPostTime;
//    private String request;
//    private String maxPostTime;
//    private boolean isFirst;
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private CircleAdpter adpter;
    private JSONArray jsonArray;
    private View view;
    private ZrcListView lv_circle;
    private MKLoader loader;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_circle, null);
        return view;
    }


    @Override
    public void initData() {
        lv_circle = (ZrcListView) view.findViewById(R.id.lv_circle);
        loader = (MKLoader) view.findViewById(R.id.loader);
        lv_circle.setOnItemClickListener(this);
        adpter = new CircleAdpter(context, questionList);
        lv_circle.setAdapter(adpter);
        initRefreshAndLoad(lv_circle, new MyCallback.InitRefreshAndLoadCallBack() {
            @Override
            public void refresh() {

//                loadQuestion("image", true);
                Service.getInstance().loadQuestion(null, true, new MyLoadQuestionCallBack());
                lv_circle.setRefreshSuccess("刷新成功");
            }

            @Override
            public void loadMore() {

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        loadQuestion("image", false);
                        Service.getInstance().loadQuestion(null, false, new MyLoadQuestionCallBack());
                        lv_circle.setLoadMoreSuccess();
                    }
                }.sendEmptyMessageDelayed(0, 4000);
            }
        });

    }


    private class MyLoadQuestionCallBack implements MyCallback.LoadQuestionCallBack {
        @Override
        public int getPage(boolean isRefresh) {
            if (isRefresh) {
                current_page = 1;
            } else {
                current_page += 1;
            }
            return current_page;
        }

        @Override
        public int getUserId() {
            return MyApplication.user.getUser_id();
        }

        @Override
        public void onSuccess(boolean isRefresh, ArrayList<Question> list) {
            if (isRefresh) {
                questionList.clear();
                questionList.addAll(list);
            } else {
                questionList.addAll(list);
            }
            adpter.notifyDataSetChanged();
        }

        @Override
        public void onDataIsNull() {
            lv_circle.stopLoadMore();
        }

        @Override
        public void onError(Throwable ex) {
            Log.d(TAG, "onError: " + ex.toString());
            Toast.makeText(context, "请求刷新错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinished() {
//                        adpter.onDataChange(questionList);
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

        @Override
        public boolean IsNotGroup() {
            return false;
        }
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
//        MyApplication.question = question;
        MyApplication.isQuestion = true;
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("question", question);
        context.startActivity(intent);
        Toast.makeText(context, "点击了item" + i, Toast.LENGTH_LONG).show();
    }
    /**
     * 加载数据
     */
//
//    private void loadQuestion(String type, final boolean isRefresh) {
//        RequestParams entity = new RequestParams(Config.url_image_text);
////        entity.addBodyParameter("type", type);
//        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
//        if (isRefresh) {
//            current_page = 1;
//            entity.addBodyParameter("page", current_page + "");
//        } else {
//            current_page += 1;
//            entity.addBodyParameter("page", current_page + "");
//        }
////        http://192.168.1.200:8080/Server/ImageTextServlet?type=image&user_id=1&page=6
//        x.http().get(entity, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d(TAG, "请求更新成功");
//                if (result != null) {
//                    if ("-1".equals(result)) {
//                        lv_circle.stopLoadMore();
//                        return;
//                    }
//                    try {
//                        jsonArray = new JSONArray(result);
//                        ArrayList<Question> tempList = new ArrayList<Question>();
//                        //遍历传入的jsonArray
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject temp = null;
//                            temp = jsonArray.optJSONObject(i);
//                            int question_id = temp.optInt()("question_id");
////                            String post_time = temp.getString("post_time");
//                            String question_content = temp.getString("question_content");
//                            String left_url = temp.getString("left_url");
//                            String right_url = temp.getString("right_url");
//                            String quizzer_name = temp.getString("quizzer_name");
//                            String portrait_url = temp.getString("portrait_url");
//                            int share_count = temp.optInt()("share_count");
//                            int comment_count = temp.optInt()("comment_count");
//                            String comment = temp.getString("comment");
//                            Question Question = new Question(question_id, question_content,
//                                    left_url, right_url, quizzer_name,
//                                    portrait_url, share_count,
//                                    comment_count, comment, null, null);
//                            tempList.add(Question);
//                        }
//                        if (isRefresh) {
//                            questionList.clear();
//                            questionList.addAll(tempList);
//                        } else {
//                            questionList.addAll(tempList);
//                        }
//                        adpter.notifyDataSetChanged();
////                        adpter.onDataChange(questionList);
////                        questionList.addAll(0, tempList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d(TAG, "onError: " + ex.toString());
//                Toast.makeText(context, "请求刷新错误", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                Log.d(TAG, "请求更新结束");
////                        adpter.onDataChange(questionList);
//                if (lv_circle.getVisibility() == View.GONE) {
//                    new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            loader.setVisibility(View.GONE);
//                            lv_circle.setVisibility(View.VISIBLE);
//                        }
//                    }.sendEmptyMessageDelayed(0, 2000);
//                }
//            }
//        });
//    }


    /**
     * 第一次加载
     */
//
//    class FirstLoadMoreTask extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            //返回获取的jasonArray 2017-03-20 21:25:53.0
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date now = new Date();
//            String startTime = format.format(now);
//            request = getQuestionJson(GET_QUESTION, startTime, Config.unixTime_min);
//            return request;
//        }
//
//        @Override
//        protected void onPostExecute(String request) {
//            if (request != null) {
//                if (!request.equals("-1")) {
//                    adpter = new ImageTextAdpter(context, questionList);
//                    isFirst = true;
//                    refreshQuestionList(GET_QUESTION);
//                    lv_circle.setAdapter(adpter);
//
//                } else {
//                    Toast.makeText(context, "已刷新为最新数据", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }
//    }

    /**
     * 刷新数据
     */
//
//    class RefreshTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            //返回获取的jasonArray 2017-03-20 21:25:53.0
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date now = new Date();
//            String startTime = format.format(now);
//            request = getQuestionJson(REFRESH_QUESTION, startTime, maxPostTime);
//            return request;
//        }
//
//        @Override
//        protected void onPostExecute(String request) {
//            if (request != null) {
//                if (!request.equals("-1")) {
//                    refreshQuestionList(REFRESH_QUESTION);
//                } else {
//                    Toast.makeText(context, "已刷新为最新数据", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }
//    }

    /**
     * 加载数据
     */
//
//    class LoadMoreTask extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            //返回获取的jasonArray 北京时间1970年01月01日08时00分00秒
//            request = getQuestionJson(GET_QUESTION, minPostTime, Config.unixTime_min);
//            return request;
//        }
//
//        @Override
//        protected void onPostExecute(String request) {
//            if (request != null) {
//                if (!request.equals("-1")) {
//                    refreshQuestionList(GET_QUESTION);
//                    lv_circle.setLoadMoreSuccess();
//                } else {
//                    lv_circle.stopLoadMore();
//
//                    Toast.makeText(context, "已加载所有数据", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    /**
     * 请求数据
     *
     * @param startTime
     * @return
     */
//
//    private String getQuestionJson(String msg, String startTime, String endTime) {
//        HttpClient hc = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//        try {
//            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//            BasicNameValuePair pair1 =
//                    new BasicNameValuePair("msg", msg);
//            BasicNameValuePair pair2 =
//                    new BasicNameValuePair("type", "image");
//            BasicNameValuePair pair3 =
//                    new BasicNameValuePair("user_id", MyApplication.user.getUser_id() + "");
//            BasicNameValuePair pair4 =
//                    new BasicNameValuePair("startTime", startTime);
//            BasicNameValuePair pair5 =
//                    new BasicNameValuePair("endTime", endTime);
//            parameters.add(pair1);
//            parameters.add(pair2);
//            parameters.add(pair3);
//            parameters.add(pair4);
//            parameters.add(pair5);
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
//            httpPost.setEntity(entity);
//            HttpResponse hr = hc.execute(httpPost);
//            if (hr.getStatusLine().getStatusCode() == 200) {
//                InputStream is = hr.getEntity().getContent();
//                String request = Utils.getTextFromStream(is);
//                return request;
//            } else {
//                return "0";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return e.toString();
//        }
//    }

    /**
     * 刷新QuestionList
     */
//
//    private void refreshQuestionList(String msg) {
//        try {
//            jsonArray = new JSONArray(request);
//            ArrayList<Question> tempList = new ArrayList<Question>();
//            //遍历传入的jsonArray
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject temp = jsonArray.optJSONObject(i);
//                //读取相应内容
//                int question_id = temp.optInt()("question_id");
//                String post_time = temp.getString("post_time");
//
//                //设置值
//                if (msg.equals(GET_QUESTION)) {
//                    if (i == jsonArray.length() - 1) {
//                        minPostTime = post_time;
//                        Log.d("qin", "minPostTime = post_time执行");
//                    }
//                    if (isFirst) {
//                        if (i == 0) {
//                            maxPostTime = post_time;
//                            Log.d("qin", "maxPostTime = post_time执行");
//                        }
//                        isFirst = false;
//                    }
//                } else {
//                    if (i == 0) {
//                        maxPostTime = post_time;
//                        Log.d("qin", "maxPostTime = post_time执行");
//                    }
//                }
//                String question_content = temp.getString("question_content");
//                String left_url = temp.getString("left_url");
//                String right_url = temp.getString("right_url");
//                String quizzer_name = temp.getString("quizzer_name");
//                String portrait_url = temp.getString("portrait_url");
//                int share_count = temp.optInt()("share_count");
//                int comment_count = temp.optInt()("comment_count");
//                String comment = temp.getString("comment");
//                Question Question = new Question(question_id, question_content,
//                        left_url, right_url, quizzer_name,
//                        portrait_url, share_count,
//                        comment_count, comment, null, null);
//                tempList.add(Question);
//            }
//            if (msg == REFRESH_QUESTION) {
//                questionList.addAll(0, tempList);
//            } else {
//                questionList.addAll(tempList);
//            }
//            adpter.onDataChange(questionList);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (lv_circle.getVisibility() == View.GONE) {
//            new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    loader.setVisibility(View.GONE);
//                    lv_circle.setVisibility(View.VISIBLE);
//                }
//            }.sendEmptyMessageDelayed(0, 2000);
//        }
//    }


}
