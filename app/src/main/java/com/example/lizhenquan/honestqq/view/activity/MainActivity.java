package com.example.lizhenquan.honestqq.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.bumptech.glide.Glide;
import com.example.lizhenquan.honestqq.BuildConfig;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.MainPresentImpl;
import com.example.lizhenquan.honestqq.presenter.MainPresenter;
import com.example.lizhenquan.honestqq.utils.ImageChioceAndTakeUtil;
import com.example.lizhenquan.honestqq.utils.ToastUtils;
import com.example.lizhenquan.honestqq.view.FragmentFactory;
import com.example.lizhenquan.honestqq.view.MainView;
import com.example.lizhenquan.honestqq.view.fragment.BaseFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.FileUtils;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, MainView, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private String[] arr = {"消息", "联系人", "圈子"};

    private Toolbar mToolbar;
    private BottomNavigationBar mBottom_navigation_bar;
    private TextView mTv_title;
    private NavigationView mNavigation;
    private RelativeLayout mHeaderView;
    private TextView mTv_nicname;
    private BadgeItem mBadgeItem;
    private MainPresenter mMainPresenter;
    private CircleImageView mCircleImageView;
    private String mHeadUrl;
    private Uri startPhotoZoom;

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
        mCircleImageView = (CircleImageView) mHeaderView.findViewById(R.id.iv_head_icon);
        mCircleImageView.setOnClickListener(this);
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
        mMainPresenter = new MainPresentImpl(this);
        initCircleImageView();
    }

    private void initCircleImageView() {
        mHeadUrl = mMainPresenter.getHeadUrl();
        if (mHeadUrl != null) {
            Glide.with(this).load(mHeadUrl).into(mCircleImageView);
        } else {
            mCircleImageView.setImageResource(R.mipmap.avatar3);
        }

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
        BottomNavigationItem conversation_item = new BottomNavigationItem(R.mipmap.conversation, arr[0]);
        //创建一个角标对象
        mBadgeItem = new BadgeItem();
        //设置位置为右侧
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setBackgroundColor("#ff0000");
        mBadgeItem.setText("0");
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.show();
        conversation_item.setBadgeItem(mBadgeItem);

        BottomNavigationItem contact_item = new BottomNavigationItem(R.mipmap.contact, arr[1]);
        BottomNavigationItem plugin_item = new BottomNavigationItem(R.mipmap.plugin, arr[2]);
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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {//更多菜单选项

            case R.id.add_friend:
                startActivity(AddFriendActivity.class, false);

                break;
            case R.id.sweep:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.myqrcode:
                startActivity(new Intent(this, QRCodeActivity.class));
                break;
//            case R.id.take_pic:
//                break;
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
//            case R.id.menu_wenjian:
//                Intent intent = new Intent(this, MyImgActivity.class);
//                startActivity(intent);
//                break;

//            case R.id.menu_resetpwd:
//                startActivity(new Intent(MainActivity.this, ResetPwdActivity.class));
//                break;
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
                        mMainPresenter.logout();
                    }
                });
                ad.show();

                break;
            case R.id.menu_weather:
                startActivity(new Intent(this, CoolWeatherActivity.class));
                break;
            default:
                break;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    showToast(scanResult);
                    mMainPresenter.addFriend(scanResult);
                    break;
                case ImageChioceAndTakeUtil.TAKE_PHOTO_PERMISSIONS:
                    ImageChioceAndTakeUtil.takePhoto(MainActivity.this);
                    break;
                case ImageChioceAndTakeUtil.REQUEST_CODE_CHOOSE:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (mSelected.size() > 0) {
                        startPhotoZoom = ImageChioceAndTakeUtil.startPhotoZoom(MainActivity.this, mSelected.get(0));
                    }
                    break;
                case ImageChioceAndTakeUtil.TAKE_PHOTO:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        String filePath = ImageChioceAndTakeUtil.saveBitmap(imageBitmap);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(filePath));
                            startPhotoZoom = ImageChioceAndTakeUtil.startPhotoZoom(MainActivity.this, photoURI);
                        } else {
                            startPhotoZoom = ImageChioceAndTakeUtil.startPhotoZoom(MainActivity.this, Uri.fromFile(new File(filePath)));
                        }
                    }
                    break;
                case ImageChioceAndTakeUtil.CROP_PHOTO:
                    if (data != null && startPhotoZoom != null) {
                        System.out.println("startPhotoZoom != null");
                        //裁剪后的图像转成BitMap
                        Bitmap photo1 = null;
                        try {
                            photo1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(startPhotoZoom));
                            Bitmap bitmap = ImageChioceAndTakeUtil.compressImage(photo1);
                            String filePath = ImageChioceAndTakeUtil.saveBitmap(bitmap);
                            Picasso.with(MainActivity.this).load(startPhotoZoom).into(mCircleImageView);
                            mMainPresenter.uploadImag(filePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }

    }


    /**
     * 图片选择和拍照
     */
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                ImageChioceAndTakeUtil.choicePhoto(MainActivity.this);
                dialog.dismiss();

            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                ImageChioceAndTakeUtil.takePhoto(MainActivity.this);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
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
            Log.d("tag", "发送好友请求成功！");
        } else {
            Log.d("tag", "发送好友请求失败！");
        }

    }

    @Override
    public void onUploadImag(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.showToast(this, "更新头像成功！");
        } else {
            ToastUtils.showToast(this, "更新头像失败！" + msg);
        }
    }

    @Override
    public void onUploadPhoto(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.showToast(this, "上传图片成功！");
        } else {
            ToastUtils.showToast(this, "上传图片失败！" + msg);
        }
    }

    //头像的点击事件
    @Override
    public void onClick(View view) {
        showTypeDialog();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            Log.e(TAG, "drawer.isDrawerOpen");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.e(TAG, "drawer.is not DrawerOpen");
            android.support.v7.app.AlertDialog.Builder alertdlg = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            alertdlg.setTitle(getString(R.string.program_finish))
                    .setMessage(getString(R.string.program_finish_msg))
                    .setPositiveButton(getString(R.string.common_btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            ActivityCompat.finishAffinity(MainActivity.this);
                            System.runFinalizersOnExit(true);
                            System.exit(0);
                        }
                    }).setNegativeButton(getString(R.string.common_btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AppCompatDialog alert = alertdlg.create();
            alert.show();
        }
    }
}
