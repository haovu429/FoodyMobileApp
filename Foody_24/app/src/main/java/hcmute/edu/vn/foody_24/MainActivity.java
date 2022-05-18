package hcmute.edu.vn.foody_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.adapters.FoodAdapter;
import hcmute.edu.vn.foody_24.adapters.MainPagerAdapter;
import hcmute.edu.vn.foody_24.models.Food;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Food> foodArray;
    FoodAdapter foodAdapter;
    int currentIndex = -1;
    ImageView edit_icon;
    Button btnAll;

    String username;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    MainPagerAdapter mainPagerAdapter;

    public int idUser;

    //View viewEndAnimation;
    //ImageView viewAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.idUser = (int) bundle.get("idUser");
        Log.i("idUserMain",String.valueOf(idUser));


        username = (String) intent.getSerializableExtra("userName");
        String password = (String) intent.getSerializableExtra("pass");


        //AccountFragment accountFragment = (AccountFragment)getSupportFragmentManager().findFragmentByTag(R.layout.fragment_account);
        TextView title_account = findViewById(R.id.title_account);
        //title_account.setText(userName);

        //listView = (ListView) findViewById(R.id.listvSaved);
        foodArray = new ArrayList<Food>();
        //edit_icon = findViewById(R.id.edit_icon);
        //btnAll = (Button) findViewById(R.id.btnAll);

        foodArray.add(new Food(R.drawable.food01, "Pizza01", "Ok, ngon", 24, 1));
        foodArray.add(new Food(R.drawable.food02, "Pizza02", "Ok, ngon", 23, 1));
        foodArray.add(new Food(R.drawable.food03, "Pizza03", "Ok, ngon", 45, 2));
        foodArray.add(new Food(R.drawable.food04, "Pizza04", "Ok, ngon", 64, 2));

        //ArrayAdapter arrayAdapter = new ArrayAdapter( MainActivity.this, android.R.layout.simple_list_item_1, foodArray);
        //listView.setAdapter(arrayAdapter);

        //create Adapter
        foodAdapter = new FoodAdapter(this, foodArray);
        //listView.setAdapter(foodAdapter);

        //Đăng ký context menu
        //registerForContextMenu(listView);

//        edit_icon.setOnClickListener(View ->{
//            PopupMenu popupMenu = new PopupMenu(this, View);
//            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.menu_add_new_item:
//                            showDialogAdd();
//                            //test choi
//                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC5GCMUwYXZTMKVc8OBvLbvQ")));
//                            return true;
//                        case R.id.menu_exit:
//                            finish();
//                            return true;
//                        default:
//                            return false;
//                    }
//                }
//            });
//            popupMenu.show();
//        });

        //Fragment
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        mainPagerAdapter = new MainPagerAdapter(this, idUser);

        viewPager.setAdapter(mainPagerAdapter);

        //viewEndAnimation = findViewById(R.id.view_end_animation);
        //viewAnimation = findViewById(R.id.view_animation);

        //Hàm thay thế cho setupWithViewPager(..)
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 1:
                    //tab.setText("Save");
                    tab.setIcon(R.drawable.save_icon);
                    break;
                case 2:
                    //tab.setText("Order");
                    tab.setIcon(R.drawable.order_icon);
                    break;
                case 3:
                    //tab.setText("Notify");
                    tab.setIcon(R.drawable.notify_icon);
                    break;
                case 4:
                    //tab.setText("Account");
                    tab.setIcon(R.drawable.acc_icon);
                    break;
                default:
                    //tab.setText("Home"); // posittion = 0
                    tab.setIcon(R.drawable.home_icon);
                    break;
            }
        }).attach();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.menu_edit_item:
                this.currentIndex = index;
                showDialogAdd();
                return true;
            case R.id.menu_delete_item:
                foodArray.remove(index);
                foodAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add_new_item:
                showDialogAdd();
                return true;
            case R.id.menu_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_food, null);

        final EditText edTitle = view.findViewById(R.id.df_title);
        final EditText edContent = view.findViewById(R.id.df_content);
        final EditText edPrice = view.findViewById(R.id.df_price);

        if(currentIndex >=0 ){
            edTitle.setText(foodArray.get(currentIndex).getName());
            edContent.setText(foodArray.get(currentIndex).getDescription());
            edPrice.setText(String.valueOf(foodArray.get(currentIndex).getPrice()));
        }

        builder.setView(view);
        builder.setTitle("Add/Update Item")
                .setPositiveButton("Save Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = edTitle.getText().toString();
                String content = edContent.getText().toString();
                double price = Float.parseFloat(edPrice.getText().toString());
                int idDiner = 1;// chua sua

                if (currentIndex >=0 ){
                    foodArray.get(currentIndex).setName(title);
                    foodArray.get(currentIndex).setDescription(content);
                    foodArray.get(currentIndex).setPrice(price);

                    currentIndex = -1;
                } else {
                    Food food = new Food(R.drawable.food01,title, content, price, idDiner);

                    foodArray.add(food);
                }

                foodAdapter.notifyDataSetChanged();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

}