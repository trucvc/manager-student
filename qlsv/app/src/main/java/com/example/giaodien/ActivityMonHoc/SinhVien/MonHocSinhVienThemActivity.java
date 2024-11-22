package com.example.giaodien.ActivityMonHoc.SinhVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.ActivityMonHoc.GiangVien.MonHocGiangVienThemAdapter;
import com.example.giaodien.HocPhi;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.ThongKe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MonHocSinhVienThemActivity extends AppCompatActivity {
    ListView lvGV;
    ArrayList<MonHocSinhVien> arrayList = new ArrayList<>();
    ArrayList<Long> danhSachMaSinhVienTonTaiTrongMonHoc = new ArrayList<>();
    SearchView timKiem;
    ImageButton imgbtnBack;
    BottomNavigationView bottomNavigationView;
    MonHocSinhVienThemAdapter monHocSinhVienThemAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    private String maKhoa, maMonHoc, tenMon;
    private Long soTinChi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_sinh_vien_them);
        lvGV = findViewById(R.id.listGV);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Intent intent = getIntent();

        maKhoa = intent.getStringExtra("maKhoa");
        maMonHoc = intent.getStringExtra("maMonHoc");
        tenMon = intent.getStringExtra("tenMonHoc");
        soTinChi = Long.valueOf(intent.getStringExtra("soTinChi"));

        monHocSinhVienThemAdapter = new MonHocSinhVienThemAdapter(MonHocSinhVienThemActivity.this, R.layout.lv_sinhvien_monhoc_them, arrayList);
        lvGV.setAdapter(monHocSinhVienThemAdapter);

        nganhHoc.child(maKhoa).child("danhSachMonHoc").child(maMonHoc).child("danhSachSinhVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Long msv = Long.valueOf(data.getKey());
                    if (msv != null) {
                        danhSachMaSinhVienTonTaiTrongMonHoc.add(msv);
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

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            }else if(item.getItemId() == R.id.thongke){
                startActivity(new Intent(getApplicationContext(), ThongKe.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                return true;
            } else if (item.getItemId() == R.id.hocphi) {
                startActivity(new Intent(getApplicationContext(), HocPhi.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                return true;
            }else if (item.getItemId() == R.id.caidat){
                startActivity(new Intent(getApplicationContext(), Setting.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void docDuLieu(String keyword) {
        //cập nhập data liên tục khi người khác thêm
        String strSeach = keyword == null ? "" : keyword.trim().toUpperCase();
        danhSachSinhVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    SinhVien sinhVien = data.getValue(SinhVien.class);
                    if (sinhVien != null && String.valueOf(sinhVien.getMsv()).substring(3, 6).equals(maKhoa) && !danhSachMaSinhVienTonTaiTrongMonHoc.contains(sinhVien.getMsv())) {
                        MonHocSinhVien monHocSinhVien = new MonHocSinhVien();
                        monHocSinhVien.setMsv(sinhVien.getMsv());
                        monHocSinhVien.setMaKhoa(Long.parseLong(maKhoa));
                        monHocSinhVien.setMaMon(maMonHoc);
                        monHocSinhVien.setSinhVien(sinhVien);
                        monHocSinhVien.setTenMonHoc(tenMon);
                        monHocSinhVien.setSoTinChi(soTinChi);

                        if (strSeach != "") {
                            String maGV = String.valueOf(sinhVien.getMsv()).toUpperCase();
                            String tenGV = sinhVien.getHoTen().toUpperCase();
                            if (maGV.contains(strSeach) || tenGV.contains(strSeach)) {
                                arrayList.add(monHocSinhVien);
                            }
                        } else {
                            arrayList.add(monHocSinhVien);
                        }
                    }
                }
                monHocSinhVienThemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}