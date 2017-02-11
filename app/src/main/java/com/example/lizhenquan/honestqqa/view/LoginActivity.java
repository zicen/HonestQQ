package com.example.lizhenquan.honestqqa.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.presenter.LoginPresentImpl;
import com.example.lizhenquan.honestqqa.presenter.LoginPresenter;
import com.example.lizhenquan.honestqqa.utils.StringUtils;
import com.example.lizhenquan.honestqqa.utils.ToastUtils;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {

    @InjectView(R.id.et_username)
    EditText        mEtUsername;
    @InjectView(R.id.til_username)
    TextInputLayout mTilUsername;
    @InjectView(R.id.et_pwd)
    EditText        mEtPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @InjectView(R.id.btn_login)
    Button          mBtnLogin;
    @InjectView(R.id.tv_newuser)
    TextView        mTvNewuser;
    @InjectView(R.id.activity_login)
    LinearLayout    mActivityLogin;
    @InjectView(R.id.tv_newuser_sms)
    TextView        mTvNewuserSms;
    private LoginPresenter mLoginPresenter;
    private static final String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};

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
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mEtPwd.setOnEditorActionListener(this);
        mLoginPresenter = new LoginPresentImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = getUsername();
        String pwd = getPwd();
        mEtUsername.setText(username);
        mEtPwd.setText(pwd);
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser, R.id.tv_newuser_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_newuser:
                //跳转到注册界面
                startActivity(RegistActivity.class, false);
                break;
            case R.id.tv_newuser_sms:
                registByMobSms();
                break;
        }
    }

    private void registByMobSms() {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    //P层执行注册逻辑
                    String randomLetter = getRandomLetter();

                    mLoginPresenter.regist(randomLetter + phone, StringUtils.getPhonePassword(phone));
                }
            }
        });
        registerPage.show(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //被授权了，可以登陆
                showToast("授权成功！");
                login();
            } else {
                showToast("授权失败！将无法登陆！");
            }
        }
    }

    private void login() {
        /**
         * 动态权限申请
         */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限，系统会弹出一个对话框，当用户点击同意或者拒绝时，系统会回调当前Activity的onRequestPermissionsResult
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

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
        showDialog("正在登录...");

        mLoginPresenter.login(username, pwd);

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (mEtPwd.getId() == textView.getId()) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onLogin(String username, String pwd, boolean isSuccess, String msg) {
        /**
         * 1、隐藏对话框
         * 2、如果登录成功-->进入MianActivity，并且保存Sp
         * 3、如果登录失败-->弹出错误提示
         */
        hideDialog();
        if (isSuccess) {
            saveUser(username, pwd);
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
            Log.d("main", "登录聊天服务器成功！");
            startActivity(MainActivity.class, true);
        } else {
            ToastUtils.showToast(this, "登录失败!" + msg);
        }
    }

    @Override
    public void onRegist(String phone, String phonePassword, boolean isSuccess, String msg) {
        if (isSuccess) {
            showToast("注册成功！密码为电话号码后六位。");
            mEtUsername.setText(phone);
            mEtPwd.setText(phonePassword);
        } else {

        }
    }


    public String getRandomLetter() {
        Random random = new Random();
        while (true) {
            int nextInt = random.nextInt() % 26;
            if (nextInt >= 0) {
                return arr[nextInt];
            }
        }
    }
}
