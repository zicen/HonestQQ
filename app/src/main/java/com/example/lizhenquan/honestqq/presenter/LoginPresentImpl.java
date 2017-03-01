package com.example.lizhenquan.honestqq.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.lizhenquan.honestqq.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqq.model.User;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.LoginView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

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
        AVUser.logInInBackground(username, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
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
        });

    }

    /**
     * 登录界面手机号注册逻辑
     * @param phone
     * @param phonePassword
     */
    @Override
    public void regist(final String phone, final String phonePassword) {
        /**
         * 1、先注册云数据库平台
         * 2、成功--》注册环信
         * 3、失败--》将云数据库平台的数据删除
         */

        final User user = new User();
        user.setUsername(phone);
        user.setPassword(phonePassword);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功，注册环信

                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(phone, phonePassword);//同步方法
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginView.onRegist(phone,phonePassword,true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //环信注册失败，我们就要删除云数据库上刚刚添加的数据
                                //通知主线程修改UI
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginView.onRegist(phone,phonePassword,false,e1.getMessage());
                                    }
                                });
                                try {
                                    user.delete();
                                } catch (AVException e2) {
                                    e2.printStackTrace();
                                }

                            }
                        }
                    });

                } else {
                    // 失败的话,将错误信息发送给view
                    mLoginView.onRegist(phone,phonePassword,false,e.getMessage());
                }
            }
        });

    }
}
