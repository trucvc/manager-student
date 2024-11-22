package com.example.giaodien.GiangVien;

import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class GiangVienSua extends AppCompatActivity {
    ImageButton imgbtnBack;
    EditText edtMaGV;
    EditText HoTen;
    RadioButton nam,nu;
    EditText queQuan;
    EditText sdt;
    EditText email;
    DatePicker ngaysinh;
    Button huy;
    Button add;
    ImageView anh;
    int check = 1;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference danhSachGiaoVien = databaseReference.child("danhSachGiangVien");
    DatabaseReference nganhHocData = databaseReference.child("nganhHoc");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference anhGiangVien = storageReference.child("AnhGiangVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giang_vien_sua);

        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("gv_sua");
        GiangVien giangVien = (GiangVien) bundle.getSerializable("gv_sua");

        edtMaGV.setText(String.valueOf(giangVien.getMaGV()));
        HoTen.setText(giangVien.getHoTen());
        if (giangVien.getGioiTinh().equals("Nam")){
            nam.setChecked(true);
        }else{
            nu.setChecked(true);
        }
        queQuan.setText(giangVien.getQueQuan());
        email.setText(giangVien.getEmail());
        sdt.setText(giangVien.getSDT());
        if (giangVien.getAnh().isEmpty()){
            if (giangVien.getGioiTinh().equals("Nam")){
                anh.setImageResource(R.drawable.student);
            }else {
                anh.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(GiangVienSua.this.getBaseContext()).load(giangVien.getAnh()).into(anh);
        }

        // cắt chuỗi ra mảng ngăn cắt "/" ví dụ : 20/10/2023 -> arr = [20, 10, 2023]
        String[] arrayNgaySinhNam = giangVien.getNgaySinh().split("/");
        int year = Integer.valueOf(arrayNgaySinhNam[2]);
        int monthOfYear = Integer.valueOf(arrayNgaySinhNam[1]);
        int dayOfMonth = Integer.valueOf(arrayNgaySinhNam[0]);
        ngaysinh.init(year, monthOfYear, dayOfMonth, null);

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
                String maGV = edtMaGV.getText().toString().trim();
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
                if (maGV.length()>0 && hoten.length()>0 && gioiTinh.length()>0 && quequan.length()>0 && sodt.length()>0 && mail.length()>0 && mail.length() < 26 && quequan.length()<15 && hoten.length()<22){
                    if (maGV.length() == 6){
                        try {
                            long masv = Long.parseLong(maGV);
                            if (mail.length()<13){
                                Toast.makeText(GiangVienSua.this,"Nhập đúng định dạng email",Toast.LENGTH_SHORT).show();
                            }else{
                                if (mail.substring(mail.length()-12).equals("@hnue.edu.vn")){
                                    if((sodt.length() == 10) && sodt.charAt(1) != '0'){
                                        try {
                                            long so = Long.parseLong(sodt);
                                            if (!hoten.equals(giangVien.getHoTen())){
                                                danhSachGiaoVien.child(maGV).child("hoTen").setValue(hoten);
                                            }
                                            if (!gioiTinh.equals(giangVien.getGioiTinh())){
                                                danhSachGiaoVien.child(maGV).child("gioiTinh").setValue(gioiTinh);
                                            }
                                            if (!quequan.equals(giangVien.getQueQuan())){
                                                danhSachGiaoVien.child(maGV).child("queQuan").setValue(quequan);
                                            }
                                            if (!sodt.equals(giangVien.getSDT())){
                                                danhSachGiaoVien.child(maGV).child("sdt").setValue(sodt);
                                            }
                                            if (!mail.equals(giangVien.getEmail())){
                                                danhSachGiaoVien.child(maGV).child("email").setValue(mail);
                                            }
                                            if (!ngaySinh.equals(giangVien.getNgaySinh())){
                                                danhSachGiaoVien.child(maGV).child("ngaySinh").setValue(ngaySinh);
                                            }
                                            if (check == 0){
                                                StorageReference anhGV = storageReference.child("AnhGiangVien").child(maGV+".png");
                                                BitmapDrawable bitmapDrawable = (BitmapDrawable) anh.getDrawable();
                                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                                byte[] img = byteArrayOutputStream.toByteArray();
                                                anhGV.putBytes(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        anhGV.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String url = uri.toString();
                                                                giangVien.setAnh(url);
                                                                danhSachGiaoVien.child(maGV).child("anh").setValue(url);
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                            Toast.makeText(GiangVienSua.this,"Sửa giảng viên với mã giảng viên "+maGV+" thành công",Toast.LENGTH_SHORT).show();
                                            intent.setClass(GiangVienSua.this, GiangVienDanhSach.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }catch (Exception e){
                                            Toast.makeText(GiangVienSua.this,"Số điện thoại là dãy số",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(GiangVienSua.this,"Số điện thoại phải là dãy 10 số",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(GiangVienSua.this,"Email phải có đuôi @hnue.edu.vn",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(GiangVienSua.this,"Mã giảng viên là dãy số",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(GiangVienSua.this,"Nhập đúng định dạng mã giảng viên 6 số",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(GiangVienSua.this,"Hãy nhập đủ dữ liệu hoặc dữ liệu không quá dài",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping(){
        edtMaGV = findViewById(R.id.edtMaGV);
        HoTen = findViewById(R.id.edtHoTen);
        nam = findViewById(R.id.btnNam);
        nu = findViewById(R.id.btnNu);
        queQuan = findViewById(R.id.edtQueQuan);
        sdt = findViewById(R.id.edtSoDT);
        email = findViewById(R.id.edtEmail);
        ngaysinh = findViewById(R.id.datengaySinh);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        huy = findViewById(R.id.buttonHuy);
        add = findViewById(R.id.buttonXacNhan);
        anh = findViewById(R.id.imageAnh);

        edtMaGV.setEnabled(false);
    }
}