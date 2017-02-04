package com.example.lizhenquan.honestqqa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lizhenquan on 2017/1/21.
 */

public class ContactSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String CONTACT_DB = "contact.db";
    public static final int VERSION = 1;

    private ContactSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ContactSQLiteOpenHelper(Context context){
        super(context, CONTACT_DB,null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_contact(_id integer primary key,username varchar(20),contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
