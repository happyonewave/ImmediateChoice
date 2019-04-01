package com.qzct.immediatechoice.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.activity.UserInfoActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.Questionnaire;
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
                    Log.d("qin:", "friendInfo: " + result);
                    try {
                        JSONArray friendArray = new JSONArray(result);
                        for (int i = 0; i < friendArray.length(); i++) {
                            JSONObject temp = friendArray.optJSONObject(i);
                            int user_id = temp.optInt("user_id");
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
     * 获取陌生人(非好友用户)信息
     */
    public void getOtherInfo(final MyCallback.OtherInfoCallBack otherInfoCallBack) {
        final List<User> userList = new ArrayList<User>();
        RequestParams entity = new RequestParams(Config.url_search);
       final String userId =   otherInfoCallBack.getUserId();
        entity.addBodyParameter("f_id", otherInfoCallBack.getUserId());
        x.http().post(entity, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null && !"[]".equals(result)) {
                    Log.d("qin", "result:   " + result);
                    try {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            User user = User.jsonObjectToUser(array.optJSONObject(i));
                            if (user.getUser_id() == Integer.parseInt(userId)) {
                                otherInfoCallBack.onSuccess(user);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                otherInfoCallBack.onError(ex);
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
                            temp = jsonArray.optJSONObject(i);
                            int question_id = temp.optInt("question_id");
//                            String post_time = temp.getString("post_time");
                            String question_content = temp.getString("question_content");
                            String left_url = temp.getString("left_url");
                            String right_url = temp.getString("right_url");
                            String quizzer_name = temp.getString("quizzer_name");
                            String portrait_url = temp.getString("portrait_url");
                            int share_count = temp.optInt("share_count");
                            int comment_count = temp.optInt("comment_count");
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






    /**
     * 登录
     *
     */
    public void login(User user, final MyCallback.LoginCallback loginCallback) {
        final RequestParams entity = new RequestParams(Config.url_login);
        entity.addBodyParameter("name",user.getUsername());
        entity.addBodyParameter("password",user.getPassword());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    loginCallback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loginCallback.onError(ex);
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
     * 获取问卷
     *
     * @param getQuestionnaireCallback
     */
    public void getQuestionnaire(final MyCallback.GetQuestionnaireCallback getQuestionnaireCallback) {
        final RequestParams entity = new RequestParams(Config.url_questionnaire);
        final int questionnaire_id = getQuestionnaireCallback.getQuestionnaireId();
        int user_id = getQuestionnaireCallback.getUserId();
        if (questionnaire_id != 0) {
            entity.addBodyParameter("questionnaire_id", questionnaire_id + "");
        } else {
            entity.addBodyParameter("user_id", user_id + "");
        }
        entity.addBodyParameter("type", "getQuestionnaire");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        Log.d("qin", "result: " + result);
                        JSONArray jsonArray = new JSONArray(result);
                        List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Questionnaire questionnaire = Questionnaire.jsonObjectToQuestionnaire(jsonArray.optJSONObject(i));
                            Log.d("qin", "questionnaire: " + jsonArray.optJSONObject(i));
                            questionnaireList.add(questionnaire);
                        }
                        if (questionnaire_id != 0) {
                            getQuestionnaireCallback.onSuccess(questionnaireList.get(0));
                            return;
                        }
                        getQuestionnaireCallback.onSuccess(questionnaireList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getQuestionnaireCallback.onError(ex);
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

    /**
     * 发布问卷
     *
     * @param questionnaire
     * @param pushQuestionnaireCallback
     * @throws JSONException
     */
    public void pushQuestionnaire(Questionnaire questionnaire, final MyCallback.PushQuestionnaireCallback pushQuestionnaireCallback) {
        JSONObject questionnaireObject = null;
        try {
            questionnaireObject = questionnaire.questionnaireToJSONObject(questionnaire);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams entity = new RequestParams(Config.url_questionnaire);
        entity.addBodyParameter("type", "pushQuestionnaire");
        entity.addBodyParameter("questionnaireObject", questionnaireObject.toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if ("1".equals(result)) {
                        pushQuestionnaireCallback.onSuccess();
                    } else {
                        pushQuestionnaireCallback.onFailure();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pushQuestionnaireCallback.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void getQuestionnaireIds(final MyCallback.GetQuestionnaireIdsCallback getQuestionnaireIdsCallback) {
        RequestParams entity = new RequestParams(Config.url_questionnaire);
        entity.addBodyParameter("type", "getQuestionnaireIds");
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONArray array = new JSONArray(result);
                        List<Integer> idList = new ArrayList<Integer>();
                        for (int i = 0; i < array.length(); i++) {
                            idList.add(array.getInt(i));
                        }
                        getQuestionnaireIdsCallback.onSuccess(idList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getQuestionnaireIdsCallback.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void chooseQuestionOption(List<Questionnaire.Choice> choiceList, final MyCallback.ChooseQuestionOptionCallback chooseQuestionOptionCallback) {
        RequestParams entity = new RequestParams(Config.url_questionnaire);
        entity.addBodyParameter("type", "chooseQuestionOption");
        JSONArray choice = new JSONArray();
        for (Questionnaire.Choice choice1 : choiceList) {
            JSONObject temp = new JSONObject();
            int questionnaire_question_id = choice1.getQuestionnaire_question_id();
            String num = choice1.getNum();
            int user_id = choice1.getUser_id();
            try {
                temp.put("questionnaire_question_id", questionnaire_question_id);
                temp.put("num", num);
                temp.put("user_id", user_id);
                choice.put(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        entity.addBodyParameter("choice", choice.toString());
        Log.d("qin", "choice: " + choice.toString());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if ("1".equals(result)) {
                        chooseQuestionOptionCallback.onSuccess();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                chooseQuestionOptionCallback.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void gradeAdd(Activity context, int num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("grade", Context.MODE_PRIVATE);
        int userGrade = sharedPreferences.getInt("grade", 0);
        userGrade += num;
        sharedPreferences.edit().putInt("grade", userGrade).commit();
    }
}
