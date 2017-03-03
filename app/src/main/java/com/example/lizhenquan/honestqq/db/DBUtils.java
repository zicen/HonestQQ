package com.example.lizhenquan.honestqq.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lizhenquan.honestqq.model.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhenquan on 2017/1/21.
 */

public class DBUtils {

    private static Context mContext;

    public DBUtils(Context context) {
        mContext = context;
    }

    public static void UpdateContactDB(String username, List<ContactBean> newFriend) {
        if (mContext == null) {
            throw new RuntimeException("使用DBUtils之前必须在Application中初始化！");
        }
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(mContext);
        SQLiteDatabase writableDatabase = contactSQLiteOpenHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        writableDatabase.delete("t_contact", "username=?", new String[]{username});
        ContentValues values = new ContentValues();
        values.put("username", username);
        for (ContactBean contactBean : newFriend) {
            values.put("contact", contactBean.username);
            System.out.println("avatarUrl:"+contactBean.avatarUrl);
            values.put("avatar", contactBean.avatarUrl);
            writableDatabase.insert("t_contact", null, values);
        }

        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();

    }

    public static List<ContactBean> getContacts(String username) {
        if (mContext == null) {
            throw new RuntimeException("使用DBUtils之前必须在Application中初始化！");
        }
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(mContext);
        List<ContactBean> contact = new ArrayList<>();
        SQLiteDatabase readableDatabase = contactSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("t_contact", new String[]{"contact","avatar"}, "username=?", new String[]{username}, null, null, "contact");
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(0);
            String avatar = cursor.getString(1);
            contact.add(new ContactBean(name,avatar));
        }
        cursor.close();
        readableDatabase.close();
        return contact;
    }

    public static  String getAvatarUrl(String contact){
        if (mContext == null) {
            throw new RuntimeException("使用DBUtils之前必须在Application中初始化！");
        }
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(mContext);
        String avatarUrl =null;
        SQLiteDatabase readableDatabase = contactSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("t_contact", new String[]{"avatar"}, "contact=?", new String[]{contact}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String avatar = cursor.getString(0);
            avatarUrl = avatar;
        }
        return avatarUrl;
    }
}
