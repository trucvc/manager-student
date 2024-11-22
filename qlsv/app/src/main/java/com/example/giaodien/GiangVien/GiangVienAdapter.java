package com.example.giaodien.GiangVien;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodien.GiangVien.GiangVien;
import com.example.giaodien.GiangVien.GiangVienChiTiet;
import com.example.giaodien.R;

import java.util.ArrayList;

public class GiangVienAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<GiangVien> GiangViens;

    TextView tenGV;
    TextView maGV;
    LinearLayout info;
    ImageView avatar;

    public GiangVienAdapter(Activity context, int layout, ArrayList<GiangVien> GiangViens) {
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

        GiangVien giangVien = GiangViens.get(position);
        tenGV.setText(giangVien.getHoTen());
        maGV.setText(giangVien.getMaGV().toString());

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("info_gv", giangVien);
                Intent intent = new Intent(context, GiangVienChiTiet.class);
                intent.putExtra("info_gv", bundle);
                context.startActivity(intent);
            }
        });

        if (giangVien.getAnh().isEmpty()) {
            if (giangVien.getGioiTinh().equals("Nam")) {
                avatar.setImageResource(R.drawable.student);
            } else {
                avatar.setImageResource(R.drawable.student_girl);
            }
        } else {
            Glide.with(context.getBaseContext()).load(giangVien.getAnh()).into(avatar);
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
        return convertView;
    }
}
