package hcmute.edu.vn.foody_24.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;

public class FoodService {
    SQLiteDatabase db;
    public ArrayList<Food> getAll() {
        ArrayList<Food> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from foods", null);
        while (cursor.moveToNext()){
            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            result.add(f);
        }
        cursor.close();
        return result;
    }

    public ArrayList<Food> getLimit(int limit) {
        ArrayList<Food> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {limit+""};
        Cursor cursor = db.rawQuery("select * from foods limit ?", selectionArgs);
        while (cursor.moveToNext()){
            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            result.add(f);
        }
        cursor.close();
        return result;
    }
    public ArrayList<Food> getByDinerId(Integer dinerId) {
        ArrayList<Food> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from foods where dinerId="+dinerId, null);
        while (cursor.moveToNext()){
            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            result.add(f);
        }
        cursor.close();
        return result;
    }
    public Food getOne(Integer id) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from foods where id="+id, null);
        if (cursor.moveToNext()){
            Food food = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));

            cursor.close();
            return food;
        }
        cursor.close();
        return null;
    }
    public void insert(Food food){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArg = {food.getImage(), food.getName(), food.getType(),
            food.getDescription(), food.getPrice(), food.getDinerId()};
        db.execSQL("insert into foods values (null, ?, ?, ?, ?, ?, ?)", bindArg);
    }
    public double getFoodPrice(int foodId){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.rawQuery("select price from foods where id="+foodId, null);
        if (cursor.moveToNext()){
            double price = cursor.getDouble(0);
            cursor.close();
            return price;
        }
        cursor.close();
        return 0;
    }
    public ArrayList<Food> getByKeyword(String keyword) {
        ArrayList<Food> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from foods where name like '%"+keyword+"%';", null);
        while (cursor.moveToNext()){
            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            result.add(f);
        }
        cursor.close();
        return result;
    }

    public List<Food> getSavedFood_forUser(int userId) {
        List<Food> foods = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+""};
        Cursor cursor = db.rawQuery("select * from foods f " +
                "join like_foods like_f on f.id = like_f.foodId join users u on like_f.userId = u.id " +
                "where u.id = ?", selectionArgs);
        while (cursor.moveToNext()){
            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            foods.add(f);
        }
        cursor.close();
        return foods;
    }
}
