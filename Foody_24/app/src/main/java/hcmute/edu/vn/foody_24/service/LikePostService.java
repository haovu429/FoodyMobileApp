package hcmute.edu.vn.foody_24.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.Post;

public class LikePostService {
    SQLiteDatabase db;

    public int isLikedPost(int userId, int postId){
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+"", postId+""};
        Cursor cursor = db.rawQuery("select id from like_posts where userId=? and postId=?", selectionArgs);
        if (cursor.moveToNext()){
            int id_like_post = cursor.getInt(0);
            cursor.close();
            return id_like_post;
        }
        cursor.close();
        return -1;
    }

    public void insert(int userId, int postId){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", postId+""};
        db.execSQL("insert into like_posts values (null, ?, ?)", selectionArgs);
    }

    public void unLikePost(int userId, int dinerId){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        String[] selectionArgs = {userId+"", dinerId+""};
        db.execSQL("delete from like_posts where userId=? and postId=?", selectionArgs);
    }
    public ArrayList<Post> getByDinerId(Integer dinerId) {
        ArrayList<Post> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from posts where dinerId="+dinerId, null);
        while (cursor.moveToNext()){

            boolean allow_comment;
            if (cursor.getInt(7)==1){
                allow_comment = true;
            } else {
                allow_comment = false;
            }

            Post p = new Post(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),allow_comment);
            result.add(p);
        }
        cursor.close();
        return result;


    }

    public int like_amount_of_post(int postId){
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {postId+""};
        Cursor cursor = db.rawQuery("select count(id) from like_posts where postId=?", selectionArgs);
        if (cursor.moveToNext()){
            int amount = cursor.getInt(0);
            cursor.close();
            return amount;
        }
        cursor.close();
        return -1;
    }

}
