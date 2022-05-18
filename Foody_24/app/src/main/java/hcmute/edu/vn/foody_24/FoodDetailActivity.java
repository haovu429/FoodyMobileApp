package hcmute.edu.vn.foody_24;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import hcmute.edu.vn.foody_24.adapters.FoodAdapter2;
import hcmute.edu.vn.foody_24.fragment.order_fragment.MyBottomSheetDialogFragment;
import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.Notify;
import hcmute.edu.vn.foody_24.my_interface.IClickEditQuantity;
import hcmute.edu.vn.foody_24.service.BillDetailService;
import hcmute.edu.vn.foody_24.service.BillService;
import hcmute.edu.vn.foody_24.service.FoodService;
import hcmute.edu.vn.foody_24.service.NotifyService;
import hcmute.edu.vn.foody_24.service.UserService;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView img_food, img_add_to_cart, img_save;
    private TextView tv_name_food, tv_diner_name;
    private TextView tv_description_food;
    private TextView tv_price_food;
    private TextView tv_quantity;

    private Button btnGiam, btnTang;

    private Context context = this;
    private Food food;
    private int idUser;

    private RecyclerView rcv_food;
    //RecyclerView rcv_food2;
    private ArrayList<Food> foodList;
    private FoodAdapter2 foodAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        food = (Food) bundle.get("object_food");

        //Toast.makeText(this, "id Food on FoodDetail: "+food.getId(), Toast.LENGTH_SHORT).show();

        idUser = (int) bundle.get("idUser");

        AnhXa();
        loadUI();

    }

    private void AnhXa(){
        img_food = findViewById(R.id.img_food);
        tv_name_food = findViewById(R.id.tv_name_food);
        tv_description_food = findViewById(R.id.tv_description);
        tv_price_food = findViewById(R.id.tv_price);
        tv_quantity = findViewById(R.id.tv_quantity);

        btnGiam = findViewById(R.id.btn_giam);
        btnTang = findViewById(R.id.btn_tang);
        img_add_to_cart = findViewById(R.id.img_AddToCart_detail);
        img_save = findViewById(R.id.img_save);
        tv_diner_name = findViewById(R.id.tv_diner_name);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUI(){

        AtomicInteger idSaveDiner = new AtomicInteger(UserService.isSavedFood(idUser, food.getId()));
        if (idSaveDiner.get() > 0){
            img_save.setImageResource(R.drawable.ic_save);
        } else {
            img_save.setImageResource(R.drawable.ic_unsave);
        }

        img_save.setOnClickListener(View -> {
            if (idSaveDiner.get() > 0){
                UserService.unSavedFood(idUser, food.getId());
                img_save.setImageResource(R.drawable.ic_unsave);
                idSaveDiner.set(UserService.isSavedDiner(idUser, food.getId()));
            } else {
                UserService.savedFood(idUser, food.getId());
                img_save.setImageResource(R.drawable.ic_save);
                idSaveDiner.set(UserService.isSavedDiner(idUser, food.getId()));
            }
        });

        tv_diner_name.setText(food.getDinerObject().getName_diner());
        img_food.setImageResource(food.getImage());
        tv_name_food.setText(food.getName());
        tv_description_food.setText(food.getDescription());
        tv_price_food.setText(food.getPrice() +" đ");
        tv_quantity.setText("1");

        tv_diner_name.setOnClickListener(View -> {
            onClickGoToDetailDiner(food.getDinerObject(), idUser);
        });

        btnGiam.setOnClickListener(View -> {
            int quantity = Integer.parseInt(tv_quantity.getText().toString());
            tv_quantity.setText(String.valueOf(quantity - 1));
        });

        btnTang.setOnClickListener(View -> {
            int quantity = Integer.parseInt(tv_quantity.getText().toString());
            tv_quantity.setText(String.valueOf(quantity + 1));
        });

        tv_quantity.setOnClickListener(View -> {
            clickOpenBottonSheetDialogFragment(Integer.parseInt(tv_quantity.getText().toString()));
        });

        tv_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int quantity;
                try {
                    quantity = Integer.parseInt(tv_quantity.getText().toString());
                } catch (Exception e){
                    quantity = 1;
                    e.printStackTrace();
                }

            }
        });

        img_add_to_cart.setOnClickListener(View ->{
            BillService billService = new BillService();
            BillDetailService billDetailService = new BillDetailService();
            //Lấy ra những đơn hàng,giỏ hàng đã tồn tại của người dung với quán ăn này đang ở trạng thái nháp(status=0)- thường chỉ có 1 cái
            Bill bill_isExisted = billService.getByUserId_diner_status(idUser, food.getDinerId(), 0);
            if (bill_isExisted != null){
                //System.out.println("add vao Bill da ton tai");
                //Nếu đã tồn giỏ hàng thì add vào giỏ hàng này
                billDetailService.insert(new BillDetails(food.getId(), bill_isExisted.getId(),
                        Integer.parseInt(tv_quantity.getText().toString())));
                Toast.makeText(this, "Đã thêm vào vào giỏ hàng", Toast.LENGTH_SHORT).show();

            } else {
                //Tạo bill mới và add vào
                Bill bill = new Bill(idUser, food.getDinerId(),0);
                billService.insertBillOnly(bill);
                // lần này lấy bill thì đã có
                bill_isExisted = billService.getByUserId_diner_status(idUser, food.getDinerId(), 0);
                System.out.println("bill_isExisted: "+bill_isExisted);
                if(bill_isExisted != null){
                    billDetailService.insert(new BillDetails(food.getId(), bill_isExisted.getId(),
                            Integer.parseInt(tv_quantity.getText().toString())));

                    //create notify
                    Notify notify = new Notify(Notify.CREATE_CART_TYPE, "Đã tạo giỏ hàng",
                            "Bạn đã tạo giỏ hàng cho cửa hàng "+bill.getStoreObject().getName_diner(),
                            LocalTime.now()+"     "+ LocalDate.now(),bill.getUserId());
                    NotifyService notifyService = new NotifyService();
                    notifyService.insert(notify);


                    Toast.makeText(this, "Đã tạo mới giỏ và thêm vào vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "bill_isExisted: " + bill_isExisted, Toast.LENGTH_SHORT).show();
                }
            }

        });

        //Mon an
        rcv_food = findViewById(R.id.rcv_other_foods);
        foodList = getFoodList();

        foodAdapter = new FoodAdapter2(this, foodList, idUser, FoodAdapter2.TYPE_IN_SHOP);
//        foodAdapter.setData(foodList, new FoodAdapter2.IclickAddToCartListener() {
//            @Override
//            public void onClickAddToCart(ImageView imgAddToCart, Food1 food) {
//                //AnimationUtil.translateAnimation(this, );
//            }
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcv_food.setLayoutManager(linearLayoutManager);
        rcv_food.setAdapter(foodAdapter);
    }
    private ArrayList<Food> getFoodList(){

        ArrayList<Food> list = new ArrayList<>();

        FoodService foodService = new FoodService();
        list = foodService.getByDinerId(food.getDinerId());
        return list;
    }

    private void addToCart(){

    }

    private void onClickGoToDetailDiner(Diner diner, int idUser) {

        Intent intent = new Intent(this, DetailShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        this.startActivity(intent);

    }

    private void clickOpenBottonSheetDialogFragment(int quantity) {
        MyBottomSheetDialogFragment sheetDialogFragment = MyBottomSheetDialogFragment.newInstance(quantity, new IClickEditQuantity() {
            @Override
            public void click_edit_change_quantity(int new_quatity) {
                if (new_quatity >0){
                    tv_quantity.setText(new_quatity+"");
                } else {
                    Toast.makeText(context,"Số lượng không hợp lệ.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        sheetDialogFragment.show(getSupportFragmentManager(), sheetDialogFragment.getTag());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        loadUI();
    }
}