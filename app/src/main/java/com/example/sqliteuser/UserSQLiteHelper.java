package com.example.sqliteuser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserSQLiteHelper extends SQLiteOpenHelper {
    public  static  final  String TABLE_USER="user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_YEAR = "year";
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public UserSQLiteHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String DATABASE_CREATE = "create table "
            + TABLE_USER + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null," + COLUMN_ADDRESS
            + " text not null,"+ COLUMN_YEAR+" text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
