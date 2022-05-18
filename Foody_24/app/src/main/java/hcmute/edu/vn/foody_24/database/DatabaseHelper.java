package hcmute.edu.vn.foody_24.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.foody_24.MyApplication;

public class DatabaseHelper extends SQLiteOpenHelper {
    //public static String DB_PATH = "/data/data/hcmute.edu.vn.foodoapp/databases/";
    private static final String DB_NAME = "foody.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance = null;
    private static final Context context = MyApplication.getContext();

    //Ten cac bang
    private static final String[] arr = { "users", "foods", "bills", "bill_details", "stores", "diners" };
    private static final List<String> listTableName = Arrays.asList(arr);


    public synchronized static DatabaseHelper getInstance() {
        if (instance == null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists users(" +
                "id integer primary key autoincrement," +
                "username varchar(30)," +
                "password varchar(30)," +
                "address varchar(100)," +
                "phone varchar(10));");
        db.execSQL("create table if not exists foods (" +
                "id integer primary key autoincrement, " +
                "image integer, " +
                "name varchar(200), " +
                "type varchar(50), " +
                "description varchar(200), " +
                "price integer, " +
                "dinerId integer);");
        db.execSQL("create table if not exists bills (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "createdAt varchar(200), " +
                "storeId integer, " +
                "totalPrice integer," +
                "address varchar(200)," +
                "status interger);");
        db.execSQL("create table if not exists bill_details (" +
                "id integer primary key autoincrement, " +
                "foodId integer, " +
                "billId integer, " +
                "amount integer, " +
                "price integer);");
        db.execSQL("create table if not exists stores (" +
                "id integer primary key autoincrement, " +
                "image integer, " +
                "name varchar(100), " +
                "openAt varchar(100), " +
                "closeAt varchar(100)," +
                "address varchar(200));");
        db.execSQL("create table if not exists diners (" +
                "id integer primary key autoincrement, " +
                "image integer, " +
                "name varchar(100), " +
                "openAt varchar(100), " +
                "closeAt varchar(100)," +
                "address varchar(200));");
        db.execSQL("create table if not exists notifies (" +
                "id integer primary key autoincrement, " +
                "type integer, " +
                "title varchar(100), " +
                "mess varchar(200), " +
                "time varchar(100), " +
                "userId integer);");
        db.execSQL("create table if not exists like_diners (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "dinerId integer);");
        db.execSQL("create table if not exists like_foods (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "foodId integer);");
        db.execSQL("create table if not exists like_posts (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "postId integer);");
        db.execSQL("create table if not exists posts (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "dinerId integer, " +
                "foodId integer, " +
                "title varchar(100), " +
                "content varchar(300), " +
                "createAt varchar(100), " +
                "allow_comment integer);");
        db.execSQL("create table if not exists diner_rates (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "dinerId integer, " +
                "postId integer, " +
                "rateViTri integer, " +
                "rateGiaCa integer, " +
                "rateChatLuong integer, " +
                "rateDichVu integer, " +
                "rateKhongGian integer);");
        db.execSQL("create table if not exists food_rates (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "foodId integer, " +
                "postId integer, " +
                "rate integer);");
        db.execSQL("create table if not exists comments (" +
                "id integer primary key autoincrement, " +
                "userId integer, " +
                "postId integer, " +
                "content varchar(300));");
        db.execSQL("create table if not exists photos (" +
                "id integer primary key autoincrement, " +
                "imgCode integer, " +
                "dinerId integer, " +
                "foodId integer, " +
                "postId integer, " +
                "userId integer);");


        Log.i("CREATE_DB", "ĐÃ TẠO BẢNG!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String getDbName() {
        return DB_NAME;
    }

    public List<String> getListTableName() {
        return listTableName;
    }
}