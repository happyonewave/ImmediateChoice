package com.qzct.immediatechoice.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.fragment.DiscoveryFragment;
import com.qzct.immediatechoice.fragment.FriendFragment;
import com.qzct.immediatechoice.fragment.FriendListFragment;
import com.qzct.immediatechoice.fragment.HomeFragment;
import com.qzct.immediatechoice.fragment.QuestionFragment;
import com.qzct.immediatechoice.fragment.QuestionnaireFragment;
import com.qzct.immediatechoice.fragment.QuestionnaireSurveyedFragment;
import com.qzct.immediatechoice.fragment.QuestionnaireSurveyingFragment;
import com.qzct.immediatechoice.fragment.RegisterFinallyFragment;
import com.qzct.immediatechoice.fragment.UserFragment;
import com.qzct.immediatechoice.fragment.VideoFragment;
import com.qzct.immediatechoice.fragment.baseFragment;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;
import com.qzct.immediatechoice.util.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * 主
 */
public class MainActivity extends AppCompatActivity implements RongIM.UserInfoProvider {

    private FrameLayout fl;
    public static Activity mainActivity;
    public static RadioGroup rg_nav;
    private String token = MyApplication.user.getToken();
    private ArrayList<UserInfo> userInfoList;
    private String TAG = "main";
//    public static View shade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_main, null));
        rg_nav = (RadioGroup) findViewById(R.id.rg_nav);
        RadioButton bt_friend = (RadioButton) findViewById(R.id.bt_friend);
        if (MyApplication.isQuestionnaireProvider) {
            bt_friend.setText("问卷");
            bt_friend.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.bt_questionnaire_bg), null, null);
//            bt_friend.setCompoundDrawables(null, getResources().getDrawable(R.drawable.bt_questionnaire_bg), null, null);
        }
//        shade = findViewById(R.id.shade);
        //用来放fragment的帧布局
        fl = (FrameLayout) findViewById(R.id.fl);
        //初始化Fragment为首页
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, 0);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
        mainActivity = this;

        userInfoList = new ArrayList<UserInfo>();
        Service.getInstance().getFriendInfo(new MyCallback.FriendInfoCallBack() {
            @Override
            public void onSuccess(List<User> userList) {
                MyApplication.userList = userList;
                userInfoList.add(MyApplication.user.toUserinfo());
                for (User user : userList) {
                    Log.d("qin", user.toUserinfo().getPortraitUri().toString());
                    userInfoList.add(user.toUserinfo());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(MainActivity.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
            }
        });
//        getFriendInfo();
//        RongIM.setUserInfoProvider(this, true);
        RongIM.getInstance().setCurrentUserInfo(MyApplication.user.toUserinfo());
//       new  RongIM().setGroupMembersProvider();
        RongMentionManager.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String s, RongIM.IGroupMemberCallback iGroupMemberCallback) {
                iGroupMemberCallback.onGetGroupMembersResult(userInfoList);
            }
        });
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
                Log.d("qin", "userid: " + s);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 获取好友信息
     */

