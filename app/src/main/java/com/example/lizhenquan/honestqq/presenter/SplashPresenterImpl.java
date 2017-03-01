package com.example.lizhenquan.honestqq.presenter;

import com.example.lizhenquan.honestqq.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        this.mSplashView = splashView;
    }

    @Override
    public void checkLogin() {
        //如果曾经登陆过并且已经连接到服务器，则可以直接跳转到主界面
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            mSplashView.onCheckLogin(true);
        } else {
            mSplashView.onCheckLogin(false);
        }
    }
}
