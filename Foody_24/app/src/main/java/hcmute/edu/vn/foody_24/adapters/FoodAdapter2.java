package hcmute.edu.vn.foody_24.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.FoodDetailActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.my_interface.IClickOtherFood;
import hcmute.edu.vn.foody_24.service.FoodService;


public class FoodAdapter2 extends RecyclerView.Adapter<FoodAdapter2.FoodViewHolder>{

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


    public FoodAdapter2(Context context, int idUser, int typeShow) {
        this.context = context;
        this.idUser = idUser;
        this.type = typeShow;
    }

    public FoodAdapter2(Context context, List<Food> listSV, int idUser, int typeShow) {
        this.context = context;
        this.foodList = listSV;
        this.idUser = idUser;
        this.type = typeShow;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        Food food = foodList.get(position);
        if (food == null){
            return;
        }
        System.out.println("food1:"+food);
        holder.tvTitle.setText(String.valueOf(food.getName()));
        if (this.type == FoodAdapter2.TYPE_SAVED){
            holder.tvContent.setText(food.getDinerObject().getName_diner());
        } else {
            holder.tvContent.setText(food.getDescription());
        }

        holder.image.setImageResource(food.getImage());
        holder.tv_price.setText(String.valueOf(food.getPrice())+" đ");
        holder.cardView.setOnClickListener(View -> {
            System.out.println("food1:"+food);
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

        ImageView image;
        TextView tvTitle, tvContent, tv_price;
        CardView cardView;
        ImageView imgAddToCart;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_item_food);
            tvTitle = itemView.findViewById(R.id.ifo_title);
            tvContent = itemView.findViewById(R.id.ifo_content);
            image = itemView.findViewById(R.id.ifo_thumbnail);
            imgAddToCart = itemView.findViewById(R.id.img_AddToCart);
            tv_price = itemView.findViewById(R.id.ifo_price);
        }
    }



//    public void setData(List<Food1> list, IclickAddToCartListener listener){
//        this.foodList = list;
//        notifyDataSetChanged();
//    }
}
