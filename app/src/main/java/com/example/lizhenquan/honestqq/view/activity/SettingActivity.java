package com.example.lizhenquan.honestqq.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.view.activity.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSetting_tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        mSetting_tv_back = (TextView) findViewById(R.id.setting_tv_back);
        mSetting_tv_back.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.setting_tv_back:

            this.finish();

                break;



        }
    }

}
