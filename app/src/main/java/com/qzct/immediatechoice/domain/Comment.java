package com.qzct.immediatechoice.domain;

/**
 * Created by qin on 2017/3/11.
 */

public class Comment {
    int comment_id;
    String comment_content;
    String comment_date;
    String commenter_name;
    String commenter_portrait_url;

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public Comment() {
    }

    public Comment(String commenter_portrait_url, String comment_date, String commenter_name, String comment_content) {
        this.commenter_portrait_url = commenter_portrait_url;
        this.comment_date = comment_date;
        this.commenter_name = commenter_name;
        this.comment_content = comment_content;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getCommenter_name() {
        return commenter_name;
    }

    public void setCommenter_name(String commenter_name) {
        this.commenter_name = commenter_name;
    }

    public String getCommenter_portrait_url() {
        return commenter_portrait_url;
    }

    public void setCommenter_portrait_url(String commenter_portrait_url) {
        this.commenter_portrait_url = commenter_portrait_url;
    }
}
