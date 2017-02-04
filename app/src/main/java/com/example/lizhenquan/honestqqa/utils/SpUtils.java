package com.example.lizhenquan.honestqqa.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lizhenquan on 2017/1/17.
 */


public class SpUtils {
    private static SharedPreferences sp;

    /**获取一个SharedPreferences需要三个参数
     * @param context
     * @param string     key
     * @param value
     */
    public static void putBoolean(Context context,String string,boolean value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(string, value).commit();
    }

    public static boolean getBoolean(Context context,String string,boolean value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(string, false);
    }

    /**
     * @param context
     * @param string     key
     * @param value
     */
    public static void putString(Context context, String string, String value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(string, value).commit();
    }

    public static String getString(Context context,String string,String value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(string,value);
    }

    public static void removeSeriaNum(Context context,
                                      String key) {
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();

    }
}

