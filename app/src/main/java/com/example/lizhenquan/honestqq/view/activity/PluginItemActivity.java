package com.example.lizhenquan.honestqq.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.ToastUtils;

public class PluginItemActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebview_music;
    private Toolbar mToolbar;
    private TextView mTv_title;
    private TextView mMusic_tv_back;
    private ProgressBar mProgress_load;
    private String Urls[] = {"http://qzone.qq.com/","http://map.baidu.com/","http://qqgame.qq.com/","https://www.zhihu.com/","http://www.ikandian.com/","http://book.qq.com/","http://ac.qq.com/",
            "http://music.163.com/","http://www.qqlubo.net/","http://www.baihe.com/betatest/betatest_newlandpage.html",
            "http://sports.qq.com/","https://ke.qq.com/course/list"};
    private String Title[] = {"空间","地图","QQ游戏","知乎","QQ看点","QQ阅读","QQ动漫","网易云音乐","QQ直播","百合网","QQ运动","QQ课堂"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        String url = getIntent().getStringExtra(Constant.URL);
        if (url == null) {
            ToastUtils.showToast(this,"没有找到页面！");
            finish();
            return;
        }

        mWebview_music = (WebView)findViewById(R.id.webview_music);
        mToolbar = (Toolbar) findViewById(R.id.toorbar);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mMusic_tv_back = (TextView) findViewById(R.id.music_tv_back);
        mProgress_load = (ProgressBar) findViewById(R.id.progress_load);
        mMusic_tv_back.setOnClickListener(this);
        //设置固定标题
        for (int i = 0; i < Urls.length; i++) {
            if (TextUtils.equals(url, Urls[i])) {
                mTv_title.setText(Title[i]);
            }
        }

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mWebview_music.loadUrl(url);

        WebSettings settings = mWebview_music.getSettings();
        settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 支持js功能
        mWebview_music.setWebViewClient(new WebViewClient() {
            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载网页了");
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                mProgress_load.setVisibility(View.INVISIBLE);

            }

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载

                //此方法还有其他应用场景, 比如写一个超链接<a href="tel:110">联系我们</a>,
                // 当点击该超链接时,可以在此方法中获取链接地址tel:110,
                // 解析该地址,获取电话号码, 然后跳转到本地打电话页面, 而不是加载网页, 从而实现了webView和本地代码的交互

                return true;
            }
        });
        mWebview_music.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if(newProgress==100){
                    mProgress_load.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    mProgress_load.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgress_load.setProgress(newProgress);//设置进度值
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                mTv_title.setText(title);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(mWebview_music.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                mWebview_music.goBack();
                return true;
            }
            else {//当webview处于第一页面时,直接退出程序
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_tv_back:
                this.finish();
                        break;

                    default:
                        break;

                }
    }
}
