package com.example.giaodien.GiangVien;

import java.io.Serializable;

public class GiangVien implements Serializable {
    private Long MaGV;
    private String HoTen;
    private String SDT;
    private String QueQuan;
    private String Email;
    private String Anh;
    private String GioiTinh;
    private String NgaySinh;

    public GiangVien() {

    }

    public GiangVien(long MaGV, String hoTen, String SDT, String queQuan, String email, String anh, String gioiTinh, String ngaySinh) {
        this.MaGV = MaGV;
        this.HoTen = hoTen;
        this.SDT = SDT;
        QueQuan = queQuan;
        Email = email;
        Anh = anh;
        GioiTinh = gioiTinh;
        NgaySinh = ngaySinh;
    }


    public Long getMaGV() {
        return MaGV;
    }

    public void setMaGV(Long maGV) {
        MaGV = maGV;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getQueQuan() {
        return QueQuan;
    }

    public void setQueQuan(String queQuan) {
        QueQuan = queQuan;
    }


    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }
}
