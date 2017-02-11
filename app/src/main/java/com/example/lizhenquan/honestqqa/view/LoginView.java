package com.example.lizhenquan.honestqqa.view;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public interface LoginView {
    void onLogin(String username, String pwd, boolean isSuccess, String msg);

    void onRegist(String phone, String phonePassword, boolean isSuccess, String msg);
}
