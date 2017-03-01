package com.example.lizhenquan.honestqq.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lizhenquan on 2017/2/4.
 */

public class FileViewPagerAdapter extends PagerAdapter {
    private String[] title = {"应用","图片","音乐","视频"};
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        TextView textView = new TextView(container.getContext());
        textView.setText(title[position]);

        //添加到viewpager组中
        container.addView(textView);

        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**************为了让页签显示标题**************/
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position] ;
    }
}
