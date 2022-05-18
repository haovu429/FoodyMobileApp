package hcmute.edu.vn.foody_24.fragment.login_signup_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hcmute.edu.vn.foody_24.MainActivity;
import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.User;
import hcmute.edu.vn.foody_24.service.UserService;

public class SignupFragment extends Fragment {

    private Button btnSignup;
    private EditText edit_email, editv_phone_number, editv_address, edit_pass, edit_repass;
    private UserService userService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup  root = (ViewGroup) inflater.inflate(R.layout.sig_up_fragment,container,false);

        edit_email = root.findViewById(R.id.email);
        editv_phone_number = root.findViewById(R.id.numberphone);
        editv_address = root.findViewById(R.id.address);
        edit_pass = root.findViewById(R.id.pass);
        edit_repass = root.findViewById(R.id.repass);

        btnSignup = root.findViewById(R.id.btn_resgiter);

        btnSignup.setOnClickListener(this::loginHandler);

        return  root;
    }
    private void loginHandler(View view){
        userService = new UserService();
        String username = edit_email.getText().toString();
        String password = edit_pass.getText().toString();


        int idUser = userService.isExisted(username);
        if (idUser > 0){

            Toast.makeText(getContext(), "This username is existed", Toast.LENGTH_SHORT).show();

        }
        else {
            if (edit_pass.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Password cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                if (edit_pass.getText().toString().equals(edit_repass.getText().toString())){
                    createUser();
                } else {
                    Toast.makeText(getContext(), "Re-password not match", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void createUser(){
        //DatabaseHelper db = DatabaseHelper.getInstance();
        //db.onCreate(new ;);

        User user = new User(edit_email.getText().toString(),edit_pass.getText().toString(),
                editv_address.getText().toString(), editv_phone_number.getText().toString());

        edit_email.setText("");
        editv_phone_number.setText("");
        editv_address.setText("");
        edit_pass.setText("");
        edit_repass.setText("");

        UserService userService = new UserService();
        userService.insert(user);
        Toast.makeText(getContext(), "Signup success", Toast.LENGTH_SHORT).show();
    }
}
