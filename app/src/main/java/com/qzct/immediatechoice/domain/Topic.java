package com.qzct.immediatechoice.domain;

/**
 * Created by qin on 2017/3/12.
 */
public class Topic {
    String topic_title;
    String topic_img_url;


    public Topic(String topic_title, String topic_img_url) {
        this.topic_title = topic_title;
        this.topic_img_url = topic_img_url;
    }

    public String getTopic_title() {
        return topic_title;
    }


    public String getTopic_img_url() {
        return topic_img_url;
    }

}
