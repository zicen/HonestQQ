package com.example.lizhenquan.honestqqa.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.example.lizhenquan.honestqqa.model.User;
import com.example.lizhenquan.honestqqa.utils.ThreadUtils;
import com.example.lizhenquan.honestqqa.view.RegistView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class RegistPresenterImpl implements RegistPresenter {
    private RegistView mRegistView;

    public RegistPresenterImpl(RegistView registView) {
        this.mRegistView = registView;
    }

    @Override
    public void regist(final String username, final String pwd) {
        /**
         * 1、先注册云数据库平台
         * 2、成功--》注册环信
         * 3、失败--》将云数据库平台的数据删除
         */

        final User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功，注册环信

                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);//同步方法
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegistView.onRegist(username,pwd,true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //环信注册失败，我们就要删除云数据库上刚刚添加的数据
                                //通知主线程修改UI
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegistView.onRegist(username,pwd,false,e1.getMessage());
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
                    mRegistView.onRegist(username,pwd,false,e.getMessage());
                }
            }
        });

    }
}
