package hcmute.edu.vn.foody_24.models;

public class DinerRate {
    private Integer id;
    private Integer userId;
    private Integer dinerId;
    private Long postId;
    private Integer rateViTri;
    private Integer rateGiaCa;
    private Integer rateChatLuong;
    private Integer rateDichVu;
    private Integer rateKhongGian;

    public DinerRate(Integer id, Integer userId, Integer dinerId, Long postId, Integer rateViTri,
                     Integer rateGiaCa, Integer rateChatLuong, Integer rateDichVu, Integer rateKhongGian) {
        this.id = id;
        this.userId = userId;
        this.dinerId = dinerId;
        this.postId = postId;
        this.rateViTri = rateViTri;
        this.rateGiaCa = rateGiaCa;
        this.rateChatLuong = rateChatLuong;
        this.rateDichVu = rateDichVu;
        this.rateKhongGian = rateKhongGian;
    }

    public DinerRate(Integer userId, Integer dinerId, Long postId, Integer rateViTri, Integer rateGiaCa,
                     Integer rateChatLuong, Integer rateDichVu, Integer rateKhongGian) {
        this.userId = userId;
        this.dinerId = dinerId;
        this.postId = postId;
        this.rateViTri = rateViTri;
        this.rateGiaCa = rateGiaCa;
        this.rateChatLuong = rateChatLuong;
        this.rateDichVu = rateDichVu;
        this.rateKhongGian = rateKhongGian;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Integer getRateViTri() {
        return rateViTri;
    }

    public void setRateViTri(Integer rateViTri) {
        this.rateViTri = rateViTri;
    }

    public Integer getRateGiaCa() {
        return rateGiaCa;
    }

    public void setRateGiaCa(Integer rateGiaCa) {
        this.rateGiaCa = rateGiaCa;
    }

    public Integer getRateChatLuong() {
        return rateChatLuong;
    }

    public void setRateChatLuong(Integer rateChatLuong) {
        this.rateChatLuong = rateChatLuong;
    }

    public Integer getRateDichVu() {
        return rateDichVu;
    }

    public void setRateDichVu(Integer rateDichVu) {
        this.rateDichVu = rateDichVu;
    }

    public Integer getRateKhongGian() {
        return rateKhongGian;
    }

    public void setRateKhongGian(Integer rateKhongGian) {
        this.rateKhongGian = rateKhongGian;
    }

    public double getAverageRate(){
        System.out.println("vitri:"+rateViTri+"-Giaca:"+rateGiaCa+"-ChatLuong:"+rateChatLuong+"-dichVu:"+rateDichVu+"-khongGian:"+rateKhongGian);
        return (rateViTri + rateGiaCa +rateChatLuong + rateDichVu + rateKhongGian+0.0)/5;
    }
}
