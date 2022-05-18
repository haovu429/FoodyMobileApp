package hcmute.edu.vn.foody_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hcmute.edu.vn.foody_24.adapters.DinerAdapter;
import hcmute.edu.vn.foody_24.adapters.FoodAdapter2;
import hcmute.edu.vn.foody_24.adapters.PhotoViewPager2Adapter;
import hcmute.edu.vn.foody_24.adapters.PostAdapter;
import hcmute.edu.vn.foody_24.effect.ZoomOutPageTransformer;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.models.PhotoSlide;
import hcmute.edu.vn.foody_24.models.Post;
import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.FoodService;
import hcmute.edu.vn.foody_24.service.PostService;
import hcmute.edu.vn.foody_24.service.UserService;
import me.relex.circleindicator.CircleIndicator3;

public class DetailShopActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private List<PhotoSlide> photoSlideList;
    private ImageView img_shop, img_save;
    private TextView tv_name_shop, tv_post_amount, tv_average_rate1, tv_average_rate2;

    private RecyclerView rcv_food, rcv_diner_near, rcv_post;
    //RecyclerView rcv_food2;
    private ArrayList<Food> foodList;
    private FoodAdapter2 foodAdapter;

    private DinerAdapter dinerAdapter;

    private PostAdapter postAdapter;

    private PhotoViewPager2Adapter photoViewPager2Adapter;

    private List<Post> postList;

    private Diner diner;
    private int idUser;

    private GoogleMap mMap;

    private LinearLayout layout_post_diner;

    private final Handler mhandler = new Handler();
    private final Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getCurrentItem() == photoSlideList.size() -1){
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.diner = (Diner) bundle.get("object_diner");
        this.idUser = (int) bundle.get("idUser");

        //System.out.println("idUser = "+ idUser);

        //Toast.makeText(this,"dinerID = "+String.valueOf(diner.getId()),Toast.LENGTH_SHORT).show();

        AnhXa();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //
//        foodAdapter2 = new FoodAdapter2(this);

    }

    private  void setupUI(){

        img_shop.setImageResource(diner.getImg_diner());
        tv_name_shop.setText(diner.getName_diner());

        double average_rate = diner.getAverageRate();
        //Nếu chưa có ai đánh giá thì cho tạm 10
        if(average_rate == 0){
            average_rate =10;
        }

        tv_average_rate1.setText(average_rate+"");

        String evaluate = "";
        if (average_rate > 9){
            evaluate = " Tuyệt vời";
        } else {
            if (average_rate > 7){
                evaluate = " Tốt";
            } else {
                if (average_rate > 5){
                    evaluate = " Trung bình";
                } else {
                    evaluate = " Tệ";
                }
            }
        }

        tv_average_rate2.setText(average_rate+evaluate);


        PostService postService = new PostService();
        tv_post_amount.setText(postService.post_amount_of_diner(diner.getId())+"");


        layout_post_diner.setOnClickListener(View -> {
            onClickGoToCreatePost(diner);
        });

        AtomicInteger idSaveDiner = new AtomicInteger(UserService.isSavedDiner(idUser, diner.getId()));
        if (idSaveDiner.get() > 0){
            img_save.setImageResource(R.drawable.ic_save);
        } else {
            img_save.setImageResource(R.drawable.ic_unsave);
        }

        img_save.setOnClickListener(View -> {
            if (idSaveDiner.get() > 0){
                UserService.unSavedDiner(idUser, diner.getId());
                img_save.setImageResource(R.drawable.ic_unsave);
                idSaveDiner.set(UserService.isSavedDiner(idUser, diner.getId()));
            } else {
                UserService.savedDiner(idUser, diner.getId());
                img_save.setImageResource(R.drawable.ic_save);
                idSaveDiner.set(UserService.isSavedDiner(idUser, diner.getId()));
            }
        });

        //auto slide
        viewPager2 = findViewById(R.id.viewpager2);
        circleIndicator3 = findViewById(R.id.circle_indicator3);

        photoSlideList = getListPhoto();
        photoViewPager2Adapter = new PhotoViewPager2Adapter(photoSlideList);
        viewPager2.setAdapter(photoViewPager2Adapter);

        circleIndicator3.setViewPager(viewPager2);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mhandler.removeCallbacks(mrunnable);
                mhandler.postDelayed(mrunnable, 3000);
            }
        });

        //Tao hieu hung chuyen slide (di copy)
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());


        //Món ăn
        rcv_food = findViewById(R.id.rcv_food_in_shop);
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

        //Post
        rcv_post = findViewById(R.id.rcv_post_shop);

        postService = new PostService();
        postList = postService.getByDinerId(diner.getId());
        Collections.reverse(postList);
        postAdapter = new PostAdapter( idUser, postList);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);

        rcv_post.setLayoutManager(linearLayoutManager2);
        rcv_post.setAdapter(postAdapter);


        // Quan an gan gan do
        rcv_diner_near = findViewById(R.id.rcv_diner_near);

        dinerAdapter = new DinerAdapter(this, idUser);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rcv_diner_near.setLayoutManager(gridLayoutManager);

        dinerAdapter.setData(getListDiner());

        rcv_diner_near.setAdapter(dinerAdapter);

    }

    private void AnhXa(){
        img_shop = findViewById(R.id.img_shop);
        tv_name_shop = findViewById(R.id.tv_name_shop);
        img_save = findViewById(R.id.img_save);
        layout_post_diner = findViewById(R.id.layout_post_diner);
        tv_post_amount = findViewById(R.id.tv_post_amount);
        tv_average_rate1 = findViewById(R.id.tv_average_rate1);
        tv_average_rate2 = findViewById(R.id.tv_average_rate2);

    }

    private void onClickGoToCreatePost(Diner diner) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private List<PhotoSlide> getListPhoto(){
        List<PhotoSlide> list = new ArrayList<>();

        list.add(new PhotoSlide(R.drawable.cafeshop1));
        list.add(new PhotoSlide(R.drawable.cafeshop2));
        list.add(new PhotoSlide(R.drawable.cafeshop3));
        list.add(new PhotoSlide(R.drawable.cafeshop4));

        return list;
    }

    private List<Diner> getListDiner(){
        ArrayList<Diner> list = new ArrayList<>();

        DinerService dinerService = new DinerService();

        list = (ArrayList<Diner>) dinerService.getAll();

        return list;

    }

    private ArrayList<Food> getFoodList(){

        ArrayList<Food> list = new ArrayList<>();

        FoodService foodService = new FoodService();
        list = foodService.getByDinerId(diner.getId());
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mhandler.removeCallbacks(mrunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mhandler.postDelayed(mrunnable, 3000);
        setupUI();
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