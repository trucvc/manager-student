package com.example.giaodien.Khoa.SinhVien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.giaodien.HocPhi;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVien.SinhVienAdapter;
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

public class SinhVienDanhSach extends AppCompatActivity {
    ListView lvSV;
    ImageButton imgbtnThem;
    ArrayList<SinhVien> arrayList;
    SearchView timKiem;
    ImageButton imgbtnBack,imgbtnLuuDS;
    BottomNavigationView bottomNavigationView;
    SinhVienAdapter sinhVienAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference sinhVienData = databaseReference.child("danhSachSinhVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_danh_sach);
        mapping();

        arrayList = new ArrayList<>();
        sinhVienAdapter = new SinhVienAdapter(SinhVienDanhSach.this, R.layout.layout_sinhvien, arrayList);
        lvSV.setAdapter(sinhVienAdapter);
        loadDataSinhVien();

        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SinhVienDanhSach.this, SinhVienThem.class);
                startActivity(intent);
            }
        });

        timKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                timKiem.clearFocus();
                sinhVienAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sinhVienAdapter.getFilter().filter(newText);
                return false;
            }
        });

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgbtnLuuDS.setOnClickListener(new View.OnClickListener() {
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
                canvas.drawText("Danh sách khoa",350,60,title);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã sinh viên", 100, 140,paint);
                canvas.drawText("Họ tên", 400, 140,paint);
                canvas.drawLine(80,150,1000,150,rect);

                int y = 150;
                if (arrayList.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có khoa nào!",100,y,paint);
                }else{
                    for (SinhVien sv : arrayList){
                        y += 40;
                        canvas.drawText(sv.getMsv()+"",100,y,paint);
                        canvas.drawText(sv.getHoTen(),400,y,paint);
                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                }
                canvas.drawRect(80,100,1000,y,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "Danh sach khoa.pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(SinhVienDanhSach.this,"Tạo danh sách khoa thành công",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d("pdf",e.toString());
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void loadDataSinhVien(){
        sinhVienData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SinhVien sv = data.getValue(SinhVien.class);
                    ArrayList<SinhVienMonHoc> mh = new ArrayList<>();
                    for (DataSnapshot dataMH : data.child("danhSachMon").getChildren()){
                        SinhVienMonHoc sinhVienMonHoc = dataMH.getValue(SinhVienMonHoc.class);
                        if (sinhVienMonHoc != null){
                            mh.add(sinhVienMonHoc);
                        }
                    }
                    sv.setMonHoc(mh);
                    if (sv != null){
                        arrayList.add(sv);
                    }
                }
                sinhVienAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void mapping(){
        lvSV = findViewById(R.id.listSV);
        imgbtnThem = findViewById(R.id.imgbtnThem);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        imgbtnLuuDS = findViewById(R.id.imgbtnLuuDS);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}