package com.qzct.immediatechoice.util;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.activity.UserInfoActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/16.
 */

public class Service {

    private Service() {
    }

    private static final Service single = new Service();

    //静态工厂方法
    public static Service getInstance() {
        return single;
    }


    /**
     * 获取好友信息
     */
    public void getFriendInfo(final MyCallback.FriendInfoCallBack friendInfoCallBack) {
        final List<User> userList = new ArrayList<User>();
        RequestParams entity = new RequestParams(Config.url_friend);
        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONArray friendArray = new JSONArray(result);
                        for (int i = 0; i < friendArray.length(); i++) {
                            JSONObject temp = friendArray.getJSONObject(i);
                            int user_id = temp.getInt("user_id");
                            String name = temp.getString("name");
                            String phone_number = temp.getString("phone_number");
                            String sex = temp.getString("sex");
                            String portrait_url = temp.getString("portrait_path");
                            User user = new User(user_id, name, phone_number, sex, portrait_url);
                            userList.add(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                friendInfoCallBack.onSuccess(userList);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                friendInfoCallBack.onError(ex);
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
     * 加载Question数据
     */
    public void loadQuestion(String type, final boolean isRefresh, final MyCallback.LoadQuestionCallBack loadQuestionCallBack) {
        RequestParams entity = new RequestParams(Config.url_image_text);
        int page = loadQuestionCallBack.getPage(isRefresh);
        int user_id = loadQuestionCallBack.getUserId();
        if (type != null) {
            entity.addBodyParameter("type", type);
        }
        if (user_id != 0) {
            entity.addBodyParameter("user_id", user_id + "");
        }
        entity.addBodyParameter("page", page + "");
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if ("-1".equals(result)) {
                        Log.d("loadQuestion_log", "请求无数据");
                        loadQuestionCallBack.onDataIsNull();
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
                        Log.d("loadQuestion_log", "请求更新完成");
                        loadQuestionCallBack.onSuccess(isRefresh, tempList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadQuestionCallBack.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.d("loadQuestion_log", "请求更新结束");
                loadQuestionCallBack.onFinished();
            }
        });
    }

    /**
     * 修改好友
     *
     * @param f_id
     */
    public void updateFriend(int f_id, final MyCallback.UpdateFriendCallback updateFriendCallback) {
        RequestParams entity = new RequestParams(Config.url_friend);
        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
        entity.addBodyParameter("f_id", f_id + "");
        String update_type = updateFriendCallback.getUpdateType();
        if ("delete".equals(update_type)) {
            entity.addBodyParameter("update_type", update_type);
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("qin", "result: " + result);
                updateFriendCallback.onSuccess(result);
//                Toast.makeText(UserInfoActivity.this, result, Toast.LENGTH_SHORT).show();
//                setResult(RESULT_OK, new Intent());
//                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                updateFriendCallback.onError(ex);
//                Toast.makeText(UserInfoActivity.this, "请求加好友失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

//    public void getQuestionnaire(int questionnaire_id,)
}
