package hcmute.edu.vn.foody_24.models;

public class Photo {
    private Integer id;
    private Integer imgId;
    private Integer dinerId;
    private Integer foodId;
    private Integer postId;
    private Integer userId;

    public Photo() {
    }

    public Photo(Integer id, Integer imgId, Integer dinerId, Integer foodId, Integer postId, Integer userId) {
        this.id = id;
        this.imgId = imgId;
        this.dinerId = dinerId;
        this.foodId = foodId;
        this.postId = postId;
        this.userId = userId;
    }

    public Photo(Integer imgId, Integer dinerId, Integer foodId, Integer postId, Integer userId) {
        this.imgId = imgId;
        this.dinerId = dinerId;
        this.foodId = foodId;
        this.postId = postId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getDinerId() {
        return dinerId;
    }

    public void setDinerId(Integer dinerId) {
        this.dinerId = dinerId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
