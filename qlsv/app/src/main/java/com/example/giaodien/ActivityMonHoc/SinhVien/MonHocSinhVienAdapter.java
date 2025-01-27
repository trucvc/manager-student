package com.example.giaodien.ActivityMonHoc.SinhVien;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.giaodien.ActivityDiemMonHoc.ChiTietDiem;
import com.example.giaodien.ActivityMonHoc.GiangVien.MonHocGiangVien;
import com.example.giaodien.GiangVien.GiangVienChiTiet;
import com.example.giaodien.Khoa.SinhVien.SinhVienChiTiet;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MonHocSinhVienAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<MonHocSinhVien> sinhViens;

    TextView tvTenSV;
    TextView tvMaSV;
    LinearLayout info;
    ImageView avatar;
    Button btnDiem, btnXoa;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");
    DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    public MonHocSinhVienAdapter(Activity context, int layout, ArrayList<MonHocSinhVien> sinhViens) {
        this.context = context;
        this.layout = layout;
        this.sinhViens = sinhViens;
    }

    @Override
    public int getCount() {
        return sinhViens.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layout, null);

        tvTenSV = convertView.findViewById(R.id.tvTenSV);
        tvMaSV = convertView.findViewById(R.id.tvMaSV);
        info = convertView.findViewById(R.id.info);
        avatar = convertView.findViewById(R.id.avatar);
        btnDiem = convertView.findViewById(R.id.btnDiem);
        btnXoa = convertView.findViewById(R.id.btnXoa);

        MonHocSinhVien monHocSinhVien = sinhViens.get(position);
        tvTenSV.setText(monHocSinhVien.getSinhVien().getHoTen());
        tvMaSV.setText(monHocSinhVien.getMsv().toString());

//        info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("sv_info", sinhVien.getSinhVien());
//                Intent intent = new Intent(context, SinhVienChiTiet.class);
//                intent.putExtra("info_sv", bundle);
//                context.startActivity(intent);
//            }
//        });

        if (monHocSinhVien.getSinhVien().getAnh().isEmpty()) {
            if (monHocSinhVien.getSinhVien().getGioiTinh().equals("Nam")) {
                avatar.setImageResource(R.drawable.student);
            } else {
                avatar.setImageResource(R.drawable.student_girl);
            }
        } else {
            Glide.with(context.getBaseContext()).load(monHocSinhVien.getSinhVien().getAnh()).into(avatar);
        }

//        avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("info_gv", sinhVien);
//                Intent intent = new Intent(context, GiangVienChiTiet.class);
//                intent.putExtra("info_gv", bundle);
//                context.startActivity(intent);
//            }
//        });

        btnDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("monHocSinhVien", monHocSinhVien);
                Intent intent = new Intent(context, ChiTietDiem.class);
                intent.putExtra("monHocSinhVien", bundle);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context contextParent = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(contextParent);
                builder.setTitle("Xóa sinh viên trong môn học");
                builder.setMessage("Xóa sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học với mã sinh viên là " + monHocSinhVien.getMsv());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nganhHoc.child(monHocSinhVien.getMaKhoa().toString()).child("danhSachMonHoc").child(monHocSinhVien.getMaMon()).child("danhSachSinhVien").child(monHocSinhVien.getMsv().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(contextParent, "Xóa thành công sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(contextParent, "Xóa thất bại sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                }
                            }
                        });

                        //xóa môn học trong danh sách sinh viên
                        danhSachSinhVien.child(monHocSinhVien.getMsv().toString()).child("danhSachMon").child(monHocSinhVien.getMaMon()).removeValue();
                    }
                });
                builder.create().show();
            }
        });
        return convertView;
    }
}
