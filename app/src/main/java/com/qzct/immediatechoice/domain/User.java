package com.qzct.immediatechoice.domain;

import android.content.Context;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

/**
 * Created by Administrator on 2017-02-26.
 */

public class User {
    int user_id;
    String username;
    String password;
    String phone_number;
    String portrait_path;
    String sex;

    public User(int user_id, String username, String password, String phone_number, String portrait_path, String sex) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.portrait_path = portrait_path;
        this.sex = sex;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public User(String username, String password, String phone_number, String portrait_path, String sex) {
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.portrait_path = portrait_path;
        this.sex = sex;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int user_id, String username, String portrait_path) {
        this.user_id = user_id;
        this.username = username;
        this.portrait_path = portrait_path;
    }

    public static User getDefaultUser(Context context) {
        return new User(0, "游客", utils.getUribyId(context, R.mipmap.default_portrait).toString());
    }


    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPortrait_path() {
        return portrait_path;
    }

    public void setPortrait_path(String portrait_path) {
        this.portrait_path = portrait_path;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", portrait_path='" + portrait_path + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
