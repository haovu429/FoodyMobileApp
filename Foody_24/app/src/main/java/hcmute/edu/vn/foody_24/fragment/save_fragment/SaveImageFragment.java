package hcmute.edu.vn.foody_24.fragment.save_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.DinerAdapter;
import hcmute.edu.vn.foody_24.adapters.FoodAdapter2;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.FoodService;

public class SaveImageFragment extends Fragment {

    private RecyclerView rcvSaveFoods;
    private DinerAdapter dinerAdapter;


    private View view;
    private int idUser;
    private ArrayList<Food> foodList;
    private FoodAdapter2 foodAdapter;
    private Food food;

    public SaveImageFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_image, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle == null){
            return null;
        }

        food = (Food) bundle.get("object_food");
        System.out.println("food:"+food);

        //Toast.makeText(this, "id Food on FoodDetail: "+food.getId(), Toast.LENGTH_SHORT).show();

        idUser = (int) bundle.get("idUser");
        Log.i("idUserShop",String.valueOf(idUser));

        AnhXa();
        loadUI();

        return view;
    }

    private void AnhXa(){
        rcvSaveFoods = view.findViewById(R.id.rcv_save_foods);
    }

    private void loadUI(){

        rcvSaveFoods = view.findViewById(R.id.rcv_save_foods);
        foodList = getFoodList();

        foodAdapter = new FoodAdapter2(this.getContext(), foodList, idUser, FoodAdapter2.TYPE_SAVED);
//        foodAdapter.setData(foodList, new FoodAdapter2.IclickAddToCartListener() {
//            @Override
//            public void onClickAddToCart(ImageView imgAddToCart, Food1 food) {
//                //AnimationUtil.translateAnimation(this, );
//            }
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());

        rcvSaveFoods.setLayoutManager(linearLayoutManager);
        rcvSaveFoods.setAdapter(foodAdapter);
    }

    private ArrayList<Food> getFoodList(){

        ArrayList<Food> list = new ArrayList<>();

        FoodService foodService = new FoodService();
        list = (ArrayList<Food>) foodService.getSavedFood_forUser(idUser);
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUI();
    }
}
