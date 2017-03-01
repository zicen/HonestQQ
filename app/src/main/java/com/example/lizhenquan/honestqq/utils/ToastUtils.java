package com.example.lizhenquan.honestqq.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class ToastUtils {
    private static Toast sToast;

    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            //这里一定要getApplicationContext，否则会造成内存泄漏
            sToast = Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
