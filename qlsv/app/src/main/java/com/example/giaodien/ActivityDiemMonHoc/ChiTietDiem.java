package com.example.giaodien.ActivityDiemMonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.ActivityMonHoc.SinhVien.DiemMonHoc;
import com.example.giaodien.ActivityMonHoc.SinhVien.MonHocSinhVien;
import com.example.giaodien.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietDiem extends AppCompatActivity {

    Button btnttQL;
    ImageButton btnSua;
    TextView tvDiemCC, tvDiemGK, tvDiemCK, tvDiemTB;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    DiemMonHoc diemMonHoc = new DiemMonHoc();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_diem);
        btnSua = findViewById(R.id.btnSua);
        btnttQL = findViewById(R.id.btnttQL);
        tvDiemCC = findViewById(R.id.tvDiemCC);
        tvDiemGK = findViewById(R.id.tvDiemGK);
        tvDiemCK = findViewById(R.id.tvDiemCK);
        tvDiemTB = findViewById(R.id.tvDiemTB);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("monHocSinhVien");
        MonHocSinhVien monHocSinhVien = (MonHocSinhVien) bundle.getSerializable("monHocSinhVien");

        nganhHoc.child(monHocSinhVien.getMaKhoa().toString()).child("danhSachMonHoc").child(monHocSinhVien.getMaMon()).child("danhSachSinhVien").child(monHocSinhVien.getMsv().toString()).child("diemMonHoc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diemMonHoc = snapshot.getValue(DiemMonHoc.class);

                diemMonHoc.setMaKhoa(monHocSinhVien.getMaKhoa());
                diemMonHoc.setMaMonHoc(monHocSinhVien.getMaMon());
                diemMonHoc.setMaSinhVien(monHocSinhVien.getMsv());

                tvDiemCC.setText(diemMonHoc.getDiemCC().toString());
                tvDiemGK.setText(diemMonHoc.getDiemGK().toString());
                tvDiemCK.setText(diemMonHoc.getDiemCK().toString());
                tvDiemTB.setText(diemMonHoc.getDiemHe10().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnttQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("diemMonHoc", diemMonHoc);
                Intent intent = new Intent(ChiTietDiem.this, SuaDiem.class);
                intent.putExtra("diemMonHoc", bundle);
                startActivity(intent);
            }
        });
    }
}