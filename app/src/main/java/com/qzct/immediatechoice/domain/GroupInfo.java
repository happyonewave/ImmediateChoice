package com.qzct.immediatechoice.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2017/4/1.
 */
public class GroupInfo {
    private int group_id;
    private String name;

    public int getGroup_id() {
        return group_id;
    }

    public String getName() {
        return name;
    }

    public GroupInfo() {
    }

    public GroupInfo(int group_id, String name) {
        this.group_id = group_id;
        this.name = name;
    }

    public static List<GroupInfo> tolistFrom(JSONArray jsonArray) throws JSONException {
        List<GroupInfo> infoList = new ArrayList<GroupInfo>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject temp = jsonArray.optJSONObject(i);
            GroupInfo info = new GroupInfo(temp.optInt("group_id"), temp.optString("name"));
            infoList.add(info);
        }
        return infoList;
    }
}
