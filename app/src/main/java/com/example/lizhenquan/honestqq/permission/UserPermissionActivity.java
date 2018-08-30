package com.example.lizhenquan.honestqq.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.example.lizhenquan.honestqq.R;


public class UserPermissionActivity extends AppCompatActivity {

    public String[] permissions;
    public String explain;

    public static void startUserPermission(Context context, int requestCode, String[] permissions, String message){
        Intent intent = new Intent(context, UserPermissionActivity.class);
        intent.putExtra("permission", permissions);
        intent.putExtra("explain", message);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_permission);
        permissions = getIntent().getStringArrayExtra("permission");
        explain = getIntent().getStringExtra("explain");
        if(explain!=null && !"".equals(explain)){
            explain = "请同意相应的权限，否则某些业务将无法使用！";
        }
        Log.i("111", "获取权限 permission = " + permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 100);
        }else{
            //无需获取
            Log.i("111", "无需获取权限");
            setResult(RESULT_OK);
            finish();
        }
    }

    //权限获取回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100){
            if (permissions.length>0 && permissions[0].equals(permissions) && grantResults[0] == PackageManager.PERMISSION_GRANTED){//同意使用
                Log.i("111", "获取权限成功");
                setResult(RESULT_OK);
                finish();
            }else{
                Log.i("111", "shouldShowRequestPermissionRationale() = " + ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]));
                //用户不同意，拒绝后第二次调用的时候，向用户解释该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    if(checkPermission(permissions[0])){//这种情况是多次请求权限或者选择了不在询问，需要在判断一次权限
                        setResult(RESULT_OK);//同意了
                        finish();
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage(explain)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getAppDetailSettingIntent(UserPermissionActivity.this);//去设置
                                        setResult(RESULT_CANCELED);
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        setResult(RESULT_CANCELED);
                                        finish();
                                    }
                                }).create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }else{
                    Log.i("111", "用户不同意");
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }else{
            Log.i("111", "获取权限失败");
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context){
        // vivo 点击设置图标>加速白名单>我的app
        // 点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent vivo = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if(vivo != null){
            context.startActivity(vivo);
            return;
        }else{
            Log.i("111", "没找到vivo的安全管家");
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        // 点击权限隐私>自启动管理>我的app
        Intent oppo = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if(oppo != null){
            context.startActivity(oppo);
            return;
        }else{
            Log.i("111", "没找到oppo的安全管家");
        }

        Intent miui = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        try {
            miui.setComponent(componentName);
            miui.putExtra("extra_pkgname", getPackageName());
            startActivity(miui);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("111", "没找到小米的安全设置");
        }

        Intent meizu = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        meizu.addCategory(Intent.CATEGORY_DEFAULT);
        meizu.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            startActivity(meizu);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("111", "没找到魅族的安全设置");
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    /**
     * 权限检查
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (geTargetSdkVersion(this) >= Build.VERSION_CODES.M) {
                result = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    /**
     * 权限检查
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (geTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
        Log.i("111", "checkPermission = " + result);
        return result;
    }

    /**
     *  获取版本号
     *  @param context
     *  @return 当前应用的版本号
     */
    public static int geTargetSdkVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
