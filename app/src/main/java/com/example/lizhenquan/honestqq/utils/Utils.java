package com.example.lizhenquan.honestqq.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.example.lizhenquan.honestqq.MyApplication;


public class Utils {


    /**
     * dpתpx
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context,float dp)
    {
        return (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
    //在屏幕适配时候使用,让代码中使用dip属性
    public static int getDimens(int resId) {

        return getResources().getDimensionPixelSize(resId);
    }

    //得到资源管理的类
    public static Resources getResources() {
        return MyApplication.context.getResources();
    }
    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( outMetrics);
        return outMetrics .widthPixels ;
    }
}
