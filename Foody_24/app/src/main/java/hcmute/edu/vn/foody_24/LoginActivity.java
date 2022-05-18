package hcmute.edu.vn.foody_24;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.adapters.LoginAdapter;
import hcmute.edu.vn.foody_24.database.DatabaseHelper;
import hcmute.edu.vn.foody_24.service.UserService;

public class LoginActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ArrayList<String> tabLayoutList;
    FloatingActionButton fb,gg, tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createUser();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.fab_facebook);
        gg = findViewById(R.id.fab_google);
        tw = findViewById(R.id.fab_twitter);
        tabLayoutList = new ArrayList<>();
        tabLayoutList.add("Login");
        tabLayoutList.add("Sign up");
        //tabLayout.addTab(tabLayout.newTab().setText("Login"));
        //tabLayout.addTab(tabLayout.newTab().setText("Sign up"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        LoginAdapter adapter = new LoginAdapter(this, tabLayoutList.size());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabLayoutList.get(position));
        }).attach();

    }

    private void createUser(){
        //DatabaseHelper db = DatabaseHelper.getInstance();
        //db.onCreate(new ;);

        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();

        UserService userService = new UserService();
        int idUser = userService.isExisted("admin");
        if (idUser > 0){
            return;
        }
        db.execSQL("insert into users values(null, 'admin', 'admin', " +
                "'1,Võ Văn Ngân, Linh Chiểu, Thủ đức, Tp.HCM', '0987654321');" );
    }

}