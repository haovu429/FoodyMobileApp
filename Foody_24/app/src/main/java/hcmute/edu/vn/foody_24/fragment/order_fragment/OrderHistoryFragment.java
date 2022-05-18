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
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.DinerService;

public class OrderHistoryFragment extends Fragment {

    RecyclerView rcvData;
    BillAdapter billAdapter;
    List<Bill> listBill;

    View view;
    int idUser;

    public OrderHistoryFragment(int idUser) {
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

    private void XuLy(){
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

        BillService billService = new BillService();
        listBill = billService.getByUserId_BillStatus(idUser,2);

        return listBill;
    }

    @Override
    public void onResume() {
        super.onResume();

        XuLy();
    }
}
