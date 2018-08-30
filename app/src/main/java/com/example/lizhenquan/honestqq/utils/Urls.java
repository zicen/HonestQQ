package com.example.lizhenquan.honestqq.utils;


public class Urls {
    //主机地址
    public static final String BASE             = "http://www.oschina.net/action/";
    //apiv2地址
    public static final String APIV2_BASE       = BASE + "apiv2/";
    //api地址
    public static final String API_BASE         = BASE + "api/";
    //最新动弹
    public static final String DT         = APIV2_BASE+"tweets?pageToken=";
    //登陆
    public static final String LOGIN            = API_BASE + "login_validate";
    //发布动弹第一步
    public static final String PUBLISH_DT_IMAGE = BASE + "apiv2/resource_image";
    //发布动弹第二步
    public static final String PUBLISH_DT_DESC  = BASE + "apiv2/tweet";

}
