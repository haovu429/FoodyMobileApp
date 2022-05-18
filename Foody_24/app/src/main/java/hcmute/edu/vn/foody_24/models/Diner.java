package hcmute.edu.vn.foody_24.models;

import java.io.Serializable;
import java.util.List;

import hcmute.edu.vn.foody_24.service.DinerRateService;

public class Diner implements Serializable {
    private int id;
    private int img_diner;
    private String name_diner;
    private String openAt, closeAt;
    private String address;

    private List<Food> foods;
    private List<User> likedUsers;

    public Diner(int id, int img_diner, String name_diner, String openAt, String closeAt, String address, List<Food> foods) {
        this.id = id;
        this.img_diner = img_diner;
        this.name_diner = name_diner;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.address = address;
        this.foods = foods;
    }

    public Diner(int img_diner, String name_diner) {
        this.img_diner = img_diner;
        this.name_diner = name_diner;
    }

    public Diner(int img_diner, String name_diner, String address) {
        this.img_diner = img_diner;
        this.name_diner = name_diner;
        this.address = address;
    }

    public int getImg_diner() {
        return img_diner;
    }

    public void setImg_diner(int img_diner) {
        this.img_diner = img_diner;
    }

    public String getName_diner() {
        return name_diner;
    }

    public void setName_diner(String name_diner) {
        this.name_diner = name_diner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAverageRate(){
        DinerRateService dinerRateService = new DinerRateService();
        List<DinerRate> rateList = dinerRateService.getByDinerId(this.getId());
        System.out.println("rateList:"+rateList.size());
        double S=0;
        for(DinerRate dr : rateList){
            S = S + dr.getAverageRate();
        }
        if (rateList.size() !=0){
            return S/rateList.size();
        }
        return 0;
    }
}
