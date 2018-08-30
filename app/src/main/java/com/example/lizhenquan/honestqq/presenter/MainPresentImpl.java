package com.example.lizhenquan.honestqq.presenter;

import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
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
    public static final String TAG = MainPresentImpl.class.getSimpleName();
    private MainView mMainView;
    private AVUser mCurrentUser;
    private final AVObject mAVObject;

    public MainPresentImpl(MainView mainView) {
        mMainView = mainView;
        mCurrentUser = AVUser.getCurrentUser();
        mAVObject = new AVObject(Constant.UPLOADIMG);
    }


    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEMCallBack() {
            @Override
            public void onMainSuccess() {//注销成功
                //跳转到登陆界面
                mMainView.onLogout(true, "");
            }

            @Override
            public void onMainError(int i, String s) {//注销失败
                //跳转到登陆界面
                mMainView.onLogout(false, s);
            }
        });
    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username, "request reason...");
                    //成功请求，通知UI
                    mMainView.onAddContact(username, true, null);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //请求失败，通知UI
                    mMainView.onAddContact(username, false, e.getMessage());
                }
            }
        });
    }

    @Override
    public void uploadImag(String path) {
        try {
            //然后上传头像
            final AVFile avFile = AVFile.withAbsoluteLocalPath("head.png", path);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    final String url = avFile.getUrl();
                    if (url != null && !url.equals("")) {
                        if (mCurrentUser != null) {
                            mCurrentUser.put(Constant.PORTRAITURL, url);
                            mCurrentUser.put(Constant.HEADOBJECTID, avFile.getObjectId());
                            mCurrentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    //TODO
                                    mMainView.onUploadImag(true, null);
                                }
                            });
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mMainView.onUploadImag(false, e.toString());
        }
    }

    @Override
    public void uploadPhoto(String filepath) {
        try {
            //然后上传头像
            final AVFile avFile = AVFile.withAbsoluteLocalPath("photo.jpg", filepath);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    final String url = avFile.getUrl();
                    if (url != null && !url.equals("")) {
                        Log.e(TAG, "Url:" + url);

                        mAVObject.put("userId", mCurrentUser.getObjectId());
                        mAVObject.put("ImgUrl", url);

                        mAVObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Log.e(TAG, "save success");
                                mMainView.onUploadPhoto(true, null);
                            }
                        });
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mMainView.onUploadPhoto(false, e.toString());
        }
    }

    /**
     * 获取当前用户头像的URL
     *
     * @return
     */
    @Override
    public String getHeadUrl() {
        if (mCurrentUser != null) {
            String portraitUrl = (String) mCurrentUser.get(Constant.PORTRAITURL);
            return portraitUrl;
        }

        return "";
    }
}
