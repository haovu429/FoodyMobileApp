package hcmute.edu.vn.foody_24.models;

import java.io.Serializable;

import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.UserService;

public class Food implements Serializable {
    private int id;
    private int image;
    private String name;
    private String type;
    private String description;
    private double price;
    private Integer dinerId;

    public Food(Integer id, int image, String name, String type, String description, double price,
                Integer dinerId) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.dinerId = dinerId;
    }

    public Food( int image, String name, String description, double price,
                Integer dinerId) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dinerId = dinerId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDinerId() {
        return dinerId;
    }

    public void setDinerId(Integer dinerId) {
        this.dinerId = dinerId;
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

    public Diner getDinerObject(){
        DinerService dinerService = new DinerService();
        if (dinerId != null){
            return dinerService.getOne(dinerId);
        }
        return null;
    }
}
