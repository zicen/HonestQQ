package com.example.lizhenquan.honestqqa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public static void UpdateContactDB(String username, List<String> newFriend) {
        if (mContext == null) {
            throw new RuntimeException("使用DBUtils之前必须在Application中初始化！");
        }
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(mContext);
        SQLiteDatabase writableDatabase = contactSQLiteOpenHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        writableDatabase.delete("t_contact", "username=?", new String[]{username});
        ContentValues values = new ContentValues();
        values.put("username", username);
        for (String s : newFriend) {
            values.put("contact", s);
            writableDatabase.insert("t_contact", null, values);
        }

        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();

    }

    public static List<String> getContacts(String username) {
        if (mContext == null) {
            throw new RuntimeException("使用DBUtils之前必须在Application中初始化！");
        }
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(mContext);
        List<String> contact = new ArrayList<>();
        SQLiteDatabase readableDatabase = contactSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("t_contact", new String[]{"contact"}, "username=?", new String[]{username}, null, null, "contact");
        while (cursor != null && cursor.moveToNext()) {
            String string = cursor.getString(0);
            contact.add(string);
        }
        cursor.close();
        readableDatabase.close();
        return contact;
    }
}
