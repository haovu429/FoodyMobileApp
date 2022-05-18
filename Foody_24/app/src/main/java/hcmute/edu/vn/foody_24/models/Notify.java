package hcmute.edu.vn.foody_24.models;

public class Notify {

    public static final int NORMAL_TYPE = 0;
    public static final int COMPLETED_BILL_TYPE = 1;
    public static final int CANCEL_BILL_TYPE = 2;
    public static final int SHIPPING_TYPE = 3;
    public static final int CREATE_CART_TYPE = 4;
    public static final int DELETE_CART_TYPE = 5;


    private int id;
    private int type;
    private String title;
    private String mess;
    private String time;
    private int userId = -1; //Là notify cho tất cả người dùng

    public Notify(int id, int type, String title, String mess, String time, int userId) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.mess = mess;
        this.time = time;
        this.userId = userId;
    }

    public Notify(int type, String title, String mess, String time, int userId) {
        this.type = type;
        this.title = title;
        this.mess = mess;
        this.time = time;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
