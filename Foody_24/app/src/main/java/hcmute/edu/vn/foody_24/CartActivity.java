package hcmute.edu.vn.foody_24;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.foody_24.adapters.BillDetailAdapter;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.my_interface.IClickItem;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.NotifyService;

public class CartActivity extends AppCompatActivity {

    Context context = this;

    List<BillDetails> listBillDetail;
    Bill bill;

    RecyclerView rcv_bill_detail;
    TextView tv_diner_name, tv_user_name, tv_amount, tv_price;
    ImageButton btn_payment, btn_delete_bill;

    BillDetailAdapter billDetailAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.bill = (Bill) bundle.get("object_bill");
        Collections.reverse(bill.getDetails());
        System.out.println("List bill detail--");

        AnhXa();

        setupUI_ofBill();

        //List item BillDetail
        listBillDetail = new ArrayList<>(bill.getDetails());

        billDetailAdapter= new BillDetailAdapter(context, bill.getUserId(), new IClickItem() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void click_btn_change_quantity(List<BillDetails> list) {

                bill.setDetails(list);
                bill.pricing();

                if(list.size() <= 0 ){
                    onClickDeleteBill(bill);
                }

                System.out.println("Chay lai UI- bill.amount =//"+bill.getAmount());
                setupUI_ofBill();
            }
        }, this);
        billDetailAdapter.setData(listBillDetail);
        setupRecyclerView();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setupUI_ofBill(){
        System.out.println("Chay laij UI- bill.amount ="+bill.getAmount());
        tv_diner_name.setText(bill.getStoreObject().getName_diner());
        tv_user_name.setText(bill.getUserObject().getUsername());
        tv_amount.setText(bill.getAmount() + " m??n");
        tv_price.setText(bill.getTotalPrice() + " ??");

        btn_payment.setOnClickListener(View -> {
            onClickGoToPayment(bill);
        });

        btn_delete_bill.setOnClickListener(View -> {
            onClickDeleteBill(bill);
        });

        tv_diner_name.setOnClickListener(View -> {
            onClickGoToDetailDiner(bill.getStoreObject(), bill.getUserId());
        });
    }

    @NonNull
    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    private void onClickGoToDetailDiner(Diner diner, int idUser) {

        Intent intent = new Intent(this, DetailShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        this.startActivity(intent);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickDeleteBill(Bill bill){
        BillService billService = new BillService();
        String diner_name = bill.getStoreObject().getName_diner();
        billService.deleteBill(bill);

        Notify notify = new Notify(Notify.DELETE_CART_TYPE, "???? xo?? gi??? h??ng",
                "Hmmm... b???n ???? xo?? gi??? h??ng c???a qu??n "+bill.getStoreObject().getName_diner()+". Gh?? qu??n l???n sau he!",
                LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
        NotifyService notifyService = new NotifyService();
        notifyService.insert(notify);
        Toast.makeText(context,"???? xo?? gi??? h??ng c???a qu??n "+ diner_name, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onClickGoToPayment(Bill bill){
        Intent intent = new Intent(this, PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_bill", bill);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    public void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // ch???nh l???i s??? l?????ng c??c ViewHolder s??? ???????c l??u l???i tr?????c khi ch??ng b??? ????a v??o nh??m ViewHolder c?? kh??? n??ng ???????c t??i s??? d???ng
        // ????y l?? gi???i ph??p t???m th???i v?? khi k??o recycle view th?? view holder c???a nh???ng item ???????c k??o s??? m???t s??? l?????ng khi k??o hi??n th??? tr??? l???i
        // N???u s??? l?????ng item trong bill >30 th?? v???n s??? x???y ra l???i tr??n (ch??a t??m ra gi???i ph??p)
        //rcv_bill_detail.setItemViewCacheSize(30); //Xem tham kh???o ??? https://viblo.asia/p/deep-dive-ve-recycler-view-Do7546DXZM6
        rcv_bill_detail.setLayoutManager(linearLayoutManager);
        rcv_bill_detail.setAdapter(billDetailAdapter);
    }

    private  void AnhXa(){
        tv_diner_name = findViewById(R.id.tv_diner_name_cart);
        tv_user_name = findViewById(R.id.tv_user_name_cart);
        tv_amount = findViewById(R.id.tv_amount_cart);
        tv_price = findViewById(R.id.tv_price_cart);
        rcv_bill_detail = findViewById(R.id.rcv_list_bill_detail);
        btn_payment = findViewById(R.id.btn_payment);
        btn_delete_bill = findViewById(R.id.btn_delete_bill);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        BillService billService = new BillService();
        //C???p nh???t l???i bill xem ????n ???? ???????c thanh to??n ch??a
        bill = billService.getBillById(bill.getId());
        bill.processing();
        setupUI_ofBill();
        billDetailAdapter.setData(bill.getDetails());
        //N???u ????n ???? ???????c thanh to??n th?? kill activity
        if (bill.getStatus() > 0 ){
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            BillService billService = new BillService();
            billService.updateBill_withBillDetail1(bill);
        } catch (Exception e){
            e.getStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}