package com.example.lizhenquan.honestqq.view;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.SettingPresentImpl;
import com.example.lizhenquan.honestqq.presenter.SettingPresenter;
import com.example.lizhenquan.honestqq.wight.SettingItemView;

public class SettingActivity extends BaseActivity implements SettingView,View.OnClickListener {
    private SettingPresenter mSettingPresenter;

    private TextView mSetting_tv_back;
    private SettingItemView mSiv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        mSetting_tv_back = (TextView) findViewById(R.id.setting_tv_back);
        mSiv_logout = (SettingItemView) findViewById(R.id.siv_logout);
        mSetting_tv_back.setOnClickListener(this);
        mSiv_logout.setOnClickListener(this);
        mSettingPresenter = new SettingPresentImpl(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.setting_tv_back:

            this.finish();

                break;
            case R.id.siv_logout:
                /**
                 * 1.弹出确认对话框
                 * 2.发送网络请求，执行注销操作
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                View dialog_exit = View.inflate(SettingActivity.this, R.layout.dialog_exit, null);
                builder.setView(dialog_exit);
                Button btn_cancel = (Button) dialog_exit.findViewById(R.id.btn_cancel);
                Button btn_exit = (Button) dialog_exit.findViewById(R.id.btn_exit);

                final AlertDialog ad = builder.create();
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        mSettingPresenter.logout();
                    }
                });
                ad.show();

                break;



        }
    }

    @Override
    public void onLogout(boolean isSuccess, String msg) {
        if (!isSuccess) {
            showToast(msg);
        }
        startActivity(LoginActivity.class,true);

    }
}
