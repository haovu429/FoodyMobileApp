package hcmute.edu.vn.foody_24.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.BillDetails;

public class BilldetailPaymentAdapter extends RecyclerView.Adapter<BilldetailPaymentAdapter.BilldetailPaymentViewHolder> {

    private List<BillDetails> listDetail;

    public void setData(List<BillDetails> billDetails){
        //Collections.reverse(billDetails);
        this.listDetail = billDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BilldetailPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_detail_payment, parent, false);
        return new BilldetailPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BilldetailPaymentViewHolder holder, int position) {

        BillDetails billDetails = listDetail.get(position);

        if (billDetails == null){
            return;
        }

        holder.tv_quantity.setText("x"+billDetails.getAmount());
        holder.tv_name_food.setText(billDetails.getFood().getName());
        holder.tv_price_item.setText(billDetails.getPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        if (listDetail != null ){
            return listDetail.size();
        }
        return 0;
    }

    public class BilldetailPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_quantity, tv_name_food, tv_price_item;

        public BilldetailPaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_quantity = itemView.findViewById(R.id.tv_item_amount_payment);
            tv_name_food = itemView.findViewById(R.id.tv_name_food_item_payment);
            tv_price_item = itemView.findViewById(R.id.tv_name_price_item_payment);
        }
    }
}
