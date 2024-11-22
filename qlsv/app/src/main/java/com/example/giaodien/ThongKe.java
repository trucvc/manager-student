package com.example.giaodien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.giaodien.SinhVien.SinhVien;
import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThongKe extends AppCompatActivity {
    PieChart pieChart;
    Spinner thongKe;
    ArrayList<SinhVien> arrayList;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference sinhVienData = databaseReference.child("danhSachSinhVien");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        mapping();

        arrayList = new ArrayList<>();
        loadDataSinhVien();

        ArrayList<String> luaChon = new ArrayList<>();
        luaChon.add("Tạo biểu đồ");
        luaChon.add("Sinh Viên");
        luaChon.add("Giới tính");
        luaChon.add("Quê quán");
        luaChon.add("Năm sinh");
        luaChon.add("GPA");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ThongKe.this, android.R.layout.simple_spinner_item, luaChon);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thongKe.setAdapter(arrayAdapter);

        thongKe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<PieEntry> arrayPie = new ArrayList<>();
                if (luaChon.get(position).equals("Sinh Viên")){
                    arrayPie.clear();
                    HashMap<String,Integer> pie = new HashMap<>();
                    for (SinhVien sv : arrayList){
                        pie.put(sv.getKhoaHoc(),1);
                    }
                    for (String k : pie.keySet()){
                        int count = 0;
                        for (SinhVien sv : arrayList){
                            if (k.equals(sv.getKhoaHoc())){
                                count++;
                            }
                        }
                        pie.replace(k,count);
                    }
                    for (Map.Entry<String,Integer> entry : pie.entrySet()){
                        arrayPie.add(new PieEntry(entry.getValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(arrayPie,"Khóa học");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextSize(10);

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateXY(1000, 1000);
                }else if (luaChon.get(position).equals("Giới tính")){
                    arrayPie.clear();
                    HashMap<String,Integer> pie = new HashMap<>();
                    for (SinhVien sv : arrayList){
                        pie.put(sv.getGioiTinh(),1);
                    }
                    for (String k : pie.keySet()){
                        int count = 0;
                        for (SinhVien sv : arrayList){
                            if (k.equals(sv.getGioiTinh())){
                                count++;
                            }
                        }
                        pie.replace(k,count);
                    }
                    for (Map.Entry<String,Integer> entry : pie.entrySet()){
                        arrayPie.add(new PieEntry(entry.getValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(arrayPie,"Giới tính");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextSize(10);

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateXY(1000, 1000);
                }else if (luaChon.get(position).equals("Quê quán")){
                    arrayPie.clear();
                    HashMap<String,Integer> pie = new HashMap<>();
                    for (SinhVien sv : arrayList){
                        pie.put(sv.getQueQuan(),1);
                    }
                    for (String k : pie.keySet()){
                        int count = 0;
                        for (SinhVien sv : arrayList){
                            if (k.equals(sv.getQueQuan())){
                                count++;
                            }
                        }
                        pie.replace(k,count);
                    }
                    for (Map.Entry<String,Integer> entry : pie.entrySet()){
                        arrayPie.add(new PieEntry(entry.getValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(arrayPie,"Quê quán");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextSize(10);

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateXY(1000, 1000);
                }else if (luaChon.get(position).equals("Năm sinh")){
                    arrayPie.clear();
                    HashMap<String,Integer> pie = new HashMap<>();
                    for (SinhVien sv : arrayList){
                        pie.put(sv.getNgaySinh().substring(sv.getNgaySinh().length()-4),1);
                    }
                    for (String k : pie.keySet()){
                        int count = 0;
                        for (SinhVien sv : arrayList){
                            if (k.equals(sv.getNgaySinh().substring(sv.getNgaySinh().length()-4))){
                                count++;
                            }
                        }
                        pie.replace(k,count);
                    }
                    for (Map.Entry<String,Integer> entry : pie.entrySet()){
                        arrayPie.add(new PieEntry(entry.getValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(arrayPie,"Năm sinh");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextSize(10);

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateXY(1000, 1000);
                }else if (luaChon.get(position).equals("GPA")){
                    arrayPie.clear();
                    HashMap<String,Integer> pie = new HashMap<>();
                    for (SinhVien sv : arrayList){
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
                            double gpa4 = Math.floor(sum/stc * 100) / 100;
                            if (gpa4 >= 3.6){
                                pie.put("Xuất sắc",1);
                            } else if (gpa4 >= 3.2) {
                                pie.put("Giỏi",1);
                            } else if (gpa4 >= 2.5) {
                                pie.put("Khá",1);
                            }else if (gpa4 >= 2) {
                                pie.put("Trung bình",1);
                            }else{
                                pie.put("Yếu",1);
                            }
                        }
                    }
                    for (String k : pie.keySet()){
                        int count = 0;
                        double gpa = 0;
                        for (SinhVien sv : arrayList){
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
                                double gpa4 = Math.floor(sum/stc * 100) / 100;
                                String xl = "";
                                if (gpa4 >= 3.6){
                                    xl = "Xuất sắc";
                                } else if (gpa4 >= 3.2) {
                                    xl = "Giỏi";
                                } else if (gpa4 >= 2.5) {
                                    xl = "Khá";
                                }else if (gpa4 >= 2) {
                                    xl = "Trung bình";
                                }else{
                                    xl = "Yếu";
                                }
                                if (k.equals(xl)){
                                    count++;
                                }
                            }
                        }
                        pie.replace(k,count);
                    }
                    for (Map.Entry<String,Integer> entry : pie.entrySet()){
                        arrayPie.add(new PieEntry(entry.getValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(arrayPie,"GPA");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextSize(10);

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateXY(1000, 1000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.thongke);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            }else if(item.getItemId() == R.id.thongke){
                return true;
            } else if (item.getItemId() == R.id.hocphi) {
                startActivity(new Intent(getApplicationContext(),HocPhi.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                return true;
            }else if (item.getItemId() == R.id.caidat){
                startActivity(new Intent(getApplicationContext(),Setting.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void mapping(){
        pieChart = findViewById(R.id.piechart);
        thongKe = findViewById(R.id.luaChon);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void loadDataSinhVien(){
        sinhVienData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    SinhVien sv = data.getValue(SinhVien.class);
                    ArrayList<SinhVienMonHoc> mh = new ArrayList<>();
                    for (DataSnapshot dataMH : data.child("danhSachMon").getChildren()){
                        SinhVienMonHoc sinhVienMonHoc = dataMH.getValue(SinhVienMonHoc.class);
                        if (sinhVienMonHoc != null){
                            mh.add(sinhVienMonHoc);
                        }
                    }
                    sv.setMonHoc(mh);
                    if (sv != null){
                        arrayList.add(sv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}