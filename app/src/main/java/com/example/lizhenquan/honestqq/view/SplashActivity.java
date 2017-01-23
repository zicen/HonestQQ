package com.example.lizhenquan.honestqq.view;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.SplashPresenter;
import com.example.lizhenquan.honestqq.presenter.SplashPresenterImpl;

public class SplashActivity extends BaseActivity implements SplashView{
    private SplashPresenter mSplashPresenter;
    private ImageView mIv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mIv_splash = (ImageView) findViewById(R.id.iv_splash);

        /**
         * 1.首先判断当前是否登录，登录则直接进入主界面
         * 2.没有登录，则闪屏2s跳转到登录界面
         */
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.checkLogin();

    }

    @Override
    public void onCheckLogin(boolean isLogin) {
        if (isLogin) {
            //直接进入主界面
            startActivity(MainActivity.class,true);
        } else {
            //闪屏2s跳转到登录界面
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        startActivity(LoginActivity.class,true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
