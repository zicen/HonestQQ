package com.example.lizhenquan.honestqq.view.fragment;

import android.content.Intent;
import android.view.View;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.view.CoolWeatherActivity;
import com.example.lizhenquan.honestqq.view.PluginView;
import com.example.lizhenquan.honestqq.wight.MineItemLayout;

/**
 * Created by lizhenquan on 2017/2/17.
 */

public class PluginFragment extends BaseFragment implements View.OnClickListener,PluginView {

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin, null);
        MineItemLayout btn_getweather = (MineItemLayout) view.findViewById(R.id.ib_weather);
        btn_getweather.setOnClickListener(this);

        return view;
    }



    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_weather:
                startActivity(new Intent(mContext, CoolWeatherActivity.class));
                break;
        }

    }


    @Override
    public void onUploadImag(boolean isSuccess, String msg) {

    }
}
