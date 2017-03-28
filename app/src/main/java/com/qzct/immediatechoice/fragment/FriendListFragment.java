package com.qzct.immediatechoice.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.domain.User;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by qin on 2017/3/28.
 */
public class FriendListFragment extends baseFragment {

    private ListView lv_friendlist;
    List<User> userList = MyApplication.userList;

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        lv_friendlist = new ListView(context);
        return lv_friendlist;
    }

    @Override
    public void initdata() {
        lv_friendlist.setAdapter(new BaseAdapter() {

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
                TextView v = null;
                if (convertView == null) {
                    v = new TextView(context);
//                    v.setHeight(50);
//                    v.setWidth(50);
                } else {
                    v = (TextView) convertView;
                }
                v.setText(userList.get(position).getUsername());
                return v;
            }
        });
        lv_friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(context,
                        userList.get(position).getUser_id() + "",
                        "与" + userList.get(position).getUsername() + "的聊天");
                Log.d("qin", "name:  " + userList.get(position).getUsername());

            }
        });
    }
}
