package com.qzct.immediatechoice.domain;

import android.content.Context;
import android.net.Uri;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2017-02-26.
 */

public class User implements Serializable {
    public static int USER_ME = 0;
    public static int USER_FRIEND = 1;
    public static int USER_QUERY = 2;
    int user_id;
    int user_type;
    String username;
    String password;
    String phone_number;
    String portrait_path;
    String sex;
    String token;

    public UserInfo toUserinfo() {
        UserInfo userInfo = new UserInfo(user_id + "", username, Uri.parse(portrait_path));
        return userInfo;
    }

    public User(int user_id, String username, String phone_number, String sex, String portrait_path) {
        this.user_id = user_id;
        this.username = username;
        this.phone_number = phone_number;
        this.sex = sex;
        this.portrait_path = portrait_path;
    }

    public String getToken() {
        return token;
    }

    public User(int user_id, String username, String password, String phone_number, String portrait_path, String sex, String token) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.portrait_path = portrait_path;
        this.sex = sex;
        this.token = token;
    }
    public User(int user_id,int user_type, String username, String password, String phone_number, String portrait_path, String sex, String token) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.portrait_path = portrait_path;
        this.sex = sex;
        this.token = token;
    }


    public int getUser_id() {
        return user_id;
    }


    public User(String username, String password, String phone_number, String portrait_path, String sex) {
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.portrait_path = portrait_path;
        this.sex = sex;
    }

    public int getUser_type() {
        return user_type;
    }

    public User(String username, int user_type, String password, String phone_number, String portrait_path, String sex) {
        this.username = username;
        this.user_type = user_type;
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


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getPhone_number() {
        return phone_number;
    }


    public String getPortrait_path() {
        return portrait_path;
    }


    public String getSex() {
        return sex;
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

    public User(int user_id) {
        this.user_id = user_id;
    }

    public static List<User> toMemberListFrom(JSONArray members) throws JSONException {

        List<User> memberList = new ArrayList<User>();
        for (int i = 0; i < members.length(); i++) {
            JSONObject temp = members.optJSONObject(i);
            User user = new User(temp.optInt("f_id"));
            memberList.add(user);
        }
        return memberList;


    }

    public static User jsonObjectToUser(JSONObject jsonObject) throws JSONException {
        int user_id = jsonObject.optInt("user_id");
        String username = jsonObject.optString("name");
//        String password = jsonObject.optString("password");
        String password = jsonObject.has("password") ? jsonObject.optString("password") : "";
//        String phone_number = jsonObject.optString("phone_number");
        String phone_number = jsonObject.has("phone_number") ? jsonObject.optString("phone_number") : "";
        String portrait_path = jsonObject.optString("portrait_path");
        String sex = jsonObject.optString("sex");
        String token = jsonObject.has("token") ? jsonObject.optString("token") : "";
//        String token = jsonObject.optString("token");

        return new User(user_id, username, password, phone_number, portrait_path, sex, token);
    }
}
