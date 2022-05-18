package hcmute.edu.vn.foody_24.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.DinerRate;
import hcmute.edu.vn.foody_24.models.Food;

public class DinerRateService {
    SQLiteDatabase db;
    public void insert(DinerRate dinerRate){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArg = {dinerRate.getUserId(), dinerRate.getDinerId(), dinerRate.getPostId(), dinerRate.getRateViTri(),
                dinerRate.getRateGiaCa(), dinerRate.getRateChatLuong(), dinerRate.getRateDichVu(), dinerRate.getRateKhongGian()};
        db.execSQL("insert into diner_rates values (null, ?, ?, ?, ?, ?, ?, ?, ?)", bindArg);
    }

    public DinerRate getOne(Integer id) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from diner_rates where id="+id, null);
        if (cursor.moveToNext()){
            DinerRate dinerRate = new DinerRate(cursor.getInt(0), cursor.getInt(1), cursor.getLong(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7));

            cursor.close();
            return dinerRate;
        }
        cursor.close();
        return null;
    }

    public DinerRate getOneByPostId(Integer postId) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from diner_rates where postId="+postId, null);
        if (cursor.moveToNext()){
            DinerRate dinerRate = new DinerRate(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getLong(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7),cursor.getInt(8));

            cursor.close();
            return dinerRate;
        }
        cursor.close();
        return null;
    }

    public List<DinerRate> getByDinerId(Integer dinerId) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        List<DinerRate> rateList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from diner_rates where dinerId="+dinerId, null);
        while (cursor.moveToNext()){
            DinerRate dinerRate = new DinerRate(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getLong(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7),cursor.getInt(8));

            rateList.add(dinerRate);
        }
        cursor.close();
        return rateList;
    }
}
