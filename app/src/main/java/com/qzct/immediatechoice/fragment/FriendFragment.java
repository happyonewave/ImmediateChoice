package com.qzct.immediatechoice.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qzct.immediatechoice.R;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * Created by qin on 2017/3/24.
 */
public class FriendFragment extends baseFragment implements View.OnClickListener {


    private StandardGSYVideoPlayer gsyVideoPlayer;
    private OrientationUtils orientationUtils;
    public final static String TRANSITION = "TRANSITION";
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private ViewPager vp_test;
    private List<Fragment> fragmentList;
    private List<UserInfo> UserInfoList;

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
//        View view = inflater.inflate(R.layout.fragment_friend,null);


        View view = inflater.inflate(R.layout.activity_test, null);
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(initConversationList());
        fragmentList.add(initConversationList());
        vp_test = (ViewPager) view.findViewById(R.id.vp_test);

        vp_test.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        bt_1 = (Button) view.findViewById(R.id.bt_1);
        bt_2 = (Button) view.findViewById(R.id.bt_2);
        bt_3 = (Button) view.findViewById(R.id.bt_3);
        bt_4 = (Button) view.findViewById(R.id.bt_4);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);

        return view;

    }


    private Fragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri;
        uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                .build();


        listFragment.setUri(uri);
        return listFragment;
    }


    @Override
    public void initdata() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_2:
                RongIM.getInstance().startPrivateChat(context, "2", "与userid：2的聊天");
                break;
            case R.id.bt_4:
                RongIM.getInstance().startPrivateChat(context, "1", "与userid：1的聊天");

                break;

            default:

                break;

        }

    }


}
