package com.qzct.immediatechoice.util;

/**
 * Created by qin on 2017/3/19.
 */

public class Config {
//                    public static String url = "http://172.18.1.153:8080/ImmediateChoice_service/";
    public static String url = "http://15.164.47.218:8080/ImmediateChoice_service/";
//    public static String url = "http://server.luckboyqin.cn/ImmediateChoice_service/";
//    public static String url = "http://192.168.1.200:8080/Server/";

    public static String url_login = url + "LoginServlet";
    public static String url_user = url + "UserServlet";
    public static String url_image_text = url + "ImageTextServlet";
    public static String url_upload = url + "UploadServlet";
    public static String url_register = url + "RegisterServlet";
    public static String url_comment = url + "CommentServlet";
    public static String url_topic = url + "TopicServlet";
    public static String url_topic_add = url + "topic/add";
    public static String url_friend = url + "FriendServlet";
    public static String url_group = url + "GroupServlet";
    public static String url_search = url + "SearchServlet";
        public static String url_questionnaire =url + "QuestionnaireServlet";
//    public static String url_questionnaire = "http://192.168.1.200:8080/Server/" + "QuestionnaireServlet";
    public static String url_chart = "http://server.luckboyqin.cn/immediatechoiceCharts/";

    public static String server_img_url = url + "image/";
    public static String server_video_url = url + "video/";
    public static String unixTime_min = "1970-01-01 08:00:00";
}
