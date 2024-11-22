package com.example.giaodien.ActivityMonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Khoa.KhoaDanhSach;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuaMonHoc extends AppCompatActivity {
    EditText edtsuaMaMon, edtsuaTenMon, edtsuaSoTinChi;
    Button btnQL, btnLuu;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference = databaseReference.child("nganhHoc");
    private DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_mon_hoc);


        edtsuaMaMon = findViewById(R.id.edtsuaMaMon);
        edtsuaTenMon = findViewById(R.id.edtsuaTenMon);
        edtsuaSoTinChi = findViewById(R.id.edtsuaSoTinChi);
        btnQL = findViewById(R.id.btnQL);
        btnLuu = findViewById(R.id.btnLuu);

        edtsuaMaMon.setEnabled(false);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("monHocInfo");
        MonHoc monHoc = (MonHoc) bundle.getSerializable("monHocInfo");


        edtsuaMaMon.setText(monHoc.getMaMon());
        edtsuaTenMon.setText(monHoc.getTenMon());
        edtsuaSoTinChi.setText(monHoc.getSoTinChi().toString());

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtsuaMaMon.getText().length() > 0 && edtsuaSoTinChi.getText().length() > 0 && edtsuaTenMon.getText().length() > 0 && edtsuaMaMon.getText().length() < 6 && edtsuaSoTinChi.getText().length() < 3 && edtsuaTenMon.getText().length()<20) {
                    try {
                        String maMonHocOld = monHoc.getMaMon();
                        String maKhoa = monHoc.getMaKhoa();

                        monHoc.setMaMon(edtsuaMaMon.getText().toString().trim());
                        monHoc.setTenMon(edtsuaTenMon.getText().toString());
                        monHoc.setSoTinChi(Long.parseLong(edtsuaSoTinChi.getText().toString()));

                        if (!edtsuaTenMon.getText().equals(monHoc.getTenMon()) || !edtsuaSoTinChi.getText().equals(monHoc.getSoTinChi())) {
                            nganhHocReference.child(maKhoa).child("danhSachMonHoc").child(monHoc.getMaMon()).child("soTinChi").setValue(monHoc.getSoTinChi());
                            nganhHocReference.child(maKhoa).child("danhSachMonHoc").child(monHoc.getMaMon()).child("tenMon").setValue(monHoc.getTenMon());

                            //tim danh sách sinh viên trong môn học để update lại tên môn học trong danh sách sinh viên
                            nganhHocReference.child(maKhoa).child("danhSachMonHoc").child(monHoc.getMaMon()).child("danhSachSinhVien").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        String maSv = data.getKey();
                                        danhSachSinhVien.child(maSv).child("danhSachMon").child(monHoc.getMaMon()).child("tenMon").setValue(edtsuaTenMon.getText().toString());
                                        if (!edtsuaSoTinChi.getText().equals(monHoc.getSoTinChi())) {
                                            Long soTinChi = Long.parseLong(edtsuaSoTinChi.getText().toString());
                                            danhSachSinhVien.child(maSv).child("danhSachMon").child(monHoc.getMaMon()).child("soTinChi").setValue(soTinChi);
                                            danhSachSinhVien.child(maSv).child("danhSachMon").child(monHoc.getMaMon()).child("hocPhi").setValue(340000 * soTinChi);
                                        }
                                    }
                                    intent.setClass(SuaMonHoc.this, KhoaDanhSach.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }



                    }catch (Exception e) {
                        Toast.makeText(SuaMonHoc.this,"Số tín chỉ phải là số",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SuaMonHoc.this,"Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}