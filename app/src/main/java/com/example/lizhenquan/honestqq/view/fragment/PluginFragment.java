package com.example.lizhenquan.honestqq.view.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.PluginListAdapter;
import com.example.lizhenquan.honestqq.view.CoolWeatherActivity;
import com.example.lizhenquan.honestqq.wight.MyListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment implements View.OnClickListener {
    private String title[] = {"游戏", "日迹", "看点", "阅读", "动漫", "音乐", "直播"};
    private int    image[] = {R.mipmap.lzc, R.mipmap.day, R.mipmap.kandian, R.mipmap.lzd, R.mipmap.dongman, R.mipmap.music, R.mipmap.zhibo};
    private String title2[] = {"同城服务", "运动", "腾讯课堂"};
    private int image2[] = {R.mipmap.city, R.mipmap.sport, R.mipmap.myclass};
    private ListView mList1;
    private View     mHeader;
    private View mFoot;
    private MyListView mPlugin_list_foot;
    private ImageButton mIb_weather;
    private PluginListAdapter mAdapter;
    private PluginListAdapter mAdapter1;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin, null);
        mList1 = (ListView) view.findViewById(R.id.plugin_list1);
        mHeader = View.inflate(mContext, R.layout.plugin_list_header, null);
        mIb_weather = (ImageButton) mHeader.findViewById(R.id.ib_weather);
        mFoot = View.inflate(mContext, R.layout.plugin_list_foot, null);
        mPlugin_list_foot = (MyListView) mFoot.findViewById(R.id.plugin_list_foot);


        mIb_weather.setOnClickListener(this);
        return view;
    }


    @Override
    protected void initData() {
        mAdapter1 = new PluginListAdapter(mContext,title2,image2);
        mPlugin_list_foot.setAdapter(mAdapter1);
        mList1.addHeaderView(mHeader);
        mList1.addFooterView(mFoot);
        mAdapter = new PluginListAdapter(mContext, title, image);
        mList1.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mList1 = null;
        mAdapter = null;
        mAdapter1 = null;
        mPlugin_list_foot = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ib_weather:
                startActivity(new Intent(mContext, CoolWeatherActivity.class));
            break;


        }
    }
}
