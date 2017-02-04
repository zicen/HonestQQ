package com.example.lizhenquan.honestqqa.presenter;

import com.example.lizhenquan.honestqqa.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqqa.view.LoginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class LoginPresentImpl implements LoginPresenter {
    private LoginView mLoginView;

    public LoginPresentImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {

        EMClient.getInstance().login(username, pwd, new MyEMCallBack() {
            @Override
            public void onMainSuccess() {
                mLoginView.onLogin(username,pwd,true,null);
            }

            @Override
            public void onMainError(int i, String s) {
                mLoginView.onLogin(username,pwd,false,s);
            }
        });
    }
}
