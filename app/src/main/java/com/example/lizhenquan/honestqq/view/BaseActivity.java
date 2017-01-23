package com.example.lizhenquan.honestqq.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.ToastUtils;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private SharedPreferences mSp;

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
        mSp = getSharedPreferences("config", MODE_PRIVATE);

    }
    public void saveUser(String username,String pwd){
        mSp.edit().putString(Constant.USERNAME,username)
                .putString(Constant.PWD,pwd)
                .commit();
    }
    public String getUsername(){
        return mSp.getString(Constant.USERNAME,"");
    }
    public String getPwd(){
        return mSp.getString(Constant.PWD,"");
    }

    public void startActivity(Class<? extends BaseActivity> clazz,boolean isFinishSelf){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (isFinishSelf) {
            this.finish();
        }
    }

    public void showDialog(String msg){
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
        }
        mDialog.setCancelable(false);
        mDialog.setMessage(msg);
        mDialog.show();

    }
    public void hideDialog(){
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
    public void showToast(String msg){
        ToastUtils.showToast(this,msg);
    }
}
