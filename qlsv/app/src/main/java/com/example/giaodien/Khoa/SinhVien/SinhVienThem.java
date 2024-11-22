package com.example.giaodien.Khoa.SinhVien;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.giaodien.R;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SinhVienThem extends AppCompatActivity {
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
    DatabaseReference sinhVienData = databaseReference.child("danhSachSinhVien");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_them);
        mapping();

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
                if (msv.length()>0 && hoten.length()>0 && gioiTinh.length()>0 && quequan.length()>0 && sodt.length()>0 && mail.length()>0 && mail.length() < 26 && quequan.length()<15 && hoten.length()<18){
                    if (msv.length() == 9){
                        try {
                            long masv = Long.parseLong(msv);
                            if (mail.length()<13){
                                Toast.makeText(SinhVienThem.this,"Nhập đúng định dạng email",Toast.LENGTH_SHORT).show();
                            }else{
                                if (mail.substring(mail.length()-12).equals("@hnue.edu.vn")){
                                    if((sodt.length() == 10) && sodt.charAt(1) != '0'){
                                        try {
                                            long so = Long.parseLong(sodt);
                                            String khoahoc = msv.substring(0,2);
                                            ArrayList<SinhVienMonHoc> array = new ArrayList<>();
                                            SinhVien sv = new SinhVien(masv,hoten,sodt,quequan,"K"+khoahoc,mail,array,"",gioiTinh,ngaySinh);
                                            if (check == 0){
                                                StorageReference anhSV = storageReference.child("AnhSinhVien").child(msv+".png");
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
                                                                sv.setAnh(url);
                                                                sinhVienData.child(msv).setValue(sv).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(SinhVienThem.this,"Thêm sinh viên "+msv+" thành công",Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }else{
                                                                            Toast.makeText(SinhVienThem.this,"Thêm sinh viên "+msv+" thất bại",Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });

                                            }else{
                                                sinhVienData.child(msv).setValue(sv).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(SinhVienThem.this,"Thêm sinh viên "+msv+" thành công",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }else{
                                                            Toast.makeText(SinhVienThem.this,"Thêm sinh viên "+msv+" thất bại",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }catch (Exception e){
                                            Toast.makeText(SinhVienThem.this,"Số điện thoại là dãy số",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(SinhVienThem.this,"Số điện thoại phải là dãy 10 số",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(SinhVienThem.this,"Email phải có đuôi @hnue.edu.vn",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(SinhVienThem.this,"Mã sinh viên là dãy số",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SinhVienThem.this,"Nhập đúng định dạng mã sinh viên",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SinhVienThem.this,"Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping(){
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
        anh = findViewById(R.id.imageView);
    }
}