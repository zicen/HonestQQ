package com.example.lizhenquan.honestqqa;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.example.lizhenquan.honestqqa.db.DBUtils;
import com.example.lizhenquan.honestqqa.event.ContactEvent;
import com.example.lizhenquan.honestqqa.utils.ThreadUtils;
import com.example.lizhenquan.honestqqa.utils.ToastUtils;
import com.example.lizhenquan.honestqqa.view.BaseActivity;
import com.example.lizhenquan.honestqqa.view.ChatActivityEaseUI;
import com.example.lizhenquan.honestqqa.view.LoginActivity;
import com.example.lizhenquan.honestqqa.view.MainActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.smssdk.SMSSDK;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class MyApplication extends android.support.multidex.MultiDexApplication {
    private static final String TAG = "tag";
    private ActivityManager mActivityManager;
    private NotificationManager mNotificationManager;
    private SoundPool mSoundPool;
    private int mDuanSound;
    private int mYuluSound;
    private List<BaseActivity> mBaseActivities ;
   /* public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;*/

    @Override
    public void onCreate() {
        super.onCreate();
       // refWatcher = LeakCanary.install(this);
     //   LeakCanary.install(this);
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initHuanXin();
        initLeanCloud();
        initLitepal();

        initDBUtils();
        initSoundPool();
        initEaseUI();
        initMobSms();
        mBaseActivities = new ArrayList<>();
    }

    private void initMobSms() {
        SMSSDK.initSDK(this, "15d5d9bc15762", "6e9211264302886949713acc7be1eef3");
    }

    private void initEaseUI() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);

    }


    public void addActivity(BaseActivity activity) {
        if (!mBaseActivities.contains(activity)) {
            mBaseActivities.add(activity);
        }
    }

    public void removeActivity(BaseActivity activity) {
        mBaseActivities.remove(activity);
    }


    private void initDBUtils() {
        new DBUtils(this);
    }
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        //预加载音乐
        mDuanSound = mSoundPool.load(this, R.raw.duan, 1);
        mYuluSound = mSoundPool.load(this, R.raw.yulu, 1);
    }
    private void initLitepal() {
        LitePalApplication.initialize(this);
    }

    private void initLeanCloud() {
        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        AVOSCloud.initialize(this, "7Ojm9oAT6zw782wcJEmMrO2X-gzGzoHsz", "DRNWRCYcawbBtYyNYRo6i74k");
    }

    private void initHuanXin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //初始化联系人监听器
        initContactListener();
        //初始化聊天监听
        initChatListener();
        //初始化连接状态监听
        initConnectListener();

    }



    private void initConnectListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int i) {
                if (i== EMError.USER_LOGIN_ANOTHER_DEVICE){
                    //被挤掉线了
                    //重新跳转到登录界面
                    Intent intent = new Intent(MyApplication.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //将app中的所有的Activity全部销毁
                    for (BaseActivity activity : mBaseActivities) {
                        activity.finish();
                    }

                    startActivity(intent);
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(getApplicationContext(),"您的账号在其他设备登录了，请重新登录！");
                        }
                    });


                }
            }
        });
    }

    private void initChatListener() {
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                EMMessage emMessage = messages.get(0);
                /**
                 * 判断当前App是否是在后台，如果是后台，则收到消息时再通知栏显示
                 */
                if (isRuningBackground()) {
                    showshowNotification(emMessage);
                    //播放长声音
                    mSoundPool.play(mYuluSound, 1, 1, 0, 0, 1);
                } else {
                    mSoundPool.play(mDuanSound, 1, 1, 0, 0, 1);
                }
                EventBus.getDefault().post(emMessage);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void showshowNotification(EMMessage emMessage) {
        String message = "";
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
             message = emTextMessageBody.getMessage();
        }

        Intent mainIntent = new Intent(this,MainActivity.class);
        //因为在非Activity中不允许启动Activity，如果要启动必须添加如下flag
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent chatIntent = new Intent(this, ChatActivityEaseUI.class);
        chatIntent.putExtra(EaseConstant.EXTRA_USER_ID,emMessage.getFrom());
        chatIntent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EMMessage.ChatType.Chat);

        Intent[] intents = {mainIntent,chatIntent};

        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setAutoCancel(true) //消息点击之后可以自动删除
                    .setPriority(Notification.PRIORITY_MAX)//设置通知的优先级
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.iicon5))//大图标
                    .setSmallIcon(R.mipmap.message)
                    .setContentTitle("你有一条新消息")
                    .setContentText(message)
                    .setContentInfo("来自"+emMessage.getFrom())
                    .setContentIntent(pendingIntent)
                    .build();
        }
        mNotificationManager.notify(1,notification);
    }

    private void initContactListener() {

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                ContactEvent contactEvent = new ContactEvent();
                contactEvent.isAdded = true;
                contactEvent.username = s;
                EventBus.getDefault().post(contactEvent);
            }

            @Override
            public void onContactDeleted(String s) {
                ContactEvent contactEvent = new ContactEvent();
                contactEvent.isAdded = false;
                contactEvent.username = s;
                EventBus.getDefault().post(contactEvent);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                //直接同意对象为好友
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onContactInvited: 添加好友失败："+e);
                }
            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private boolean isRuningBackground(){
        /**
         * 获取手机中所有正在运行的任务栈
         * 需要权限： <uses-permission android:name="android.permission.GET_TASKS"/>
         */
        List<ActivityManager.RunningTaskInfo> runningTasks = mActivityManager.getRunningTasks(100);
        //获取第一个任务栈
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        //获取任务栈中第一个Activity
        ComponentName topActivity = runningTaskInfo.topActivity;
        //如果这个Activity的包名和app的包名一致则说明在前台
        if (topActivity.getPackageName().equals(getPackageName())){
            return false;
        }else {
            return true;
        }
    }
}
