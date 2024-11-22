package com.example.giaodien.ActivityMonHoc.GiangVien;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.ActivityMonHoc.MonHoc;
import com.example.giaodien.GiangVien.GiangVien;
import com.example.giaodien.HocPhi;

import com.example.giaodien.Khoa.SinhVien.SinhVienChiTiet;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;

import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;
import com.example.giaodien.ThongKe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DanhSachMonHocGiangVien extends AppCompatActivity {
    ListView lvGV;
    ImageButton imgbtnThem;
    ArrayList<MonHocGiangVien> arrayList;
    ArrayList<GiangVien> giangViens = new ArrayList<>();
    SearchView timKiem;
    ImageButton imgbtnBack,pdf;
    BottomNavigationView bottomNavigationView;
    MonHocGiangVienAdapter giangVienAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    DatabaseReference danhSachGiangVien = databaseReference.child("danhSachGiangVien");

    private String maKhoa, maMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giang_vien_mon_hoc_danh_sach);

        lvGV = findViewById(R.id.listGV);
        imgbtnThem = findViewById(R.id.imgbtnThem);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnLuuDS);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("monHocInfo");
        MonHoc monHoc = (MonHoc) bundle.getSerializable("monHocInfo");
        maKhoa = monHoc.getMaKhoa();
        maMonHoc = monHoc.getMaMon();

        arrayList = new ArrayList<>();
        giangVienAdapter = new MonHocGiangVienAdapter(DanhSachMonHocGiangVien.this, R.layout.lv_giangvien_monhoc, arrayList);
        lvGV.setAdapter(giangVienAdapter);

        danhSachGiangVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    GiangVien giangVien = data.getValue(GiangVien.class);
                    if (giangVien != null && giangVien.getMaGV().toString().substring(0, 3).equals(maKhoa)) {
                        giangViens.add(giangVien);
                    }
                }
                docDuLieu("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                Intent intent = new Intent(DanhSachMonHocGiangVien.this, MonHocGiangVienThem.class);

//                bundle1.putSerializable("giangViens", giangViens);
//                intent.putExtra("giangViens", bundle1);

                bundle1.putSerializable("maKhoa", maKhoa);
                intent.putExtra("maKhoa", bundle1);

                bundle1.putSerializable("maMonHoc", maMonHoc);
                intent.putExtra("maMonHoc", bundle1);

                startActivity(intent);
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

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint rect = new Paint();
                Paint title = new Paint();
                paint.setTextSize(40);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,1920,2).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                title.setTextSize(50);
                title.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("Thông tin giảng viên",320,60,title);
                canvas.drawText("Mã khoa: "+maKhoa,100,130,paint);
                canvas.drawText("Mã môn: "+maMonHoc,100,180,paint);
                canvas.drawText("Danh sách giảng viên: ",100,230,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã giảng viên", 100, 320,paint);
                canvas.drawText("Tên giảng viên", 500, 320,paint);
                canvas.drawText("Giới tính", 850, 320,paint);
                canvas.drawLine(80,330,1000,330,rect);

                int y = 330;
                if (arrayList.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có giảng viên trong môn học này!",100,y,paint);
                }else{
                    for (MonHocGiangVien monHocGiangVien : arrayList){
                        y += 40;
                        canvas.drawText(monHocGiangVien.getGiangVien().getMaGV()+"",100,y,paint);
                        canvas.drawText(monHocGiangVien.getGiangVien().getHoTen(),500,y,paint);
                        canvas.drawText(monHocGiangVien.getGiangVien().getGioiTinh(),870,y,paint);
                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                }
                canvas.drawRect(80,280,1000,y,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "GV_"+maMonHoc+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(DanhSachMonHocGiangVien.this,"Tạo danh sách giảng viên thành công",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d("pdf",e.toString());
                    throw new RuntimeException(e);
                }
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
        nganhHoc.child(maKhoa).child("danhSachMonHoc").child(maMonHoc).child("danhSachGiaoVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MonHocGiangVien giangVien = data.getValue(MonHocGiangVien.class);
                    giangVien.setMaKhoa(Long.parseLong(maKhoa));
                    giangVien.setMaMon(maMonHoc);
                    if (giangVien != null) {
                        for (GiangVien gv : giangViens) {
                            if (gv.getMaGV().equals(giangVien.getMaGV())) {
                                giangVien.setGiangVien(gv);
                            }
                        }
                        if (strSeach != "") {
                            String maGV = String.valueOf(giangVien.getMaGV()).toUpperCase();
                            String tenGV = giangVien.getGiangVien().getHoTen().toUpperCase();
                            if (maGV.contains(strSeach) || tenGV.contains(strSeach)) {
                                arrayList.add(giangVien);
                            }
                        } else {
                            arrayList.add(giangVien);
                        }
                    }
                }
                giangVienAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}