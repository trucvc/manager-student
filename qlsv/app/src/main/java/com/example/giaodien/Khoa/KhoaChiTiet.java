package com.example.giaodien.Khoa;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodien.GiangVien.GiangVienChiTiet;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class KhoaChiTiet extends AppCompatActivity {
    TextView tvMaKhoa1, tvTenKhoa1, tvDiaChi1, tvSoDT1, tvEmail1;
    ImageButton imgbtnGoiDien, imgbtnEmail, sua, imgbtnXoa, imgbtnXuatFile, imgbtnBack,pdf;
    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference =  firebaseDatabase.getReference();

    private DatabaseReference nganhHocReference =  databaseReference.child("nganhHoc");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa_chi_tiet);

        tvMaKhoa1 = findViewById(R.id.tvMaKhoa1);
        tvTenKhoa1 = findViewById(R.id.tvTenKhoa1);
        tvDiaChi1 = findViewById(R.id.tvDiaChi1);
        tvSoDT1 = findViewById(R.id.tvSoDT1);
        tvEmail1 = findViewById(R.id.tvEmail1);
        imgbtnGoiDien = findViewById(R.id.imgbtnGoiDien);
        imgbtnEmail = findViewById(R.id.imgbtnEmail);
        sua = findViewById(R.id.sua);
        imgbtnXoa = findViewById(R.id.imgbtnXoa);
        imgbtnXuatFile = findViewById(R.id.imgbtnXuatFile);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pdf = findViewById(R.id.imgbtnXuatFile);

        //khoa
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("khoaInfo");
        Khoa khoa = (Khoa) bundle.getSerializable("khoaInfo");

        tvMaKhoa1.setText(khoa.getMaKhoa().toString());
        tvTenKhoa1.setText(khoa.getTenKhoa());
        tvDiaChi1.setText(khoa.getDiaChi());
        tvSoDT1.setText(khoa.getSDT().toString());
        tvEmail1.setText(khoa.getEmail());

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
                bundle1.putSerializable("khoaSua",khoa);
                Intent intent1 = new Intent(KhoaChiTiet.this, KhoaSua.class);
                intent1.putExtra("khoaSua",bundle1);
                startActivity(intent1);
            }
        });

        imgbtnGoiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri goiDien = Uri.parse("tel: "+ khoa.getSDT());
                Intent goiDienIntent = new Intent(Intent.ACTION_DIAL, goiDien);
                startActivity(goiDienIntent);
            }
        });

        imgbtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KhoaChiTiet.this);
                builder.setTitle("Xóa Khoa");
                builder.setMessage("Xóa khoa "+khoa.getTenKhoa()+" với mã khoa là "+khoa.getMaKhoa());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nganhHocReference.child(khoa.getMaKhoa().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(KhoaChiTiet.this,"Xóa thành công khoa "+khoa.getTenKhoa(),Toast.LENGTH_SHORT);
//                                    Intent intent1 = new Intent(KhoaChiTiet.this, KhoaDanhSach.class);
//                                    startActivity(intent1);
                                }else{
                                    Toast.makeText(KhoaChiTiet.this,"Xóa thất bại khoa "+khoa.getTenKhoa(),Toast.LENGTH_SHORT);
                                }
                                finish();
                            }
                        });
                    }
                });
                builder.create().show();
            }
        });

        imgbtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(KhoaChiTiet.this, Mail.class);
                Bundle bundle1 = new Bundle();
                SinhVien sinhVien = new SinhVien();
                sinhVien.setEmail(khoa.getEmail());
                bundle1.putSerializable("mail", sinhVien);
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
                canvas.drawText("Thông tin khoa",420,60,title);
                canvas.drawText("Mã khoa: "+khoa.getMaKhoa(),100,130,paint);
                canvas.drawText("Tên khoa: "+khoa.getTenKhoa(),100,180,paint);
                canvas.drawText("Email: "+khoa.getEmail(),100,230,paint);
                canvas.drawText("Số điện thoại: "+khoa.getSDT(),100,280,paint);
                canvas.drawText("Địa chỉ: "+khoa.getDiaChi(),100,330,paint);
                rect.setStyle(Paint.Style.STROKE);
                rect.setStrokeWidth(3);
                paint.setTextSize(30);


                pdfDocument.finishPage(page);

                File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = String.valueOf(khoa.getMaKhoa())+".pdf";
                File file = new File(download,name);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();
                    fileOutputStream.close();
                    Toast.makeText(KhoaChiTiet.this,"Tạo thông tin khoa thành công",Toast.LENGTH_SHORT).show();
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