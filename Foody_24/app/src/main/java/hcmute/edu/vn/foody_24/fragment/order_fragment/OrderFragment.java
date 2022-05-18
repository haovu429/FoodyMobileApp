package hcmute.edu.vn.foody_24.fragment.order_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.adapters.OrderPagerAdapter;
import hcmute.edu.vn.foody_24.adapters.SavePagerAdapter;

public class OrderFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    OrderPagerAdapter orderPagerAdapter;

    View view;
    int idUser;

    public OrderFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = view.findViewById(R.id.tab_layout_order);
        viewPager = view.findViewById(R.id.viewpager_order);
        orderPagerAdapter = new OrderPagerAdapter(this.getActivity(), idUser);



        viewPager.setAdapter(orderPagerAdapter);

        //Hàm thay thế cho setupWithViewPager(..)
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Delivering"); // posittion = 0
                    break;
                case 2:
                    tab.setText("History");
                    break;
                default:
                    tab.setText("Cart");
                    break;
            }
        }).attach();

        return view;
    }

}
