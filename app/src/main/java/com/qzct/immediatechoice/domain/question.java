package com.qzct.immediatechoice.domain;

/**
 * Created by Administrator on 2017-02-26.
 */

public class question {

    String question_content;
    String image_left;
    String image_right;
    String quizzer_name;
    int share_count;
    int comment_count;
    String comment;

    public question(String question_content, String image_left, String image_right, String quizzer_name, int share_count, int comment_count, String comment) {
        this.question_content = question_content;
        this.image_left = image_left;
        this.image_right = image_right;
        this.quizzer_name = quizzer_name;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.comment = comment;
    }
    public question(String question_content, String image_left, String image_right) {
        this.question_content = question_content;
        this.image_left = image_left;
        this.image_right = image_right;
    }
    public question() {
    }


    public String getQuestion_content() {
        return question_content;
    }

    public String getImage_left() {
        return image_left;
    }

    public String getImage_right() {
        return image_right;
    }

    public String getQuizzer_name() {
        return quizzer_name;
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

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public void setImage_left(String image_left) {
        this.image_left = image_left;
    }

    public void setImage_right(String image_right) {
        this.image_right = image_right;
    }

    public void setQuizzer_name(String quizzer_name) {
        this.quizzer_name = quizzer_name;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "question{" +
                "question_content='" + question_content + '\'' +
                ", image_left='" + image_left + '\'' +
                ", image_right='" + image_right + '\'' +
                ", quizzer_name='" + quizzer_name + '\'' +
                ", share_count=" + share_count +
                ", comment_count=" + comment_count +
                ", comment='" + comment + '\'' +
                '}';
    }
}
