package com.example.giaodien.ActivityMonHoc;

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

import com.example.giaodien.GiangVien.GiangVienChiTiet;
import com.example.giaodien.HocPhi;
import com.example.giaodien.Khoa.Khoa;
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

public class DanhSachMonHoc extends AppCompatActivity {
    private ListView listViewMonHoc;
    private ImageButton imgbtnBack, imgbtnThem,pdf;
    SearchView timKiem;
    BottomNavigationView bottomNavigationView;
    Khoa k;

    private ArrayList<MonHoc> monHocList;

    private MonHocAdapter monHocAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference =  firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference =  databaseReference.child("nganhHoc");

    private String maKhoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_mon_hoc);

        //find object ListView in activity_khoa_danh_sach.xml
        listViewMonHoc = findViewById(R.id.listViewMonHoc);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        imgbtnThem = findViewById(R.id.imgbtnThem);
        timKiem = findViewById(R.id.imgbtnTimKiem);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnLuuDS);

        //lấy khoa bên DanhSachKhoa truyền sang
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("khoaInfo");
        Khoa khoa = (Khoa) bundle.getSerializable("khoaInfo");
        k = khoa;

        maKhoa = khoa.getMaKhoa().toString();

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        monHocList = new ArrayList<>();
        monHocAdapter = new MonHocAdapter(DanhSachMonHoc.this, R.layout.lv_monhoc, monHocList);
        listViewMonHoc.setAdapter(monHocAdapter);
        docDuLieu("");

        //them khoa
        imgbtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("maKhoa", khoa.getMaKhoa().toString());
                Intent intent1 = new Intent(DanhSachMonHoc.this, ThemMonHoc.class);
                intent1.putExtra("maKhoa", bundle1);
                startActivity(intent1);
            }
        });
    }

    private void docDuLieu(String keyword) {
        //cập nhập data liên tục khi người khác thêm
        String strSeach = keyword == null ? "" : keyword.trim().toUpperCase();
        nganhHocReference.child(maKhoa).child("danhSachMonHoc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //khi dữ liệu thay đổi thì phải clear hết data cũ đi.
                monHocList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MonHoc monHoc = dataSnapshot.getValue(MonHoc.class);
                    if (monHoc != null) {
                        monHoc.setMaKhoa(maKhoa);
                        if (strSeach != "") {
                            String tenMon = String.valueOf(monHoc.getTenMon()).toUpperCase();
                            String maMon = monHoc.getMaMon().toString().toUpperCase();
                            if (tenMon.contains(strSeach) || maMon.contains(strSeach)) {
                                monHocList.add(monHoc);
                            }
                        } else {
                            monHocList.add(monHoc);
                        }
                    }
                }

                //thông báo sự thay đổi
                monHocAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                canvas.drawText("Thông tin môn học của khoa",250,60,title);
                canvas.drawText("Mã khoa: "+k.getMaKhoa(),100,130,paint);
                canvas.drawText("Tên khoa: "+k.getTenKhoa(),100,180,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Mã môn", 100, 270,paint);
                canvas.drawText("Tên môn", 420, 270,paint);
                canvas.drawText("Số tín chỉ", 850, 270,paint);
                canvas.drawLine(80,280,1000,280,rect);

                int y = 280;
                if (monHocList.isEmpty()){
                    y += 40;
                    canvas.drawText("Chưa có môn học nào!",100,y,paint);
                }else{
                    for (MonHoc mh : monHocList){
                        y += 40;
                        canvas.drawText(mh.getMaMon(),100,y,paint);
                        canvas.drawText(mh.getTenMon(),420,y,paint);
                        canvas.drawText(String.valueOf(mh.getSoTinChi()),900,y,paint);
                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                }
                canvas.drawRect(80,230,1000,y,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = String.valueOf("MH_"+k.getMaKhoa())+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(DanhSachMonHoc.this,"Tạo thông tin môn học khoa thành công",Toast.LENGTH_SHORT).show();
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
}