package com.qzct.immediatechoice.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.MyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;

public abstract class baseFragment extends Fragment {
    Activity context;
    MyApplication myApplication;
    public int current_page;
    String TAG = "qin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        myApplication = (MyApplication) context.getApplication();
        super.onCreate(savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    public abstract void initData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = initView(inflater, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (getView() != null) {
            getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
        super.setMenuVisibility(menuVisible);
    }


    public void initRefreshAndLoad(final ZrcListView listView, final MyCallback.InitRefreshAndLoadCallBack initRefreshAndLoadCallBack) {

        // 设置下拉刷新的样式
        SimpleHeader header = new SimpleHeader(context);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);
        // 设置加载更多的样式
        SimpleFooter footer = new SimpleFooter(context);
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);
        // 设置列表项出现动画
//        lv_home.setItemAnimForTopIn(R.anim.topitem_in);
//        lv_home.setItemAnimForBottomIn(R.anim.bottomitem_in);
        // 加载更多事件回调
        listView.startLoadMore();
        // 下拉刷新事件回调
        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
//                refresh();
                initRefreshAndLoadCallBack.refresh();
                // 加载更多事件回调
                listView.startLoadMore();
            }
        });
        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
//                loadMore();
                initRefreshAndLoadCallBack.loadMore();
            }
        });
        initRefreshAndLoadCallBack.refresh();
    }

    /**
     * 上拉加载
     */
    public void loadMore() {
    }

    /**
     * 下拉刷新
     */
    public void refresh() {

    }

    public void questionLoad(final ZrcListView listView, final List<Question> questionList, final BaseAdapter adpter, String type, final boolean isRefresh) {
        RequestParams entity = new RequestParams(Config.url_image_text);
        entity.addBodyParameter("type", type);
        if (isRefresh) {
            current_page = 1;
            entity.addBodyParameter("page", current_page + "");
        } else {
            current_page += 1;
//        entity.addBodyParameter("user_id", "0");
            entity.addBodyParameter("page", current_page + "");
        }
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "请求更新成功");
                if (result != null) {
                    if ("-1".equals(result)) {
                        listView.stopLoadMore();
                        return;
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        ArrayList<Question> tempList = new ArrayList<Question>();
                        //遍历传入的jsonArray
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = null;
                            temp = jsonArray.getJSONObject(i);
                            int question_id = temp.getInt("question_id");
//                            String post_time = temp.getString("post_time");
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
                        if (isRefresh) {
                            questionList.clear();
                            questionList.addAll(tempList);
                        } else {
                            questionList.addAll(tempList);
                        }
                        adpter.notifyDataSetChanged();
//                        adpter.onDataChange(questionList);
//                        questionList.addAll(0, tempList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError: " + ex.toString());
                Toast.makeText(context, "请求刷新错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.d(TAG, "请求更新结束");
//                        adpter.onDataChange(questionList);
            }
        });
    }

}
