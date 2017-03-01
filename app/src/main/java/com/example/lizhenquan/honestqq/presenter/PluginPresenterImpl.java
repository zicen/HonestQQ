package com.example.lizhenquan.honestqq.presenter;

import android.os.Environment;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.view.PluginView;

import java.io.FileNotFoundException;

/**
 * Created by lizhenquan on 2017/2/18.
 */

public class PluginPresenterImpl implements PluginPresenter {
    private final PluginView mPluginView;
    private       AVUser     mCurrentUser ;

    public PluginPresenterImpl(PluginView pluginView) {
        this.mPluginView = pluginView;
        mCurrentUser = AVUser.getCurrentUser();
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
                                mPluginView.onUploadImag(true,null);
                            }
                        });
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mPluginView.onUploadImag(true,e.toString());
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
