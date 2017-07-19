package com.example.lizhenquan.honestqq.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.RegistPresenter;
import com.example.lizhenquan.honestqq.presenter.RegistPresenterImpl;
import com.example.lizhenquan.honestqq.utils.StringUtils;
import com.example.lizhenquan.honestqq.view.RegistView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements TextView.OnEditorActionListener,RegistView {

    @InjectView(R.id.et_username)
    EditText        mEtUsername;
    @InjectView(R.id.til_username)
    TextInputLayout mTilUsername;
    @InjectView(R.id.et_pwd)
    EditText        mEtPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @InjectView(R.id.btn_register)
    Button          mBtnRegister;
    @InjectView(R.id.activity_login)
    LinearLayout    mActivityLogin;
    private RegistPresenter mRegistPresenter;
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
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        mEtPwd.setOnEditorActionListener(this);
        mRegistPresenter = new RegistPresenterImpl(this);
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        regist();
    }

    /**
     *
     * @param textView  其实就是用到了多态而已，TextView是EditText的父类
     * @param i    软键盘右下角的哪个按键
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() == mEtPwd.getId()) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                regist();
                //返回true就告诉系统我们自己消费掉了此事件。
                return true;
            }
        }
        //返回false则让输入法自己处理事件
        return false;
    }

    private void regist() {
        /**
         *1、获取用户名和密码
         * 2、校验数据
         *    1。用户名：3到20位的字符，首字符必须为英文
         *    2.密码：3到20位的数字
         * 3、提交给服务器
         */
        String username = mEtUsername.getText().toString();
        String pwd = mEtPwd.getText().toString();
        if (!StringUtils.checkUsername(username)) {
            //用户名不合法
            //1、显示错误信息
            mTilUsername.setErrorEnabled(true);
            mTilUsername.setError("用户名不合法！必须是3-20位的字符");
            //2、定位焦点
            mEtUsername.requestFocus(View.FOCUS_RIGHT);

            return;
        } else {
            mTilUsername.setErrorEnabled(false);
            mTilUsername.setError("");

        }
        if (!StringUtils.checkPwd(pwd)) {
            mTilPwd.setErrorEnabled(true);
            mTilPwd.setError("密码不合法");
            mEtPwd.requestFocus(View.FOCUS_RIGHT);
            return;
        } else {
            mTilPwd.setErrorEnabled(false);
            mTilPwd.setError("");
        }
        //显示进度条对话框
        showDialog("正在注册...");
        mRegistPresenter.regist(username,pwd);

    }

    @Override
    public void onRegist(String username, String pwd, boolean isSuccess, String message) {
        /**
         * 1、隐藏对话框
         * 2、如果注册成功，跳转到登录界面，弹出Toast，以及辉县usernaem和pwd(保存到sp)
         * 3、如果失败，弹出Toast提示
         */
        hideDialog();
        if (isSuccess) {
            showToast("注册成功！");
            saveUser(username, pwd);
            startActivity(LoginActivity.class,false);

        } else {
            showToast("注册失败"+message);
        }
    }
}
