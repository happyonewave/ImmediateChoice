package com.qzct.immediatechoice.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qin on 2017/3/12.
 */
public class Topic {
    int topic_id;
    String topic_title;
    String topic_img_url;

    public static Topic jsonObjectToTotic(JSONObject object) throws JSONException {
        int id = object.getInt("topic_id");
        String title = object.getString("topic_title");
        String img_url = object.getString("topic_img_url");
        return new Topic(id, title, img_url);
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("topic_id", topic_id);
        object.put("topic_title", topic_title);
        object.put("topic_img_url", topic_img_url);
        return object;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public Topic(int topic_id, String topic_title, String topic_img_url) {
        this.topic_id = topic_id;
        this.topic_title = topic_title;
        this.topic_img_url = topic_img_url;
    }


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
