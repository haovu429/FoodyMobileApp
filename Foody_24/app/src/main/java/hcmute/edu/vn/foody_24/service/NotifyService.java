package hcmute.edu.vn.foody_24.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.models.User;

public class NotifyService {
    SQLiteDatabase db;
    public Notify getOne(int notifyId) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select type, title, mess, time, userId " +
                "from notifies where id="+notifyId, null);
        if (cursor.moveToNext()){
            Notify notify = new Notify(notifyId, cursor.getInt(0),cursor.getString(1), cursor.getString(2),
                    cursor.getString(3),cursor.getInt(4));
            cursor.close();
            return notify;
        }
        cursor.close();
        return null;
    }

    public List<Notify> getByUserId(Integer userId) {
        List<Notify> notifies = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] bindArg = {String.valueOf(userId)};
        Cursor cursor = db.rawQuery("select * from notifies where userId=? OR userId=-1", bindArg);
        while (cursor.moveToNext()){
            Notify notify = new Notify(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4),cursor.getInt(5));
            notifies.add(notify);
        }
        cursor.close();
        return notifies;
    }

    public void insert(Notify notify) {
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArgs = {notify.getType(), notify.getTitle(), notify.getMess(), notify.getTime(), notify.getUserId()};
        db.execSQL("insert into notifies values (null, ?, ?, ?, ?, ?)", bindArgs);
    }
}
