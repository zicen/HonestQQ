package com.example.lizhenquan.honestqq.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class StringUtils {


    public static boolean hasEmpty(String... strs) {
        for (String str : strs) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取时间差
     *
     * @param date
     * @return
     */
    public static String getDate(String date) {
        String times = null;
        try {
            long time = stringToLong(date, "yyyy-MM-dd HH:mm:ss");
            long currentTimeMillis = System.currentTimeMillis();
            long preTime = (currentTimeMillis - time) / 1000;
            if (preTime < 60) {
                times = preTime + "秒钟之前";
            } else if (preTime < 60 * 60) {
                times = preTime / 60 + "分钟之前";
            } else if (preTime < 60 * 60 * 24) {
                times = preTime / 60 / 60 + "小时之前";
            } else if (preTime < 60 * 60 * 24 * 2) {
                times = "昨天";
            } else if (preTime < 60 * 60 * 24 * 3) {
                times = "前天";
            } else {
                times = preTime / 60 / 60 / 24 + "天之前";
            }
            return times;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static boolean checkUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        } else {
            return username.matches("^[a-zA-Z]\\w{2,19}$");
        }
    }
    public  static  boolean isEmailAdaress(String email){
        //电子邮件
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher("dffdfdf@qq.com");
        boolean isMatched = matcher.matches();
        return  isMatched;
    }
    public static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        } else {
            //return pwd.matches("^\\d{3,20}$");
            return pwd.matches("^\\w+$");
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

    /**
     * 获取电话号码的后六位作为密码
     *
     * @return
     */
    public static String getPhonePassword(String phone) {
        return phone.substring(5);
    }
}
