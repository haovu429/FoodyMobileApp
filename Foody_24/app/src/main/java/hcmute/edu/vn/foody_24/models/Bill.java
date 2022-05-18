package hcmute.edu.vn.foody_24.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.UserService;

public class Bill implements Serializable {
    private Integer id;
    private Integer userId;
    private String createdAt;
    private Integer storeId;
    private double totalPrice;
    private String address;
    private int amount;
    private int status; //0-nháp /1-Đã xác nhận, đang giao /2-hoàn thành.

    private List<BillDetails> details;

    public Bill(Bill bill) {
        this.id = bill.getId();
        this.userId = bill.getUserId();
        this.createdAt = bill.getCreatedAt();
        this.storeId = bill.getStoreId();
        this.totalPrice = bill.getTotalPrice();
        this.address = bill.getAddress();
        this.status = bill.getStatus();
        processing();
    }

    public Bill(Integer id, Integer userId, String createdAt, Integer storeId, Integer totalPrice, String address, int status) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.status = status;
        processing();
    }

    public Bill(Integer id, Integer userId, Integer storeId, String address, List<BillDetails> details, int status) {
        this.id = id;
        this.userId = userId;
        //this.createdAt = createdAt;
        this.storeId = storeId;
        //this.totalPrice = totalPrice;
        this.address = address;
        this.details = details;
        this.status = status;
        processing();
    }

    public Bill(Integer userId, Integer storeId, int status) {
        this.userId = userId;
        this.storeId = storeId;
        this.status = status;
        this.totalPrice = 0;

        UserService userService = new UserService();
        User user = userService.getOne(userId);

        //Mặc định là địa chỉ của người dùng
        this.address = user.getAddress();
        processing();
    }

    public double getTotalPrice() {
        pricing();
        if ((Double)totalPrice != null){
            //System.out.println("Totao2 price = "+totalPrice);
            return totalPrice;
        }
        return 0;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<BillDetails> getDetails() {
        return details;
    }

    public void addDetails(List<BillDetails> details) {
        addListBillDetail(details);
    }
    public void setDetails(List<BillDetails> details) {
        this.details = details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addDetail(BillDetails billDetail){
        if (this.details != null){
            for(BillDetails old_b : this.details){
                if(billDetail.getFoodId() == old_b.getFoodId()){
                    old_b.setAmount(old_b.getAmount()+billDetail.getAmount());
                    pricing();
                    return;
                    //Nếu một món được thêm vào đã có trong giỏ hàng thì sẽ cộng số lượng của món trên giỏ hàng lên, chứ không thêm nữa
                }
            }
            details.add(billDetail);
        } else {
            details = new ArrayList<>();
            details.add(billDetail);
        }
        pricing();
    }

    public void addListBillDetail(List<BillDetails> listDetails){
        //Tạo một danh sách mới lọc ra những billDetail không bị trùng với các BillDetail trong danh sách Bill
        for(BillDetails b : listDetails){
            addDetail(b);
        }
    }

    public void pricing(){
        int amount = 0;
        double totalPrice = 0;
        for (BillDetails b : details){
            totalPrice = totalPrice + b.getPrice();
            amount = amount + b.getAmount();
        }
        this.totalPrice = totalPrice;
        this.amount = amount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPriceWithMoneyFormat() {
        String s = this.totalPrice + "";
        String result = " đ";
        int i;
        for (i = s.length() - 3; i > 0; i -= 3)
            result = "." + s.substring(i, i + 3) + result;
        if (i <= 0)
            result = s.substring(0, i + 3) + result;
        return result.charAt(0) == '.' ? result.substring(1) : result;
    }

    //Gộp các item có cùng loại đồ ăn (mua món đó 2 lần)
    public void listProcessing(){
        ArrayList<BillDetails> copy_details = (ArrayList<BillDetails>) ((ArrayList<BillDetails>)this.details).clone();
        this.details.clear();
        addListBillDetail(copy_details);
        //ArrayList<Integer> remove_points = new ArrayList<>();
        /*for (int i = 0; i < copy_details.size(); i++) {
            System.out.println("Size this.de ="+details.size());
            for (int j = 0; j < i; j++)
                if (copy_details.get(i).getFoodId() == copy_details.get(j).getFoodId()) {
                    copy_details.get(j).setAmount(copy_details.get(j).getAmount() + copy_details.get(i).getAmount());
                    copy_details.remove(i);
                    if(i != copy_details.size()-1){
                        i--;
                    }
                }
        }
        this.details = copy_details;*/
    }

    public void processing() {
        getDetailsFromDB();
        this.totalPrice = 0;
        if (details != null){
            listProcessing();
            pricing();
        }
    }

    public void getDetailsFromDB(){
        BillService billService = new BillService();
        details = billService.getBillDetailsByBillId(id);
    }

    public Diner getStoreObject(){
        DinerService dinerService = new DinerService();
        if (storeId != null){
            return dinerService.getOne(storeId);
        }
        return null;
    }

    public User getUserObject(){
        UserService userService = new UserService();
        if (userId != null){
            return userService.getOne(userId);
        }
        return null;
    }


}
