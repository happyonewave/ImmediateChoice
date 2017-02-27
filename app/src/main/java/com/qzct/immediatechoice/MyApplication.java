package com.qzct.immediatechoice;

import android.app.Application;

import com.qzct.immediatechoice.domain.User;

/**
 * Created by Administrator on 2017-02-27.
 */

 public class MyApplication extends Application {

    static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
    }

    @Override
    public void onCreate() {
        user = new User();
        super.onCreate();
    }
}
