package hcmute.edu.vn.foody_24.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.foody_24.FoodDetailActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.my_interface.IClickOtherFood;


public class FoodAdapter3 extends RecyclerView.Adapter<FoodAdapter3.FoodViewHolder>{

    public final static int TYPE_IN_SHOP = 0;
    public final static int TYPE_SAVED = 1;
    private int type;
    private final Context context;
    private List<Food> foodList;
    private IClickOtherFood iClickOtherFood;

    private final int idUser;
    //IclickAddToCartListener iclickAddToCartListener;

//    public interface IclickAddToCartListener {
//        public void onClickAddToCart(ImageView imgAddToCart, Food1 food);
//    }

//    public FoodAdapter2(List<Food1> listSV) {
//        this.foodList = listSV;
//    }


    public FoodAdapter3(Context context, int idUser, int typeShow) {
        this.context = context;
        this.idUser = idUser;
        this.type = typeShow;
    }

    public FoodAdapter3(Context context, List<Food> listSV, int idUser, int typeShow) {
        this.context = context;
        this.foodList = listSV;
        this.idUser = idUser;
        this.type = typeShow;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food3,parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        Food food = foodList.get(position);
        if (food == null){
            return;
        }
        int img = food.getImage();
        holder.imageDiner.setImageResource(food.getImage());
        //Hiển thị giới hạn tên của quán, trong item tránh bị vỡ giao diện item
        String text_name_display = food.getName();
        if(text_name_display.length() > 17){
            text_name_display = food.getName().substring(0,18) +"..";
        }
        holder.textName.setText(text_name_display);
        //holder.tv_description.setText(diner.getAddress());

        holder.cardView.setOnClickListener(View -> {
            onClickGoToDetail(food, idUser);
        });


//        holder.imgAddToCart.setOnClickListener(View ->{
//            if (!food.isAddToCart()) {
//                iclickAddToCartListener.onClickAddToCart(holder.imgAddToCart, food);
//            }
//
//        });
    }

    private void onClickGoToDetail(Food food, int idUser) {
        //Log.i("idUser ",String.valueOf(idUser));
        //Log.d("rrr", context != null ? "###" :"null");
        Intent intent = new Intent(context, FoodDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_food", food);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(foodList != null){
            return foodList.size();
        }
        return 0;
    }

    class FoodViewHolder extends  RecyclerView.ViewHolder{

        ImageView imageDiner;
        TextView textName, tv_description;
        CardView cardView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_item);
            imageDiner = itemView.findViewById(R.id.img_food);
            //itemView.setAlpha(Float.valueOf(0.8f)); // Chỉnh độ trong suốt
            textName = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }



//    public void setData(List<Food1> list, IclickAddToCartListener listener){
//        this.foodList = list;
//        notifyDataSetChanged();
//    }
}
