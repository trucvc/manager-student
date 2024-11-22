package com.example.giaodien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePass extends AppCompatActivity {
    EditText mk,mk1,mk2;
    Button huy,doi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        mapping();

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String matkhau = mk.getText().toString().trim();
                String matkhau1 = mk1.getText().toString().trim();
                String matkhau2 = mk2.getText().toString().trim();
                if (matkhau.length() > 0 && matkhau1.length() > 0 && matkhau2.length() > 0){
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), matkhau);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        if (matkhau1.equals(matkhau2)){
                                            if (matkhau1.length()>=6){
                                                changPass(matkhau1);
                                            }else {
                                                Toast.makeText(ChangePass.this,"Mật khẩu mới phải từ 6 kí tự trở lên",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(ChangePass.this,"Mật khẩu mới chưa trùng khớp với nhau",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(ChangePass.this,"Mật khẩu cũ chưa chính xác",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(ChangePass.this,"Không được để trống mật khẩu",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changPass(String pass){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ChangePass.this,"Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChangePass.this,"Thay đổi mật khẩu thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mapping(){
        mk = findViewById(R.id.mkHT);
        mk1 = findViewById(R.id.mk1);
        mk2 = findViewById(R.id.mk2);
        huy = findViewById(R.id.huy);
        doi = findViewById(R.id.changpass);
    }
}