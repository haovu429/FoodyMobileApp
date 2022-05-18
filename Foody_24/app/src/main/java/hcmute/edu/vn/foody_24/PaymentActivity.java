package hcmute.edu.vn.foody_24;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.BillDetailAdapter;
import hcmute.edu.vn.foody_24.adapters.BilldetailPaymentAdapter;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.NotifyService;

public class PaymentActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    Bill bill;
    TextView tv_name_diner, tv_user_name, tv_edit_info, tv_delivery_time,
            tv_quantity, tv_total_price, tv_delivery_charge, tv_discount,
            tv_final_price, tv_final_price_momo, tv_final_price_cash,
            tv_other_payment_methods, tv_distance;

    EditText editv_address, editv_phone_number;

    CardView cardv_momo, cardv_cash;
    LinearLayout layout_momo, layout_cash;

    ImageButton btn_payment;

    RecyclerView rcv_details;

    int delivery_time; // minutes
    double delivery_charge = 3;
    double discount = 1;

    double final_price = 0;

    private static final int CHOOSE_METHOD_COLOR = R.color.payment_method_color;
    private static final int NOT_CHOOSE_METHOD_COLOR = R.color.not_payment_method_color;

    BilldetailPaymentAdapter billdetailPaymentAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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


        //ham cap nhat thoi gian tu google
        tv_delivery_time.setText(23+" phút");

        tv_edit_info.setOnClickListener(View ->{
            //event Edit
            onClickEdit();
        });

        tv_quantity.setText("Tổng "+bill.getAmount()+" phần");
        tv_distance.setText(5.6 +" km");
        tv_total_price.setText(bill.getTotalPrice()+"đ");
        tv_delivery_charge.setText(delivery_charge+"đ");
        tv_discount.setText("-"+discount);

        final_price = bill.getTotalPrice() + delivery_charge - discount;
        tv_final_price.setText(final_price +"đ" );
        tv_final_price_momo.setText(final_price +"đ");
        tv_final_price_cash.setText(final_price +"đ");

        //Vô hiệu hoá sửa số đt
        editv_phone_number.setFocusable(false);
        editv_phone_number.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        editv_phone_number.setClickable(false); // user navigates with wheel and selects widget
        //bill.setAddress(editv_address.getText().toString());

        //Vô hiệu hoá sửa địa chỉ
        tv_edit_info.setText("Edit");
        editv_address.setFocusable(false);
        editv_address.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        editv_address.setClickable(false); // user navigates with wheel and selects widget
        //bill.setAddress(editv_address.getText().toString());


        cardv_momo.setOnClickListener(View ->{
            //Thanh toán bằng momo
            layout_momo.setBackgroundResource(R.drawable.payment_method_color);
            layout_cash.setBackgroundResource(R.drawable.not_payment_method_color);
        });

        cardv_cash.setOnClickListener(View -> {
            //Thanh toán bằng cash
            layout_momo.setBackgroundResource(R.drawable.not_payment_method_color);
            layout_cash.setBackgroundResource(R.drawable.payment_method_color);
        });

        tv_other_payment_methods.setOnClickListener(View -> {
            //Chon phuong thuc thanh toan khac
        });

        btn_payment.setOnClickListener(View -> {
            saveBillToDB();
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Notify notify = new Notify(Notify.SHIPPING_TYPE, "Đơn của bạn đang vận chuyển",
                    "Bạn đã thanh toán cho đơn hàng: "+bill.getId()+", Đơn đang được giao tới. Chờ tí nhé ^_-",
                    LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
            NotifyService notifyService = new NotifyService();
            notifyService.insert(notify);
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
        //Bill đã tồn tại trong database nhưng đang ở trạng thái card,tức bill nháp,
        // chưa thanh toán. Khi thanh toán ta đổi trạng thái cho nó thành "Bill đã thanh toán, đang giao" (status = 1)
        BillService billService = new BillService();
        bill.setStatus(1);
        System.out.println("address da luu:" + bill.getAddress());
        billService.updateBillOnly(bill);
    }


    void onClickEdit(){
        if (tv_edit_info.getText().toString().equals("Edit")) {
            tv_edit_info.setText("Save");
            editv_address.setFocusable(true);
            editv_address.setFocusableInTouchMode(true);
            editv_address.setClickable(true);

            editv_phone_number.setFocusable(true);
            editv_phone_number.setFocusableInTouchMode(true);
            editv_phone_number.setClickable(true);
        } else {
            tv_edit_info.setText("Edit");
            editv_address.setFocusable(false);
            editv_address.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editv_address.setClickable(false); // user navigates with wheel and selects widget
            System.out.println("address la:" + editv_address.getText().toString());
            bill.setAddress(editv_address.getText().toString());
            BillService billService = new BillService();
            billService.updateBillOnly(bill);

            editv_phone_number.setFocusable(false);
            editv_phone_number.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editv_phone_number.setClickable(false); // user navigates with wheel and selects widget
            System.out.println("address la:" + editv_address.getText().toString());
            //bill.setAddress(editv_address.getText().toString());
        }
    }

    private void AnhXa() {
        tv_name_diner = findViewById(R.id.tv_diner_name_payment);
        tv_user_name = findViewById(R.id.tv_user_name_payment);
        tv_edit_info = findViewById(R.id.tv_edit_payment_info);
        tv_delivery_time = findViewById(R.id.tv_tg_giao_payment);
        tv_quantity = findViewById(R.id.tv_total_quantity_payment);
        tv_total_price = findViewById(R.id.tv_total_price_payment);
        tv_delivery_charge = findViewById(R.id.tv_delivery_charges);
        tv_discount = findViewById(R.id.tv_discount_payment);
        tv_final_price = findViewById(R.id.tv_final_price_payment);
        tv_final_price_momo = findViewById(R.id.tv_final_price_momo);
        tv_final_price_cash = findViewById(R.id.tv_final_price_cash);
        tv_other_payment_methods = findViewById(R.id.tv_other_payment_methods);
        tv_distance = findViewById(R.id.tv_distance_payment);

        editv_address = findViewById(R.id.editv_address_payment);
        editv_phone_number = findViewById(R.id.editv_phone_number);

        cardv_momo = findViewById(R.id.cardv_momo);
        cardv_cash = findViewById(R.id.cardv_cash);

        layout_momo = findViewById(R.id.layout_momo);
        layout_cash = findViewById(R.id.layout_cash);

        btn_payment = findViewById(R.id.btn_payment_final);

        rcv_details = findViewById(R.id.rcv_list_detail_payment);
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