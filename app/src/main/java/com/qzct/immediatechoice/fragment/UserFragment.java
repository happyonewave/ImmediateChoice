package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jrmf360.rylib.JrmfClient;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.activity.ConversationListActivity;
import com.qzct.immediatechoice.activity.GradeActivity;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.activity.PushQuestionnaireActivity;
import com.qzct.immediatechoice.activity.QuestionnaireActivity;
import com.qzct.immediatechoice.activity.SettingActivity;
import com.qzct.immediatechoice.activity.TopicActivity;
import com.qzct.immediatechoice.activity.UserInfoActivity;
import com.qzct.immediatechoice.adpter.TopicAdpter;
import com.qzct.immediatechoice.adpter.TopicRecyclerAdpter;
import com.qzct.immediatechoice.adpter.UserAdpter;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.Topic;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserFragment extends baseFragment implements View.OnClickListener {
    private View v;
    List<Question> questionList;
    GridView lv;
    RecyclerView rv_attention;
    User user;
    private String portrait_path;
    private TextView tv_username;
    private ImageView blurImageView;
    private ImageView user_portrait;
    private ImageView bt_setting;
    private Button hint_mypush;
    private ImageView iv_userinfo;
    private LinearLayout user_sign;
    private LinearLayout user_wallet;
    //    private HintPopupWindow hintPopupWindow;
//    private LinearLayout user_questionnaire;
//    private LinearLayout user_data;
    private LinearLayout user_layout;
    private ArrayList<Topic> topicList;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        v = v.inflate(context, R.layout.fragment_user, null);
        user = MyApplication.user;
        tv_username = (TextView) v.findViewById(R.id.user_tv_username);
        blurImageView = (ImageView) v.findViewById(R.id.iv_bg);
        user_portrait = (ImageView) v.findViewById(R.id.user_portrait);
        user_layout = (LinearLayout) v.findViewById(R.id.user_layout);
        lv = (GridView) v.findViewById(R.id.gv_user);
        rv_attention = (RecyclerView) v.findViewById(R.id.rv_attention);
        bt_setting = (ImageView) v.findViewById(R.id.bt_setting);
        iv_userinfo = (ImageView) v.findViewById(R.id.iv_userinfo);
        hint_mypush = (Button) v.findViewById(R.id.hint_mypush);
        user_sign = (LinearLayout) v.findViewById(R.id.user_sign);
        user_wallet = (LinearLayout) v.findViewById(R.id.user_wallet);
//        user_questionnaire = (LinearLayout) v.findViewById(R.id.user_questionnaire);
//        user_data = (LinearLayout) v.findViewById(R.id.user_data);
        return v;
    }

    @Override
    public void initData() {
        portrait_path = user.getPortrait_path();
        Glide.with(this).load(portrait_path)
                .bitmapTransform(new BlurTransformation(context, 25), new CenterCrop(context))
                .into(blurImageView);


        Glide.with(this).load(portrait_path)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(user_portrait);

        tv_username.setText(user.getUsername());
//        user_portrait.setOnClickListener(this);
        bt_setting.setOnClickListener(this);
        iv_userinfo.setOnClickListener(this);
        user_sign.setOnClickListener(this);
        user_wallet.setOnClickListener(this);
        user_layout.setOnClickListener(this);
//        user_questionnaire.setOnClickListener(this);
//        user_data.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionList.get(position);
//                MyApplication.question = question;
                if (question.getLeft_url().contains("image")) {
                    MyApplication.isQuestion = true;
                } else {
                    MyApplication.isQuestion = false;
                }
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("question", question);
                context.startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(context, 3);
        //设置布局管理器
        rv_attention.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
//        rv_attention.setAdapter(new TopicRecyclerAdpter(context,));
        //设置分隔线
//        rv_attention.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        rv_attention.setItemAnimator(new DefaultItemAnimator());
//        rv_attention.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        rv_attention.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(context, TopicActivity.class);
//                intent.putExtra("topic_info", topicList.get(position).toStringArray());
//                Log.d("qin1", "topic_info" + topicList.get(position).toStringArray()[1]);
//                context.startActivity(intent);
//            }
//        });

        if (MyApplication.logined) {
            hint_mypush.setVisibility(View.GONE);
            getAttentionTopic();
            getMyPush();

        }
        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add("发布问卷");
        strList.add("查看问卷");

        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PushQuestionnaireActivity.class);
                startActivity(intent);
            }
        };
        View.OnClickListener clickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionnaireActivity.class);
                startActivity(intent);
                Toast.makeText(context, "查看问卷", Toast.LENGTH_SHORT).show();
            }
        };
        clickList.add(clickListener1);
        clickList.add(clickListener2);

        //具体初始化逻辑看下面的图
