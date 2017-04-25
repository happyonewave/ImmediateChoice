package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.activity.UserInfoActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by qin on 2017/3/28.
 */
public class FriendListFragment extends baseFragment {

    private ListView lv_friendlist;
    List<User> userList = MyApplication.userList;
    private SearchView friendlist_search;
    private LinearLayout v;
    private FriendListAdapter friendListAdapter;
    private ArrayAdapter<String> searchAdapter;
    private List<String> mStrList = new ArrayList<String>();
    private List<User> queryfriendList;
    private SharedPreferences sharedPreferences;
    private int ADD_FRIEND = 0;
    private FriendListFragment Me = this;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        v = new LinearLayout(context);
        v.setOrientation(LinearLayout.VERTICAL);
        friendlist_search = new SearchView(context);
        friendlist_search.setQueryHint("用户Id");
        //* 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
        friendlist_search.setSubmitButtonEnabled(true);
        // * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
//        friendlist_search.onActionViewExpanded();
        lv_friendlist = new ListView(context);
        v.addView(friendlist_search);
        v.addView(lv_friendlist);

        return v;
    }

    private class FriendListAdapter extends BaseAdapter {

        @Override

        public int getCount() {
            return userList.size();
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
            if (convertView == null) {
                v = View.inflate(context, R.layout.fragment_friendlist_item, null);
            } else {
                v = convertView;
            }
            ImageView portrait = (ImageView) v.findViewById(R.id.friend_portrait);
            TextView name = (TextView) v.findViewById(R.id.friend_name);
            Button btn_delete = (Button) v.findViewById(R.id.friend_delete);

            Glide.with(context).load(userList.get(position).getPortrait_path())
                    .transform(new GlideCircleTransform(context)).into(portrait);
//                ImageOptions options = new ImageOptions.Builder().setCircular(true).setSize(80, 80).build();
//                x.image().bind(portrait, userList.get(position).getPortrait_path(), options
//                );
            name.setText(userList.get(position).getUsername());
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "已点击btn_delete", Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ADD_FRIEND) {
        Toast.makeText(context, "从用户介绍界面返回", Toast.LENGTH_SHORT).show();
        friendlist_search.clearFocus();
        friendlist_search.setQuery(null, false);
        lv_friendlist.setAdapter(friendListAdapter);
        mStrList.clear();
        mStrList.add("无此用户");
        Service.getInstance().getFriendInfo(new MyCallback.FriendInfoCallBack() {
            @Override
            public void onSuccess(List<User> userList) {
                Me.userList = userList;
                friendListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(context, "获取好友失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    }

    @Override
    public void initData() {
        friendListAdapter = new FriendListAdapter();
        mStrList.add("无此用户");
        searchAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mStrList);
        lv_friendlist.setAdapter(friendListAdapter);
        lv_friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!"无此用户".equals(mStrList.get(0))) {
                    User user = queryfriendList.get(position);
                    Toast.makeText(context, "打开用户介绍", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("user_type", User.USER_QUERY);
//                    MyApplication.queryfriend = user;
//                    sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
                    startActivityForResult(intent, ADD_FRIEND);
                    return;
                }

//                if (mStrList.size() != 1 && !"无此用户".equals(mStrList.get(0))) {
                if (friendlist_search.getQuery().toString().isEmpty()) {
                    RongIM.getInstance().startPrivateChat(context,
                            userList.get(position).getUser_id() + "",
                            userList.get(position).getUsername());
                    Log.d("qin", "name:  " + userList.get(position).getUsername());
                }
            }
        });

        friendlist_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                lv_friendlist.setVisibility(View.VISIBLE);
//                ll_layout.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    RequestParams entity = new RequestParams(Config.url_search);
                    entity.addBodyParameter("f_id", newText);
                    x.http().get(entity, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (result != null && !"[]".equals(result)) {
                                Log.d("qin", "result:   " + result);
                                try {
                                    queryfriendList = new ArrayList<User>();
                                    mStrList.clear();
                                    JSONArray array = new JSONArray(result);
                                    for (int i = 0; i < array.length(); i++) {
                                        User user = User.jsonObjectToUser(array.optJSONObject(i));
                                        mStrList.add(user.getUsername());
                                        queryfriendList.add(user);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(context, "连接搜索服务错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {
                            lv_friendlist.setAdapter(searchAdapter);
                        }
                    });
//                    lv_search.setFilterText(newText);
                } else {
//                    lv_search.clearTextFilter();
                    friendlist_search.clearFocus();
                    lv_friendlist.setAdapter(friendListAdapter);
                    mStrList.clear();
                    mStrList.add("无此用户");

                }
                return false;
            }
        });
        friendlist_search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                mStrList.clear();
//                mStrList.add("无此用户");
//                lv_friendlist.setAdapter(friendListAdapter);
                return false;
            }
        });
    }
}
