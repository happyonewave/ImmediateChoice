package com.qzct.immediatechoice.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzct.immediatechoice.R;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * Created by qin on 2017/3/24.
 */
public class FriendFragment extends baseFragment {

    private ViewPager vp_friend;
    private List<Fragment> fragmentList;

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(initConversationList());
        fragmentList.add(new FriendListFragment());
        vp_friend = (ViewPager) view.findViewById(R.id.vp_friend);

        vp_friend.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        return view;

    }


    private Fragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri;
        uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
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


}
