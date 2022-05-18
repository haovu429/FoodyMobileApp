package hcmute.edu.vn.foody_24.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody_24.DetailShopActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Diner;

public class DinerAdapter extends RecyclerView.Adapter<DinerAdapter.DinerViewHolder> {

    private Context context;
    private List<Diner> dinerList;
    private final int idUser;

    public DinerAdapter(Context context, int idUser) {
        this.context = context;
        this.idUser = idUser;
    }

    public void setData(List<Diner> list){
        this.dinerList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DinerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diner,parent,false);
        return new DinerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DinerViewHolder holder, int position) {
        final Diner diner = dinerList.get(position);
        if (diner == null){
            return;
        }
        int img = diner.getImg_diner();
        holder.imageDiner.setImageResource(diner.getImg_diner());
        //Hiển thị giới hạn tên của quán, trong item tránh bị vỡ giao diện item
        String text_name_display = diner.getName_diner();
        if(text_name_display.length() > 17){
            text_name_display = diner.getName_diner().substring(0,18) +"..";
        }
        holder.textName.setText(text_name_display);
        //holder.tv_description.setText(diner.getAddress());

        holder.cardView.setOnClickListener(View -> {
            onClickGoToDetail(diner);
        });

    }

    private void onClickGoToDetail(Diner diner) {
        Intent intent = new Intent(context, DetailShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void Release(){
        context = null; // avoid leak memory
    }


    @Override
    public int getItemCount() {
        if (dinerList != null){
            return dinerList.size();
        }
        return 0;
    }

    public void filterList(ArrayList<Diner> filterDiner) {
        dinerList = filterDiner;
        notifyDataSetChanged();
    }

    class DinerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageDiner;
        TextView textName, tv_description;
        CardView cardView;

        public DinerViewHolder(@NonNull View itemView) {
            super(itemView);

            //Glide.with(context).load("http://goo.gl/gEgYUd").into(imageDiner);
            cardView = itemView.findViewById(R.id.cardview_item);
            imageDiner = itemView.findViewById(R.id.img_diner);
            //itemView.setAlpha(Float.valueOf(0.8f)); // Chỉnh độ trong suốt
            textName = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}
