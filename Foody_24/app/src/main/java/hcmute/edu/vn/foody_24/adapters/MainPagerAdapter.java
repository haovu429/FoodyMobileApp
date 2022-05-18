package hcmute.edu.vn.foody_24.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.edu.vn.foody_24.fragment.account_fragment.AccountFragment;
import hcmute.edu.vn.foody_24.fragment.home_fragment.HomeFragment;
import hcmute.edu.vn.foody_24.fragment.notify_fragment.NotifyFragment;
import hcmute.edu.vn.foody_24.fragment.order_fragment.OrderFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    private final int idUser;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int idUser) {
        super(fragmentActivity);
        this.idUser = idUser;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SaveFragment(idUser);
            case 2:
                return new OrderFragment(idUser);
            case 3:
                return new  NotifyFragment(idUser) ;
            case 4:
                return new AccountFragment(idUser);
            default:
                return new HomeFragment(idUser);//0
        }
    }

    @Override
    public int getItemCount() {
        return 5; // có 4 mục
    }
}
