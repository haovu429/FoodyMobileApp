package hcmute.edu.vn.foody_24.models;

public class FoodRate {

    private Integer id;
    private Integer userId;
    private Integer foodId;
    private Integer postId;
    private Integer rate;

    public FoodRate(Integer id, Integer userId, Integer foodId, Integer postId, Integer rate) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.postId = postId;
        this.rate = rate;
    }

    public FoodRate(Integer userId, Integer foodId, Integer postId, Integer rate) {
        this.userId = userId;
        this.foodId = foodId;
        this.postId = postId;
        this.rate = rate;
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

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
