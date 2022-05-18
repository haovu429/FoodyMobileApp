package hcmute.edu.vn.foody_24.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.Diner;


public class DinerService {
    SQLiteDatabase db;
    FoodService foodService;
    public List<Diner> getAll() {
        List<Diner> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from diners", null);
        while (cursor.moveToNext()){
            foodService = new FoodService();
            List<Food> listFood = foodService.getByDinerId(cursor.getInt(0));
            Diner s = new Diner(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), listFood);
            result.add(s);
        }
        cursor.close();
        return result;
    }

    public Diner getOne(int id) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from diners where id="+id, null);
        if (cursor.moveToNext()){
            //System.out.println("Co ket qua");
            foodService = new FoodService();
            List<Food> listFood = foodService.getByDinerId(cursor.getInt(0));
            Diner diner = new Diner(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), listFood);
            cursor.close();
            return  diner;
        }
        cursor.close();
        return null;
    }

//    public List<Food> getFoodsFromDiner(Integer DinerId) {
//        List<Food> result = new ArrayList<>();
//        db = DatabaseHelper.getInstance().getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from foods where DinerId="+DinerId, null);
//        if (cursor.moveToNext()){
//            Food f = new Food(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
//                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
//            result.add(f);
//        }
//        return result;
//    }
    public void insert(Diner diner){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArg = {diner.getImg_diner(), diner.getName_diner(), diner.getOpenAt(),
                diner.getCloseAt(), diner.getAddress()};
        db.execSQL("insert into diners values (null, ?, ?, ?, ?, ?)", bindArg);
    }

    public List<Diner> getByKeyword(String keyword) {
        List<Diner> result = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from diners where name like '%"+keyword+"%';", null);
        while (cursor.moveToNext()){
            foodService = new FoodService();
            List<Food> listFood = foodService.getByDinerId(cursor.getInt(0));
            Diner s = new Diner(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), listFood);
            result.add(s);
        }
        cursor.close();
        return result;
    }

    public List<Diner> getSavedDiner_forUser(int userId) {
        List<Diner> diners = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] selectionArgs = {userId+""};
        Cursor cursor = db.rawQuery("select * from diners d " +
                "join like_diners like_d on d.id = like_d.dinerId join users u on like_d.userId = u.id " +
                "where u.id = ?", selectionArgs);
        while (cursor.moveToNext()){
            foodService = new FoodService();
            List<Food> listFood = foodService.getByDinerId(cursor.getInt(0));
            Diner s = new Diner(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), listFood);
            diners.add(s);
        }
        cursor.close();
        return diners;
    }

}
