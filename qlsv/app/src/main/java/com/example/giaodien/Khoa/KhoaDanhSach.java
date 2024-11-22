package com.example.giaodien.Khoa;

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

public class KhoaDanhSach extends AppCompatActivity {

    private ListView listView;
    private ImageButton imgbtnBack, imgbtnThem;
    SearchView timKiem;
    BottomNavigationView bottomNavigationView;

    ImageButton pdf;

    private ArrayList<Khoa> listKhoa;

    private KhoaAdapter khoaAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference =  firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference =  databaseReference.child("nganhHoc");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa_danh_sach);

        //find object ListView in activity_khoa_danh_sach.xml
        listView = findViewById(R.id.listSV);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        imgbtnThem = findViewById(R.id.imgbtnThem);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnLuuDS);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhoaDanhSach.this, KhoaThem.class);
                startActivity(intent);
            }
        });

        timKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                timKiem.clearFocus();
//                sinhVienAdapter.getFilter().filter(query);
                docDuLieu(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                sinhVienAdapter.getFilter().filter(newText);
                docDuLieu(newText);
                return false;
            }
        });

        listKhoa = new ArrayList<>();
        khoaAdapter = new KhoaAdapter(KhoaDanhSach.this, R.layout.layout_khoa, listKhoa);
        listView.setAdapter(khoaAdapter);
        docDuLieu("");

        //them khoa
        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent themKhoaItent = new Intent(KhoaDanhSach.this, KhoaThem.class);
                startActivity(themKhoaItent);
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
                canvas.drawText("Danh sách khoa",350,60,title);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã khoa", 100, 140,paint);
                canvas.drawText("Tên khoa", 400, 140,paint);
                canvas.drawLine(80,150,1000,150,rect);

                int y = 150;
                if (listKhoa.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có khoa nào!",100,y,paint);
                }else{
                    for (Khoa khoa : listKhoa){
                        y += 40;
                        canvas.drawText(khoa.getMaKhoa()+"",100,y,paint);
                        canvas.drawText(khoa.getTenKhoa(),400,y,paint);
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
                    Toast.makeText(KhoaDanhSach.this,"Tạo danh sách khoa thành công",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d("pdf",e.toString());
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void docDuLieu(String keyword) {
        //cập nhập data liên tục khi người khác thêm
        String strSeach = keyword == null ? "" : keyword.trim().toUpperCase();
        nganhHocReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //khi dữ liệu thay đổi thì phải clear hết data cũ đi.
                listKhoa.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Khoa khoa = dataSnapshot.getValue(Khoa.class);
                    if (khoa != null) {
                        if (strSeach != "") {
                            String maKhoa = String.valueOf(khoa.getMaKhoa()).toUpperCase();
                            String tenKhoa = khoa.getTenKhoa().toUpperCase();
                            if (maKhoa.contains(strSeach) || tenKhoa.contains(strSeach)) {
                                listKhoa.add(khoa);
                            }
                        } else {
                            listKhoa.add(khoa);
                        }
                    }
                }

                //thông báo sự thay đổi
                khoaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}