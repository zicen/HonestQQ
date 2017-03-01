package com.example.lizhenquan.honestqq.presenter;

import com.example.lizhenquan.honestqq.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.SettingView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

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

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"request reason...");
                    //成功请求，通知UI
                    mSettingView.onAddContact(username,true,null);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //请求失败，通知UI
                    mSettingView.onAddContact(username,false,e.getMessage());
                }
            }
        });
    }
}
