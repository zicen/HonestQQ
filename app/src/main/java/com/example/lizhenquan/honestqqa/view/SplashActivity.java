package com.example.lizhenquan.honestqqa.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.presenter.SplashPresenter;
import com.example.lizhenquan.honestqqa.presenter.SplashPresenterImpl;

public class SplashActivity extends BaseActivity implements SplashView{
    private SplashPresenter mSplashPresenter;
    private ImageView mIv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在设置布局之前，设置沉浸式状态栏，
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }
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
