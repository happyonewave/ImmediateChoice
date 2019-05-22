package com.qzct.immediatechoice.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-26.
 */

public class Question implements Serializable {
    int question_id;
    int group_id;
    int topic_id;
    String question_content;
    String left_url;
    String right_url;
    String quizzer_name;
    String portrait_url;
    int share_count;
    int comment_count;
    String comment;
    String location;
    String post_time;
    boolean left_selected;
    boolean right_selected;

    @Override
    public String toString() {
        return "Question{" +
                "question_id=" + question_id +
                ", group_id=" + group_id +
                ", question_content='" + question_content + '\'' +
                ", left_url='" + left_url + '\'' +
                ", right_url='" + right_url + '\'' +
                ", quizzer_name='" + quizzer_name + '\'' +
                ", portrait_url='" + portrait_url + '\'' +
                ", share_count=" + share_count +
                ", comment_count=" + comment_count +
                ", comment='" + comment + '\'' +
                ", location='" + location + '\'' +
                ", post_time='" + post_time + '\'' +
                '}';
    }

    public Question(int question_id, int group_id, int topic_id, String question_content, String left_url, String right_url, String quizzer_name, String portrait_url, int share_count, int comment_count, String comment, String location, String post_time) {
        this.question_id = question_id;
        this.group_id = group_id;
        this.topic_id = topic_id;
        this.question_content = question_content;
        this.left_url = left_url;
        this.right_url = right_url;
        this.quizzer_name = quizzer_name;
        this.portrait_url = portrait_url;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.location = location;
        this.post_time = post_time;
    }

    public Question(int question_id, int group_id, String question_content, String left_url, String right_url, String quizzer_name, String portrait_url, int share_count, int comment_count, String comment, String location, String post_time) {
        this.question_id = question_id;
        this.group_id = group_id;
        this.question_content = question_content;
        this.left_url = left_url;
        this.right_url = right_url;
        this.quizzer_name = quizzer_name;
        this.portrait_url = portrait_url;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.location = location;
        this.post_time = post_time;
    }

    public Question() {
    }

    public Question(int question_id, String question_content, String left_url, String right_url, String quizzer_name, String portrait_url, int share_count, int comment_count, String comment, String location, String post_time) {
        this.question_id = question_id;
        this.question_content = question_content;
        this.left_url = left_url;
        this.right_url = right_url;
        this.quizzer_name = quizzer_name;
        this.portrait_url = portrait_url;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.location = location;
        this.post_time = post_time;
    }

    public Question(int question_id, String question_content, String left_url, String right_url, String quizzer_name, String portrait_url, int share_count, int comment_count, String comment, String location, String post_time,boolean left_selected,boolean right_selected) {
        this.question_id = question_id;
        this.question_content = question_content;
        this.left_url = left_url;
        this.right_url = right_url;
        this.quizzer_name = quizzer_name;
        this.portrait_url = portrait_url;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.location = location;
        this.post_time = post_time;
        this.left_selected = left_selected;
        this.right_selected = right_selected;
    }


    public int getQuestion_id() {
        return question_id;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public String getLeft_url() {
        return left_url;
    }

    public String getRight_url() {
        return right_url;
    }

    public String getQuizzer_name() {
        return quizzer_name;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public int getShare_count() {
        return share_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public String getComment() {
        return comment;
    }

    public String getLocation() {
        return location;
    }

    public String getPost_time() {
        return post_time;
    }

    public JSONObject getJSONObject() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("question_id", question_id);
            jsonObject.put("group_id", group_id);
            jsonObject.put("topic_id", topic_id);
            jsonObject.put("question_content", question_content);
            jsonObject.put("left_url", left_url);
            jsonObject.put("right_url", right_url);
            jsonObject.put("quizzer_name", quizzer_name);
            jsonObject.put("portrait_url", portrait_url);
            jsonObject.put("share_count", share_count);
            jsonObject.put("comment_count", comment_count);
            jsonObject.put("comment", comment);
            jsonObject.put("location", location);
            jsonObject.put("post_time", post_time);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static Question jsonObjectToQuestion(JSONObject jsonObject) throws JSONException {
        int question_id = jsonObject.optInt("question_id");
        String question_content = jsonObject.optString("question_content");
        String left_url = jsonObject.optString("left_url");
        String right_url = jsonObject.optString("right_url");
        String quizzer_name = jsonObject.optString("quizzer_name");
        String portrait_url = jsonObject.optString("portrait_url");
        int share_count = jsonObject.optInt("share_count");
        int comment_count = jsonObject.optInt("comment_count");
        String comment = jsonObject.optString("comment");
        String location = jsonObject.optString("location");
//        String post_time = jsonObject.optString("post_time");
        return new Question(question_id, question_content,
                left_url, right_url, quizzer_name,
                portrait_url, share_count,
                comment_count, comment, location, null);
    }

}
