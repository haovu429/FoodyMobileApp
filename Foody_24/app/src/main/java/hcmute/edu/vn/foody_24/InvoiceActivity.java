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

        tv_quantity.setText("Tổng "+bill.getAmount()+" phần");
        tv_distance.setText(5.6 +" km");
        tv_total_price.setText(bill.getTotalPrice()+"đ");
        tv_delivery_charge.setText(delivery_charge+"đ");
        tv_discount.setText("-"+discount+"đ");

        final_price = bill.getTotalPrice() + delivery_charge - discount;
        tv_final_price.setText(final_price +"đ" );

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

            //Không hiển thị Nút Edit thông tin
            View edit_info = findViewById(R.id.tv_edit_payment_info);
            edit_info.setVisibility(View.GONE);

            if (bill.getStatus() == 2){
                btn_confirm.setText("Đã hoàn thành!");
                btn_confirm.setEnabled(false);

                View view_button_cancel = findViewById(R.id.btn_cancel_invoice);
                view_button_cancel.setVisibility(View.GONE);
            }

        }

        btn_confirm.setOnClickListener(View -> {
            saveBillToDB(); // update đơn hàng đã hoàn thành

            //create notify
            Notify notify = new Notify(Notify.COMPLETED_BILL_TYPE, "Hoàn thành đơn hàng",
                    "Yeh, yeh. Tuyệt vời! Đơn hàng có mã "+bill.getId()+" của bạn đã được giao thành công\nCảm ơn bạn đã mua hàng, lần sau lại ghé nhé, hihi",
                    LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
            NotifyService notifyService = new NotifyService();
            notifyService.insert(notify);

            Toast.makeText(this, "Đã xác nhận hoàn thành đơn hàng!", Toast.LENGTH_SHORT).show();
            btn_confirm.setText("Đã hoàn thành!");
            btn_confirm.setEnabled(false);
            finish();
        });

        btn_cancel.setOnClickListener(View -> {
            cancelBill();
            //create notify
            Notify notify = new Notify(Notify.CANCEL_BILL_TYPE, "Huỷ đơn hàng",
                    "Hmmm... đơn hàng có mã "+bill.getId()+" đã bị huỷ. Hãy tìm nó trong giỏ hàng của bạn nhé",
                    LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
            NotifyService notifyService = new NotifyService();
            notifyService.insert(notify);

            Toast.makeText(this, "Huỷ giao hàng, đơn đã trở về giỏ!", Toast.LENGTH_SHORT).show();
            btn_confirm.setText("Đã hoàn thành!");
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
        //Bill đã tồn tại trạng thái đang giao (status = 1) chưa hoàn thành -> chuyển thành đã hoàn thành status =2
        BillService billService = new BillService();
        bill.setStatus(2);
        billService.updateBillOnly(bill);
    }

    void cancelBill(){
        //Bill đã tồn tại trạng thái đang giao (status = 1) chưa hoàn thành -> chuyển về giỏ hàng status = 0
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

        //Bỏ một button khỏi giao diện
        //View b = findViewById(R.id.btn_confirm_invoice);
        //b.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng shop = new LatLng(10.850916, 106.771024); // lưu vĩ độ kinh độ trong db, tới đây lấy ra xài
        mMap.addMarker(new MarkerOptions()
                .position(shop)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop, 16f));
    }
}