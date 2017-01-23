package com.example.lizhenquan.honestqq.utils;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lizhenquan on 2016/12/22.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
       client.newCall(request).enqueue(callback);

    }
}
