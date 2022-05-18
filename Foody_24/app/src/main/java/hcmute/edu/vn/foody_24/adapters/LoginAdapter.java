package hcmute.edu.vn.foody_24.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.edu.vn.foody_24.fragment.login_signup_fragment.LoginTableFragment;
import hcmute.edu.vn.foody_24.fragment.login_signup_fragment.SignupFragment;

public class LoginAdapter extends FragmentStateAdapter {
    int totalTabs;

    public LoginAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabs) {
        super(fragmentActivity);
        this.totalTabs = totalTabs;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                LoginTableFragment loginTableFragment = new LoginTableFragment();
                return loginTableFragment;
            case 1:
                SignupFragment signupFragment = new SignupFragment();
                return signupFragment;
            default:
                System.out.println("Return null here");
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
