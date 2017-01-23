package com.example.lizhenquan.honestqq.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.view.fragment.BaseFragment;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private String[] arr         = {"消息", "联系人", "动态"};

    private Toolbar             mToolbar;
    private BottomNavigationBar mBottom_navigation_bar;
    private TextView mTv_title;
    private NavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toorbar);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mBottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mNavigation.setNavigationItemSelectedListener(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        initBottomNavigationBar();
        initFragment();
    }

    private void initFragment() {
        /**
         * 解决热部署的重影BUG
         * 如果发现Activity中有缓存的老的Fragment就将其移除掉
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for(int i=0;i<arr.length;i++){

            Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(i+"");
            if (fragmentByTag!=null){
                Log.d(TAG, "initFragment: 发现有老的缓存Fragment"+fragmentByTag);
                fragmentTransaction.remove(fragmentByTag);
            }
        }
        fragmentTransaction.commit();

        /**
         * 默认只添加第一个Fragment
         */
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,FragmentFactory.getFragmentByPosition(0),"0")
                .commit();
        mTv_title.setText(arr[0]);
    }

    private void initBottomNavigationBar() {
        BottomNavigationItem conversation_item = new BottomNavigationItem(R.mipmap.conversation_selected_2, arr[0]);
        BottomNavigationItem contact_item = new BottomNavigationItem(R.mipmap.contact_selected_2, arr[1]);
        BottomNavigationItem plugin_item = new BottomNavigationItem(R.mipmap.plugin_selected_2, arr[2]);
        mBottom_navigation_bar.addItem(conversation_item);
        mBottom_navigation_bar.addItem(contact_item);
        mBottom_navigation_bar.addItem(plugin_item);
        mBottom_navigation_bar.setActiveColor(R.color.colorPrimary);
        mBottom_navigation_bar.setInActiveColor("#9c9c9c");
        mBottom_navigation_bar.initialise();
        mBottom_navigation_bar.setTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {//更多菜单选项

            case R.id.create_group:
                showToast("create_group");

                break;
            case R.id.add_friend:


                break;
            case R.id.sweep:


                break;
            case R.id.send_fast:


                break;
            case R.id.take_pic:


                break;


        }
        return true;
    }

    @Override
    public void onTabSelected(int position) {  //切换Fragment

                /**
                 * 1.修改标题
                 * 2.切换Fragment
                 */
                mTv_title.setText(arr[position]);

                BaseFragment fragment = FragmentFactory.getFragmentByPosition(position);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (!fragment.isAdded()) {
                    fragmentTransaction.add(R.id.fl_content,fragment,position+"");
                }
                fragmentTransaction.show(fragment);
               fragmentTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {
        /**
         * 隐藏未选中的Fragment
         */
        BaseFragment fragment = FragmentFactory.getFragmentByPosition(position);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragment).commit();
    }


    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
                    case R.id.menu_shezhi:
                    startActivity(SettingActivity.class,false);
                        break;

                    default:
                        break;

                }
        return false;
    }
}
