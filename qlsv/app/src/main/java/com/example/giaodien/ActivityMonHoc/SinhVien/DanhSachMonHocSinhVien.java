package com.example.giaodien.ActivityMonHoc.SinhVien;

import android.annotation.SuppressLint;
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

import com.example.giaodien.ActivityMonHoc.GiangVien.DanhSachMonHocGiangVien;
import com.example.giaodien.ActivityMonHoc.GiangVien.MonHocGiangVien;
import com.example.giaodien.ActivityMonHoc.GiangVien.MonHocGiangVienAdapter;
import com.example.giaodien.ActivityMonHoc.MonHoc;
import com.example.giaodien.GiangVien.GiangVien;
import com.example.giaodien.GiangVien.GiangVienThem;
import com.example.giaodien.HocPhi;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;
import com.example.giaodien.Support.Diem;
import com.example.giaodien.Support.SinhVienDiem;
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

public class DanhSachMonHocSinhVien extends AppCompatActivity {
    ListView lvGV;
    ImageButton imgbtnThem;
    ArrayList<MonHocSinhVien> arrayList;
    ArrayList<SinhVien> sinhVienArrayList = new ArrayList<>();
    ArrayList<SinhVienDiem> svd;
    SearchView timKiem;
    ImageButton imgbtnBack,pdf;
    BottomNavigationView bottomNavigationView;
    MonHocSinhVienAdapter monHocSinhVienAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");
    DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    private String maKhoa, maMonHoc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_mon_hoc_danh_sach);

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
        svd = new ArrayList<>();
        inPDF();

        arrayList = new ArrayList<>();
        monHocSinhVienAdapter = new MonHocSinhVienAdapter(DanhSachMonHocSinhVien.this, R.layout.lv_sinhvien_monhoc, arrayList);
        lvGV.setAdapter(monHocSinhVienAdapter);
//        docDuLieu("");

        danhSachSinhVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    SinhVien sinhVien = data.getValue(SinhVien.class);
                    if (sinhVien != null && String.valueOf(sinhVien.getMsv()).substring(3, 6).equals(maKhoa)) {
                        sinhVienArrayList.add(sinhVien);
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
                Intent intent = new Intent(DanhSachMonHocSinhVien.this, MonHocSinhVienThemActivity.class);
                intent.putExtra("maKhoa", maKhoa);
                intent.putExtra("maMonHoc", maMonHoc);
                intent.putExtra("soTinChi", monHoc.getSoTinChi().toString());
                intent.putExtra("tenMonHoc", monHoc.getTenMon());
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

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,10920,1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                title.setTextSize(50);
                title.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("Thông tin sinh viên",320,60,title);
                canvas.drawText("Mã khoa: "+maKhoa,100,130,paint);
                canvas.drawText("Mã môn: "+maMonHoc,100,180,paint);
                canvas.drawText("Danh sách sinh viên: ",100,230,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã sinh viên", 100, 320,paint);
                canvas.drawText("Tên sinh viên", 500, 320,paint);
                canvas.drawText("Điểm", 850, 320,paint);
                canvas.drawLine(80,330,1000,330,rect);

                int y = 330;
                if (arrayList.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có sinh viên trong môn học này!",100,y,paint);
                }else{
                    for (MonHocSinhVien monHocSinhVien : arrayList){
                        y += 40;
                        canvas.drawText(monHocSinhVien.getSinhVien().getMsv()+"",100,y,paint);
                        canvas.drawText(monHocSinhVien.getSinhVien().getHoTen(),500,y,paint);
                        for (SinhVienDiem sinhVienDiem : svd){
                            if (sinhVienDiem.getMsv() == monHocSinhVien.getSinhVien().getMsv()){
                                canvas.drawText(sinhVienDiem.getDiem().getDiemHe10()+"",870,y,paint);
                            }
                        }

                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                }
                canvas.drawRect(80,280,1000,y,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "SV_"+maMonHoc+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(DanhSachMonHocSinhVien.this,"Tạo danh sách sinh viên thành công",Toast.LENGTH_SHORT).show();
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

    private void inPDF(){
        nganhHoc.child(maKhoa).child("danhSachMonHoc").child(maMonHoc).child("danhSachSinhVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                svd.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    SinhVienDiem sv = (SinhVienDiem) data.getValue(SinhVienDiem.class);
                    Diem diem = (Diem) data.child("diemMonHoc").getValue(Diem.class);
                    sv.setDiem(diem);
                    if (sv != null){
                        svd.add(sv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void docDuLieu(String keyword) {
        //cập nhập data liên tục khi người khác thêm
        String strSeach = keyword == null ? "" : keyword.trim().toUpperCase();
        nganhHoc.child(maKhoa).child("danhSachMonHoc").child(maMonHoc).child("danhSachSinhVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MonHocSinhVien monHocSinhVien = data.getValue(MonHocSinhVien.class);
                    monHocSinhVien.setMaKhoa(Long.parseLong(maKhoa));
                    monHocSinhVien.setMaMon(maMonHoc);
                    if (monHocSinhVien != null) {
                        for (SinhVien sv : sinhVienArrayList) {
                            if (monHocSinhVien.getMsv().equals(sv.getMsv())) {
                                monHocSinhVien.setSinhVien(sv);
                            }
                        }
                        if (strSeach != "") {
                            String maGV = String.valueOf(monHocSinhVien.getMsv()).toUpperCase();
                            String tenGV = monHocSinhVien.getSinhVien().getHoTen().toUpperCase();

//                            //thêm chi tiết điểm của sinh viên
//                            DiemMonHoc diemMonHoc = new DiemMonHoc();
//                            long a = data.child("diemMonHoc").getChildrenCount();
//                            System.out.println(a);
//                            String test = monHocSinhVien.getMsv().toString();
//                            System.out.println(test);
////                            DiemMonHoc diemMonHoc = data.child("diemMonHoc").getValue(DiemMonHoc.class);
//                            for (DataSnapshot dataSnapshot : data.child("diemMonHoc").getChildren()) {
////                                diemMonHoc = dataSnapshot.getValue(DiemMonHoc.class);
//                                String a1 = (String) dataSnapshot.child("diemCC").getValue();
//                                System.out.println(a1);
//
//                            }
//                            diemMonHoc.setMaKhoa(Long.parseLong(maKhoa));
//                            diemMonHoc.setMaMonHoc(maMonHoc);
//                            diemMonHoc.setMaSinhVien(monHocSinhVien.getMsv());
//
//                            monHocSinhVien.setDiemMonHocChiTiet(diemMonHoc);

                            if (maGV.contains(strSeach) || tenGV.contains(strSeach)) {
                                arrayList.add(monHocSinhVien);
                            }
                        } else {
                            arrayList.add(monHocSinhVien);
                        }
                    }
                }
                monHocSinhVienAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}