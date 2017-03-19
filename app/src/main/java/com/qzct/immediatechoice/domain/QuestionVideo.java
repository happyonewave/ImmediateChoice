package com.qzct.immediatechoice.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017-03-17.
 */

public class QuestionVideo {
    int question_video_id;
    String question_video_content;
    String video_left_url;
    String video_right_url;
    String quizzer_name;
    String quizzer_portrait;
    int share_count;
    int comment_count;
    String comment;
    String locations;

    public QuestionVideo(String question_video_content, String video_left_url, String video_right_url, String quizzer_name, String quizzer_portrait, String locations) {
        this.question_video_content = question_video_content;
        this.video_left_url = video_left_url;
        this.video_right_url = video_right_url;
        this.quizzer_name = quizzer_name;
        this.quizzer_portrait = quizzer_portrait;
        this.locations = locations;

    }

    public JSONObject getJSONObject() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("question_video_id", question_video_id);
            jsonObject.put("question_video_content", question_video_content);
            jsonObject.put("video_left_url", video_left_url);
            jsonObject.put("video_right_url", video_right_url);
            jsonObject.put("quizzer_name", quizzer_name);
            jsonObject.put("quizzer_portrait", quizzer_portrait);
            jsonObject.put("share_count", share_count);
            jsonObject.put("comment_count", comment_count);
            jsonObject.put("comment", comment);
            jsonObject.put("locations", locations);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return jsonObject;

        }
    }

    public int getQuestion_video_id() {
        return question_video_id;
    }

    public String getQuestion_video_content() {
        return question_video_content;
    }

    public String getVideo_left() {
        return video_left_url;
    }

    public String getVideo_right() {
        return video_right_url;
    }

    public String getQuizzer_name() {
        return quizzer_name;
    }

    public String getQuizzer_portrait() {
        return quizzer_portrait;
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

    public String getLocations() {
        return locations;
    }

    public QuestionVideo(int question_video_id, String question_video_content, String video_left_url, String video_right_url, String quizzer_name, String quizzer_portrait, int share_count, int comment_count, String comment, String locations) {
        this.question_video_id = question_video_id;
        this.question_video_content = question_video_content;
        this.video_left_url = video_left_url;
        this.video_right_url = video_right_url;
        this.quizzer_name = quizzer_name;
        this.quizzer_portrait = quizzer_portrait;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.locations = locations;
    }
}
