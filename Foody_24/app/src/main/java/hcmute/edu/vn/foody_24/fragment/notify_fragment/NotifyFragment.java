package hcmute.edu.vn.foody_24.fragment.notify_fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.NotifyAdapter;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.service.NotifyService;

public class NotifyFragment extends Fragment {

    private int userId;
    private List<Notify> listNotify;
    private RecyclerView rcv_notify;
    private View view;

    public NotifyFragment(int userId) {
        this.userId = userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notify, container, false);

        AnhXa();

        setupUI();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupUI(){
        listNotify = getData(userId);

        NotifyAdapter notifyAdapter = new NotifyAdapter(listNotify);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rcv_notify.setLayoutManager(linearLayoutManager);
        rcv_notify.setAdapter(notifyAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Notify> getData(int userId){

        List<Notify> notifyList = new ArrayList<>();
       /* Notify notify = new Notify(Notify.CREATE_BILL_TYPE, "Đã đặt đơn hàng", "Bạn đã đặt đơn hàng có mã: 3",
                LocalTime.now()+"     "+ LocalDate.now(),1);*/
        //notifyList.add(notify);

        NotifyService notifyService = new NotifyService();
        notifyList = notifyService.getByUserId(userId);
        return notifyList;
    }

    private void AnhXa(){
        rcv_notify = view.findViewById(R.id.rcv_notify);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        setupUI();
    }
}
