package hcmute.edu.vn.foody_24;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;

import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.DinerRate;
import hcmute.edu.vn.foody_24.models.Post;
import hcmute.edu.vn.foody_24.service.DinerRateService;
import hcmute.edu.vn.foody_24.service.PostService;

public class CreatePostActivity extends AppCompatActivity {

    private EditText edit_title;
    private EditText edit_content_post;

    private Switch switch_allow_sub_comment;

    private SeekBar seekbar_vi_tri, seekbar_gia_ca, seekbar_chat_luong, seekbar_dich_vu, seekbar_khong_gian;
    private TextView tv_rate_vi_tri, tv_rate_gia_ca, tv_rate_chat_luong, tv_rate_dich_vu, tv_rate_khong_gian,
            tv_post, tv_diner_name, tv_diner_address, tv_average_rate;

    private Spinner spinner_people_number, spinner_price_level, spinner_return;

    private int idUser;
    private Diner diner;

    private boolean isRated = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }

        this.diner = (Diner) bundle.get("object_diner");
        this.idUser = (int) bundle.get("idUser");

        AnhXa();
        setupUI();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupUI(){

        tv_diner_name.setText(diner.getName_diner());
        tv_diner_address.setText(diner.getAddress());

        tv_post.setOnClickListener(View -> {
            //lưu post và đánh giá cảu post vào DB
            if (edit_content_post.getText().toString().isEmpty() || edit_content_post.getText().toString().equals("")){
                Toast.makeText(this,"Bạn cần nhập nội dung!", Toast.LENGTH_SHORT).show();
            } else {
                if (isRated == false) {
                    Toast.makeText(this,"Bạn chưa đánh giá!", Toast.LENGTH_SHORT).show();
                } else {
                    //Lưu DB
                    if (edit_title.getText().toString().isEmpty() || edit_title.getText().toString().equals("")){
                        edit_title.setText(diner.getName_diner());
                    }
                    //Luu post
                    Post post = new Post(idUser, diner.getId(), edit_title.getText().toString(),
                            edit_content_post.getText().toString(), LocalTime.now()+"   "+ LocalDate.now(), true);
                    PostService postService = new PostService();
                    long postId = postService.insert(post);

                    int viTri = Integer.parseInt(tv_rate_vi_tri.getText().toString());
                    int giaCa = Integer.parseInt(tv_rate_gia_ca.getText().toString());
                    int chatLuong = Integer.parseInt(tv_rate_chat_luong.getText().toString());
                    int dichVu = Integer.parseInt(tv_rate_dich_vu.getText().toString());
                    int khongGian = Integer.parseInt(tv_rate_khong_gian.getText().toString());

                    //Luu rate

                    DinerRate dinerRate = new DinerRate(idUser, diner.getId(), postId, viTri, giaCa, chatLuong, dichVu, khongGian);
                    DinerRateService dinerRateService = new DinerRateService();
                    dinerRateService.insert(dinerRate);
                    finish();
                    Toast.makeText(this,"Hoàn thành bình luận!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekbar_vi_tri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tv_rate_vi_tri.setText(""+progress);
                calculate_result_rate();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar_gia_ca.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_rate_gia_ca.setText(i+"");
                calculate_result_rate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar_chat_luong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_rate_chat_luong.setText(i+"");
                calculate_result_rate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar_dich_vu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_rate_dich_vu.setText(i+"");
                calculate_result_rate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar_khong_gian.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_rate_khong_gian.setText(i+"");
                calculate_result_rate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void AnhXa(){
        edit_title = (EditText) findViewById(R.id.edit_title_comment);
        edit_content_post = (EditText) findViewById(R.id.edit_content_post);
        tv_post = (TextView) findViewById(R.id.tv_post);

        switch_allow_sub_comment = (Switch) findViewById(R.id.switch_allow_sub_comment);

        seekbar_vi_tri = (SeekBar) findViewById(R.id.seekbar_vi_tri);
        seekbar_gia_ca = (SeekBar) findViewById(R.id.seekbar_gia_ca);
        seekbar_chat_luong = (SeekBar) findViewById(R.id.seekbar_chat_luong);
        seekbar_dich_vu = (SeekBar) findViewById(R.id.seekbar_dich_vu);
        seekbar_khong_gian = (SeekBar) findViewById(R.id.seekbar_khong_gian);

        tv_rate_vi_tri = (TextView) findViewById(R.id.tv_rate_vi_tri);
        tv_rate_gia_ca = (TextView) findViewById(R.id.tv_rate_gia_ca);
        tv_rate_chat_luong = (TextView) findViewById(R.id.tv_rate_chat_luong);
        tv_rate_dich_vu = (TextView) findViewById(R.id.tv_rate_dich_vu);
        tv_rate_khong_gian = (TextView) findViewById(R.id.tv_rate_khong_gian);

        spinner_people_number = (Spinner) findViewById(R.id.spinner_people_number);
        spinner_price_level = (Spinner) findViewById(R.id.spinner_price_level);
        spinner_return = (Spinner) findViewById(R.id.spinner_return);

        tv_post = (TextView) findViewById(R.id.tv_post);
        tv_diner_name = (TextView) findViewById(R.id.tv_diner_name);
        tv_diner_address = (TextView) findViewById(R.id.tv_diner_address);

        tv_average_rate =(TextView) findViewById(R.id.tv_average_rate);

    }

    private void calculate_result_rate(){

        if (isRated == false){
            isRated = true;
        }

        int viTri = Integer.parseInt(tv_rate_vi_tri.getText().toString());
        int giaCa = Integer.parseInt(tv_rate_gia_ca.getText().toString());
        int chatLuong = Integer.parseInt(tv_rate_chat_luong.getText().toString());
        int dichVu = Integer.parseInt(tv_rate_dich_vu.getText().toString());
        int khongGian = Integer.parseInt(tv_rate_khong_gian.getText().toString());

        double result = (viTri + giaCa + chatLuong + dichVu + khongGian + 0.0)/5;

        tv_average_rate.setText(result+"");
    }
}