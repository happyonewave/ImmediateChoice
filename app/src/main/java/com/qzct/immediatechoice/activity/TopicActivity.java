package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.TopicAdpter;
import com.qzct.immediatechoice.adpter.UserAdpter;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.Topic;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/14.
 */

public class TopicActivity extends Activity implements View.OnClickListener {
    private ImageView topic_img;
    private TextView topic_title;
    private Button btn_push;
    private Button btn_attention;
    private GridView gv_topic;
    private String[] topic_info;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_topic, "话题"));
        initView();
        initData();
    }

    private void initView() {
        topic_img = (ImageView) findViewById(R.id.topic_img);
        topic_title = (TextView) findViewById(R.id.topic_title);
        btn_push = (Button) findViewById(R.id.btn_push);
        btn_attention = (Button) findViewById(R.id.btn_attention);
        gv_topic = (GridView) findViewById(R.id.gv_topic);

    }

    private void initData() {
        topic_info = getIntent().getStringArrayExtra("topic_info");
//        Glide.with(this).load(topic_info[2]).into(topic_img);
        ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.notdata).setLoadingDrawableId(R.mipmap.notdata).build();
        x.image().bind(topic_img, topic_info[2], options);
        topic_title.setText(topic_info[1]);
        getTopicList();
        gv_topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionList.get(position);
                MyApplication.question = question;
                if (question.getLeft_url().contains("image")) {
                    MyApplication.isQuestion = true;
                } else {
                    MyApplication.isQuestion = false;
                }
                Intent intent = new Intent(TopicActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        });
        btn_push.setOnClickListener(this);
        btn_attention.setOnClickListener(this);
    }

    private void getTopicList() {
        RequestParams entity = new RequestParams(Config.url_topic);
        entity.addBodyParameter("topic_id", topic_info[0]);
        Log.d("qin", "topic_id: " + topic_info[0]);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("qin", "result: " + result);
                if (result != null) {
                    try {
                        questionList = new ArrayList<Question>();
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Question question = Question.jsonObjectToQuestion(jsonArray.getJSONObject(i));
                            Log.d("qin", "question: " + jsonArray.getJSONObject(i).toString());
                            questionList.add(question);
                        }
                        Log.d("qin", "questionList: " + questionList.toString());
                        gv_topic.setAdapter(new UserAdpter(TopicActivity.this, questionList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TopicActivity.this, "请求话题失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Toast.makeText(TopicActivity.this, "话题请求完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_push:
                Intent intent = new Intent(this, PushActivity.class);
                intent.putExtra("isImage", true);
                startActivity(intent);
                break;
            case R.id.btn_attention:
                Toast.makeText(this, "关注", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
