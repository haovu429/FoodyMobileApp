package hcmute.edu.vn.foody_24.models;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {
    private Integer id;
    private int image;
    private String name;
    private String openAt, closeAt;
    private String address;

    private List<Food> foods;

    public Store(Integer id, int image, String name, String openAt, String closeAt, String address, List<Food> foods) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.foods = foods;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.address = address;
    }
    public Store(Integer id, int image, String name, String openAt, String closeAt, String address) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
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

    public void addFood(Food food) {
        this.foods.add(food);
    }



    public void addFood(Integer id, int image, String name, String type, String description, int price, Integer storeId) {
        this.foods.add(new Food(id, image, name, type, description, price, storeId));
    }



    public String getListFoods() {
        String s = "";
        for (int i = 0; i < this.foods.size(); i++)
            s += " ," + this.foods.get(i).getName();
        return s.length() > 0 ? s.substring(2) : "empty";
    }
}
