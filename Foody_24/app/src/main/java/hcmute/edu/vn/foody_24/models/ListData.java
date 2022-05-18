package hcmute.edu.vn.foody_24.models;

import java.util.List;

public class ListData {

    //private List<hcmute.edu.vn.recyclerview_inside_recyclerview.model.Category> listCategory;
    private Bill bill;

    public ListData(Bill bill) {
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    //    public List<hcmute.edu.vn.recyclerview_inside_recyclerview.model.Category> getListCategory() {
//        return listCategory;
//    }
//
//    public void setListCategory(List<hcmute.edu.vn.recyclerview_inside_recyclerview.model.Category> listCategory) {
//        this.listCategory = listCategory;
//    }


}
