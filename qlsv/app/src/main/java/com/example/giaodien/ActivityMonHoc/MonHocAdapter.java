package com.example.giaodien.ActivityMonHoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.giaodien.ActivityMonHoc.GiangVien.DanhSachMonHocGiangVien;
import com.example.giaodien.ActivityMonHoc.SinhVien.DanhSachMonHocSinhVien;
import com.example.giaodien.Khoa.Khoa;
import com.example.giaodien.Khoa.KhoaChiTiet;
import com.example.giaodien.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MonHocAdapter extends ArrayAdapter {
    private Activity context;
    private int resource;
    ArrayList<MonHoc> listMonHoc;
    LinearLayout layout;
    TextView tvMonHoc, tvMaMon;

    Button btnSinhVien, btnGiaoVien;

    public MonHocAdapter(Activity context, int resource, ArrayList<MonHoc> listMonHoc) {
        super(context, resource, listMonHoc);
        this.context = context;
        this.resource = resource;
        this.listMonHoc = listMonHoc;
    }

    @Override
    public int getCount() {
        return listMonHoc.size();
    }

    public ArrayList<MonHoc> getlistMonHoc() {
        return listMonHoc;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        tvMonHoc = customView.findViewById(R.id.tvMonHoc);
        tvMaMon = customView.findViewById(R.id.tvMaMon);
        layout = customView.findViewById(R.id.layoutMonHoc);
        btnSinhVien = customView.findViewById(R.id.btnSinhVien);
        btnGiaoVien = customView.findViewById(R.id.btnGiaoVien);

        MonHoc monHoc = listMonHoc.get(position);
        tvMonHoc.setText(monHoc.getTenMon());
        tvMaMon.setText(monHoc.getMaMon().toString());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("monHocInfo", monHoc);
                Intent intent = new Intent(context, ChiTietMonHoc.class);
                intent.putExtra("monHocInfo", bundle);
                context.startActivity(intent);
            }
        });

        btnGiaoVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("monHocInfo", monHoc);
                Intent intent = new Intent(context, DanhSachMonHocGiangVien.class);
                intent.putExtra("monHocInfo", bundle);
                context.startActivity(intent);
            }
        });

        btnSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("monHocInfo", monHoc);
                Intent intent = new Intent(context, DanhSachMonHocSinhVien.class);
                intent.putExtra("monHocInfo", bundle);
                context.startActivity(intent);
            }
        });

        return customView;
    }
}