//    private void getFriendInfo() {
//        final List<User> userList = new ArrayList<User>();
//        RequestParams entity = new RequestParams(Config.url_friend);
//        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
//        x.http().post(entity, new MyCallback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                if (result != null) {
//                    try {
//                        JSONArray friendArray = new JSONArray(result);
//                        for (int i = 0; i < friendArray.length(); i++) {
//                            JSONObject temp = friendArray.optJSONObject(i);
//                            int user_id = temp.optInt()("user_id");
//                            String name = temp.getString("name");
//                            String phone_number = temp.getString("phone_number");
//                            String sex = temp.getString("sex");
//                            String portrait_url = temp.getString("portrait_path");
//                            User user = new User(user_id, name, phone_number, sex, portrait_url);
//                            userList.add(user);
//                        }
//                        MyApplication.userList = userList;
//                        userInfoList.add(MyApplication.user.toUserinfo());
//                        for (User user : userList) {
//                            Log.d("qin", user.toUserinfo().getPortraitUri().toString());
//                            userInfoList.add(user.toUserinfo());
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(MainActivity.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }
    @Override
    public UserInfo getUserInfo(String s) {

        for (UserInfo user : userInfoList) {
            if (user.getUserId().equals(s)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 退出提醒
     *
     * @param keyCode
     * @param event
     * @return
     */
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "快速点两次就退出了哦！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    public void click(View v) {
        RadioButton btn = (RadioButton) v;
        int id = v.getId();
        int index = 0;
        boolean change = true;
        switch (id) {
            //首页
            case R.id.bt_home:
                index = 0;
                break;
            //发现
            case R.id.bt_discovery:
                index = 1;
                break;
            //朋友
            case R.id.bt_friend:
                if (!MyApplication.logined) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
//                    change = false;
                    return;
                }
                index = 2;
                break;
            //我的
            case R.id.bt_user:
                if (!MyApplication.logined) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                index = 3;
                break;

            default:
                break;
        }
        //Fragment切换
//        if (change) {
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, index);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
//        }
    }

    /**
     * Fragment适配器
     */
    FragmentStatePagerAdapter Adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int arg0) {
            baseFragment baseFragment = null;
            switch (arg0) {
                case 0:
                    baseFragment = new HomeFragment();
                    break;
                case 1:
                    baseFragment = new DiscoveryFragment();
                    break;
                case 2:
                    if (MyApplication.isQuestionnaireProvider) {
                        baseFragment = new QuestionnaireFragment();
                    } else {
                        baseFragment = new FriendFragment();
                    }
                    break;
                case 3:
                    baseFragment = new UserFragment();
                    break;
                default:
                    break;
            }
            return baseFragment;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "main onActivityResult: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = null;
//        switch (requestCode) {
//            case baseFragment.GET_QRCODE:
//                fragment = findFragmentByClass(DiscoveryFragment.class);
//                break;
//            case baseFragment.ADD_FRIEND:
//            case baseFragment.START_PRIVATE_CHAT:
//                fragment = findFragmentByClass(FriendListFragment.class);
//                break;
//            case baseFragment.INTENT_COMMENT:
//                fragment = findFragmentByClass(QuestionFragment.class);
//                break;
//            case baseFragment.INTENT_COMMENT_VIDEO:
//                fragment = findFragmentByClass(VideoFragment.class);
//                break;
//            case baseFragment.PUSH_QUESTIONNAIRE:
//                fragment = findFragmentByClass(QuestionnaireSurveyedFragment.class);
//                break;
//            case baseFragment.PUSH_QUESTIONNAIRE_DOING:
//                fragment = findFragmentByClass(QuestionnaireSurveyingFragment.class);
//                break;
//            case baseFragment.IMAGE_PORTRAIT_UPLOAD:
//                fragment = findFragmentByClass(RegisterFinallyFragment.class);
//                break;
//            default:
//                break;
//        }
//        if (fragment != null)
//            fragment.onActivityResult(requestCode, resultCode, data);
    }

    private Fragment findFragmentByClass(Class cls) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment = fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if (fragment == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x");
                return null;
            } else {
                Log.d(TAG, "fragment name1:" + fragment.getClass().getSimpleName());
                Log.d(TAG, "fragment class1:" + fragment.getClass().getName());
                Log.d(TAG, "fragment class2:" + cls.getName());
                if (fragment.getClass().getName().equals(cls.getName())) {
                    Log.d(TAG, "fragment name2:" + fragment.getClass().getSimpleName());
                    return fragment;
                }
                Fragment f = handleResult(fragment, cls);
                if (f != null) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     */
    private Fragment handleResult(Fragment fragment, Class cls) {
        Fragment f = null;
        List<Fragment> childs = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childs == null || childs.size() == 0) {
            return f;
        }
        for (Fragment child : childs) {
            if (child != null) {
                Log.d(TAG, "fragment name1:" + child.getClass().getSimpleName());
                Log.d(TAG, "fragment class1:" + child.getClass().getName());
                Log.d(TAG, "fragment class2:" + cls.getName());
                if (child.getClass().getName().equals(cls.getName())) {
                    Log.d(TAG, "fragment name2:" + child.getClass().getSimpleName());
                    f = child;
                    break;
                }
                f = handleResult(child, cls);
            }
        }
        return f;
    }


}