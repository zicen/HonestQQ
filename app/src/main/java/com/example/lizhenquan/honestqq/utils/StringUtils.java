package com.example.lizhenquan.honestqq.utils;

import android.text.TextUtils;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class StringUtils {
    public static boolean checkUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return  false;
        }else {
            return username.matches("^[a-zA-Z]\\w{2,19}$");
        }
    }

    public static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return  false;
        }else {
            return pwd.matches("^\\d{3,20}$");
        }
    }

    public static String getInitial(String contact) {
        if (TextUtils.isEmpty(contact)) {
            return "";
        } else {
            return contact.substring(0,1).toUpperCase();
        }

    }
}
