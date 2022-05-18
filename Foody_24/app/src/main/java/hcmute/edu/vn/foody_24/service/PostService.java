package hcmute.edu.vn.foody_24.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.Post;

public class PostService {
    SQLiteDatabase db;

    public long insert(Post post){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        int allow_comment;
        if (post.isAllow_comment()) {
            allow_comment = 1;
        } else {
            allow_comment = 0;
        }

        db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", post.getUserId());
        values.put("dinerId", post.getDinerId());
        values.put("foodId", post.getFoodId());
        values.put("title", post.getTitle());
        values.put("content", post.getContent());
        values.put("createAt",post.getCreateAt());
        values.put("allow_comment", allow_comment);
        return db.insert("posts", null, values);

        /*Object[] bindArg = {post.getUserId(), post.getDinerId(), post.getFoodId(), post.getTitle(),
                post.getContent(), post.getCreateAt(), allow_comment};
        db.execSQL("insert into posts values (null, ?, ?, ?, ?, ?, ?, ?, ?)", bindArg);*/
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

    public int post_amount_of_user(int userId){
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+""};
        Cursor cursor = db.rawQuery("select count(id) from posts where userId=?", selectionArgs);
        if (cursor.moveToNext()){
            int amount = cursor.getInt(0);
            cursor.close();
            return amount;
        }
        cursor.close();
        return -1;
    }

    public int post_amount_of_diner(int dinerId){
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {dinerId+""};
        Cursor cursor = db.rawQuery("select count(id) from posts where dinerId=?", selectionArgs);
        if (cursor.moveToNext()){
            int amount = cursor.getInt(0);
            cursor.close();
            return amount;
        }
        cursor.close();
        return -1;
    }

}
