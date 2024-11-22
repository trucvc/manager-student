package com.example.giaodien.Khoa;

import android.content.Intent;
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

public class KhoaSua extends AppCompatActivity {

    EditText edtMaKhoa, edtTenKhoa, edtDiaChi, edtSoDT, edtEmail;
    Button buttonHuy, buttonXacNhan;

    ImageButton imgbtnBack;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference = databaseReference.child("nganhHoc");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa_sua);

        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSoDT = findViewById(R.id.edtSoDT);
        edtEmail = findViewById(R.id.edtEmail);
        buttonHuy = findViewById(R.id.buttonHuy);
        buttonXacNhan = findViewById(R.id.buttonXacNhan);
        imgbtnBack = findViewById(R.id.imgbtnBack);

        //không cho sửa edittext
        edtMaKhoa.setEnabled(false);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("khoaSua");
        Khoa khoa = (Khoa) bundle.getSerializable("khoaSua");

        edtMaKhoa.setText(khoa.getMaKhoa().toString());
        edtTenKhoa.setText(khoa.getTenKhoa());
        edtDiaChi.setText(khoa.getDiaChi());
        edtSoDT.setText(khoa.getSDT().toString());
        edtEmail.setText(khoa.getEmail());

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

        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maKhoa = edtMaKhoa.getText().toString();
                String tenKhoa = edtTenKhoa.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String sdt = edtSoDT.getText().toString();
                String email = edtEmail.getText().toString();

                if (tenKhoa.length() > 0 && diaChi.length() > 0 && sdt.length() > 0 && email.length() > 0 && maKhoa.length() < 4 && tenKhoa.length() < 21 && diaChi.length() < 40 && email.length() < 26) {
                    if (email.length() < 13) {
                        Toast.makeText(KhoaSua.this, "Nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
                    } else {
                        if (email.substring(email.length() - 12).equals("@hnue.edu.vn")) {
                            if ((sdt.length() == 10) && sdt.charAt(1) != '0') {
                                try {
                                    long so = Long.parseLong(sdt);
                                    Khoa khoa = new Khoa(Long.parseLong(maKhoa), tenKhoa, email, diaChi, sdt);
                                    nganhHocReference.child(maKhoa).setValue(khoa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(KhoaSua.this, "Sửa khoa " + tenKhoa + " thành công", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(KhoaSua.this, "Sửa khoa " + tenKhoa + " thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                            Intent intent1 = new Intent(KhoaSua.this, KhoaDanhSach.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent1);
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(KhoaSua.this, "Số điện thoại là dãy số", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(KhoaSua.this, "Số điện thoại phải là dãy 10 số", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(KhoaSua.this, "Email phải có đuôi @hnue.edu.vn", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(KhoaSua.this, "Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}