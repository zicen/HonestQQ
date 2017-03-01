package com.example.lizhenquan.honestqq.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.presenter.PluginPresenter;
import com.example.lizhenquan.honestqq.presenter.PluginPresenterImpl;
import com.example.lizhenquan.honestqq.utils.ToastUtils;
import com.example.lizhenquan.honestqq.view.CoolWeatherActivity;
import com.example.lizhenquan.honestqq.view.PluginView;
import com.example.lizhenquan.honestqq.wight.MineItemLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lizhenquan on 2017/2/17.
 */

public class PluginFragment extends BaseFragment implements View.OnClickListener,PluginView {

    private CircleImageView mCircleImageView;
    private Uri             mUri;
    private  String path = "/sdcard/HonestQQ/myHead/";
    private AVUser mCurrentUser;
    private PluginPresenter mPluginPresenter;
    private String mHeadUrl;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin, null);
        MineItemLayout btn_getweather = (MineItemLayout) view.findViewById(R.id.ib_weather);
        mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_portrait);
        btn_getweather.setOnClickListener(this);
        mCircleImageView.setOnClickListener(this);
        mPluginPresenter = new PluginPresenterImpl(this);

        initCircleImageView();
        return view;
    }

    private void initCircleImageView() {
         mHeadUrl = mPluginPresenter.getHeadUrl();
        Glide.with(mContext).load(mHeadUrl).into(mCircleImageView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_portrait:
                showTypeDialog();
                break;
            case R.id.ib_weather:
                startActivity(new Intent(mContext, CoolWeatherActivity.class));
                break;
        }

    }


    private void uploadDb() {
        File dbFile = new File(getActivity().getApplication().getDatabasePath("Records")+".db");
        if (!dbFile.exists()) {
            Toast.makeText(getContext(), "本地和云存储均没有数据！", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            final String recordDbObjectId = (String) mCurrentUser.get("recordDbObjectId");
            if (recordDbObjectId != null && !recordDbObjectId.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final AVFile avFileDel;
                        try {
                            avFileDel = AVFile.withObjectId(recordDbObjectId);
                            avFileDel.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {

                                }
                            });
                        } catch (AVException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }


            final AVFile avFile = AVFile.withAbsoluteLocalPath("Records.db", dbFile.getAbsolutePath());
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    mCurrentUser.put("recordDbObjectId", avFile.getObjectId());
                    mCurrentUser.put("netRecordDbDate", new Date());
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {

                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    mUri = data.getData();
                    cropPhoto(mUri);
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    File temp = new File(Environment.getExternalStorageDirectory() + "/HonestQQ/myHead/head.jpg");
                    mUri = Uri.fromFile(temp);
                    cropPhoto(mUri);
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);
                        mCircleImageView.setImageBitmap(head);// 用ImageView显示出来
                        //上传到AVCloud
                        mPluginPresenter.uploadImag();
                    }
                }
                break;
            default:
                break;

        }
    }


    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUploadImag(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.showToast(mContext, "更新头像成功！");
        } else {
            ToastUtils.showToast(mContext,"更新头像失败！"+msg);
        }
    }
}
