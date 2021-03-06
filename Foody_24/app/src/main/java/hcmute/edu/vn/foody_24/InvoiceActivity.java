package hcmute.edu.vn.foody_24;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDate;
import java.time.LocalTime;

import hcmute.edu.vn.foody_24.adapters.BilldetailPaymentAdapter;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.NotifyService;

public class InvoiceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Bill bill;
    private TextView tv_name_diner, tv_user_name, tv_delivery_time,
            tv_quantity, tv_total_price, tv_delivery_charge, tv_discount,
            tv_final_price, tv_distance;//,edit_info;

    private EditText editv_address, editv_phone_number;

    private Button btn_confirm, btn_cancel;

    private RecyclerView rcv_details;

    int delivery_time; // minutes
    double delivery_charge = 3;
    double discount = 1;

    double final_price = 0;

    private static final int CHOOSE_METHOD_COLOR = R.color.payment_method_color;
    private static final int NOT_CHOOSE_METHOD_COLOR = R.color.not_payment_method_color;

    private BilldetailPaymentAdapter billdetailPaymentAdapter;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.bill = (Bill) bundle.get("object_bill");

        AnhXa();
        setupUI();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupUI(){
        tv_name_diner.setText(bill.getStoreObject().getName_diner());
        tv_user_name.setText(bill.getUserObject().getUsername());

        if (bill.getAddress().isEmpty() || bill.getAddress() == null){
            editv_address.setText(bill.getUserObject().getAddress());
        } else {
            editv_address.setText(bill.getAddress());
        }

        if (bill.getUserObject().getPhone().isEmpty() || bill.getUserObject().getPhone() == null ){

        } else {
            editv_phone_number.setText(bill.getUserObject().getPhone());
        }

        tv_quantity.setText("T???ng "+bill.getAmount()+" ph???n");
        tv_distance.setText(5.6 +" km");
        tv_total_price.setText(bill.getTotalPrice()+"??");
        tv_delivery_charge.setText(delivery_charge+"??");
        tv_discount.setText("-"+discount+"??");

        final_price = bill.getTotalPrice() + delivery_charge - discount;
        tv_final_price.setText(final_price +"??" );

        if (bill.getStatus() > 0){
            editv_phone_number.setFocusable(false);
            editv_phone_number.setEnabled(false);
            editv_phone_number.setCursorVisible(false);
            editv_phone_number.setKeyListener(null);
            editv_phone_number.setBackgroundColor(Color.TRANSPARENT);

            editv_address.setFocusable(false);
            editv_address.setEnabled(false);
            editv_address.setCursorVisible(false);
            editv_address.setKeyListener(null);
            editv_address.setBackgroundColor(Color.TRANSPARENT);

            //Kh??ng hi???n th??? N??t Edit th??ng tin
            View edit_info = findViewById(R.id.tv_edit_payment_info);
            edit_info.setVisibility(View.GONE);

            if (bill.getStatus() == 2){
                btn_confirm.setText("???? ho??n th??nh!");
                btn_confirm.setEnabled(false);

                View view_button_cancel = findViewById(R.id.btn_cancel_invoice);
                view_button_cancel.setVisibility(View.GONE);
            }

        }

        btn_confirm.setOnClickListener(View -> {
            saveBillToDB(); // update ????n h??ng ???? ho??n th??nh

            //create notify
            Notify notify = new Notify(Notify.COMPLETED_BILL_TYPE, "Ho??n th??nh ????n h??ng",
                    "Yeh, yeh. Tuy???t v???i! ????n h??ng c?? m?? "+bill.getId()+" c???a b???n ???? ???????c giao th??nh c??ng\nC???m ??n b???n ???? mua h??ng, l???n sau l???i gh?? nh??, hihi",
                    LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
            NotifyService notifyService = new NotifyService();
            notifyService.insert(notify);

            Toast.makeText(this, "???? x??c nh???n ho??n th??nh ????n h??ng!", Toast.LENGTH_SHORT).show();
            btn_confirm.setText("???? ho??n th??nh!");
            btn_confirm.setEnabled(false);
            finish();
        });

        btn_cancel.setOnClickListener(View -> {
            cancelBill();
            //create notify
            Notify notify = new Notify(Notify.CANCEL_BILL_TYPE, "Hu??? ????n h??ng",
                    "Hmmm... ????n h??ng c?? m?? "+bill.getId()+" ???? b??? hu???. H??y t??m n?? trong gi??? h??ng c???a b???n nh??",
                    LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
            NotifyService notifyService = new NotifyService();
            notifyService.insert(notify);

            Toast.makeText(this, "Hu??? giao h??ng, ????n ???? tr??? v??? gi???!", Toast.LENGTH_SHORT).show();
            btn_confirm.setText("???? ho??n th??nh!");
            btn_confirm.setEnabled(false);
            finish();
        });

        tv_name_diner.setOnClickListener(View -> {
            onClickGoToDetailDiner(bill.getStoreObject(),bill.getUserId());
        });

        setupRecyclerView();
    }

    private void onClickGoToDetailDiner(Diner diner, int idUser) {

        Intent intent = new Intent(this, DetailShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }


    public void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        billdetailPaymentAdapter = new BilldetailPaymentAdapter();
        billdetailPaymentAdapter.setData(bill.getDetails());

        rcv_details.setLayoutManager(linearLayoutManager);
        rcv_details.setAdapter(billdetailPaymentAdapter);
    }

    void saveBillToDB(){
        //Bill ???? t???n t???i tr???ng th??i ??ang giao (status = 1) ch??a ho??n th??nh -> chuy???n th??nh ???? ho??n th??nh status =2
        BillService billService = new BillService();
        bill.setStatus(2);
        billService.updateBillOnly(bill);
    }

    void cancelBill(){
        //Bill ???? t???n t???i tr???ng th??i ??ang giao (status = 1) ch??a ho??n th??nh -> chuy???n v??? gi??? h??ng status = 0
        BillService billService = new BillService();
        bill.setStatus(0);
        billService.updateBillOnly(bill);
    }


    private void AnhXa() {
        tv_name_diner = findViewById(R.id.tv_diner_name_payment);
        tv_user_name = findViewById(R.id.tv_user_name_payment);
//        tv_edit_info = (TextView) findViewById(R.id.tv_edit_payment_info);
        tv_delivery_time = findViewById(R.id.tv_tg_giao_payment);
        tv_quantity = findViewById(R.id.tv_total_quantity_payment);
        tv_total_price = findViewById(R.id.tv_total_price_payment);
        tv_delivery_charge = findViewById(R.id.tv_delivery_charges);
        tv_discount = findViewById(R.id.tv_discount_payment);
        tv_final_price = findViewById(R.id.tv_final_price_payment);
        tv_distance = findViewById(R.id.tv_distance_invoice);
//        tv_final_price_momo = (TextView) findViewById(R.id.tv_final_price_momo);
//        tv_final_price_cash = (TextView) findViewById(R.id.tv_final_price_cash);
//        tv_other_payment_methods = (TextView) findViewById(R.id.tv_other_payment_methods);
//        edit_info = (TextView) findViewById(R.id.tv_other_payment_methods);

        editv_address = findViewById(R.id.editv_address_payment1);
        editv_phone_number = findViewById(R.id.editv_phone_number);

//        cardv_momo = (CardView) findViewById(R.id.cardv_momo);
//        cardv_cash = (CardView) findViewById(R.id.cardv_cash);

//        layout_momo = (LinearLayout) findViewById(R.id.layout_momo);
//        layout_cash = (LinearLayout) findViewById(R.id.layout_cash);
        btn_cancel = findViewById(R.id.btn_cancel_invoice);
        btn_confirm = findViewById(R.id.btn_confirm_invoice);

        rcv_details = findViewById(R.id.rcv_list_detail_payment);

        //B??? m???t button kh???i giao di???n
        //View b = findViewById(R.id.btn_confirm_invoice);
        //b.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng shop = new LatLng(10.850916, 106.771024); // l??u v?? ????? kinh ????? trong db, t???i ????y l???y ra x??i
        mMap.addMarker(new MarkerOptions()
                .position(shop)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop, 16f));
    }
}