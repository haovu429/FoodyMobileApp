package hcmute.edu.vn.foody_24.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;


public class BillService {
    SQLiteDatabase db;
    public List<BillDetails> getBillDetailsByBillId(Integer billId) {
        List<BillDetails> details = new ArrayList<>();
        FoodService foodService;
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select bd.id, bd.foodId, bd.billId, bd.amount, bd.price from bill_details bd " +
                "join bills b on bd.billId = b.id " +
                "where b.id = " + billId, null);
        while (cursor.moveToNext()){
            BillDetails aDetails = new BillDetails(cursor.getInt(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));

            foodService = new FoodService();
            aDetails.setFood(foodService.getOne(cursor.getInt(1)));
            details.add(aDetails);
        }
        cursor.close();
        return details;
    }


    public Bill getBillAll() {
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bills" , null);
        if (cursor.moveToNext()){
            Bill b = new Bill(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6));
            cursor.close();
            return b;
        }
        cursor.close();
        return null;
    }

    public Bill getBillById(Integer billId) {
        //List<Bill> bills = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bills where id=" + billId, null);
        if (cursor.moveToNext()){
            Bill b = new Bill(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6));
            //b.addDetails(getBillDetailsByBillId(billId));
            cursor.close();
            return b;
        }
        cursor.close();
        return null;
    }

    public List<Bill> getByUserId(Integer userId) {
        List<Bill> bills = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bills where userId="+userId, null);
        while (cursor.moveToNext()){
            Bill bill = new Bill(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6));


            //bill.setDetails(getBillDetailsByBillId(cursor.getInt(0)));

            /*System.out.println("Sau khi setDetail---------");
            System.out.println("Dang la bill: "+cursor.getInt(0));
            for(BillDetails b : bill.getDetails()){
                System.out.println("Ten: "+b.getFoodObject().getName()+"-SoLuong: "+b.getAmount());
            }*/
            bills.add(bill);
        }
        cursor.close();
        return bills;
    }

    public List<Bill> getByUserId_BillStatus(Integer userId, int status) {
        List<Bill> bills = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] bindArg = {String.valueOf(userId), String.valueOf(status)};
        Cursor cursor = db.rawQuery("select * from bills where userId=? AND status=?", bindArg);
        while (cursor.moveToNext()){
            Bill bill = new Bill(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6));
            bills.add(bill);
        }
        cursor.close();
        return bills;
    }

    public Bill getByUserId_diner_status(int userId, int dinerId, int status) {
        //List<Bill> bills = new ArrayList<>();
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] bindArg = {String.valueOf(userId), String.valueOf(dinerId), String.valueOf(status)};
        Cursor cursor = db.rawQuery("select * from bills where userId=? AND storeId=? AND status=?", bindArg);
        while (cursor.moveToNext()){
            Bill bill = new Bill(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6));

            //Nếu giỏ hàng này đã có billdetail thì load lên, không có thì thôi
            List<BillDetails> details = getBillDetailsByBillId(cursor.getInt(0));
            if (details != null){
                bill.addDetails(details);
            }
            cursor.close();
            return bill;
            //bills.add(bill);
        }
        cursor.close();
        return null;
    }

    public void insertBillWithDetails(Bill bill){
        List<BillDetails> billDetails = bill.getDetails();
        if (billDetails != null) {
            long insertedBillId = insertBillOnly(bill);
            if (insertedBillId > -1) {
                for (BillDetails details : billDetails) {
                    details.setBillId((int)insertedBillId);
                    insertBillDetails(details);
                }
            }
        }
    }

    public void insertBillDetails(BillDetails details){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("foodId", details.getFoodId());
        values.put("billId", details.getBillId());
        values.put("amount", details.getAmount());
        values.put("price", details.getPrice());
        System.out.println("result = "+db.insert("bill_details", null, values));
    }
    public long insertBillOnly(Bill bill) {
        db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", bill.getUserId());
        values.put("createdAt", bill.getCreatedAt());
        values.put("storeId", bill.getStoreId());
        values.put("totalPrice", bill.getTotalPrice());
        values.put("address", bill.getAddress());
        values.put("status", bill.getStatus());
        return db.insert("bills", null, values);
    }

    public int calculateBillTotalPrice(Bill bill){
        int total = 0;
        for (BillDetails detail : bill.getDetails()) {
            total += detail.getPrice();
        }
        return total;
    }

    public double calculateBillDetailsPrice(BillDetails details){
        return details.getAmount() * details.getFood().getPrice();
    }

    public void syncBill(Bill bill) {
        db = DatabaseHelper.getInstance().getWritableDatabase();
        BillService billService = new BillService();
        //Tự cập nhật, tính tổng số lượng, giá
        Bill b = new Bill(billService.getBillById(bill.getId()));
        updateBillOnly(b);
    }

    public void updateBillOnly(Bill bill){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        Object[] bindArgs = {bill.getTotalPrice(), bill.getAddress(), bill.getStatus(), bill.getId()};
        db.execSQL("update  bills set totalPrice=?, address=?, status=? " +
                "where id=?;", bindArgs);
    }

    public  void deleteAllBillDetail(int billId){
        db = DatabaseHelper.getInstance().getWritableDatabase();
        db.execSQL("delete  from bill_details where billId="+billId);
    }

    public void updateBill_withBillDetail1(Bill bill){
        updateBillOnly(bill);
        deleteAllBillDetail(bill.getId());

        BillDetailService billDetailService = new BillDetailService();

        for(BillDetails billDetails : bill.getDetails()){
            billDetailService.insertOnly(billDetails);
        }
    }

    public void updateBill_withBillDetail(Bill bill){
        Bill old_bill = getBillById(bill.getId());
        //update bill--Update thông tin cơ bản của bill (trừ amount,
        // và price là không tin cậy, tự đồng bộ hoá lại)
        updateBillOnly(bill);

        //Update BillDetail of bill
        if(bill.getDetails() != null){

            List<BillDetails> billDetails_update = new ArrayList<>();
            List<BillDetails> billDetails_addNew = new ArrayList<>();
            List<BillDetails> billDetails_delete = new ArrayList<>();

            for(BillDetails new_detail : bill.getDetails()){
                Boolean is_new = true;
                for(BillDetails old_detail : old_bill.getDetails()){
                    if(new_detail.getFoodId() == old_detail.getFoodId()){
                        billDetails_update.add(new_detail);
                        is_new = false;
                    }
                }
                if (is_new) {
                    billDetails_addNew.add(new_detail);
                }
            }
            bill.getDetails().removeAll(billDetails_addNew);
            bill.getDetails().removeAll(billDetails_update);
            billDetails_delete = bill.getDetails();

            BillDetailService billDetailService = new BillDetailService();
            for(BillDetails b : billDetails_addNew){
                billDetailService.insert(b);
            }
            for(BillDetails b : billDetails_update){
                billDetailService.updateBillDetail(b);
            }
            billDetailService.deleteMany(billDetails_delete);
        }
    }

    public void deleteBill(Bill bill){
        //Xoá các BillDetail của đơn này
        BillDetailService billDetailService = new BillDetailService();
        billDetailService.deleteMany(bill.getDetails());

        //Xoá đơn này
        db = DatabaseHelper.getInstance().getReadableDatabase();
        String[] bindArg = {String.valueOf(bill.getId())};
        db.execSQL("delete from bills where id=?", bindArg);
    }
}
