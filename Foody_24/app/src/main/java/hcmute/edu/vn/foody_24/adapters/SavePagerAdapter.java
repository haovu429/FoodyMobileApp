package hcmute.edu.vn.foody_24.adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.edu.vn.foody_24.MainActivity;
import hcmute.edu.vn.foody_24.fragment.account_fragment.AccountFragment;
import hcmute.edu.vn.foody_24.fragment.home_fragment.HomeFragment;
import hcmute.edu.vn.foody_24.fragment.notify_fragment.NotifyFragment;
import hcmute.edu.vn.foody_24.fragment.order_fragment.OrderFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveAllFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveImageFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SaveLocationFragment;
import hcmute.edu.vn.foody_24.fragment.save_fragment.SavePostFragment;

public class SavePagerAdapter extends FragmentStateAdapter {

    MainActivity mainActivity;

    public SavePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mainActivity = (MainActivity) fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SaveLocationFragment(mainActivity.idUser);
            case 2:
                return new SaveImageFragment(mainActivity.idUser);
            case 3:
                return new SavePostFragment();
            default:
                return new SaveAllFragment(mainActivity.idUser);//0
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
