package com.example.giaodien.GiangVien;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class GiangVienThem extends AppCompatActivity {
    EditText edtMaSV;
    EditText HoTen;
    RadioButton nam,nu;
    EditText queQuan;
    EditText sdt;
    EditText email;
    DatePicker ngaysinh;
    ImageButton imgbtnBack;
    Button huy;
    Button add;
    ImageView anh;
    int check = 1;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference danhSachGiangVien = databaseReference.child("danhSachGiangVien");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giang_vien_them);

        edtMaSV = findViewById(R.id.edtMaSV);
        HoTen = findViewById(R.id.edtHoTen);
        nam = findViewById(R.id.rbtNam);
        nu = findViewById(R.id.rbtNu);
        queQuan = findViewById(R.id.edtQueQuan);
        sdt = findViewById(R.id.edtSoDT);
        email = findViewById(R.id.edtEmail);
        ngaysinh = findViewById(R.id.datengaySinh);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        huy = findViewById(R.id.buttonHuy);
        add = findViewById(R.id.buttonLuu);
        anh = findViewById(R.id.imageAnh);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ActivityResultLauncher chonAnhLaucher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        anh.setImageURI(o);
                        check = 0;
                    }
                });

        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonAnhLaucher.launch("image/*");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msv = edtMaSV.getText().toString().trim();
                String hoten = HoTen.getText().toString().trim();
                String gioiTinh="";
                if (nam.isChecked()){
                    gioiTinh = "Nam";
                }
                if(nu.isChecked()){
                    gioiTinh = "Nữ";
                }
                String quequan = queQuan.getText().toString().trim();
                String sodt = sdt.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String ngaySinh = ngaysinh.getDayOfMonth()+"/"+(ngaysinh.getMonth()-1)+"/"+ngaysinh.getYear();
                if (msv.length()>0 && hoten.length()>0 && gioiTinh.length()>0 && quequan.length()>0 && sodt.length()>0 && mail.length()>0 && mail.length() < 26 && quequan.length()<15 && hoten.length()<22){
                    if (msv.length() == 6){
                        try {
                            long masv = Long.parseLong(msv);
                            if (mail.length()<13){
                                Toast.makeText(GiangVienThem.this,"Nhập đúng định dạng email",Toast.LENGTH_SHORT).show();
                            }else{
                                if (mail.substring(mail.length()-12).equals("@hnue.edu.vn")){
                                    if((sodt.length() == 10) && sodt.charAt(1) != '0'){
                                        try {
                                            long so = Long.parseLong(sodt);
                                            GiangVien giangVien = new GiangVien(masv,hoten,sodt,quequan,mail,"",gioiTinh,ngaySinh);
                                            if (check == 0){
                                                StorageReference anhSV = storageReference.child("AnhGiangVien").child(msv+".png");
                                                BitmapDrawable bitmapDrawable = (BitmapDrawable) anh.getDrawable();
                                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                                byte[] img = byteArrayOutputStream.toByteArray();
                                                anhSV.putBytes(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        anhSV.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String url = uri.toString();
                                                                giangVien.setAnh(url);
                                                                danhSachGiangVien.child(msv).setValue(giangVien).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(GiangVienThem.this,"Thêm giảng viên "+msv+" thành công",Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }else{
                                                                            Toast.makeText(GiangVienThem.this,"Thêm giảng viên "+msv+" thất bại",Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });

                                            }else{
                                                danhSachGiangVien.child(msv).setValue(giangVien).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(GiangVienThem.this,"Thêm giảng viên "+msv+" thành công",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }else{
                                                            Toast.makeText(GiangVienThem.this,"Thêm giảng viên "+msv+" thất bại",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }catch (Exception e){
                                            Toast.makeText(GiangVienThem.this,"Số điện thoại là dãy số",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(GiangVienThem.this,"Số điện thoại phải là dãy 10 số",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(GiangVienThem.this,"Email phải có đuôi @hnue.edu.vn",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(GiangVienThem.this,"Mã giảng viên là dãy số",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(GiangVienThem.this,"Nhập đúng định dạng mã giảng viên độ dài 6 số",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(GiangVienThem.this,"Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}