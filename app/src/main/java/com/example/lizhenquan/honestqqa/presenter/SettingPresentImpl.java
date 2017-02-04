package com.example.lizhenquan.honestqqa.presenter;

import com.example.lizhenquan.honestqqa.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqqa.view.SettingView;
import com.hyphenate.chat.EMClient;

/**
 * Created by lizhenquan on 2017/1/18.
 */

public class SettingPresentImpl implements SettingPresenter {
    private SettingView mSettingView;

    public SettingPresentImpl(SettingView settingView) {
        mSettingView = settingView;
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEMCallBack() {
            @Override
            public void onMainSuccess() {//注销成功
                //跳转到登陆界面
                mSettingView.onLogout(true,"");
            }

            @Override
            public void onMainError(int i, String s) {//注销失败
                //跳转到登陆界面
                mSettingView.onLogout(false,s);
            }
        });
    }
}
