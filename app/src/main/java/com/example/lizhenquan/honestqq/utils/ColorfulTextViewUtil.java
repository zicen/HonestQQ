package com.example.lizhenquan.honestqq.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.example.lizhenquan.honestqq.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Maj1nBuu
 * @data 2017/2/26 11:23.
 * @overView 文本添加图片-富文本
 */

public class ColorfulTextViewUtil {

    /**
     * 判断上传时间是否是今天
     * @param pubDate 上传时间
     * @return
     */
    public static boolean publicDataIsToday(String pubDate) {
        return PubDate(pubDate).equals(currentData());
    }

    /**
     * 添加多张图片的SpannableString
     * @param title 添加富文本的文本
     * @param drawableId 图片ID. For Example:R.mipmap.ic_lunch.
     * @return SpannableString
     */
    public static SpannableString addTagWord(String title, int... drawableId) {
        SpannableString spannableString = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2*drawableId.length; i++) {
            sb.append(" ");
        }
        for (int i = 0; i < drawableId.length; i++) {
            if (i == 0) {
                sb.append(title);
                spannableString = new SpannableString(sb);
            } else {
                spannableString = new SpannableString(spannableString);
            }
            Drawable drawable = MyApplication.context.getResources().getDrawable(drawableId[i]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            spannableString.setSpan(new ImageSpan(drawable), i*2, i*2+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * @param pubDate 上传时间 类型为"2017-02-20 10:54:05"
     * @return 返回上传日期, 格式:yyyy-MM-dd
     */
    private static String PubDate(String pubDate) {
        String[] split = pubDate.split(" ");
        return split[0];
    }

    /**
     * 获取当前时间
     * @return 返回目前的日期, 格式:yyyy-MM-dd
     */
    private static String currentData() {
        Date date = new Date();
        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        String[] split = s.split(" ");
        return split[0];
    }
}
