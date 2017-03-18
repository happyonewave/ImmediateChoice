package com.qzct.immediatechoice.domain;

/**
 * Created by Administrator on 2017-03-17.
 */

public class QuestionVideo {
    int question_video_id;
    String question_video_content;
    String video_left;
    String video_right;
    String quizzer_name;
    String quizzer_portrait;
    int share_count;
    int comment_count;
    String comment;
    String location;

    public int getQuestion_video_id() {
        return question_video_id;
    }

    public String getQuestion_video_content() {
        return question_video_content;
    }

    public String getVideo_left() {
        return video_left;
    }

    public String getVideo_right() {
        return video_right;
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

    public String getLocation() {
        return location;
    }

    public QuestionVideo(int question_video_id, String question_video_content, String video_left, String video_right, String quizzer_name, String quizzer_portrait, int share_count, int comment_count, String comment, String location) {
        this.question_video_id = question_video_id;
        this.question_video_content = question_video_content;
        this.video_left = video_left;
        this.video_right = video_right;
        this.quizzer_name = quizzer_name;
        this.quizzer_portrait = quizzer_portrait;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
        this.location = location;
    }
}
