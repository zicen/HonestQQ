package com.example.lizhenquan.honestqqa.adapter;

import com.example.lizhenquan.honestqqa.utils.ThreadUtils;
import com.hyphenate.EMCallBack;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public abstract  class MyEMCallBack implements EMCallBack {
    public  abstract void onMainSuccess();
    public  abstract void onMainError(int i, String s);
    @Override
    public void onSuccess() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                onMainError(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {

    }
}
