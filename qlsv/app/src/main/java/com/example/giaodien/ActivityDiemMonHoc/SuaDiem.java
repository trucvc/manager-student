package com.example.giaodien.ActivityDiemMonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.ActivityMonHoc.SinhVien.DiemMonHoc;
import com.example.giaodien.Khoa.KhoaDanhSach;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuaDiem extends AppCompatActivity {

    EditText edtsuaCC, edtsuaGK, edtsuaCK;

    Button btnQL, btnLuu;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");
    DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_diem);

        btnQL = findViewById(R.id.btnQL);
        btnLuu = findViewById(R.id.btnLuu);
        edtsuaCC = findViewById(R.id.edtsuaCC11);
        edtsuaGK = findViewById(R.id.edtsuaGK11);
        edtsuaCK = findViewById(R.id.edtsuaCK11);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("diemMonHoc");
        DiemMonHoc diemMonHoc = (DiemMonHoc) bundle.getSerializable("diemMonHoc");

        edtsuaCC.setText(diemMonHoc.getDiemCC().toString());
        edtsuaGK.setText(diemMonHoc.getDiemGK().toString());
        edtsuaCK.setText(diemMonHoc.getDiemCK().toString());

        btnQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdiemCC = edtsuaCC.getText().toString().trim();
                String sdiemGK = edtsuaGK.getText().toString().trim();
                String sdiemCK = edtsuaCK.getText().toString().trim();

                if (sdiemCC.length() < 5 && sdiemGK.length()<5 && sdiemCK.length()<5){
                    try {
                        Double diemCC = Double.parseDouble(sdiemCC);
                        Double diemGK = Double.parseDouble(sdiemGK);
                        Double diemCK = Double.parseDouble(sdiemCK);

                        if (diemCC <= 10.0 && diemCK <= 10.0 && diemGK <= 10.0) {
                            DiemMonHoc diemMonHocSua = new DiemMonHoc();
                            diemMonHocSua.setDiemCC(diemCC);
                            diemMonHocSua.setDiemGK(diemGK);
                            diemMonHocSua.setDiemCK(diemCK);
                            Double diemHe10 = 0.1 * diemCC + 0.3 * diemGK + 0.6 * diemCK;
                            diemMonHocSua.setDiemHe10((double) Math.round(diemHe10 * 10) / 10);

                            danhSachSinhVien.child(diemMonHoc.getMaSinhVien().toString()).child("danhSachMon").child(diemMonHoc.getMaMonHoc().toString()).child("diemHe10").setValue(Double.valueOf((double) Math.round(diemHe10 * 10) / 10));
                            nganhHoc.child(diemMonHoc.getMaKhoa().toString()).child("danhSachMonHoc").child(diemMonHoc.getMaMonHoc()).child("danhSachSinhVien").child(diemMonHoc.getMaSinhVien().toString()).child("diemMonHoc").setValue(diemMonHocSua).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SuaDiem.this,"Sửa điểm thành công",Toast.LENGTH_SHORT).show();
                                    intent.setClass(SuaDiem.this, KhoaDanhSach.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(SuaDiem.this,"Điểm cần phải là số <= 10",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SuaDiem.this,"Điểm cần phải là số <= 10",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SuaDiem.this,"Hãy nhập đủ dữ liệu",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}