package hcmute.edu.vn.foody_24.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.User;

public class BillDetailService {

    SQLiteDatabase db;

    public List<BillDetails> getBillDetailAll() {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        List<BillDetails> details = new ArrayList<>();
        FoodService foodService;
        Cursor cursor = db.rawQuery("select * from bill_details" , null);
        while (cursor.moveToNext()){
            BillDetails aDetails = new BillDetails(cursor.getInt(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4));

            foodService = new FoodService();
            aDetails.setFood(foodService.getOne(cursor.getInt(1)));
            details.add(aDetails);
        }
        cursor.close();
        return details;
    }

    public BillDetails getBillDetailById(int id) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bill_details where id="+id , null);
        if (cursor.moveToNext()){
            BillDetails b = new BillDetails(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getInt(3), cursor.getDouble(4));
            cursor.close();
            return b;
        }
        cursor.close();
        return null;
    }


    public List<BillDetails> getBillDetailsByBillId(Integer billId) {
        List<BillDetails> details = new ArrayList<>();
        FoodService foodService;
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bill_details where billId="+billId, null);
        while (cursor.moveToNext()){
            System.out.println("Ban ghi billDetail cho bill");
            BillDetails aDetails = new BillDetails(cursor.getInt(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4));
            foodService = new FoodService();
            aDetails.setFood(foodService.getOne(cursor.getInt(1)));
            details.add(aDetails);
        }
        cursor.close();
        return details;
    }

    public List<BillDetails> getBillDetailsByBillId_FoodId(Integer billId, Integer foodId) {
        List<BillDetails> details = new ArrayList<>();
        FoodService foodService;
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] bindArg = {String.valueOf(billId), String.valueOf(foodId)};
        Cursor cursor = db.rawQuery("select * from bill_details where billId=? AND foodId=?", bindArg);
        while (cursor.moveToNext()){
            BillDetails aDetails = new BillDetails(cursor.getInt(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4));
            foodService = new FoodService();
            aDetails.setFood(foodService.getOne(cursor.getInt(1)));
            details.add(aDetails);
        }
        cursor.close();
        return details;
    }

    public void updateBillDetail(BillDetails billDetails) {
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArgs = {billDetails.getFoodId(), billDetails.getBillId(),
                billDetails.getAmount(), billDetails.getPrice(), billDetails.getId()};
        db.execSQL("update  bill_details set foodId=?, billId=?, amount=?, price=? " +
                "where id=?;", bindArgs);
        syncBill(billDetails.getBillId());
    }

    public void insert(BillDetails billDetails){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        //Kiểm tra trong Db đã có BillDetail trùng BillId và FoodId hay chưa? lấy list
        //Lấy list BillDetail có cùng BillId và FoodId trong DB
        List<BillDetails> exited_same_BillDetail = new ArrayList<>();
        System.out.println(billDetails.getBillId()+"===="+billDetails.getFoodId());
        exited_same_BillDetail = getBillDetailsByBillId_FoodId(billDetails.getBillId(), billDetails.getFoodId());
        for (BillDetails b : exited_same_BillDetail){

            System.out.println(b.getFoodObject().getName());
        }
        // TH1: nếu tồn tại nhiều hơn 1 thì gộp tất cả
        // -> cập nhập BillDetail đầu tiên trong list và số lượng bằng tất cả các BillDetail cộng lại,
        // sau đó chèn BillDetail mới thì cộng vào BillDetail cũ đó luôn
        // TH2: Nếu chỉ có 1 BillDetail cùng tính chất thì cộng số lượng, updata BillDetail cũ đó
        // TH3: Nếu không có BillDetail nào cùng tính chất thì thêm trực tiếp vào DB
        int new_amount = billDetails.getAmount();
        System.out.println("new_amount: "+new_amount);
        if (exited_same_BillDetail != null && exited_same_BillDetail.size() > 0){
            System.out.println("Theo san pham da co sameEntry ");
            if (exited_same_BillDetail.size() > 1){
                System.out.println("Theo san pham da co sameEntry - Vao 1");

                for(int i = 1; i<exited_same_BillDetail.size(); i++){
                    new_amount = new_amount + exited_same_BillDetail.get(i).getAmount();
                    deleteOne(exited_same_BillDetail.get(i).getId());
                }
            }
            //cộng thêm số lượng mới vào bản ghi đã có
            System.out.println("Luu so luong: "+ exited_same_BillDetail.get(0).getAmount()+"***"+new_amount);
            exited_same_BillDetail.get(0).setAmount(exited_same_BillDetail.get(0).getAmount()+new_amount);
            updateBillDetail(exited_same_BillDetail.get(0));
        } else {
            //Thêm mới một bản ghi
            insertOnly(billDetails);
        }
        syncBill(billDetails.getBillId());
    }

    public void insertOnly(BillDetails billDetails){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("foodId", billDetails.getFoodId());
        values.put("billId", billDetails.getBillId());
        values.put("amount", billDetails.getAmount());
        values.put("price", billDetails.getPrice());
        System.out.println("result = "+db.insert("bill_details", null, values));

        /*Object[] bindArg = {billDetails.getFoodId(), billDetails.getBillId(),
                billDetails.getAmount(), billDetails.getPrice()};
        db.execSQL("insert into bill_details values (null, ?, ?, ?, ?)", bindArg);*/

        //syncBill(billDetails.getBillId());
    }

    public void deleteOne(int billDetailId) {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        int billId = -1;
        //BillDetailService billDetailService = new BillDetailService();
        //billId = billDetailService.getBillDetailById(billDetailId).getBillId();

        String[] bindArg = {String.valueOf(billDetailId)};
        db.execSQL("delete from bill_details where id=?", bindArg);
        //syncBill(billId);
    }


    public void deleteMany(List<BillDetails> details){
        for(BillDetails billDetails : details){
            deleteOne(billDetails.getId());
        }
    }

    public void syncBill(int billId){
        BillService billService = new BillService();
        Bill bill = new Bill(billService.getBillById(billId));
        billService.syncBill(bill);
    }
}
