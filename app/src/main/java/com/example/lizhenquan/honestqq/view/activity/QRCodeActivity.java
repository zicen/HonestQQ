package com.example.lizhenquan.honestqq.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lizhenquan.honestqq.R;
import com.google.zxing.WriterException;
import com.hyphenate.chat.EMClient;
import com.zxing.encoding.EncodingHandler;


public class QRCodeActivity extends AppCompatActivity {


    private ImageView mIvQrcode;
    private RelativeLayout mActivityQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        mIvQrcode = (ImageView) findViewById(R.id.iv_qrcode);
        mActivityQrcode = (RelativeLayout) findViewById(R.id.activity_qrcode);

        String currentUser = EMClient.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.trim().length() > 0) {
            //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
            Bitmap qrCodeBitmap = null;
            try {
                qrCodeBitmap = EncodingHandler.createQRCode(currentUser, 350);
                mIvQrcode.setImageBitmap(qrCodeBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(QRCodeActivity.this, "没有获取到当前用户的二维码", Toast.LENGTH_SHORT).show();
        }


    }
}
