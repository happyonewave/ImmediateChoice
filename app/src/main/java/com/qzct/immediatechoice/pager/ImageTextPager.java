package com.qzct.immediatechoice.pager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.dialog.Comment_dialog;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.qzct.immediatechoice.application.MyApplication.itemData;


/**
 * Created by Administrator on 2017-03-05.
 */
public class ImageTextPager extends BasePager implements AdapterView.OnItemClickListener {


    private static String GET_MAX_ID = "0";
    private static final String GET_QUESTION = "1";
    private static final String REFRESH_QUESTION = "2";
    private static final String TAG = "VideoPager";
    private static final String url = MyApplication.url_image_text;
    private ListView lv_home;
    private TwinklingRefreshLayout home_refreshLayout;
    private ArrayList<Question> questionlist = new ArrayList<Question>();
    private ImageTextAdpter adpter;
    private int questionId;
    private JSONArray jsonArray;
    private int maxId;
    private String request;


    public ImageTextPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        view = inflater.inflate(R.layout.view_image_text, null);
        return view;
    }


    @Override
    public void initData() {
        lv_home = (ListView) view.findViewById(R.id.lv_home);
        sendFabIsVisible(lv_home);
        lv_home.setOnItemClickListener(this);
        home_refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.home_refreshLayout);
        SinaRefreshView sinaRefreshView = new SinaRefreshView(context);
        home_refreshLayout.setHeaderView(sinaRefreshView);
        //监听下拉，上拉事件
        home_refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {

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
                        home_refreshLayout.finishRefreshing();
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
                                new UpdateFromJsonArrayTask(context, url, questionId);
                        updateFromJsonArrayTask.execute();
                        home_refreshLayout.finishLoadmore();
                    }
                }, 2000);


            }
        });
        GetMaxIdTask getMaxIdTask = new GetMaxIdTask(url);
        getMaxIdTask.execute();


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemData = getItemData(view);
        Dialog dialog = new Comment_dialog(context, R.style.comment_Dialog);
        dialog.show();
//        Intent intent = new Intent(context, CommentActivity.class);
//        context.startActivity(intent);

        Toast.makeText(context, "点击了item" + i, Toast.LENGTH_LONG).show();
    }


    private ItemData getItemData(View view) {

        TextView tv_question = (TextView) view.findViewById(R.id.tv_question);    //拿到相应的View对象
        SmartImageView image_text_item_img_left = (SmartImageView) view.findViewById(R.id.image_text_item_img_left);
        SmartImageView image_text_item_img_right = (SmartImageView) view.findViewById(R.id.image_text_item_img_right);
        TextView item_username = (TextView) view.findViewById(R.id.item_username);
        ImageView item_portrait = (ImageView) view.findViewById(R.id.item_portrait);
        Button comment_icon = (Button) view.findViewById(R.id.comment_icon);
        Button share_icon = (Button) view.findViewById(R.id.share_icon);
        TextView item_comment = (TextView) view.findViewById(R.id.item_comment);

        String question_content = tv_question.getText().toString();
        Drawable image_left = (Drawable) image_text_item_img_left.getDrawable();
        Drawable image_right = (Drawable) image_text_item_img_right.getDrawable();
        String username = item_username.getText().toString();
        Drawable portrait = (Drawable) item_portrait.getDrawable();
        String comment_num = comment_icon.getText().toString();
        String share_num = share_icon.getText().toString();
        String comment = item_comment.getText().toString();
        int question_id = 0;
        for (Question question : questionlist) {
            if (question.getQuizzer_name().equals(username) && question.getQuestion_content().equals(question_content)) {
                question_id = question.getQuestion_id();
                Toast.makeText(context, "取到的question_id：" + question_id, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        ItemData itemData = new ItemData(question_id, question_content, image_left, image_right, username, portrait,
                comment_num, share_num, comment);
//        image_text_item_img_left.setImageDrawable(image_text_item_img_right.getDrawable());
        return itemData;
    }

    public static class ItemData implements Serializable {
        int question_id;
        String question_content;
        Drawable image_left;
        Drawable image_right;
        String username;
        Drawable portrait;
        String comment_num;
        String share_num;
        String comment;

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public ItemData(int question_id, String question_content, Drawable image_left, Drawable image_right, String username, Drawable portrait, String comment_num, String share_num, String comment) {
            this.question_id = question_id;
            this.question_content = question_content;
            this.image_left = image_left;
            this.image_right = image_right;
            this.username = username;
            this.portrait = portrait;
            this.comment_num = comment_num;
            this.share_num = share_num;
            this.comment = comment;
        }

        public ItemData(String question_content, Drawable image_left, Drawable image_right, String username, Drawable portrait, String comment_num, String share_num, String comment) {
            this.question_content = question_content;
            this.image_left = image_left;
            this.image_right = image_right;
            this.username = username;
            this.portrait = portrait;
            this.comment_num = comment_num;
            this.share_num = share_num;
            this.comment = comment;
        }

        public String getQuestion_content() {
            return question_content;
        }

        public void setQuestion_content(String question_content) {
            this.question_content = question_content;
        }

        public Drawable getImage_left() {
            return image_left;
        }

        public void setImage_left(Drawable image_left) {
            this.image_left = image_left;
        }

        public Drawable getImage_right() {
            return image_right;
        }

        public void setImage_right(Drawable image_right) {
            this.image_right = image_right;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Drawable getPortrait() {
            return portrait;
        }

        public void setPortrait(Drawable portrait) {
            this.portrait = portrait;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getShare_num() {
            return share_num;
        }

        public void setShare_num(String share_num) {
            this.share_num = share_num;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
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
                questionId = Integer.parseInt(id);
                maxId = questionId;
                ShowFromJsonArrayTask showFromJsonArrayTask =
                        new ShowFromJsonArrayTask(context, lv_home, url, questionId);
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
                    lv_home.setAdapter(adpter);
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
                int question_id = temp.getInt("question_id");

                //设置值
                if (msg == REFRESH_QUESTION) {
                    if (i == jsonArray.length() - 1) {
                        maxId = question_id;
                        Log.d(TAG, "maxId: " + maxId);
                    }
                } else {
                    if (i == 0) {
                        questionId = question_id - 1;
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
                Question Question = new Question(question_id, question_content,
                        image_left, image_right, quizzer_name,
                        quizzer_portrait, share_count,
                        comment_count, comment, null);
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
