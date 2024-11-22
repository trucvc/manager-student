package com.example.giaodien.ActivityMonHoc.GiangVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.GiangVien.GiangVien;
import com.example.giaodien.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MonHocGiangVienThem extends AppCompatActivity {
    ListView lvGV;
    ArrayList<MonHocGiangVien> arrayList = new ArrayList<>();
    ArrayList<Long> danhSachMaGiangVienTonTaiTrongMonHoc = new ArrayList<>();
    SearchView timKiem;
    ImageButton imgbtnBack;
    MonHocGiangVienThemAdapter giangVienThemAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    DatabaseReference danhSachGiangVien = databaseReference.child("danhSachGiangVien");

    private String maKhoa, maMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_giang_vien_them);

        lvGV = findViewById(R.id.listGV);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        imgbtnBack = findViewById(R.id.imgbtnBack);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("maKhoa");
        maKhoa = (String) bundle.getSerializable("maKhoa");

        bundle = intent.getBundleExtra("maMonHoc");
        maMonHoc = (String) bundle.getSerializable("maMonHoc");

        giangVienThemAdapter = new MonHocGiangVienThemAdapter(MonHocGiangVienThem.this, R.layout.lv_giangvien_monhoc_them, arrayList);
        lvGV.setAdapter(giangVienThemAdapter);

        nganhHoc.child(maKhoa).child("danhSachMonHoc").child(maMonHoc).child("danhSachGiaoVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Long maGv = Long.valueOf(data.getKey());
                    if (maGv != null) {
                        danhSachMaGiangVienTonTaiTrongMonHoc.add(maGv);
                    }
                }
                docDuLieu("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        timKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                timKiem.clearFocus();
                docDuLieu(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                docDuLieu(newText);
                return false;
            }
        });

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void docDuLieu(String keyword) {
        //cập nhập data liên tục khi người khác thêm
        String strSeach = keyword == null ? "" : keyword.trim().toUpperCase();
        danhSachGiangVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    GiangVien giangVien = data.getValue(GiangVien.class);
                    if (giangVien != null && giangVien.getMaGV().toString().substring(0, 3).equals(maKhoa) && !danhSachMaGiangVienTonTaiTrongMonHoc.contains(giangVien.getMaGV())) {
                        MonHocGiangVien monHocGiangVien = new MonHocGiangVien();
                        monHocGiangVien.setMaGV(giangVien.getMaGV());
                        monHocGiangVien.setMaKhoa(Long.parseLong(maKhoa));
                        monHocGiangVien.setMaMon(maMonHoc);
                        monHocGiangVien.setGiangVien(giangVien);

                        if (strSeach != "") {
                            String maGV = String.valueOf(giangVien.getMaGV()).toUpperCase();
                            String tenGV = giangVien.getHoTen().toUpperCase();
                            if (maGV.contains(strSeach) || tenGV.contains(strSeach)) {
                                arrayList.add(monHocGiangVien);
                            }
                        } else {
                            arrayList.add(monHocGiangVien);
                        }
                    }
                }
                giangVienThemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}