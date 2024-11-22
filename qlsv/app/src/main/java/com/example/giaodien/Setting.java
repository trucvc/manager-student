package com.example.giaodien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Setting extends AppCompatActivity {
    Button dx,mk,sua;
    EditText name;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mapping();

        getName();
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = name.getText().toString().trim();
                if (ten.length() > 0){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(ten).build();
                    user.updateProfile(profileChangeRequest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Setting.this,"Đổi tên người dùng thành công",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Setting.this,"Đổi tên người dùng thất bại",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Setting.this,"Bạn không được để trống tên người dùng",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Setting.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,ChangePass.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.caidat);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            }else if(item.getItemId() == R.id.thongke){
                startActivity(new Intent(getApplicationContext(),ThongKe.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.hocphi) {
                startActivity(new Intent(getApplicationContext(),HocPhi.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                return true;
            }else if (item.getItemId() == R.id.caidat){
                return true;
            }
            return false;
        });
    }

    private void getName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ten = user.getDisplayName();
        if (!ten.isEmpty()){
            name.setText(ten);
        }
    }

    private void mapping(){
        dx = findViewById(R.id.out);
        mk = findViewById(R.id.changePass);
        sua = findViewById(R.id.btnTen);
        name = findViewById(R.id.edtTenND);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}