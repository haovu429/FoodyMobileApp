package hcmute.edu.vn.foody_24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hcmute.edu.vn.foody_24.models.Bill;
import hcmute.edu.vn.foody_24.models.User;
import hcmute.edu.vn.foody_24.service.UserService;

public class ChangePassActivity extends AppCompatActivity {

    private Button btn_change;
    private EditText edit_old_pass,edit_pass, edit_re_pass;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.user = (User) bundle.get("object_user");

        AnhXa();

        btn_change.setOnClickListener(View -> {
            onClickEdit();
        });


    }

    private void AnhXa(){
        btn_change = findViewById(R.id.btn_change);
        edit_old_pass = findViewById(R.id.edit_old_password);
        edit_pass = findViewById(R.id.edit_pass);
        edit_re_pass = findViewById(R.id.edit_re_password);
    }

    void onClickEdit() {
        if (btn_change.getText().toString().equals("Edit")) {
            btn_change.setText("Save");
            edit_old_pass.setFocusable(true);
            edit_old_pass.setFocusableInTouchMode(true);
            edit_old_pass.setClickable(true);

            edit_pass.setFocusable(true);
            edit_pass.setFocusableInTouchMode(true);
            edit_pass.setClickable(true);

            edit_re_pass.setFocusable(true);
            edit_re_pass.setFocusableInTouchMode(true);
            edit_re_pass.setClickable(true);
        } else {
            if (edit_old_pass.getText().toString().equals(user.getPassword())){
                if (!edit_pass.getText().toString().isEmpty()){
                    if (edit_pass.getText().toString().equals(edit_re_pass.getText().toString())){


                        user.setPassword(edit_pass.getText().toString());

                        UserService userService = new UserService();
                        userService.update(user);

                        edit_old_pass.setText("");
                        edit_pass.setText("");
                        edit_re_pass.setText("");

                        Toast.makeText(this, "Password has been changed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Re-password not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}