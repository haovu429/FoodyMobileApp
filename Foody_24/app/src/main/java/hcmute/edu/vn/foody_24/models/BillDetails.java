package hcmute.edu.vn.foody_24.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.FoodService;

public class BillDetails implements Serializable {
    private int id;
    private int foodId;
    private int billId;
    private int amount;
    private double price;

    private Food food;

    //Dành cho DAO
    public BillDetails( int id,@NonNull int foodId,@NonNull int billId,@NonNull int amount, double price) {
        this.id = id;
        this.foodId = foodId;
        this.billId = billId;
        this.amount = amount;
        this.price = price;
        pricing();
    }

    public BillDetails(@NonNull int foodId,@NonNull int billId, @NonNull int amount) {
        this.foodId = foodId;
        this.billId = billId;
        this.amount = amount;
        pricing();

    }

    private void pricing(){
        FoodService foodService = new FoodService();
        this.food = foodService.getOne(foodId);
        if(food != null) {
            this.price = food.getPrice() * this.amount;
        }

    }

    public Food getFoodFromDB(int foodId){
        FoodService foodService = new FoodService();
        return foodService.getOne(foodId);
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {

        food = getFoodFromDB(foodId);
        this.foodId = foodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.price = food.getPrice() * amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceWithMoneyFormat() {
        String s = this.price + "";
        String result = " đ";
        int i;
        for (i = s.length() - 3; i > 0; i -= 3)
            result = "." + s.substring(i, i + 3) + result;
        if (i <= 0)
            result = s.substring(0, i + 3) + result;
        return result.charAt(0) == '.' ? result.substring(1) : result;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void addAmount() { this.amount++; }

    public Food getFoodObject(){
        FoodService foodService = new FoodService();
        if ((Integer)foodId != null){
            return foodService.getOne(foodId);
        }
        return null;
    }

    public Bill getBillObject(){
        BillService billService = new BillService();
        if ((Integer)billId != null){
            return billService.getBillById(billId);
        }
        return null;
    }
}
