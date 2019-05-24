package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.activity.ConversationActivity;
import com.qzct.immediatechoice.activity.UserInfoActivity;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;
import com.qzct.immediatechoice.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.jrmf360.rylib.wallet.JrmfWalletClient.getApplicationContext;

/**
 * Created by qin on 2017/3/28.
 */
public class FriendListFragment extends BaseFragment {

    private SwipeMenuListView lv_friendlist;
    List<User> userList = MyApplication.userList;
    private SearchView friendlist_search;
    private LinearLayout v;
    private FriendListAdapter friendListAdapter;
    private ArrayAdapter<String> searchAdapter;
    private List<String> mStrList = new ArrayList<String>();
    private List<User> queryfriendList;
    private SharedPreferences sharedPreferences;
    private FriendListFragment Me = this;
    private View view;
    private EditText et_searchView;
    private TextView tv_searchView;
    private LinearLayout ll;
    private ImageView searchButton;
    private SearchView.SearchAutoComplete mSearchSrcTextView;
    private ImageView close_search;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
//        v = new LinearLayout(context);
        v = (LinearLayout) inflater.inflate(R.layout.fragment_friendlist, null);
        v.setOrientation(LinearLayout.VERTICAL);
        close_search = (ImageView) v.findViewById(R.id.close);
        friendlist_search = (SearchView) v.findViewById(R.id.friendlist_search);
        searchButton = (ImageView) friendlist_search.findViewById(R.id.search_button);
//        mSearchSrcTextView = (SearchView.SearchAutoComplete) friendlist_search.findViewById(R.id.search_src_text);
//        searchButton.setImageResource(R.mipmap.search);
//        friendlist_search = new SearchView(context);
        friendlist_search.setQueryHint("用户名");
        //* 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
//        friendlist_search.setSubmitButtonEnabled(true);
        // * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
//        friendlist_search.onActionViewExpanded();
//        lv_friendlist = new SwipeMenuListView(context);
        lv_friendlist = (SwipeMenuListView) v.findViewById(R.id.lv_friend_list);
        tv_searchView = (TextView) v.findViewById(R.id.tv_searchView);
//        ll = (LinearLayout) v.findViewById(R.id.ll);
//
//        if (friendlist_search != null) {
//            try {        //--拿到字节码
//                Class<?> argClass = friendlist_search.getClass();
//                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
//                Field ownField = argClass.getDeclaredField("ll");
//                //--暴力反射,只有暴力反射才能拿到私有属性
//                ownField.setAccessible(true);
//                View mView = (View) ownField.get(friendlist_search);
//                //--设置背景
//                mView.setBackgroundColor(Color.TRANSPARENT);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }}

//        view = (View) v.findViewById(R.id.view);

//        lv_friendlist = new ListView(context);
//        v.addView(friendlist_search);
//        v.addView(lv_friendlist);
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
//                    Toast.makeText(context, "已点击btn_delete", Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ADD_FRIEND) {
//        Toast.makeText(context, "从用户介绍界面返回", Toast.LENGTH_SHORT).show();
        friendlist_search.clearFocus();
        friendlist_search.setQuery(null, false);
        lv_friendlist.setAdapter(friendListAdapter);
        mStrList.clear();
        mStrList.add("无此用户");

//        Me.userList = MyApplication.userList;
//        friendListAdapter.notifyDataSetChanged();
//        Service.getInstance().getFriendInfo(new MyCallback.FriendInfoCallBack() {
//            @Override
//            public void onSuccess(List<User> userList) {
//                Me.userList = userList;
//                friendListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Throwable ex) {
//                Toast.makeText(context, "获取好友失败", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //    }
    public float firstX;

    @Override
    public void initData() {
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                tv_searchView.setVisibility(View.VISIBLE);
                friendlist_search.onActionViewCollapsed();
            }
        });
        tv_searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendlist_search.onActionViewExpanded();
                v.setVisibility(View.GONE);
//                mSearchSrcTextView.setText("");
//                int mCollapsedImeOptions = mSearchSrcTextView.getImeOptions();
//                mSearchSrcTextView.setImeOptions(mCollapsedImeOptions | EditorInfo.IME_FLAG_NO_FULLSCREEN);
//                mSearchSrcTextView.setText("");
            }
        });
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: " + "event:" + event.toString());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    firstX = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getX() > firstX + 100) {
                        new FriendFragment().vp_friend.setCurrentItem(0);
                    }
                }
                return true;
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(getResources().getColor(R.color.apporange)));
                openItem.setWidth(Utils.Dp2Px(context, 90));
                openItem.setTitle("更多");
                openItem.setTitleSize(15);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//
//                deleteItem.setWidth(180);
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lv_friendlist.setMenuCreator(creator);
        // Right
        lv_friendlist.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_friendlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open

//                        Toast.makeText(context, "打开用户介绍", Toast.LENGTH_SHORT).show();
                        User user = userList.get(position);
//                        for (User temp : MyApplication.userList) {
//                            if (temp.getUser_id() == Integer.parseInt(targetId)) {
//                                user = temp;
//                                break;
//                            }
//                        }
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("user_type", User.USER_FRIEND);
//                    MyApplication.queryfriend = user;
//                    sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
                        startActivity(intent);
//                        Toast.makeText(context, "open", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // 监测用户在ListView的SwipeMenu侧滑事件。
//        lv_friendlist.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
//
//            @Override
//            public void onSwipeStart(int pos) {
//                Log.d("位置:" + pos, "开始侧滑...");
//            }
//
//            @Override
//            public void onSwipeEnd(int pos) {
//                Log.d("位置:" + pos, "侧滑结束.");
//            }
//        });

        friendListAdapter = new FriendListAdapter();
        mStrList.add("无此用户");
        searchAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mStrList);
        lv_friendlist.setAdapter(friendListAdapter);
        lv_friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!"无此用户".equals(mStrList.get(0))) {
                    User user = queryfriendList.get(position);
//                    Toast.makeText(context, "打开用户介绍", Toast.LENGTH_SHORT).show();
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
                    startPrivateChat(
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
                    entity.addBodyParameter("name", newText);
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
        friendlist_search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_searchView.setVisibility(View.GONE);
                close_search.setVisibility(View.VISIBLE);
            }
        });
        friendlist_search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                mStrList.clear();
//                mStrList.add("无此用户");
//                lv_friendlist.setAdapter(friendListAdapter);
                tv_searchView.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    public void startPrivateChat(String targetUserId, String title) {
        if (context != null && !TextUtils.isEmpty(targetUserId)) {
            if (RongContext.getInstance() == null) {
                throw new ExceptionInInitializerError("RongCloud SDK not init");
            } else {
                Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase()).appendQueryParameter("targetId", targetUserId).appendQueryParameter("title", title).build();
                startActivityForResult(new Intent("android.intent.action.VIEW", uri), START_PRIVATE_CHAT);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("qin","onResume");
        Me.userList = MyApplication.userList;
        friendListAdapter.notifyDataSetChanged();
    }
}
