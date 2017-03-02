package com.qzct.immediatechoice.domain;

/**
 * Created by Administrator on 2017-02-26.
 */

public class User {
    String username;
    String password;
    String phone_number;
    String portrait_path;
    String sex;

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
