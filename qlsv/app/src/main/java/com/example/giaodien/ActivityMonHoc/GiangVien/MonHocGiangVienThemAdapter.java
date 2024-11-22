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

public class MonHocGiangVienThemAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<MonHocGiangVien> GiangViens;

    TextView tenGV;
    TextView maGV;
    LinearLayout info;
    ImageView avatar;
    Button btnThem;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");

    public MonHocGiangVienThemAdapter(Activity context, int layout, ArrayList<MonHocGiangVien> GiangViens) {
        this.context = context;
        this.layout = layout;
        this.GiangViens = GiangViens;
    }

    @Override
    public int getCount() {
        return GiangViens.size();
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
        btnThem = convertView.findViewById(R.id.btnThem);

        MonHocGiangVien giangVien = GiangViens.get(position);
        tenGV.setText(giangVien.getGiangVien().getHoTen());
        maGV.setText(giangVien.getMaGV().toString());


        if (giangVien.getGiangVien().getAnh().isEmpty()) {
            if (giangVien.getGiangVien().getGioiTinh().equals("Nam")) {
                avatar.setImageResource(R.drawable.student);
            } else {
                avatar.setImageResource(R.drawable.student_girl);
            }
        } else {
            Glide.with(context.getBaseContext()).load(giangVien.getGiangVien().getAnh()).into(avatar);
        }

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context contextParent = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(contextParent);
                builder.setTitle("Thêm giảng viên trong môn học");
                builder.setMessage("Thêm giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học với mã giảng viên là " + giangVien.getMaGV());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nganhHoc.child(giangVien.getMaKhoa().toString()).child("danhSachMonHoc").child(giangVien.getMaMon()).child("danhSachGiaoVien").child(giangVien.getMaGV().toString()).child("maGV").setValue(giangVien.getMaGV()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(contextParent, "Thêm thành công giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(contextParent, "Thêm thất bại giảng viên " + giangVien.getGiangVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
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
