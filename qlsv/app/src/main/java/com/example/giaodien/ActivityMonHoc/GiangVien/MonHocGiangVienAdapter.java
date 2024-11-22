package com.example.giaodien.ActivityMonHoc.GiangVien;

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
import com.example.giaodien.GiangVien.GiangVienChiTiet;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MonHocGiangVienAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<MonHocGiangVien> monHocGiangVienArrayList;

    TextView tenGV;
    TextView maGV;
    LinearLayout info;
    ImageView avatar;
    Button btnXoa;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    public MonHocGiangVienAdapter(Activity context, int layout, ArrayList<MonHocGiangVien> monHocGiangVienArrayList) {
        this.context = context;
        this.layout = layout;
        this.monHocGiangVienArrayList = monHocGiangVienArrayList;
    }

    @Override
    public int getCount() {
        return monHocGiangVienArrayList.size();
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

        tenGV = convertView.findViewById(R.id.tvTenGV);
        maGV = convertView.findViewById(R.id.tvMaGV);
        info = convertView.findViewById(R.id.layoutGiangVien);
        avatar = convertView.findViewById(R.id.avatarGiangVien);
        btnXoa = convertView.findViewById(R.id.btnXoa);

        MonHocGiangVien giangVien = monHocGiangVienArrayList.get(position);
        tenGV.setText(giangVien.getGiangVien().getHoTen());
        maGV.setText(giangVien.getMaGV().toString());

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("info_gv", giangVien.getGiangVien());
                Intent intent = new Intent(context, GiangVienChiTiet.class);
                intent.putExtra("info_gv", bundle);
                context.startActivity(intent);
            }
        });

        if (giangVien.getGiangVien().getAnh().isEmpty()) {
            if (giangVien.getGiangVien().getGioiTinh().equals("Nam")) {
                avatar.setImageResource(R.drawable.student);
            } else {
                avatar.setImageResource(R.drawable.student_girl);
            }
        } else {
            Glide.with(context.getBaseContext()).load(giangVien.getGiangVien().getAnh()).into(avatar);
        }

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("info_gv", giangVien);
                Intent intent = new Intent(context, GiangVienChiTiet.class);
                intent.putExtra("info_gv", bundle);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context contextParent = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(contextParent);
                builder.setTitle("Xóa giảng viên trong môn học");
                builder.setMessage("Xóa giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học với mã giảng viên là " + giangVien.getMaGV());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nganhHoc.child(giangVien.getMaKhoa().toString()).child("danhSachMonHoc").child(giangVien.getMaMon()).child("danhSachGiaoVien").child(giangVien.getMaGV().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(contextParent, "Xóa thành công giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(contextParent, "Xóa thất bại giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                });
                builder.create().show();
            }
        });
        return convertView;
    }
}
