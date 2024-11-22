package com.example.giaodien.ActivityMonHoc.SinhVien;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.giaodien.ActivityMonHoc.GiangVien.MonHocGiangVien;
import com.example.giaodien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MonHocSinhVienThemAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<MonHocSinhVien> monHocSinhVienArrayList;

    TextView tvMaSV;
    TextView tvTenSV;
    LinearLayout info;
    ImageView avatar;
    Button btnThem;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nganhHoc = databaseReference.child("nganhHoc");
    DatabaseReference danhSachSinhVien = databaseReference.child("danhSachSinhVien");

    public MonHocSinhVienThemAdapter(Activity context, int layout, ArrayList<MonHocSinhVien> monHocSinhVienArrayList) {
        this.context = context;
        this.layout = layout;
        this.monHocSinhVienArrayList = monHocSinhVienArrayList;
    }

    @Override
    public int getCount() {
        return monHocSinhVienArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return monHocSinhVienArrayList.get(position);
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
        info = convertView.findViewById(R.id.layoutSinhVien);
        avatar = convertView.findViewById(R.id.avatarSinhVien);
        btnThem = convertView.findViewById(R.id.btnThem);

        MonHocSinhVien monHocSinhVien = monHocSinhVienArrayList.get(position);
        tvTenSV.setText(monHocSinhVien.getSinhVien().getHoTen());
        tvMaSV.setText(monHocSinhVien.getMsv().toString());


        if (monHocSinhVien.getSinhVien().getAnh().isEmpty()) {
            if (monHocSinhVien.getSinhVien().getGioiTinh().equals("Nam")) {
                avatar.setImageResource(R.drawable.student);
            } else {
                avatar.setImageResource(R.drawable.student_girl);
            }
        } else {
            Glide.with(context.getBaseContext()).load(monHocSinhVien.getSinhVien().getAnh()).into(avatar);
        }

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context contextParent = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(contextParent);
                builder.setTitle("Thêm sinh viên trong môn học");
                builder.setMessage("Thêm sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học với mã sinh viên là " + monHocSinhVien.getMsv());
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nganhHoc.child(monHocSinhVien.getMaKhoa().toString()).child("danhSachMonHoc").child(monHocSinhVien.getMaMon()).child("danhSachSinhVien").child(monHocSinhVien.getMsv().toString()).child("msv").setValue(monHocSinhVien.getMsv()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(contextParent, "Thêm thành công sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(contextParent, "Thêm thất bại sinh viên " + monHocSinhVien.getSinhVien().getHoTen() + " trong môn học", Toast.LENGTH_SHORT);
                                }
                            }
                        });

                        //thêm điểm môn học
                        DiemMonHoc diemMonHoc = new DiemMonHoc((double) 0,(double) 0,(double) 0,(double) 0);
                        nganhHoc.child(monHocSinhVien.getMaKhoa().toString()).child("danhSachMonHoc").child(monHocSinhVien.getMaMon()).child("danhSachSinhVien").child(monHocSinhVien.getMsv().toString()).child("diemMonHoc").setValue(diemMonHoc);

                        //thêm môn học vào danh sách sinh viên
                        DatabaseReference danhSachSinhVienMonHoc = danhSachSinhVien.child(monHocSinhVien.getMsv().toString()).child("danhSachMon").child(monHocSinhVien.getMaMon());

                        danhSachSinhVienMonHoc.child("diemHe10").setValue(diemMonHoc.getDiemHe10());
                        danhSachSinhVienMonHoc.child("hocPhi").setValue(340000 * monHocSinhVien.getSoTinChi());
                        danhSachSinhVienMonHoc.child("maMon").setValue(monHocSinhVien.getMaMon());
                        danhSachSinhVienMonHoc.child("soTinChi").setValue(monHocSinhVien.getSoTinChi());
                        danhSachSinhVienMonHoc.child("tenMon").setValue(monHocSinhVien.getTenMonHoc());
                    }
                });
                builder.create().show();
            }
        });

        return convertView;
    }
}
