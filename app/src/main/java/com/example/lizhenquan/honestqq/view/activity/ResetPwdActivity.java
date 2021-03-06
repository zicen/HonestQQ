package com.example.lizhenquan.honestqq.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ResetPwdActivity extends AppCompatActivity {


    @InjectView(R.id.et_email_address)
    EditText mEtEmailAddress;
    @InjectView(R.id.btn_bindemail)
    Button   mBtnBindemail;
    @InjectView(R.id.btn_watchemail)
    Button   mBtnWatchemail;
    @InjectView(R.id.btn_resetpwd)
    Button   mBtnResetpwd;
    @InjectView(R.id.tv_email_now)
    TextView mTvEmailNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.inject(this);

        mTvEmailNow.setText("当前邮箱地址:"+AVUser.getCurrentUser().getString("email"));
    }

    @OnClick({R.id.btn_bindemail, R.id.btn_watchemail, R.id.btn_resetpwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bindemail:
                bindEmail();
                break;
            case R.id.btn_watchemail:
                mTvEmailNow.setText("当前邮箱地址:"+AVUser.getCurrentUser().getString("email"));
                break;
            case R.id.btn_resetpwd:
                resetPwd();
                break;
        }
    }

    private void bindEmail() {
        final String email = mEtEmailAddress.getText().toString();
        System.out.println("email" + email);

        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    AVUser.getCurrentUser().put("email", email);
                    AVUser.getCurrentUser().saveInBackground();
                    ToastUtils.showToast(ResetPwdActivity.this,"绑定邮箱成功");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void resetPwd() {
        String currentEmail = AVUser.getCurrentUser().getString("email");
        System.out.println("currentEmail:" + currentEmail);
        AVUser.requestPasswordResetInBackground(currentEmail, new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.showToast(ResetPwdActivity.this, "邮件发送成功!");
                    System.out.println("btn_resetpwd is clicked2");
                } else {
                    e.printStackTrace();
                    if (e.getCode() == 205) {//An user with the specified email was not found
                        ToastUtils.showToast(ResetPwdActivity.this, "没有找到绑定此邮箱的用户！请先绑定邮箱。");
                    } else if (e.getCode() == 1) {
                        ToastUtils.showToast(ResetPwdActivity.this, "请不要往同一个邮件地址发送太多邮件。");
                    } else if (e.getCode() == 204) {
                        ToastUtils.showToast(ResetPwdActivity.this, "The email is missing, and must be specified");
                    } else {
                        ToastUtils.showToast(ResetPwdActivity.this, e.toString());
                    }
                }
            }
        });
    }


}
