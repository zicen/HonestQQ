package com.example.lizhenquan.honestqq.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.FileViewPagerAdapter;

public class FileActivity extends BaseActivity {

    private TextView mTv_title;
    private Toolbar mToorbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private GeoCoder mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        initToorBar();
        initViewPager();
        mSearch = GeoCoder.newInstance();
    }

    private void initViewPager() {
       /* LatLng ptCenter = new LatLng((Float.valueOf(lat.getText()
                .toString())), (Float.valueOf(lon.getText().toString())));
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));*/
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FileViewPagerAdapter fileViewPagerAdapter = new FileViewPagerAdapter();
       mViewPager.setAdapter(fileViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initToorBar() {
        mToorbar = (Toolbar) findViewById(R.id.toorbar);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_title.setText("查看文件");
        mToorbar.setTitle("");
        // 设置toolbar到actionbar上
        setSupportActionBar(mToorbar);

        // 拿到actionbar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            finish();
            break;
        }
        return true;
    }
}
