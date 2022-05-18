package hcmute.edu.vn.foody_24.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Notify;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {

    List<Notify> listNotify;

    public NotifyAdapter(List<Notify> listNotify) {
        Collections.reverse(listNotify);
        this.listNotify = listNotify;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify,parent, false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {

        Notify notify =listNotify.get(position);

        if (notify == null){
            return;
        }

        switch (notify.getType()){
            case Notify.COMPLETED_BILL_TYPE:
                holder.img_type.setImageResource(R.drawable.ic_completed);
                break;
            case Notify.CANCEL_BILL_TYPE:
                holder.img_type.setImageResource(R.drawable.ic_cancel);
                break;
            case Notify.SHIPPING_TYPE:
                holder.img_type.setImageResource(R.drawable.ic_shipping);
                break;
            case Notify.CREATE_CART_TYPE:
                holder.img_type.setImageResource(R.drawable.ic_create_bill);
                break;
            case Notify.DELETE_CART_TYPE:
                holder.img_type.setImageResource(R.drawable.ic_delete_cart);
                break;
            default:
                holder.img_type.setImageResource(R.drawable.ic_normal_notify);
        }

        holder.tv_title.setText(notify.getTitle());
        holder.tv_mess.setText(notify.getMess());
        holder.tv_time.setText(notify.getTime());

    }

    private void setData(List<Notify> list){
        this.listNotify = list;
    }

    @Override
    public int getItemCount() {
        if (listNotify != null){
            return listNotify.size();
        }
        return 0;
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_mess, tv_time;
        private ImageView img_type;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_type = itemView.findViewById(R.id.img_type_notify);
            tv_title = itemView.findViewById(R.id.tv_title_notify);
            tv_mess = itemView.findViewById(R.id.tv_messenger_notify);
            tv_time = itemView.findViewById(R.id.tv_time_notify);
        }
    }

}
