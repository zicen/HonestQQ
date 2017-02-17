package com.example.lizhenquan.honestqqa.view.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.adapter.PluginListAdapter;
import com.example.lizhenquan.honestqqa.utils.Constant;
import com.example.lizhenquan.honestqqa.view.CoolWeatherActivity;
import com.example.lizhenquan.honestqqa.view.PluginItemActivity;
import com.example.lizhenquan.honestqqa.wight.MyListView;

import static com.example.lizhenquan.honestqqa.R.id.map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment2 extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String title[]  = {"游戏", "知乎", "看点", "阅读", "动漫", "音乐", "直播"};
    private int    image[]  = {R.mipmap.lzc, R.mipmap.day, R.mipmap.kandian, R.mipmap.lzd, R.mipmap.dongman, R.mipmap.music, R.mipmap.zhibo};
    private String title2[] = {"同城交友","运动", "腾讯课堂"};
    private int    image2[] = {R.mipmap.city, R.mipmap.sport, R.mipmap.myclass};
    private ListView          mList1;
    private View              mHeader;
    private View              mFoot;
    private MyListView        mPlugin_list_foot;
    private ImageButton       mIb_weather;
    private PluginListAdapter mAdapter;
    private PluginListAdapter mAdapter1;
    private Intent            mIntent;
    private String CenterUrls[] = {"http://qqgame.qq.com/", "https://www.zhihu.com/", "http://news.163.com/", "http://book.qq.com/", "http://ac.qq.com/",
            "http://music.163.com/", "http://www.qqlubo.net/"};
    private String FootUrls[]   = {"http://www.youyuan.com/",
            "http://sports.qq.com/", "https://ke.qq.com/course/list"};
    private ImageButton BaiduMap;
    private ImageButton MyPlugin;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mList1 = null;
        mAdapter = null;
        mAdapter1 = null;
        mPlugin_list_foot = null;
        mHeader = null;
        mIb_weather = null;
        BaiduMap = null;
        MyPlugin = null;
        mIntent = null;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin2, null);
        mList1 = (ListView) view.findViewById(R.id.plugin_list1);
        mHeader = View.inflate(mContext, R.layout.plugin_list_header, null);
        mIb_weather = (ImageButton) mHeader.findViewById(R.id.ib_weather);
        BaiduMap = (ImageButton) mHeader.findViewById(map);
        MyPlugin = (ImageButton) mHeader.findViewById(R.id.plugin);
        mFoot = View.inflate(mContext, R.layout.plugin_list_foot, null);
        mPlugin_list_foot = (MyListView) mFoot.findViewById(R.id.plugin_list_foot);
        mIntent = new Intent(mContext, PluginItemActivity.class);

        mIb_weather.setOnClickListener(this);
        BaiduMap.setOnClickListener(this);
        MyPlugin.setOnClickListener(this);

        mList1.setOnItemClickListener(this);
        mPlugin_list_foot.setOnItemClickListener(this);

        return view;
    }


    @Override
    protected void initData() {
        mAdapter1 = new PluginListAdapter(mContext, title2, image2);
        mPlugin_list_foot.setAdapter(mAdapter1);
        mList1.addHeaderView(mHeader);
        mList1.addFooterView(mFoot);
        mAdapter = new PluginListAdapter(mContext, title, image);
        mList1.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ib_weather:
                startActivity(new Intent(mContext, CoolWeatherActivity.class));
                break;
            case map:
                mIntent.putExtra(Constant.URL, "http://ditu.amap.com/");
                startActivity(mIntent);
                break;
            case R.id.plugin:
                mIntent.putExtra(Constant.URL, "http://qzone.qq.com/");
                startActivity(mIntent);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.plugin_list1:
                mIntent.putExtra(Constant.URL, CenterUrls[i - 1]);
                startActivity(mIntent);

                break;
            case R.id.plugin_list_foot:
                mIntent.putExtra(Constant.URL, FootUrls[i]);
                startActivity(mIntent);


                break;

        }

    }
}
