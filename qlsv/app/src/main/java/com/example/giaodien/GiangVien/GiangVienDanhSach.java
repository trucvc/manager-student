package com.example.giaodien.GiangVien;

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

import com.example.giaodien.HocPhi;
import com.example.giaodien.Khoa.SinhVien.SinhVienDanhSach;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.SinhVien.SinhVien;
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

public class GiangVienDanhSach extends AppCompatActivity {
    ListView lvGV;
    ImageButton imgbtnThem;
    ArrayList<GiangVien> arrayList;
    SearchView timKiem;
    ImageButton imgbtnBack,pdf;
    BottomNavigationView bottomNavigationView;
    GiangVienAdapter giangVienAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference danhSachGiangVien = databaseReference.child("danhSachGiangVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giang_vien_danh_sach);

        lvGV = findViewById(R.id.listGV);
        imgbtnThem = findViewById(R.id.imgbtnThem);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnLuuDS);


        arrayList = new ArrayList<>();
        giangVienAdapter = new GiangVienAdapter(GiangVienDanhSach.this, R.layout.lv_giaovien, arrayList);
        lvGV.setAdapter(giangVienAdapter);
        docDuLieu("");

        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiangVienDanhSach.this, GiangVienThem.class);
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

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,20920,10).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                title.setTextSize(50);
                title.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("Danh sách giảng viên",350,60,title);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã giảng viên", 100, 140,paint);
                canvas.drawText("Họ tên", 400, 140,paint);
                canvas.drawText("Mã khoa", 860, 140,paint);
                canvas.drawLine(80,150,1000,150,rect);

                int y = 150;
                if (arrayList.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có giảng viên nào!",100,y,paint);
                }else{
                    for (GiangVien gv : arrayList){
                        y += 40;
                        canvas.drawText(gv.getMaGV()+"",100,y,paint);
                        canvas.drawText(gv.getHoTen(),400,y,paint);
                        canvas.drawText(gv.getMaGV().toString().substring(0,3), 860, y,paint);
                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                }
                canvas.drawRect(80,100,1000,y,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "Danh sach giang vien.pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(GiangVienDanhSach.this,"Tạo danh sách giảng viên thành công",Toast.LENGTH_SHORT).show();
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
        danhSachGiangVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    GiangVien giangVien = data.getValue(GiangVien.class);
                    if (giangVien != null) {
                        if (strSeach != "") {
                            String maGV = String.valueOf(giangVien.getMaGV()).toUpperCase();
                            String tenGV = giangVien.getHoTen().toUpperCase();
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