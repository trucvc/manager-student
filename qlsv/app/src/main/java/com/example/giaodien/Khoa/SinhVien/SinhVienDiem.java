package com.example.giaodien.Khoa.SinhVien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodien.R;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;

public class SinhVienDiem extends AppCompatActivity {
    TextView diem10,diem4,xeploai,msv,ten;
    ImageView anh;
    ImageButton huy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_diem);
        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("diem_sv");
        SinhVien sv = (SinhVien) bundle.getSerializable("sv_diem");

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (sv.getAnh().isEmpty()){
            if (sv.getGioiTinh().equals("Nam")){
                anh.setImageResource(R.drawable.student);
            }else {
                anh.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(SinhVienDiem.this.getBaseContext()).load(sv.getAnh()).into(anh);
        }

        if (!sv.getMonHoc().isEmpty()){
            double sum = 0,sum_10 = 0;
            int stc = 0;
            for (SinhVienMonHoc sinhVienMonHoc : sv.getMonHoc()){
                stc += sinhVienMonHoc.getSoTinChi();
                sum_10 += sinhVienMonHoc.getDiemHe10()*sinhVienMonHoc.getSoTinChi();
                double he4;
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
            }
            double gpa4 = Math.floor(sum/stc * 100) / 100;
            double gpa10 = Math.floor(sum_10/stc * 100) / 100;
            diem4.setText(String.valueOf(gpa4));
            diem10.setText(String.valueOf(gpa10));
            if (gpa4 >= 3.6){
                xeploai.setText("Xuất sắc");
            } else if (gpa4 >= 3.2) {
                xeploai.setText("Giỏi");
            } else if (gpa4 >= 2.5) {
                xeploai.setText("Khá");
            }else if (gpa4 >= 2) {
                xeploai.setText("Trung bình");
            }else{
                xeploai.setText("Yếu");
            }
            msv.setText(sv.getMsv()+"");
            ten.setText(sv.getHoTen());
        }else{
            diem4.setText("Chưa có môn học nào");
            diem10.setText("Chưa có môn học nào");
            xeploai.setText("Chưa có môn học nào");
        }
    }

    private void mapping(){
        diem4 = findViewById(R.id.edtDiem4);
        diem10 = findViewById(R.id.edtDiem10);
        xeploai = findViewById(R.id.edtXepLoai);
        msv = findViewById(R.id.msv);
        ten = findViewById(R.id.name);
        huy = findViewById(R.id.imgbtnBack);
        anh = findViewById(R.id.imageAnh);
    }
}