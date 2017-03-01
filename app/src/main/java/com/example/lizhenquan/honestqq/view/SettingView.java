package com.example.lizhenquan.honestqq.view;

/**
 * Created by lizhenquan on 2017/1/18.
 */

public interface SettingView {
    void onLogout(boolean isSuccess, String msg);

    void onAddContact(String username, boolean isSuccess, String msg);
}
