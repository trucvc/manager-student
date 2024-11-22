package com.example.giaodien.Khoa.SinhVien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giaodien.HocPhi;
import com.example.giaodien.Mail;
import com.example.giaodien.MainActivity;
import com.example.giaodien.R;
import com.example.giaodien.Setting;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;
import com.example.giaodien.ThongKe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SinhVienChiTiet extends AppCompatActivity {
    TextView msv,ten,gioitinh,quequan,sdt,email,ngaysinh;
    ImageView anh;
    ImageButton huy,sua,goi,mail,xoa,pdf;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference sinhVienData = databaseReference.child("danhSachSinhVien");
    DatabaseReference nganhHocData = databaseReference.child("nganhHoc");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference anhSinhVien = storageReference.child("AnhSinhVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_chi_tiet);
        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("info_sv");
        SinhVien sv = (SinhVien) bundle.getSerializable("sv_info");
        msv.setText(String.valueOf(sv.getMsv()));
        ten.setText(sv.getHoTen());
        gioitinh.setText(sv.getGioiTinh());
        quequan.setText(sv.getQueQuan());
        ngaysinh.setText(sv.getNgaySinh());
        email.setText(sv.getEmail());
        sdt.setText(sv.getSdt());
        if (sv.getAnh().isEmpty()){
            if (sv.getGioiTinh().equals("Nam")){
                anh.setImageResource(R.drawable.student);
            }else {
                anh.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(SinhVienChiTiet.this.getBaseContext()).load(sv.getAnh()).into(anh);
        }

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("sv_sua",sv);
                Intent intent1 = new Intent(SinhVienChiTiet.this, SinhVienSua.class);
                intent1.putExtra("sua_sv",bundle1);
                startActivity(intent1);
            }
        });

        goi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri goiDien = Uri.parse("tel: "+ sv.getSdt());
                Intent goiDienIntent = new Intent(Intent.ACTION_DIAL, goiDien);
                startActivity(goiDienIntent);
            }
        });

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SinhVienChiTiet.this);
                builder.setTitle("Xóa sinh viên");
                builder.setMessage("Xóa sinh viên "+sv.getHoTen()+" với mã sinh viên là "+sv.getMsv());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!sv.getAnh().isEmpty()){
                            anhSinhVien.child(String.valueOf(sv.getMsv())+".png").delete();
                        }
                        if (!sv.getMonHoc().isEmpty()){
                            String khoa = String.valueOf(sv.getMsv()).substring(3,6);
                            ArrayList<SinhVienMonHoc> svMH = sv.getMonHoc();
                            for (SinhVienMonHoc mh : svMH) {
                                mh.getMaMon();
                                nganhHocData.child(khoa).child("danhSachMonHoc").child(mh.getMaMon()).child("danhSachSinhVien").child(String.valueOf(sv.getMsv())).removeValue();
                            }

                        }
                        sinhVienData.child(sv.getMsv()+"").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SinhVienChiTiet.this,"Xóa thành công sinh viên "+sv.getHoTen(),Toast.LENGTH_SHORT);
                                    Intent intent1 = new Intent(SinhVienChiTiet.this, SinhVienDanhSach.class);
                                    startActivity(intent1);
                                }else{
                                    Toast.makeText(SinhVienChiTiet.this,"Xóa thất bại sinh viên "+sv.getHoTen(),Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                });
                builder.create().show();
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SinhVienChiTiet.this, Mail.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("mail",sv);
                intent1.putExtra("mail_sv",bundle1);
                startActivity(intent1);
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
                canvas.drawText("Sơ yếu lý lịch",420,60,title);
                canvas.drawText("Mã sinh viên: "+sv.getMsv(),100,130,paint);
                canvas.drawText("Họ tên: "+sv.getHoTen(),100,180,paint);
                canvas.drawText("Giới tính: "+sv.getGioiTinh(),100,230,paint);
                canvas.drawText("Email: "+sv.getEmail(),100,280,paint);
                canvas.drawText("Số điện thoại: "+sv.getSdt(),100,330,paint);
                canvas.drawText("Khóa: "+sv.getKhoaHoc(),100,380,paint);
                canvas.drawText("Quê quán: "+sv.getQueQuan(),100,430,paint);
                canvas.drawText("Ngày sinh: "+sv.getNgaySinh(),100,480,paint);
                canvas.drawText("Danh sách điểm: ",100,530,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);
                canvas.drawText("Tên môn", 100, 620,paint);
                canvas.drawText("Số tín chỉ", 600, 620,paint);
                canvas.drawText("Điểm", 900, 620,paint);
                canvas.drawLine(80,630,1000,630,rect);

                int y = 630;
                if (sv.getMonHoc().isEmpty()){
                    y += 40;
                    canvas.drawText("Sinh viên chưa học môn nào!",100,y,paint);
                }else{
                    double sum = 0;
                    int stc = 0;
                    for (SinhVienMonHoc sinhVienMonHoc : sv.getMonHoc()){
                        y += 40;
                        canvas.drawText(sinhVienMonHoc.getTenMon(),100,y,paint);
                        canvas.drawText(sinhVienMonHoc.getSoTinChi()+"",620,y,paint);
                        double he4;
                        stc += sinhVienMonHoc.getSoTinChi();
                        if (sinhVienMonHoc.getDiemHe10() >= 8.5){
                            he4 = 4;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 7.8) {
                            he4 = 3.5;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 7) {
                            he4 = 3;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 6.3) {
                            he4 = 2.5;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 5.5) {
                            he4 = 2;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 4.8) {
                            he4 = 1.5;
                        } else if (sinhVienMonHoc.getDiemHe10() >= 4) {
                            he4 = 1;
                        }else {
                            he4 = 0;
                        }
                        sum += he4*sinhVienMonHoc.getSoTinChi();
                        canvas.drawText(String.valueOf(Math.floor(he4 * 100) / 100),920,y,paint);
                        y +=10;
                        canvas.drawLine(80,y,1000,y,rect);
                    }
                    y += 40;
                    canvas.drawText("GPA: "+String.valueOf(Math.floor(sum/stc * 100) / 100),870,y,paint);
                }
                canvas.drawRect(80,580,1000,y+10,rect);

                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = String.valueOf(sv.getMsv())+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(SinhVienChiTiet.this,"Tạo sơ yếu lý lịch thành công",Toast.LENGTH_SHORT).show();
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

    private void mapping(){
        msv = findViewById(R.id.tvMaSV1);
        ten = findViewById(R.id.tvHoTen1);
        gioitinh = findViewById(R.id.tvGioiTinh1);
        quequan = findViewById(R.id.tvQueQuan1);
        sdt = findViewById(R.id.tvSoDT1);
        email = findViewById(R.id.tvEmail1);
        ngaysinh = findViewById(R.id.tvNgaySinh1);
        anh = findViewById(R.id.imageAnh);
        huy = findViewById(R.id.imgbtnBack);
        sua = findViewById(R.id.sua);
        goi = findViewById(R.id.imgbtnGoiDien);
        mail = findViewById(R.id.imgbtnEmail);
        xoa = findViewById(R.id.imgbtnXoa);
        pdf = findViewById(R.id.imgbtnXuatFile);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}