package hcmute.edu.vn.foody_24.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.foody_24.CartActivity;
import hcmute.edu.vn.foody_24.FoodDetailActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.fragment.order_fragment.MyBottomSheetDialogFragment;
import hcmute.edu.vn.foody_24.models.BillDetails;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.my_interface.IClickEditQuantity;
import hcmute.edu.vn.foody_24.my_interface.IClickItem;
import hcmute.edu.vn.foody_24.service.FoodService;


public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.BillViewHolder> {

    private final Context context;
    public List<BillDetails> listBillDetail;
    private final IClickItem iClickItem;
    private final int idUser;
    private Activity activity;


    public BillDetailAdapter(Context context, int idUser, IClickItem iClickItem, Activity activity) {
        this.context = context;
        this.idUser = idUser;
        this.iClickItem = iClickItem;
        this.activity = activity;
    }

    public void setData(List<BillDetails> list){
        //Collections.reverse(list);
        this.listBillDetail = list;
        notifyDataSetChanged();
    }

    private Listener listener;

    public interface Listener{
        void UpdateData(List<BillDetails> listBillDetail); // phương thức này sẽ trả về kết quả
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_detail, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, @SuppressLint("RecyclerView") int position) {

        BillDetails billDetails = listBillDetail.get(position);

        if (billDetails == null){
            return;
        }

        FoodService foodService = new FoodService();
        Food food = foodService.getOne(billDetails.getFoodId());

        holder.tvFoodName.setText(String.valueOf(food.getName()));
        holder.tv_Quantily.setText(String.valueOf(billDetails.getAmount()));

        String price_text = food.getPrice()+" x "+billDetails.getAmount()
                +" = "+ billDetails.getPrice() + " đ";
        holder.tvPrice.setText(price_text);
        holder.img_item.setImageResource(food.getImage());

        //Đơn đã hoàn thành hoặc đơn đang giao, không thể chỉnh sửa
        if (billDetails.getBillObject().getStatus() > 0){
            holder.btn_giam.setEnabled(false);
            holder.btn_tang.setEnabled(false);
            // Vô hiệu hoá edittext số lượng
            holder.tv_Quantily.setFocusable(false);
            holder.tv_Quantily.setEnabled(false);
            holder.tv_Quantily.setCursorVisible(false);
            holder.tv_Quantily.setKeyListener(null);
            holder.tv_Quantily.setBackgroundColor(Color.TRANSPARENT);

        }

        holder.btn_giam.setOnClickListener(View ->{
            //Cập nhât số lượng thông qua editText
            int quantity = Integer.parseInt(holder.tv_Quantily.getText().toString());
            //holder.tv_Quantily.setText(String.valueOf(quantity - 1));

            if (quantity <= 1) {
                //holder.getEditvQuantily().setText("1");
                deleteItem(position);
            } else {
                capNhatBillBenNgoai(position, Integer.parseInt(holder.getTv_Quantily().getText().toString())-1);
                //Cap nhat lai textprice
                String price_text_temp = food.getPrice()+" x "+billDetails.getAmount()
                        +" = "+ billDetails.getPrice() + " đ";
                holder.tvPrice.setText(price_text_temp);
            }
        });

        holder.btn_tang.setOnClickListener(View ->{
            //Cập nhât số lượng thông qua editText
            //int quantity = Integer.parseInt(holder.tv_Quantily.getText().toString());
            //System.out.println("So luong truoc :"+quantity);
            //holder.tv_Quantily.setText(String.valueOf(quantity + 1));

            capNhatBillBenNgoai(position, Integer.parseInt(holder.getTv_Quantily().getText().toString())+1);
            //Cap nhat lai textprice
            String price_text_temp = food.getPrice()+" x "+billDetails.getAmount()
                    +" = "+ billDetails.getPrice() + " đ";
            holder.tvPrice.setText(price_text_temp);

        });

        holder.tv_Quantily.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clickOpenBottonSheetDialogFragment(position,Integer.parseInt(holder.tv_Quantily.getText().toString()));
            }
        });

        holder.img_delete_detail.setOnClickListener(View -> {
            deleteItem(position);
        });

        /*holder.tv_Quantily.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("So luong thay doi");
                int quantity = Integer.parseInt(holder.getTv_Quantily().getText().toString());
                System.out.println("So luong sau thay doi:"+quantity);

                try{
                    if (quantity != billDetails.getAmount()) {
                        if (quantity < 1) {
                            //holder.getEditvQuantily().setText("1");
                            listBillDetail.remove(billDetails);
                            iClickItem.click_btn_change_quantity(listBillDetail);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                        } else {
                            capNhatBillBenNgoai(position, Integer.parseInt(holder.getTv_Quantily().getText().toString()));
                            //Cap nhat lai textprice
                            String price_text = food.getPrice()+" x "+billDetails.getAmount()
                                    +" = "+ billDetails.getPrice() + " đ";
                            holder.tvPrice.setText(price_text);
                        }
                    }

                } catch (Exception e){
                    holder.getTv_Quantily().setText("1");
                    capNhatBillBenNgoai(position, Integer.parseInt(holder.getTv_Quantily().getText().toString()));
                    //Cap nhat lai textprice
                    String price_text = food.getPrice()+" x "+billDetails.getAmount()
                            +" = "+ billDetails.getPrice() + " đ";
                    holder.tvPrice.setText(price_text);
                    System.out.println("Loi so luong 1 o day: "+e.getStackTrace());

                }

            }
        });*/

        holder.tvFoodName.setOnClickListener(View -> {
            onClickGoToDetailFood(food, idUser);
        });
    }

    private void clickOpenBottonSheetDialogFragment(int position, int quantity) {
        if (quantity < 1){
            deleteItem(position);
            return;
        }
        MyBottomSheetDialogFragment sheetDialogFragment = MyBottomSheetDialogFragment.newInstance(quantity, new IClickEditQuantity() {
            @Override
            public void click_edit_change_quantity(int quantity) {
                if (quantity > 0){
                    listBillDetail.get(position).setAmount(quantity);
                    notifyItemChanged(position);
                    capNhatBillBenNgoai(position, quantity);
                } else {
                    Toast.makeText(context,"Số lượng không hợp lệ.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        sheetDialogFragment.show(((CartActivity) activity).getSupportFragmentManager(), sheetDialogFragment.getTag());
    }

    private void deleteItem(int position){
        listBillDetail.remove(position);
        iClickItem.click_btn_change_quantity(listBillDetail);
        //notifyItemRemoved(position); Loi khi xoá, lần đầu ko sao,
        // xoá item bên dưới lần xoá 1 sẽ bị lỗi (giữ nguyên position nhưng index trong list bị giảm -> khác postion -> xoá ngoài listSize -> lỗi
        notifyDataSetChanged();
    }

    private void onClickGoToDetailFood(Food food, int idUser) {
        //Log.i("idUser ",String.valueOf(idUser));
        //Log.d("rrr", context != null ? "###" :"null");
        Intent intent = new Intent(context, FoodDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_food", food);
        bundle.putSerializable("idUser", idUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
        System.out.println("Chay xong activity");
    }

    private void capNhatBillBenNgoai(int position, int quantity){
        listBillDetail.get(position).setAmount(quantity);
        notifyItemChanged(position);
        iClickItem.click_btn_change_quantity(listBillDetail);
    }

    private void updatePriceText(BillDetails billDetails, int quantity){

    }


    @Override
    public int getItemCount() {
        if (listBillDetail!=null){
            return listBillDetail.size();
        }
        return 0;
    }

    public class BillViewHolder extends RecyclerView.ViewHolder{

        //private EditText ;
        private TextView tvFoodName, tvPrice, tv_Quantily;
        private ImageView img_item, img_delete_detail;
        private ImageButton btn_giam, btn_tang;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFoodName = itemView.findViewById(R.id.tv_food_name_item_detail);
            tv_Quantily = itemView.findViewById(R.id.tv_quantity_bill_detail);
            tvPrice = itemView.findViewById(R.id.tv_price_item_bill_detail);
            img_item = itemView.findViewById(R.id.img_food_item_on_detail);
            btn_giam = itemView.findViewById(R.id.btn_giam_bill_detail);
            btn_tang = itemView.findViewById(R.id.btn_tang_bill_detail);
            img_delete_detail = itemView.findViewById(R.id.img_delete_detail);

        }

        public TextView getTv_Quantily() {
            return tv_Quantily;
        }

        public void setTv_Quantily(TextView tv_Quantily) {
            this.tv_Quantily = tv_Quantily;
        }

        public TextView getTvFoodName() {
            return tvFoodName;
        }

        public void setTvFoodName(TextView tvFoodName) {
            this.tvFoodName = tvFoodName;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

        public void setTvPrice(TextView tvPrice) {
            this.tvPrice = tvPrice;
        }

        public ImageView getImg_item() {
            return img_item;
        }

        public void setImg_item(ImageView img_item) {
            this.img_item = img_item;
        }

        public ImageButton getBtn_giam() {
            return btn_giam;
        }

        public void setBtn_giam(ImageButton btn_giam) {
            this.btn_giam = btn_giam;
        }

        public ImageButton getBtn_tang() {
            return btn_tang;
        }

        public void setBtn_tang(ImageButton btn_tang) {
            this.btn_tang = btn_tang;
        }
    }
}
