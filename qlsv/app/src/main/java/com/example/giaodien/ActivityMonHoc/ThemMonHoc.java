package com.example.giaodien.ActivityMonHoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.giaodien.Khoa.SinhVien.SinhVienThem;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThemMonHoc extends AppCompatActivity {
    EditText edtaddMaMon, edtaddTenMon, edtaddSoTinChi;
    Button btnQL, btnLuu;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference =  firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference =  databaseReference.child("nganhHoc");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_hoc);
        edtaddMaMon = findViewById(R.id.edtaddMaMon);
        edtaddTenMon = findViewById(R.id.edtaddTenMon);
        edtaddSoTinChi = findViewById(R.id.edtaddSoTinChi);
        btnQL = findViewById(R.id.btnQL);
        btnLuu = findViewById(R.id.btnLuu);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("maKhoa");
        String maKhoa = (String) bundle.getSerializable("maKhoa");

        btnQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maMon = edtaddMaMon.getText().toString();
                String soTinChi = edtaddSoTinChi.getText().toString();
                String tenMon = edtaddTenMon.getText().toString();
                if (maMon.length() > 0 && soTinChi.length() > 0 && tenMon.length() > 0 && maMon.length() < 6 && soTinChi.length() < 3 && tenMon.length()<20) {
                    try {
                        MonHoc monHoc = new MonHoc();
                        monHoc.setMaMon(maMon);
                        monHoc.setSoTinChi(Long.parseLong(soTinChi));
                        monHoc.setTenMon(tenMon);
                        monHoc.setMaKhoa(maKhoa);
                        nganhHocReference.child(maKhoa).child("danhSachMonHoc").child(maMon).setValue(monHoc).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ThemMonHoc.this,"Thêm môn học "+maMon+" thành công",Toast.LENGTH_SHORT).show();
                                } else  {
                                    Toast.makeText(ThemMonHoc.this,"Thêm môn học "+maMon+" thất bại",Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        });
                    }catch (Exception e) {
                        Toast.makeText(ThemMonHoc.this,"Số tín chỉ phải là số",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ThemMonHoc.this,"Hãy nhập đủ dữ liệu hoặc dữ liêệu không quá dài",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}