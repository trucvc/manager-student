package com.example.giaodien.Khoa;

import java.io.Serializable;

public class Khoa implements Serializable {
    private Long MaKhoa;
    private String TenKhoa;
    private String Email;
    private String DiaChi;
    private String SDT;

    public Khoa(Long maKhoa, String tenKhoa, String email, String diaChi, String SDT) {
        MaKhoa = maKhoa;
        TenKhoa = tenKhoa;
        Email = email;
        DiaChi = diaChi;
        this.SDT = SDT;
    }

    public Khoa() {
    }

    public Object getMaKhoa() {
        return MaKhoa;
    }

    public void setMaKhoa(Long maKhoa) {
        MaKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return TenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        TenKhoa = tenKhoa;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
}
