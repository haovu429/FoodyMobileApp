package hcmute.edu.vn.foody_24.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.User;

public class UserService {
    SQLiteDatabase db;
    public int authenticate(String username, String password) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {username, password};
        Cursor cursor = db.rawQuery("select id from users where username=? and password=?", selectionArgs);
        if (cursor.moveToNext()){
            int idUser = cursor.getInt(0);
            cursor.close();
            return idUser;
        }
        cursor.close();
        return -1;
    }

    public User getOne(int userId) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select username, password, address, phone " +
                "from users where id="+userId, null);
        if (cursor.moveToNext()){
            User user = new User(userId, cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }
    public void insert(User user) {
        if (isExisted(user.getUsername()) == -1){
            db = DatabaseHelper.getInstance().getWritableDatabase();
            Object[] bindArgs = {user.getUsername(), user.getPassword(), user.getAddress(), user.getPhone()};
            db.execSQL("insert into users values (null, ?, ?, ?, ?)", bindArgs);
        }
    }
    public void update(User user) {
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArgs = {user.getPassword(), user.getAddress(), user.getPhone(), user.getId()};
        db.execSQL("update  users set password=?, address=?, phone=? " +
                "where id=?;", bindArgs);
    }

    public int isExisted(String username) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery("select id from users where username=?", selectionArgs);
        if (cursor.moveToNext()){
            int idUser = cursor.getInt(0);
            cursor.close();
            return idUser;
        }
        cursor.close();
        return -1;
    }

    public static int isSavedDiner(int userId, int dinerId) {
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+"", dinerId+""};
        Cursor cursor = db.rawQuery("select id from like_diners where userId=? and dinerId=?", selectionArgs);
        if (cursor.moveToNext()){
            int idUser = cursor.getInt(0);
            cursor.close();
            return idUser;
        }
        cursor.close();
        return -1;
    }


    public static void unSavedDiner(int userId, int dinerId){
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", dinerId+""};
        db.execSQL("delete from like_diners where userId=? and dinerId=?", selectionArgs);
    }

    public static void savedDiner(int userId, int dinerId){
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", dinerId+""};
        db.execSQL("insert into like_diners values (null, ?, ?)", selectionArgs);
    }

    public static int isSavedFood(int userId, int foodId) {
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+"", foodId+""};
        Cursor cursor = db.rawQuery("select id from like_foods where userId=? and foodId=?", selectionArgs);
        if (cursor.moveToNext()){
            int idUser = cursor.getInt(0);
            cursor.close();
            return idUser;
        }
        cursor.close();
        return -1;
    }


    public static void unSavedFood(int userId, int foodId){
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", foodId+""};
        db.execSQL("delete from like_foods where userId=? and foodId=?", selectionArgs);
    }

    public static void savedFood(int userId, int foodId){
        SQLiteDatabase db;
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", foodId+""};
        db.execSQL("insert into like_foods values (null, ?, ?)", selectionArgs);
    }
    //Tham khao cau lenh: https://vncoder.vn/bai-hoc/insert-update-delete-du-lieu-menh-de-where-trong-sqlite-188
}
