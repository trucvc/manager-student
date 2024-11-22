package com.example.giaodien.ActivityMonHoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.HocPhi;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.ThongKe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietMonHoc extends AppCompatActivity {
    TextView tvttMaMon, tvttTenMon1, tvttSoTinChi;
    ImageButton sua, imgbtnXoa, imgbtnXuatFile, imgbtnBack;
    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference =  firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference =  databaseReference.child("nganhHoc");

    private DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_hoc);
        tvttMaMon = findViewById(R.id.tvttMaMon);
        tvttTenMon1 = findViewById(R.id.tvttTenMon1);
        tvttSoTinChi = findViewById(R.id.tvttSoTinChi);
        sua = findViewById(R.id.sua);
        imgbtnXoa = findViewById(R.id.imgbtnXoa);
        imgbtnXuatFile = findViewById(R.id.imgbtnXuatFile);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("monHocInfo");
        MonHoc monHoc = (MonHoc) bundle.getSerializable("monHocInfo");

        tvttMaMon.setText(monHoc.getMaMon().toString());
        tvttTenMon1.setText(monHoc.getTenMon());
        tvttSoTinChi.setText(monHoc.getSoTinChi().toString());

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("monHocInfo", monHoc);
                Intent intent1 = new Intent(ChiTietMonHoc.this, SuaMonHoc.class);
                intent1.putExtra("monHocInfo",bundle1);
                startActivity(intent1);
            }
        });

        imgbtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietMonHoc.this);
                builder.setTitle("Xóa môn học");
                builder.setMessage("Xóa môn học "+monHoc.getTenMon()+" với mã môn là "+monHoc.getMaMon());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //khi xóa môn học cần xóa tất cả môn học đang có trong danh sách sinh viên...
                        //tim danh sách sinh viên trong môn học để xóa môn học trong danh sách sinh viên
                        nganhHocReference.child(monHoc.getMaKhoa()).child("danhSachMonHoc").child(monHoc.getMaMon()).child("danhSachSinhVien").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    String maSv = data.getKey();
                                    danhSachSinhVien.child(maSv).child("danhSachMon").child(monHoc.getMaMon()).removeValue();
                                }

                                //sau khi xóa xong thì mới xóa môn học
                                nganhHocReference.child(monHoc.getMaKhoa()).child("danhSachMonHoc").child(monHoc.getMaMon()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ChiTietMonHoc.this,"Xóa thành công môn học "+monHoc.getTenMon(),Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ChiTietMonHoc.this,"Xóa thất bại môn học "+monHoc.getTenMon(),Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.create().show();
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
}