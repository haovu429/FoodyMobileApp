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
import hcmute.edu.vn.foody_24.service.UserService;

public class LoginTableFragment extends Fragment {

    private Button btnLogin;
    private EditText edit_email, edit_pass;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup  root = (ViewGroup) inflater.inflate(R.layout.login_fragment,container,false);

        edit_email = root.findViewById(R.id.edit_email);
        edit_pass = root.findViewById(R.id.edit_pass);
        btnLogin = root.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this::loginHandler);

        return  root;
    }
    private void loginHandler(View view){
        userService = new UserService();
        String username = edit_email.getText().toString(), password = edit_pass.getText().toString();
        int idUser = userService.authenticate(username, password);
        if (idUser > 0){
            //Toast.makeText(this.getContext(), "bat dc r", Toast.LENGTH_SHORT).show();

            edit_email.setText("");
            edit_pass.setText("");

            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("idUser", idUser);
            intent.putExtras(bundle);
            startActivity(intent);

//            if(edit_email.getText().toString().equals("admin") && edit_pass.getText().toString().equals("admin")){
//                Intent intent = new Intent(this.getActivity(), MainActivity.class);
//                intent.putExtra("userName", edit_email.getText().toString());
//                intent.putExtra("pass", edit_pass.getText().toString());
//                startActivity(intent);
//            } else {
//                Toast.makeText(this.getContext(), "email or password invalue", Toast.LENGTH_SHORT).show();
//            }
        }
        else {
            Toast.makeText(getContext(), "email or password invalue", Toast.LENGTH_SHORT).show();
        }


    }
}
