package com.example.lizhenquan.honestqq.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lizhenquan.honestqq.MyApplication;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.ToastUtils;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private SharedPreferences mSp;
    private MyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mApplication = (MyApplication) getApplication();
        mApplication.addActivity(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
      mApplication.removeActivity(this);
    }
}
