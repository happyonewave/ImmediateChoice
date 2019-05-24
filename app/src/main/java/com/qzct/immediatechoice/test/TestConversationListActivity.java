//package com.qzct.immediatechoice.test;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//
//import com.qzct.immediatechoice.R;
//import com.qzct.immediatechoice.util.Utils;
//
//import io.rong.imkit.fragment.ConversationListFragment;
//import io.rong.imlib.model.Conversation;
//
///**
// * Created by qin on 2017/3/25.
// */
//
//class TestConversationListActivity extends FragmentActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(utils.getUsableView(this, R.layout.layout_fragment));
//    }
//
//    private Fragment initConversationList() {
//        ConversationListFragment listFragment = new ConversationListFragment();
//        Uri uri;
//        uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversationlist")
//                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
//                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
//                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
//                .build();
//
//
//        listFragment.setUri(uri);
//        return listFragment;
//    }
//}
