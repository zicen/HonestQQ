package com.example.lizhenquan.honestqq.presenter;

import com.example.lizhenquan.honestqq.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqq.view.LoginView;
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
