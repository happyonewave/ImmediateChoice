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

    public String getComment_content() {
        return comment_content;
    }

    public String getCommenter_portrait_url() {
        return commenter_portrait_url;
    }

    public String getComment_date() {
        return comment_date;
    }
    public Comment(String commenter_portrait_url, String comment_date, String commenter_name, String comment_content) {
        this.commenter_portrait_url = commenter_portrait_url;
        this.comment_date = comment_date;
        this.commenter_name = commenter_name;
        this.comment_content = comment_content;
    }
    public String getCommenter_name() {
        return commenter_name;
    }


}
