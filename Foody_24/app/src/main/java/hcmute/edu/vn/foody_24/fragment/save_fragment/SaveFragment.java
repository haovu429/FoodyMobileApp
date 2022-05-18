package hcmute.edu.vn.foody_24.fragment.save_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.MainPagerAdapter;
import hcmute.edu.vn.foody_24.adapters.SavePagerAdapter;

public class SaveFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    SavePagerAdapter savePagerAdapter;

    View view;

    private final int idUser;

    public SaveFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_save, container, false);

        tabLayout = view.findViewById(R.id.tab_layout_save);
        viewPager = view.findViewById(R.id.viewpager_save);
        savePagerAdapter = new SavePagerAdapter(this.getActivity());

        viewPager.setAdapter(savePagerAdapter);

        //Hàm thay thế cho setupWithViewPager(..)
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Location");
                    break;
                case 2:
                    tab.setText("Food");
                    break;
                case 3:
                    tab.setText("Post");
                    break;
                default:
                    tab.setText("All"); // posittion = 0
                    break;
            }
        }).attach();


        return view;
    }
}
