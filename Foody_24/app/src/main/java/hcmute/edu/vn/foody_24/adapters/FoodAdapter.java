package hcmute.edu.vn.foody_24.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Food;

public class FoodAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Food> dataList;

    public FoodAdapter(Activity activity, ArrayList<Food> dataList){
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.item_food, null);

        ImageView thumbnail = view.findViewById(R.id.ifo_thumbnail);
        TextView title = view.findViewById(R.id.ifo_title);
        TextView content = view.findViewById(R.id.ifo_content);
        TextView price = view.findViewById(R.id.ifo_price);

        Food food = dataList.get(position);

        thumbnail.setImageResource(food.getImage());
        title.setText(food.getName());
        content.setText(food.getDescription());
        price.setText(String.valueOf(food.getPrice()));

        return view;
    }


}
