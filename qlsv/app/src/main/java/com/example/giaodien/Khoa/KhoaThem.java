package com.example.giaodien.Khoa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KhoaThem extends AppCompatActivity {
    EditText edtMaKhoa, edtTenKhoa, edtDiaChi, edtSoDT, edtEmail;
    Button buttonHuy, buttonLuu;

    ImageButton imgbtnBack;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference = databaseReference.child("nganhHoc");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa_them);

        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSoDT = findViewById(R.id.edtSoDT);
        edtEmail = findViewById(R.id.edtEmail);
        buttonHuy = findViewById(R.id.buttonHuy);
        buttonLuu = findViewById(R.id.buttonLuu);
        imgbtnBack = findViewById(R.id.imgbtnBack);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maKhoa = edtMaKhoa.getText().toString();
                String tenKhoa = edtTenKhoa.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String sdt = edtSoDT.getText().toString();
                String email = edtEmail.getText().toString();

                if (maKhoa.length() > 0 && tenKhoa.length() > 0 && diaChi.length() > 0 && sdt.length() > 0 && email.length() > 0 && maKhoa.length() < 4 && tenKhoa.length() < 21 && diaChi.length() > 40 && email.length() < 26) {
                    if (email.length() < 13) {
                        Toast.makeText(KhoaThem.this, "Nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
                    } else {
                        if (email.substring(email.length() - 12).equals("@hnue.edu.vn")) {
                            try {
                                long makhoa = Long.parseLong(maKhoa);
                                if (maKhoa.length() == 3 && makhoa>=100) {
                                    if ((sdt.length() == 10) && sdt.charAt(1) != '0') {
                                        try {
                                            long so = Long.parseLong(sdt);
                                            Khoa khoa = new Khoa(makhoa, tenKhoa, email, diaChi, sdt);
                                            nganhHocReference.child(maKhoa).setValue(khoa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(KhoaThem.this, "Thêm khoa " + tenKhoa + " thành công", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(KhoaThem.this, "Thêm khoa " + tenKhoa + " thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                    finish();
                                                }
                                            });
                                        } catch (Exception e) {
                                            Toast.makeText(KhoaThem.this, "Số điện thoại là dãy số", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(KhoaThem.this, "Số điện thoại phải là dãy 10 số", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(KhoaThem.this, "Mã khoa phải là dãy 3 số", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(KhoaThem.this, "Mã khoa phải là dãy số", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(KhoaThem.this, "Email phải có đuôi @hnue.edu.vn", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(KhoaThem.this, "Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}