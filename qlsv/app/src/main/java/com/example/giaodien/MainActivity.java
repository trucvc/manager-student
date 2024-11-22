package com.example.giaodien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.giaodien.GiangVien.GiangVienDanhSach;
import com.example.giaodien.Khoa.KhoaDanhSach;
import com.example.giaodien.Khoa.SinhVien.SinhVienDanhSach;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    BarAdapter barAdapter;
    ArrayList<Bar> arrayList;
    BottomNavigationView bottomNavigationView;
    TextView ten, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        arrayList = new ArrayList<>();
        arrayList.add(new Bar("Sinh Viên",R.drawable.student));
        arrayList.add(new Bar("Khoa",R.drawable.faculty));
        arrayList.add(new Bar("Giảng Viên",R.drawable.teacher));
        barAdapter = new BarAdapter(this,R.layout.gridview_bar,arrayList);
        gridView.setAdapter(barAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SinhVienDanhSach.class);
                String strName = arrayList.get(position).getName();
                if("Giảng Viên".equals(strName)){
                    intent.setClass(MainActivity.this, GiangVienDanhSach.class);
                } else if ("Khoa".equals(strName)) {
                    intent.setClass(MainActivity.this, KhoaDanhSach.class);
                }

                startActivity(intent);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                return true;
            }else if(item.getItemId() == R.id.thongke){
                startActivity(new Intent(getApplicationContext(),ThongKe.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
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

        userInfo();
    }

    private void userInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            if (name.isEmpty()){
                ten.setVisibility(View.GONE);
            }else {
                ten.setText(name);
            }
            mail.setText(email);
        }
    }

    private void mapping(){
        gridView = findViewById(R.id.gridViewBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ten = findViewById(R.id.ten);
        mail = findViewById(R.id.tk);
    }

}