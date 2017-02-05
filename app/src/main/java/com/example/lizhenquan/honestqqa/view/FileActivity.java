package com.example.lizhenquan.honestqqa.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.adapter.FileViewPagerAdapter;

public class FileActivity extends BaseActivity {

    private TextView mTv_title;
    private Toolbar mToorbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        initToorBar();
        initViewPager();
    }

    private void initViewPager() {

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
