package com.example.lizhenquan.honestqq.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.permission.UserPermissionActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建人: zhenquan
 * 创建日期: 2018/8/30
 * 邮箱: 1140377034@qq.com
 * github: https://github.com/zicen
 */

public class ImageChioceAndTakeUtil {
    public static final String TAG = "ImageChioceAndTakeUtil";
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int TAKE_PHOTO = 100;
    public static final int CHOICE_PHOTO = 200;
    public static final int TAKE_PHOTO_PERMISSIONS = 300;
    public static final int CROP_PHOTO = 400;
    public static final int REQUEST_CODE_CHOOSE = 500;
    public static final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pic";

    /**
     * takePhoto
     *
     * @param context
     */
    public static void takePhoto(Activity context) {
        if (!UserPermissionActivity.checkPermission(context.getApplicationContext(), permissions[0]) || !UserPermissionActivity.checkPermission(context.getApplicationContext(), permissions[1])) {
            Intent intent = new Intent(context.getApplicationContext(), UserPermissionActivity.class);
            intent.putExtra("permission", permissions);
            context.startActivityForResult(intent, TAKE_PHOTO_PERMISSIONS);
        } else {
            try {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(context, "没有找到内存卡", Toast.LENGTH_SHORT).show();
                    return;
                }
//                File takeImage = new File(IMAGE_PATH, "take_image.jpg");
//                if (takeImage.exists()) takeImage.delete();
//                takeImage.createNewFile();
//                Uri takeImageUri = Uri.fromFile(takeImage);
//                if(takeImageUri==null){
//                    Toast.makeText(context, "获取拍照路径失败", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, takeImageUri);
                context.startActivityForResult(intent, TAKE_PHOTO);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "请检查权限是否开启，存储是否正常", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Uri getMediaFileUri(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "相册名字");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == TYPE_TAKE_PHOTO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return Uri.fromFile(mediaFile);
    }
    /**
     * Matisse
     *
     * @param context
     */
    public static void choicePhoto(Activity context) {
        Matisse.from(context)
                .choose(MimeType.allOf())
                .countable(false)
                .maxSelectable(1)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    /**
     * 裁剪图片
     * 附加选项	数据类型	描述
     * crop	String	发送裁剪信号
     * aspectX	int	X方向上的比例
     * aspectY	int	Y方向上的比例
     * outputX	int	裁剪区的宽
     * outputY	int	裁剪区的高
     * scale	boolean	是否保留比例
     * return-data	boolean	是否将数据保留在Bitmap中返回
     * data	Parcelable	相应的Bitmap数据
     * circleCrop	String	圆形裁剪区域？
     * MediaStore.EXTRA_OUTPUT ("output")	URI	将URI指向相应的file:///...
     */
    public static Uri startPhotoZoom(Activity context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        Uri uritempFile;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //重点:针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "crop_image.jpg");
        }
        //        下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //        intent.putExtra("return-data", true)
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, CROP_PHOTO);
        return uritempFile;
    }

    //4.4以后解析隐式url
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Uri handlerImageOnKitKat(Context context, Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents" == uri.getAuthority()) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents" == uri.getAuthority()) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(context, contentUri, null);
            }
        } else if ("content".equals(uri.getScheme())) {
            //如果是content类型的URI，则使用普通方式处理
            imagePath = getImagePath(context, uri, null);
        } else if ("file".equals(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        if (imagePath.equals("") || imagePath == null) {
            Log.e(TAG, "handlerImageOnKitKat uri:" + uri.toString());
            return uri;
        } else {
            Log.e(TAG, "handlerImageOnKitKat uri:" + imagePath);
            return Uri.fromFile(new File(imagePath));
        }
    }

    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = "";
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 质量压缩方法
     *
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bao);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (bao.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            bao.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, bao);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(bao.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * @param @param bitmap
     * @return String    返回类型
     * @Title: bitmapToBase64
     * @Description: TODO(Bitmap 转换为字符串)
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String reslut = null;// 要返回的字符串
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();
                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;
    }

    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Pic";
        File file = new File(path);
        file.mkdirs();
        long i = System.currentTimeMillis();
        file = new File(file.toString() + "/" + i + ".jpg");
        Log.e("fileNew", file.getPath());
        Log.e("fileNew getAbsolutePath", file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }
}
