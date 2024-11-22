package com.example.giaodien.GiangVien;

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
import com.example.giaodien.Khoa.SinhVien.SinhVienChiTiet;
import com.example.giaodien.Khoa.SinhVien.SinhVienDanhSach;
import com.example.giaodien.Khoa.SinhVien.SinhVienSua;
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

public class GiangVienChiTiet extends AppCompatActivity {
    TextView msv,ten,gioitinh,quequan,sdt,email,ngaysinh;
    ImageView anh;
    ImageButton huy,sua,goi,mail,xoa,pdf;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference danhSachGiangVien = databaseReference.child("danhSachGiangVien");
    DatabaseReference nganhHocData = databaseReference.child("nganhHoc");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference anhGiangVien = storageReference.child("AnhGiangVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giang_vien_chi_tiet);

        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("info_gv");
        GiangVien sv = (GiangVien) bundle.getSerializable("info_gv");

        msv.setText(String.valueOf(sv.getMaGV()));
        ten.setText(sv.getHoTen());
        gioitinh.setText(sv.getGioiTinh());
        quequan.setText(sv.getQueQuan());
        ngaysinh.setText(sv.getNgaySinh());
        email.setText(sv.getEmail());
        sdt.setText(sv.getSDT());

        if (sv.getAnh().isEmpty()){
            if (sv.getGioiTinh().equals("Nam")){
                anh.setImageResource(R.drawable.student);
            }else {
                anh.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(GiangVienChiTiet.this.getBaseContext()).load(sv.getAnh()).into(anh);
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
                bundle1.putSerializable("gv_sua",sv);
                Intent intent1 = new Intent(GiangVienChiTiet.this, GiangVienSua.class);
                intent1.putExtra("gv_sua",bundle1);
                startActivity(intent1);
            }
        });

        goi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri goiDien = Uri.parse("tel: "+ sv.getSDT());
                Intent goiDienIntent = new Intent(Intent.ACTION_DIAL, goiDien);
                startActivity(goiDienIntent);
            }
        });

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GiangVienChiTiet.this);
                builder.setTitle("Xóa giảng viên");
                builder.setMessage("Xóa giảng viên "+sv.getHoTen()+" với mã giảng viên là "+sv.getMaGV());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!sv.getAnh().isEmpty()){
                            anhGiangVien.child(String.valueOf(sv.getMaGV())+".png").delete();
                        }
                        danhSachGiangVien.child(sv.getMaGV()+"").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(GiangVienChiTiet.this,"Xóa thành công giảng viên "+sv.getHoTen(),Toast.LENGTH_SHORT);
                                    Intent intent1 = new Intent(GiangVienChiTiet.this, GiangVienDanhSach.class);
                                    startActivity(intent1);
                                }else{
                                    Toast.makeText(GiangVienChiTiet.this,"Xóa thất bại giảng viên "+sv.getHoTen(),Toast.LENGTH_SHORT);
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
                Intent intent1 = new Intent(GiangVienChiTiet.this, Mail.class);
                Bundle bundle1 = new Bundle();
                SinhVien sinhVien = new SinhVien();
                sinhVien.setEmail(sv.getEmail());
                bundle1.putSerializable("mail",sinhVien);
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
                canvas.drawText("Mã giảng viên: "+sv.getMaGV(),100,130,paint);
                canvas.drawText("Họ tên: "+sv.getHoTen(),100,180,paint);
                canvas.drawText("Giới tính: "+sv.getGioiTinh(),100,230,paint);
                canvas.drawText("Email: "+sv.getEmail(),100,280,paint);
                canvas.drawText("Số điện thoại: "+sv.getSDT(),100,330,paint);
                canvas.drawText("Khoa: "+sv.getMaGV().toString().substring(0,3),100,380,paint);
                canvas.drawText("Quê quán: "+sv.getQueQuan(),100,430,paint);
                canvas.drawText("Ngày sinh: "+sv.getNgaySinh(),100,480,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);


                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = String.valueOf(sv.getMaGV())+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(GiangVienChiTiet.this,"Tạo sơ yếu lý lịch thành công",Toast.LENGTH_SHORT).show();
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
        msv = findViewById(R.id.tvMaGV1);
        ten = findViewById(R.id.tvHoTen1);
        gioitinh = findViewById(R.id.tvGioiTinh1);
        quequan = findViewById(R.id.tvQueQuan1);
        sdt = findViewById(R.id.tvSoDT1);
        email = findViewById(R.id.tvEmail1);
        ngaysinh = findViewById(R.id.tvNgaySinh1);
        anh = findViewById(R.id.imageAnh);
        huy = findViewById(R.id.imgbtnBack);
        sua = findViewById(R.id.imgbtnSua);
        goi = findViewById(R.id.imgbtnGoiDien);
        mail = findViewById(R.id.imgbtnEmail);
        xoa = findViewById(R.id.imgbtnXoa);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnXuatFile);
    }
}