package hcmute.edu.vn.foody_24.fragment.account_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hcmute.edu.vn.foody_24.ChangePassActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.database.CreateData;
import hcmute.edu.vn.foody_24.models.User;
import hcmute.edu.vn.foody_24.service.UserService;

public class AccountFragment extends Fragment {

    private View view;
    private TextView tv_userName, tv_address;
    private EditText edit_phone, edit_address;
    private Button btnUpdate, btn_change_pass, btn_edit_info;

    ImageView img_logout;

    private int idUser;
    private User user;

    public AccountFragment(int idUser) {
        super();
        this.idUser = idUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_account, container, false);

        UserService userService = new UserService();
        user = userService.getOne(idUser);

        AnhXa();
        setupUI();

        return view;
    }

    private void onClickGoToChangePass(User user) {
        Intent intent = new Intent(getContext(), ChangePassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    void setupUI(){

        tv_userName.setText(user.getUsername());
        tv_address.setText(user.getAddress());
        edit_address.setText(user.getAddress());
        edit_phone.setText(user.getPhone());

        //Vô hiệu hoá sửa địa chỉ
        edit_address.setFocusable(false);
        edit_address.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edit_address.setClickable(false); // user navigates with wheel and selects widget

        //Vô hiệu hoá sửa phone
        edit_phone.setFocusable(false);
        edit_phone.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edit_phone.setClickable(false); // user navigates with wheel and selects widget


        btnUpdate.setOnClickListener(View ->{
            CreateData createData = new CreateData();
            createData.createDB();
        });

        btn_change_pass.setOnClickListener(View -> {
            //CreateData createData = new CreateData();
            //createData.deleteDB();
            onClickGoToChangePass(user);
        });

        btn_edit_info.setOnClickListener(View ->{
            onClickEdit();
        });

        img_logout.setOnClickListener(View ->{
            getActivity().finish();
        });
    }
    void onClickEdit(){
        if (btn_edit_info.getText().toString().equals("Edit")) {
            btn_edit_info.setText("Save");
            edit_address.setFocusable(true);
            edit_address.setFocusableInTouchMode(true);
            edit_address.setClickable(true);

            edit_phone.setFocusable(true);
            edit_phone.setFocusableInTouchMode(true);
            edit_phone.setClickable(true);
        } else {
            btn_edit_info.setText("Edit");
            //Vô hiệu hoá sửa địa chỉ
            edit_address.setFocusable(false);
            edit_address.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edit_address.setClickable(false); // user navigates with wheel and selects widget
            user.setAddress(edit_address.getText().toString());
            tv_address.setText(edit_address.getText().toString());

            //Vô hiệu hoá sửa phone
            edit_phone.setFocusable(false);
            edit_phone.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edit_phone.setClickable(false); // user navigates with wheel and selects widget
            user.setPhone(edit_phone.getText().toString());

            UserService userService = new UserService();
            userService.update(user);
        }
    }

    public void setInfoAccount(String username) {

        tv_userName.setText(username);
//        us = sv;
//        if(txtHoTen != null){
//            setInfoSinhVien2();
//        }
        System.out.println("Day la ham setInfo-");
        System.out.println("Đây la giá trị txtHoTen khi bắt đầu vào setInfo " + tv_userName);
    }

    public void setInfoAccount(){
        tv_userName.setText(user.getUsername());
    }

    public void AnhXa(){

        btnUpdate = view.findViewById(R.id.btn_update);
        btn_change_pass = view.findViewById(R.id.btn_change_pass);
        tv_userName= view.findViewById(R.id.tv_username_profile);
        tv_address = view.findViewById(R.id.tv_address);
        btn_edit_info = view.findViewById(R.id.btn_edit_info_profile);
        edit_phone = view.findViewById(R.id.edit_phone_number_profile);
        edit_address = view.findViewById(R.id.edit_address_profile);
        img_logout = view.findViewById(R.id.img_logout);
    }

    public TextView getTv_userName() {
        return tv_userName;
    }

    public void setTv_userName(TextView tv_userName) {
        this.tv_userName = tv_userName;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
