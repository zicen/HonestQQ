package com.example.lizhenquan.honestqqa.wight;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lizhenquan.honestqqa.R;

/**
 * Created by lizhenquan on 2017/1/18.
 */

public class SettingItemView extends RelativeLayout {

    private TextView mSetting_tv_title;
    private ImageView mIv_setting_user;
    private TextView mTv_telephonenum;

    public SettingItemView(Context context) {
        super(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.setting_item, null);
        this.addView(view);
        mSetting_tv_title = (TextView) view.findViewById(R.id.setting_tv_title);
        mIv_setting_user = (ImageView) view.findViewById(R.id.iv_setting_user);
        mTv_telephonenum = (TextView) view.findViewById(R.id.tv_telephonenum);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String title = typedArray.getString(R.styleable.SettingItemView_title);
        boolean isSettingImage = typedArray.getBoolean(R.styleable.SettingItemView_rightImage, false);
        if (isSettingImage) {
            mIv_setting_user.setVisibility(View.VISIBLE);
        }
        boolean isSettingText = typedArray.getBoolean(R.styleable.SettingItemView_rightText, false);
        if (isSettingText) {
            mTv_telephonenum.setVisibility(View.VISIBLE);
        }
        mSetting_tv_title.setText(title);
    }


}
