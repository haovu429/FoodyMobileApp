package hcmute.edu.vn.foody_24.fragment.home_fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

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
import hcmute.edu.vn.foody_24.adapters.FoodAdapter3;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.FoodService;

public class HomeFragment extends Fragment {

    private RecyclerView rcvDiner, rcvFood;
    private DinerAdapter dinerAdapter;
    private EditText edit_search;

    private GridView gridView;

    ArrayList<Diner> dinerList;
    ArrayList<Diner> listShow;
    View view;

    private List<Food> foodList;
    private FoodAdapter3 foodAdapter;

    private final int idUser;

    public HomeFragment(int idUser) {
        super();
        this.idUser = idUser;
    }
    public static HomeFragment newInstance(int idUser) {
        Bundle args = new Bundle();
        args.putInt("idUser", idUser);
        HomeFragment f = new HomeFragment(idUser);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        AnhXa();

        FoodService foodService = new FoodService();
        foodList = foodService.getLimit(20);

        foodAdapter = new FoodAdapter3(this.getContext(), foodList, idUser, FoodAdapter2.TYPE_IN_SHOP);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this.getContext(), 2);

        rcvFood.setLayoutManager(gridLayoutManager2);
        rcvFood.setAdapter(foodAdapter);


        //Cua hang
        dinerAdapter = new DinerAdapter(this.getContext(), idUser);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);

        rcvDiner.setLayoutManager(gridLayoutManager);
        getListDiner();
        dinerAdapter.setData(dinerList);

        rcvDiner.setAdapter(dinerAdapter);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(getContext(),"idUser = " + String.valueOf(idUser), Toast.LENGTH_SHORT).show();
                filter(editable.toString());
            }
        });


        return view;
    }

    private void filter(String text){

        //Không thể dùng litShow.clear() --> bay màu cả dinerList luôn (không hiểu nổi)
        listShow = new ArrayList<>();

        ArrayList<Diner> filterDiner = new ArrayList<>();

        for (Diner item : dinerList){
            if (item.getName_diner().toLowerCase().contains(text.toLowerCase())){
                filterDiner.add(item);

                listShow.add(item);
            }
        }
        dinerAdapter.filterList(filterDiner);
    }

    private void getListDiner(){
        ArrayList<Diner> list = new ArrayList<>();

        DinerService dinerService = new DinerService();

        list = (ArrayList<Diner>) dinerService.getAll();

        dinerList = list;
        this.listShow = list;

    }

    private void AnhXa(){
        rcvDiner = view.findViewById(R.id.rcv_diner);
        rcvFood= view.findViewById(R.id.rcv_food);
        edit_search = view.findViewById(R.id.editv_search);
    }
}
