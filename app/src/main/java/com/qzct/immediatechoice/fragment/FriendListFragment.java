package com.qzct.immediatechoice.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.GlideCircleTransform;

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
                View v = null;
                if (convertView == null) {
                    v = View.inflate(context, R.layout.fragment_friendlist_item, null);
                } else {
                    v = (TextView) convertView;
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
        });
        lv_friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(context,
                        userList.get(position).getUser_id() + "",
                        userList.get(position).getUsername());
                Log.d("qin", "name:  " + userList.get(position).getUsername());

            }
        });
    }
}
