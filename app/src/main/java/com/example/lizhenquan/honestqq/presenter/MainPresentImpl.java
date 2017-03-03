package com.example.lizhenquan.honestqq.presenter;

import android.os.Environment;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.lizhenquan.honestqq.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.MainView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.FileNotFoundException;

/**
 * Created by lizhenquan on 2017/1/18.
 */

public class MainPresentImpl implements MainPresenter {
    private MainView mMainView;
    private AVUser   mCurrentUser ;
    public MainPresentImpl(MainView mainView) {
        mMainView = mainView;
        mCurrentUser = AVUser.getCurrentUser();
    }


    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEMCallBack() {
            @Override
            public void onMainSuccess() {//注销成功
                //跳转到登陆界面
                mMainView.onLogout(true,"");
            }

            @Override
            public void onMainError(int i, String s) {//注销失败
                //跳转到登陆界面
                mMainView.onLogout(false,s);
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
                    mMainView.onAddContact(username,true,null);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //请求失败，通知UI
                    mMainView.onAddContact(username,false,e.getMessage());
                }
            }
        });
    }

    @Override
    public void uploadImag() {
        try {
            /*//如果获取到头像不为空就先删除头像
            final String headObjectId = (String) mCurrentUser.get(Constant.HEADOBJECTID);
            if (headObjectId != null && !headObjectId.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final AVFile avFileDel;
                        try {
                            avFileDel = AVFile.withObjectId(headObjectId);
                            avFileDel.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    //TODO
                                }
                            });
                        } catch (AVException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

          }*/
            //然后上传头像
            final AVFile avFile = AVFile.withAbsoluteLocalPath("head.png", Environment.getExternalStorageDirectory() + "/HonestQQ/myHead/head.jpg");
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    final String url = avFile.getUrl();
                    if (url != null && !url.equals("")) {
                        mCurrentUser.put(Constant.PORTRAITURL, url);
                        mCurrentUser.put(Constant.HEADOBJECTID, avFile.getObjectId());
                        mCurrentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                //TODO
                                mMainView.onUploadImag(true,null);
                            }
                        });
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mMainView.onUploadImag(true,e.toString());
        }
    }

    /**
     * 获取当前用户头像的URL
     * @return
     */
    @Override
    public String getHeadUrl() {
        String portraitUrl = (String) mCurrentUser.get(Constant.PORTRAITURL);
        return  portraitUrl;
    }
}
