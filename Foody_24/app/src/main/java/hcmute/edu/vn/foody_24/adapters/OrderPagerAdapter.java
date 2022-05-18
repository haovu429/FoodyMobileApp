package hcmute.edu.vn.foody_24.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Map;

import hcmute.edu.vn.foody_24.fragment.order_fragment.OrderCartFragment;
import hcmute.edu.vn.foody_24.fragment.order_fragment.OrderDeliveringFragment;
import hcmute.edu.vn.foody_24.fragment.order_fragment.OrderHistoryFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveAllFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveImageFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveLocationFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SavePostFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {
    private Map<Integer, String> mFragmentTags;
    private FragmentManager fragmentManager;
    private final Context context;

    private final int idUser;

    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity, int idUser) {
        super(fragmentActivity);
        this.idUser = idUser;
        context = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new OrderDeliveringFragment(idUser);//0
            case 2:
                return new OrderHistoryFragment(idUser);
            default:
                return new OrderCartFragment(idUser);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
