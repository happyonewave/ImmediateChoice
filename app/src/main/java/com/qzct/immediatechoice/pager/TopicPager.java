package com.qzct.immediatechoice.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.TopicAdpter;
import com.qzct.immediatechoice.domain.Topic;
import com.qzct.immediatechoice.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qin on 2017/3/11.
 */

public class TopicPager extends BasePager implements AdapterView.OnItemClickListener {

    private static final String TAG = "tag";
    private List<Topic> topicList;
    private JSONArray jsonArray;
    private TopicAdpter adpter;
    private GridView gv_class;

    public TopicPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        view = inflater.inflate(R.layout.view_topic, null);
        return view;
    }

    @Override
    public void initData() {
        gv_class = (GridView) view.findViewById(R.id.gv_class);
        gv_class.setOnItemClickListener(this);
        getTopicListfromServer();
//        gv_class.setAdapter(adpter);
    }


    /**
     * 从服务器获取topicList
     */
    private void getTopicListfromServer() {
        RequestParams entity = new RequestParams(Config.url_topic);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {

                    if (result != "-1") {
                        try {
                            jsonArray = new JSONArray(result);
                            refreshTopicList();
                        } catch (JSONException e) {
                            Log.d(TAG, "不是合法的json");
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "没有获取到", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
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
     * 刷新TopicList
     */
    private void refreshTopicList() {
        topicList = new ArrayList<Topic>();
        try {
//            jsonArray = new JSONArray();
            //遍历传入的jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                //读取相应内容.
                String topic_title = temp.getString("topic_title");
                String topic_img_url = temp.getString("topic_img_url");
                Topic topic = new Topic(topic_title, topic_img_url);
                topicList.add(topic);
            }
            adpter = new TopicAdpter(context,topicList);
            Log.d(TAG, "adpter new成功 ");
            gv_class.setAdapter(adpter);
            Log.d(TAG, "setAdapter成功 ");
//            adpter.onDataChange();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * GridView的item监听事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(context, "点击了：" + i, Toast.LENGTH_SHORT).show();
    }
}
