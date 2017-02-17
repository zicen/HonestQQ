package com.example.lizhenquan.honestqqa.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.view.CoolWeatherActivity;

/**
 * Created by lizhenquan on 2017/2/17.
 */

public class PluginFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin, null);
        ImageButton btn_getweather = (ImageButton) view.findViewById(R.id.ib_weather);
        btn_getweather.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(mContext, CoolWeatherActivity.class));
    }
}
