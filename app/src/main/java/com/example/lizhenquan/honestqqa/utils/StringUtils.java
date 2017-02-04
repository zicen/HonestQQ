package com.example.lizhenquan.honestqqa.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class StringUtils {
    public static boolean checkUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        } else {
            return username.matches("^[a-zA-Z]\\w{2,19}$");
        }
    }

    public static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        } else {
            return pwd.matches("^\\d{3,20}$");
        }
    }

    public static String getInitial(String contact) {
        if (TextUtils.isEmpty(contact)) {
            return "";
        } else {
            return contact.substring(0, 1).toUpperCase();
        }
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
