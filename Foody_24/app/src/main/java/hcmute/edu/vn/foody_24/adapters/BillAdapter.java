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

import hcmute.edu.vn.foody_24.CartActivity;
import hcmute.edu.vn.foody_24.DetailShopActivity;
import hcmute.edu.vn.foody_24.InvoiceActivity;
import hcmute.edu.vn.foody_24.R;

import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.service.DinerService;


public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private final int idUser;
    private List<Bill> mListBill;
    private Context context;

    public BillAdapter(List<Bill> mListBill, Context mContext, int idUser) {
        Collections.reverse(mListBill);
        this.mListBill = mListBill;
        this.context = mContext;
        this.idUser = idUser;
    }

    public void setData(List<Bill> mListBill, Context mContext ){
        Collections.reverse(mListBill);
        this.mListBill = mListBill;
        this.context = mContext;
        notifyDataSetChanged();
    }

//    public int getItemViewType(int position) {
//
//        return bill.getDetails().get(position).getType();
//    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {

        Bill bill = mListBill.get(position);
        if (bill == null){
            return;
        }

        setupUI(holder,bill);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
//        holder.rcv_item.setLayoutManager(linearLayoutManager);
//
//        BillDetailAdapter billDetailAdapter = new BillDetailAdapter();
//        billDetailAdapter.setData(bill.getDetails());
//        holder.rcv_item.setAdapter(billDetailAdapter);
    }

    private void setupUI(@NonNull BillViewHolder holder, Bill bill){
        DinerService dinerService = new DinerService();
        Diner diner = dinerService.getOne(bill.getStoreId());

        if (diner != null){
            //Hiển thị giới hạn tên của quán, trong item tránh bị vỡ giao diện item
            String text_name_display = diner.getName_diner();
            if(text_name_display.length() > 26){
                text_name_display = diner.getName_diner().substring(0,27) +"..";
            }
            holder.tv_diner_name.setText(text_name_display);
            holder.tv_item_amount.setText(bill.getAmount() +" phần");
            holder.tv_bill_price.setText(bill.getTotalPrice() +" đ");
            //holder.linearLayout.setBackgroundResource(diner.getImg_diner());
            holder.img_dinner.setImageResource(bill.getStoreObject().getImg_diner());

            holder.cardView.setOnClickListener(View ->{
                if (bill.getStatus() > 0){
                    onClickGoToInVoice(context,bill);
                } else {
                    onClickGoToCart(bill);
                }
            });

            holder.tv_diner_name.setOnClickListener(View -> {
                onClickGoToDetailDiner(diner, idUser);
            });
        }
    }

    private void onClickGoToDetailDiner(Diner diner, int idUser) {

        Intent intent = new Intent(context, DetailShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diner", diner);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void onClickGoToInVoice(Context context, Bill bill){
        Intent intent = new Intent(context, InvoiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_bill", bill);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void onClickGoToCart(Bill bill) {
        try{
            Intent intent = new Intent(context, CartActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_bill", bill);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e){
            System.out.println("Day la loi dung cart: "+ e.getStackTrace());
        }

    }

    @Override
    public int getItemCount() {

        if (mListBill != null){
            return mListBill.size();
        }
        return 0;
    }

    public class BillViewHolder extends RecyclerView.ViewHolder{

        TextView tv_diner_name, tv_item_amount, tv_bill_price;
        //RecyclerView rcv_item;
        CardView cardView;
        //LinearLayout linearLayout;

        ImageView img_dinner;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_diner_name = itemView.findViewById(R.id.tv_diner_name);
            tv_item_amount = itemView.findViewById(R.id.tv_item_amount);
            tv_bill_price = itemView.findViewById(R.id.tv_bill_price);
            cardView = itemView.findViewById(R.id.cardview_item_bill);
            //linearLayout = itemView.findViewById(R.id.linearlayout_item_bill);
            img_dinner = itemView.findViewById(R.id.img_diner_bill_item);
            //rcv_item = itemView.findViewById(R.id.rcv_item_cart);
            //itemView.setAlpha(Float.valueOf(0.8f)); // Chỉnh độ trong suốt
        }
    }

}
