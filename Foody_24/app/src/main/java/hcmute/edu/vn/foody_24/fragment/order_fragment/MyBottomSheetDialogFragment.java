package hcmute.edu.vn.foody_24.fragment.order_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.my_interface.IClickEditQuantity;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String KEY_QUANTITY = "quantity_object";
    private Integer quantity;
    private EditText edit_quantity;
    private Button btn_ok, btn_cancel;
    public IClickEditQuantity iClickEditQuantity;

    public static MyBottomSheetDialogFragment newInstance(int quantity, IClickEditQuantity iClickEditQuantity) {
        MyBottomSheetDialogFragment myBottomSheetDialogFragment = new MyBottomSheetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_QUANTITY, quantity);
        myBottomSheetDialogFragment.iClickEditQuantity = iClickEditQuantity;
        myBottomSheetDialogFragment.setArguments(bundle);

        return myBottomSheetDialogFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            quantity = (int) bundleReceive.get(KEY_QUANTITY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_button_sheet_fragment, null);

        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);
        edit_quantity.setText(quantity+"");
        btn_ok.setOnClickListener(View -> {
            System.out.println("So luong trong edit: "+edit_quantity.getText().toString());
            iClickEditQuantity.click_edit_change_quantity(Integer.parseInt(edit_quantity.getText().toString()));
            onDestroyView();
        });

        btn_cancel.setOnClickListener(View ->{
            onDestroyView();
        });

        return bottomSheetDialog;
    }

    private void initView(View view){
        edit_quantity  = view.findViewById(R.id.edit_quantity);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_cancel = view.findViewById(R.id.btn_cancel);

    }

    private void setDataOrder(){
        if (this.quantity == null){
            return;
        }
        edit_quantity.setText(""+quantity);

    }

}
