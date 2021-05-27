package com.example.sqliteuser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private SQLiteDatabase database ;
    private UserSQLiteHelper dbHelper;
    private String[] allColumns={UserSQLiteHelper.COLUMN_ID,UserSQLiteHelper.COLUMN_NAME,UserSQLiteHelper.COLUMN_ADDRESS,UserSQLiteHelper.COLUMN_YEAR};
    public UserDatabase(Context context){
        dbHelper = new UserSQLiteHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public  User createUser(String name,String address, String year){
        ContentValues values= new ContentValues();
        values.put(UserSQLiteHelper.COLUMN_NAME,name);
        values.put(UserSQLiteHelper.COLUMN_ADDRESS,address);
        values.put(UserSQLiteHelper.COLUMN_YEAR,year);
        long insertId=database.insert(UserSQLiteHelper.TABLE_USER, null,values);
        Cursor cursor =database.query(UserSQLiteHelper.TABLE_USER,allColumns,UserSQLiteHelper.COLUMN_ID+ " = " + insertId,null,
                null,null,null);
        cursor.moveToFirst();
        User newUser= cursorUser(cursor);
        cursor.close();
        return newUser;
    }


    private User cursorUser(Cursor cursor) {
        User user= new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setAddress(cursor.getString(2));
        user.setYear(cursor.getString(3));
        return user;
    }

    public List<User> getListUser(){
        List<User> users= new ArrayList<>();
        Cursor cursor= database.query(UserSQLiteHelper.TABLE_USER,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user =cursorUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public void delete(User user){
        long id = user.getId();
        Log.e("SQLite", "User entry deleted with id: " + id);
        database.delete(UserSQLiteHelper.TABLE_USER, UserSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void update(User user){

        ContentValues values= new ContentValues();
        values.put(UserSQLiteHelper.COLUMN_NAME,user.getName());
        values.put(UserSQLiteHelper.COLUMN_ADDRESS,user.getAddress());
        values.put(UserSQLiteHelper.COLUMN_YEAR,user.getYear());

        database.update("user", values,null,null);
    }

    public List<User> searchListUser(String name){
        List<User> users= new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from user where name LIKE '%'||:name||'%' ",new String[] {name});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user =cursorUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public void deleteAllUser( ){

        database.delete(UserSQLiteHelper.TABLE_USER, null, null);
    }
}
