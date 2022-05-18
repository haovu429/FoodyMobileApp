package hcmute.edu.vn.foody_24.fragment.save_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.DinerAdapter;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.service.DinerService;

public class SaveAllFragment extends Fragment {

    private RecyclerView rcvSaveAll;
    private DinerAdapter dinerAdapter;


    private View view;
    private final int idUser;

    public SaveAllFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved_all, container, false);
        AnhXa();
        loadUI();

        return view;
    }

    private void AnhXa(){
        rcvSaveAll = view.findViewById(R.id.rcv_save_all);
    }

    private void loadUI(){
        dinerAdapter = new DinerAdapter(this.getContext(), idUser);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);

        rcvSaveAll.setLayoutManager(gridLayoutManager);
        dinerAdapter.setData(getListSaveAll());
        rcvSaveAll.setAdapter(dinerAdapter);
    }

    private List<Diner> getListSaveAll(){
        List<Diner> list = new ArrayList<>();

        DinerService dinerService = new DinerService();
        list = dinerService.getSavedDiner_forUser(idUser);

        /*list.add(new Diner(R.drawable.diner5, "Diner 1"));
        list.add(new Diner(R.drawable.diner5, "Diner 2"));
        list.add(new Diner(R.drawable.diner5, "Diner 3"));
        list.add(new Diner(R.drawable.diner5, "Diner 4"));

        list.add(new Diner(R.drawable.diner5, "Diner 1"));
        list.add(new Diner(R.drawable.diner5, "Diner 2"));
        list.add(new Diner(R.drawable.diner5, "Diner 3"));
        list.add(new Diner(R.drawable.diner5, "Diner 4"));

        list.add(new Diner(R.drawable.diner5, "Diner 1"));
        list.add(new Diner(R.drawable.diner5, "Diner 2"));
        list.add(new Diner(R.drawable.diner5, "Diner 3"));
        list.add(new Diner(R.drawable.diner5, "Diner 4"));*/


        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUI();
    }
}
