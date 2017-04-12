package com.qzct.immediatechoice.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.application.MyApplication;
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
 * Created by Administrator on 2017-03-05.
 */
public class AttentionFragment extends baseFragment {

    ListView lv_home_attention;
    private List<Topic> topicList;
    private View v;


    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.view_attention, null);
        lv_home_attention = (ListView) v.findViewById(R.id.lv_home_attention);
        return v;
    }

    @Override
    public void initdata() {
        TextView v = new TextView(context);
        v.setText("关注的话题");
        v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        v.setWidth(context.getWindowManager().getDefaultDisplay().getWidth());
        lv_home_attention.addHeaderView(v);
        getAttentionTopic();

    }

    private void getAttentionTopic() {
        RequestParams entity = new RequestParams(Config.url_topic);
        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("qin", "onSuccess ");
                if (result != null) {
                    try {
                        JSONArray resultJson = new JSONArray(result);
                        topicList = new ArrayList<Topic>();
                        for (int i = 0; i < resultJson.length(); i++) {
                            JSONObject temp = resultJson.getJSONObject(i);
                            Topic topic = Topic.jsonObjectToTotic(temp);
                            topicList.add(topic);
                        }
                        Log.d("qin", "onSuccess: topicList :" + topicList.toString());
                        lv_home_attention.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return topicList.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View v = null;
                                if (convertView != null) {
                                    v = convertView;
                                } else {
                                    v = View.inflate(context, R.layout.view_attention_item, null);
                                }

                                ImageView topic_img = (ImageView) v.findViewById(R.id.topic_img);
                                TextView topic_title = (TextView) v.findViewById(R.id.topic_title);

                                Topic i = topicList.get(position);

                                x.image().bind(topic_img, i.getTopic_img_url());
                                topic_title.setText(i.getTopic_title());
                                return v;
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "请求话题失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

}
