package com.example.giaodien.SinhVien;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodien.Khoa.SinhVien.SinhVienChiTiet;
import com.example.giaodien.Khoa.SinhVien.SinhVienDiem;
import com.example.giaodien.R;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;

import java.util.ArrayList;

public class SinhVienAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<SinhVien> sinhViens,sinhViensBackUp,sinhViensFilter;

    public SinhVienAdapter(Activity context, int layout, ArrayList<SinhVien> sinhViens) {
        this.context = context;
        this.layout = layout;
        this.sinhViens = this.sinhViensBackUp = sinhViens;
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

    private class ViewHolder{
        TextView tenSV;
        TextView maSV;
        Button diem;
        LinearLayout info;
        ImageView avatar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout,null);

            viewHolder = new ViewHolder();

            viewHolder.tenSV = convertView.findViewById(R.id.tvTenSV);
            viewHolder.maSV = convertView.findViewById(R.id.tvMaSV);
            viewHolder.diem = convertView.findViewById(R.id.btnDiem);
            viewHolder.info = convertView.findViewById(R.id.layoutSV);
            viewHolder.avatar = convertView.findViewById(R.id.avatarSV);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = sinhViens.get(position);
        viewHolder.tenSV.setText(sv.getHoTen());
        viewHolder.maSV.setText(sv.getMsv()+"");
        viewHolder.diem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("sv_diem",sv);
                Intent intent = new Intent(context, SinhVienDiem.class);
                intent.putExtra("diem_sv",bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("sv_info",sv);
                Intent intent = new Intent(context, SinhVienChiTiet.class);
                intent.putExtra("info_sv",bundle);
                context.startActivity(intent);
            }
        });
        if (sv.getAnh().isEmpty()){
            if (sv.getGioiTinh().equals("Nam")){
                viewHolder.avatar.setImageResource(R.drawable.student);
            }else {
                viewHolder.avatar.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(context.getBaseContext()).load(sv.getAnh()).into(viewHolder.avatar);
        }
        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("sv_info",sv);
                Intent intent = new Intent(context, SinhVienChiTiet.class);
                intent.putExtra("info_sv",bundle);
                context.startActivity(intent);
            }
        });

        if (!sv.getMonHoc().isEmpty()){
            double sum = 0;
            int stc = 0;
            for (SinhVienMonHoc sinhVienMonHoc : sv.getMonHoc()){
                stc += sinhVienMonHoc.getSoTinChi();
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
            viewHolder.diem.setText(String.valueOf(Math.floor(sum/stc * 100) / 100));
        }else{
            viewHolder.diem.setText(String.valueOf(0));
        }

        return convertView;
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().trim().toLowerCase();

                if (query.length()<1){
                    sinhViensFilter = sinhViensBackUp;
                }else {
                    sinhViensFilter = new ArrayList<>();
                    for (SinhVien sv: sinhViensBackUp) {
                        if (String.valueOf(sv.getMsv()).contains(query) || sv.getHoTen().toLowerCase().contains(query)){
                            sinhViensFilter.add(sv);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sinhViensFilter;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sinhViens = (ArrayList<SinhVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
