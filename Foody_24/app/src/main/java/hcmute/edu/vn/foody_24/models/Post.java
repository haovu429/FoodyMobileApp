package hcmute.edu.vn.foody_24.models;

public class Post {
    private Integer id;
    private Integer userId;
    private Integer dinerId;
    private Integer foodId;
    private String title;
    private String content;
    private String createAt;
    private boolean allow_comment;

    public Post(Integer id, Integer userId, Integer dinerId, Integer foodId, String title, String content, String createAt, boolean allow_comment) {
        this.id = id;
        this.userId = userId;
        this.dinerId = dinerId;
        this.foodId = foodId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.allow_comment = allow_comment;
    }

    public Post(Integer userId, Integer dinerId, String title, String content, String createAt, boolean allow_comment) {
        this.userId = userId;
        this.dinerId = dinerId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.allow_comment = allow_comment;
    }

    public Post(Integer userId, String title, String content, String createAt, boolean allow_comment, Integer foodId) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.allow_comment = allow_comment;
        this.foodId = foodId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public boolean isAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(boolean allow_comment) {
        this.allow_comment = allow_comment;
    }


}
