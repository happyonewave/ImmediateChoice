package com.qzct.immediatechoice.Application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.qzct.immediatechoice.BuildConfig;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;


/**
 * Created by Qin on 2017-02-27.
 */

public class MyApplication extends MultiDexApplication {


    public static User user;

    //    public static Question question;
    public static boolean isQuestion = false;
    public static boolean logined = false;
    public static boolean isQuestionnaireProvider = false;
    public static List<User> userList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        user = User.getDefaultUser(getApplicationContext());
        //融云IM初始化
        RongIM.init(this);
        //设置消息体内是否携带用户信息。
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        //初始化xUtils
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能.
//        x.Ext.setDebug(BuildConfig.DEBUG);

    }

}
