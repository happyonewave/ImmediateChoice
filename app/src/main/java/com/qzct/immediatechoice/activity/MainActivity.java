package com.qzct.immediatechoice.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.fragment.DiscoveryFragment;
import com.qzct.immediatechoice.fragment.FriendFragment;
import com.qzct.immediatechoice.fragment.HomeFragment;
import com.qzct.immediatechoice.fragment.UserFragment;
import com.qzct.immediatechoice.fragment.baseFragment;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_main, null));
        rg_nav = (RadioGroup) findViewById(R.id.rg_nav);
        //用来放fragment的帧布局
        fl = (FrameLayout) findViewById(R.id.fl);
        //初始化Fragment为首页
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, 0);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
        mainActivity = this;

        userInfoList = new ArrayList<UserInfo>();
        getFriendInfo();
        RongIM.setUserInfoProvider(this, true);
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
    private void getFriendInfo() {
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
                        MyApplication.userList = userList;
                        userInfoList.add(MyApplication.user.toUserinfo());
                        for (User user : userList) {
                            Log.d("qin", user.toUserinfo().getPortraitUri().toString());
                            userInfoList.add(user.toUserinfo());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
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
        int id = v.getId();
        int index = 0;
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
                index = 2;
                break;
            //我的
            case R.id.bt_user:
                index = 3;
                break;

            default:
                break;
        }
        //Fragment切换
        Fragment fragment = (Fragment) Adapter.instantiateItem(fl, index);
        Adapter.setPrimaryItem(fl, 0, fragment);
        Adapter.finishUpdate(fl);
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
                    baseFragment = new FriendFragment();
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
}