//        hintPopupWindow = new HintPopupWindow(context, strList, clickList);

    }

    public void getMyPush() {
        RequestParams entity = new RequestParams(Config.url_user);
        entity.addBodyParameter("quizzer_name", user.getUsername());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    //new一个question数组
                    questionList = new ArrayList<Question>();
                    //遍历传入的jsonArray
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = jsonArray.optJSONObject(i);
                        //读取相应内容
                        int question_id = temp.optInt("question_id");
                        String post_time = temp.getString("post_time");
                        String question_content = temp.getString("question_content");
                        String left_url = temp.getString("left_url");
                        String right_url = temp.getString("right_url");
                        String quizzer_name = temp.getString("quizzer_name");
                        String portrait_url = temp.getString("portrait_url");
                        int share_count = temp.optInt("share_count");
                        int comment_count = temp.optInt("comment_count");
                        String comment = temp.getString("comment");

                        Question question = new Question(question_id, question_content,
                                left_url, right_url, quizzer_name, portrait_url,
                                share_count, comment_count, comment, null, post_time);
                        System.out.println("user:   " + question.toString());
                        questionList.add(question);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //设置适配器
                lv.setAdapter(new UserAdpter(context, questionList));
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

    private void getAttentionTopic() {
        RequestParams entity = new RequestParams(Config.url_topic);
        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONArray resultJson = new JSONArray(result);
                        topicList = new ArrayList<Topic>();
                        for (int i = 0; i < resultJson.length(); i++) {
                            JSONObject temp = resultJson.optJSONObject(i);
                            Topic topic = Topic.jsonObjectToTotic(temp);
                            Log.d("qin", "temp: " + temp.toString());
                            topicList.add(topic);
                        }
                        TopicRecyclerAdpter adpter = new TopicRecyclerAdpter(context, topicList);
                        adpter.setOnItemClickListener(new TopicRecyclerAdpter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(context, TopicActivity.class);
                                intent.putExtra("topic_info", topicList.get(position).toStringArray());
                                Log.d("qin1", "topic_info" + topicList.get(position).toStringArray()[1]);
                                context.startActivity(intent);
                            }
                        });
                        rv_attention.setAdapter(adpter);


//                        lv_home_attention.setAdapter(new BaseAdapter() {
//                            @Override
//                            public int getCount() {
//                                return topicList.size();
//                            }
//
//                            @Override
//                            public Object getItem(int position) {
//                                return null;
//                            }
//
//                            @Override
//                            public long getItemId(int position) {
//                                return 0;
//                            }
//
//                            @Override
//                            public View getView(int position, View convertView, ViewGroup parent) {
//                                View v = null;
//                                if (convertView != null) {
//                                    v = convertView;
//                                } else {
//                                    v = View.inflate(context, R.layout.view_attention_item, null);
//                                }
//
//                                ImageView topic_img = (ImageView) v.findViewById(R.id.topic_img);
//                                TextView topic_title = (TextView) v.findViewById(R.id.topic_title);
//
//                                Topic i = topicList.get(position);
//
////                                ImageOptions options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.notdata).build();
////                                x.image().bind(topic_img, i.getTopic_img_url(), options);
//                                Glide.with(AttentionFragment.this).load(i.getTopic_img_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(topic_img);
//                                topic_title.setText(i.getTopic_title());
//                                return v;
//                            }
//                        });

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setting:
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_userinfo:
//                JrmfClient.openSingleRp();
//                JrmfManager.openRedEnvelope(context, "https://api-test.jrmf360.com//h5/v1/operateRedEnvelope/common/index.shtml?envelopeId=32f8222e-0046-4a90-a8e9-a534f0f3dfa3&partnerId=jrmf");
                Intent intent1 = new Intent(context, ConversationListActivity.class);
                startActivity(intent1);
                break;
            case R.id.user_portrait:
                break;
            case R.id.user_sign:
                Service.getInstance().gradeAdd(context, 1);
                Intent grade = new Intent(context, GradeActivity.class);
                startActivity(grade);
                Toast.makeText(context, "你已签到，积分+1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_wallet:
                JrmfClient.intentWallet(getActivity());
                break;
            case R.id.user_layout:
                if (MyApplication.logined) {
                    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
                    userInfoIntent.putExtra("user", user);
                    startActivity(userInfoIntent);
                    return;
                }
                Intent login = new Intent(context, LoginActivity.class);
                startActivity(login);
                break;
//            case R.id.user_questionnaire:
////                hintPopupWindow.showPopupWindow(v);
//                break;
//            case R.id.user_data:
//                Intent intent1 = new Intent(context, DataActivity.class);
//                startActivity(intent1);
//                break;
        }
    }


}



	