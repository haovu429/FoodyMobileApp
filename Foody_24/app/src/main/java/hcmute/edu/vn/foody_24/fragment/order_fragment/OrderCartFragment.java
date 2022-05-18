package hcmute.edu.vn.foody_24.fragment.order_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.BillAdapter;
import hcmute.edu.vn.foody_24.fragment.home_fragment.HomeFragment;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.DinerService;

public class OrderCartFragment extends Fragment {

    RecyclerView rcvData;
    BillAdapter billAdapter;
    List<Bill> listBill;

    View view;
    int idUser;

    public OrderCartFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    public static OrderCartFragment newInstance(int idUser) {
        Bundle args = new Bundle();
        args.putInt("idUser", idUser);
        OrderCartFragment f = new OrderCartFragment(idUser);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order_cart, container, false);

        XuLy();

        return view;
    }

    public void XuLy(){
        rcvData = view.findViewById(R.id.rcv_data);

        listBill = getListData();
        billAdapter = new BillAdapter(listBill, getContext(), idUser);

        boolean errorData = false;
        for (Bill bill : listBill){
            DinerService dinerService = new DinerService();
            Diner diner = dinerService.getOne(bill.getStoreId());
            if (diner == null){
                errorData = true;
                System.out.println("Chỗ này bị lỗi db khi load diner cho bill");
            }
        }
        if (!errorData){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

            rcvData.setLayoutManager(linearLayoutManager);

            //billAdapter.setData(getListData(), getContext());
            rcvData.setAdapter(billAdapter);
        }
    }

    private List<Bill> getListData() {
        List<Bill> listBill = new ArrayList<>();

        /*List<BillDetails> listBillDetail = new ArrayList<>();
        listBillDetail.add(new BillDetails(1,0,3));
        listBillDetail.add(new BillDetails(2,0,2));
        listBillDetail.add(new BillDetails(2,0,7));

        List<BillDetails> listBillDetail2 = new ArrayList<>();
        listBillDetail2.add(new BillDetails(3,0,7));
        listBillDetail2.add(new BillDetails(2,0,2));
        listBillDetail2.add(new BillDetails(3,0,5));

        Bill bill = new Bill(0,1,1,"Nghệ An", listBillDetail,0);
        Bill bill2 = new Bill(0,1,2,"Nghệ An", listBillDetail2,0);
        System.out.println("Price Bill" + String.valueOf(bill.getTotalPrice()));

        System.out.println("List Bill detail 1");
        for(BillDetails billDetails : bill.getDetails()){
            System.out.println(billDetails.getFood().getName()+ "-"+billDetails.getAmount());
        }
        System.out.println("List Bill detail 2");
        for(BillDetails billDetails : bill2.getDetails()){
            System.out.println(billDetails.getFood().getName()+ "-"+billDetails.getAmount());
        }

        listBill.add(bill);
        listBill.add(bill2);*/

        BillService billService = new BillService();
        listBill = billService.getByUserId_BillStatus(idUser,0);

        /*for(Bill b : listBill){
            System.out.println("List Bill detail "+ b.getStoreObject().getName_diner());
            for(BillDetails billDetails : b.getDetails()){
                System.out.println(billDetails.getFood().getName()+ "-"+billDetails.getAmount());
            }
        }*/

        return listBill;
    }

    @Override
    public void onResume() {
        super.onResume();

        XuLy();
    }
}
