package com.example.giaodien;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;

import java.util.ArrayList;

public class HocPhiAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<SinhVien> sinhVien,sinhViensBackUp,sinhViensFilter;

    public HocPhiAdapter(Activity context, int layout, ArrayList<SinhVien> sinhVien) {
        this.context = context;
        this.layout = layout;
        this.sinhVien = this.sinhViensBackUp = sinhVien;
    }

    @Override
    public int getCount() {
        return sinhVien.size();
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
        TextView hocPhi;
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
            viewHolder.hocPhi = convertView.findViewById(R.id.hocphi);
            viewHolder.avatar = convertView.findViewById(R.id.avatarSV);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = sinhVien.get(position);
        viewHolder.tenSV.setText(sv.getHoTen());
        viewHolder.maSV.setText(sv.getMsv()+"");
        if (sv.getAnh().isEmpty()){
            if (sv.getGioiTinh().equals("Nam")){
                viewHolder.avatar.setImageResource(R.drawable.student);
            }else {
                viewHolder.avatar.setImageResource(R.drawable.student_girl);
            }
        }else{
            Glide.with(context.getBaseContext()).load(sv.getAnh()).into(viewHolder.avatar);
        }
        if (sv.getMonHoc().isEmpty()){
            viewHolder.hocPhi.setText("0");
        }else{
            long sum = 0;
            for (SinhVienMonHoc sinhVienMonHoc : sv.getMonHoc()){
                sum += sinhVienMonHoc.getHocPhi();
            }
            viewHolder.hocPhi.setText(sum/1000+"");
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
                sinhVien = (ArrayList<SinhVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
