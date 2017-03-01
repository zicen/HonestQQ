package com.example.lizhenquan.honestqq.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.SettingPresentImpl;
import com.example.lizhenquan.honestqq.presenter.SettingPresenter;
import com.example.lizhenquan.honestqq.view.fragment.BaseFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, SettingView {
    private static final String   TAG = "MainActivity";
    private              String[] arr = {"消息", "联系人", "动态"};

    private Toolbar             mToolbar;
    private BottomNavigationBar mBottom_navigation_bar;
    private TextView            mTv_title;
    private NavigationView      mNavigation;
    private RelativeLayout      mHeaderView;
    private TextView            mTv_nicname;
    private BadgeItem           mBadgeItem;
    private SettingPresenter    mSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toorbar);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mBottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mHeaderView = (RelativeLayout) mNavigation.getHeaderView(0);
        mTv_nicname = (TextView) mHeaderView.findViewById(R.id.tv_nicname);
        mNavigation.setNavigationItemSelectedListener(this);
        mToolbar.setTitle("");
        String currentUser = EMClient.getInstance().getCurrentUser();
        if (currentUser != null) {
            mTv_nicname.setText(currentUser);
        }
        setSupportActionBar(mToolbar);
        initBottomNavigationBar();
        initFragment();
        EventBus.getDefault().register(this);
        mSettingPresenter = new SettingPresentImpl(this);
    }

    private void initFragment() {
        /**
         * 解决热部署的重影BUG
         * 如果发现Activity中有缓存的老的Fragment就将其移除掉
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < arr.length; i++) {

            Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(i + "");
            if (fragmentByTag != null) {
                Log.d(TAG, "initFragment: 发现有老的缓存Fragment" + fragmentByTag);
                fragmentTransaction.remove(fragmentByTag);
            }
        }
        fragmentTransaction.commit();

        /**
         * 默认只添加第一个Fragment
         */
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, FragmentFactory.getFragmentByPosition(0), "0")
                .commit();
        mTv_title.setText(arr[0]);
    }

    private void initBottomNavigationBar() {
        BottomNavigationItem conversation_item = new BottomNavigationItem(R.mipmap.conversation_selected_2, arr[0]);
        //创建一个角标对象
        mBadgeItem = new BadgeItem();
        //设置位置为右侧
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setBackgroundColor("#ff0000");
        mBadgeItem.setText("0");
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.show();
        conversation_item.setBadgeItem(mBadgeItem);

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
    protected void onResume() {
        super.onResume();
        updateUnreadMsgCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateUnreadMsgCount() {
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount > 99) {
            mBadgeItem.setText("99+");
            mBadgeItem.show();
        } else if (unreadMsgsCount > 0) {
            mBadgeItem.setText("" + unreadMsgsCount);
            mBadgeItem.show();
        } else {
            mBadgeItem.hide();
        }
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

        /*    case R.id.create_group:
                showToast("create_group");

                break;*/
            case R.id.add_friend:
                startActivity(AddFriendActivity.class, false);

                break;
            case R.id.sweep:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
          case R.id.myqrcode:
                startActivity(new Intent(this,QRCodeActivity.class));
                break;
          /*    case R.id.take_pic:


                break;*/
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
            fragmentTransaction.add(R.id.fl_content, fragment, position + "");
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        updateUnreadMsgCount();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.menu_tequan:
                showToast("QQ会员是腾讯为QQ用户提供的一项增值服务,涵盖了QQ特权、游戏特权、生活特权、装扮特权等80余项精彩特权。其中包括等级加速、多彩气泡、超级群、身份铭牌、个性名片等...");
                break;
            case R.id.menu_qianbao:
                showToast("等待开发中。。。");
                break;
            case R.id.menu_zhuangban:
                showToast("等待开发中。。。");
                break;
            case R.id.menu_shoucang:
                showToast("等待开发中。。。");
                break;
            case R.id.menu_xiangche:
                showToast("等待开发中。。。");
                break;
            case R.id.menu_wenjian:
                Intent intent = new Intent(this, FileActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_richeng:
                showToast("等待开发中。。。");
                break;
            case R.id.menu_shezhi:
                startActivity(SettingActivity.class, false);
                break;*/
            case R.id.menu_logout:
                /**
                 * 1.弹出确认对话框
                 * 2.发送网络请求，执行注销操作
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialog_exit = View.inflate(MainActivity.this, R.layout.dialog_exit, null);
                builder.setView(dialog_exit);
                Button btn_cancel = (Button) dialog_exit.findViewById(R.id.btn_cancel);
                Button btn_exit = (Button) dialog_exit.findViewById(R.id.btn_exit);

                final AlertDialog ad = builder.create();
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        mSettingPresenter.logout();
                    }
                });
                ad.show();

                break;
            default:
                break;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (requestCode == 0 && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                showToast(scanResult);
                mSettingPresenter.addFriend(scanResult);
        }
    }




    @Override
    public void onLogout(boolean isSuccess, String msg) {
        if (!isSuccess) {
            showToast(msg);
        }
        startActivity(LoginActivity.class, true);
    }

    @Override
    public void onAddContact(String username, boolean isSuccess, String msg) {
            if (isSuccess) {
              Log.d("tag","发送好友请求成功！");
            } else {
                Log.d("tag","发送好友请求失败！");
            }

    }
}
