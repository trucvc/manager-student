package com.example.giaodien.Khoa;

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

import com.example.giaodien.ActivityMonHoc.DanhSachMonHoc;
import com.example.giaodien.Khoa.SinhVien.SinhVienChiTiet;
import com.example.giaodien.R;

import java.util.ArrayList;

public class KhoaAdapter extends ArrayAdapter {
    private Activity context;
    private int resource;
    ArrayList<Khoa> listKhoa;
    LinearLayout layout;
    TextView tvMaKhoa, tvTenKhoa;

    Button btnMonHoc;

    public KhoaAdapter(Activity context, int resource, ArrayList<Khoa> listKhoa) {
        super(context, resource, listKhoa);
        this.context = context;
        this.resource = resource;
        this.listKhoa = listKhoa;
    }

    @Override
    public int getCount() {
        return listKhoa.size();
    }

    public ArrayList<Khoa> getListKhoa() {
        return listKhoa;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        tvMaKhoa = customView.findViewById(R.id.tvMaKhoa);
        tvTenKhoa = customView.findViewById(R.id.tvTenKhoa);
        btnMonHoc = customView.findViewById(R.id.btnMonHoc);
        layout = customView.findViewById(R.id.layoutKhoa);

        Khoa khoa = listKhoa.get(position);
        tvMaKhoa.setText(khoa.getMaKhoa().toString());
        tvTenKhoa.setText(khoa.getTenKhoa());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("khoaInfo", khoa);
                Intent intent = new Intent(context, KhoaChiTiet.class);
                intent.putExtra("khoaInfo", bundle);
                context.startActivity(intent);
            }
        });

        btnMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("khoaInfo", khoa);
                Intent intent = new Intent(context, DanhSachMonHoc.class);
                intent.putExtra("khoaInfo", bundle);
                context.startActivity(intent);
            }
        });

        return customView;
    }
}
