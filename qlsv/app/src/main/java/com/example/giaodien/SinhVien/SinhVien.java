package com.example.giaodien.SinhVien;

import com.example.giaodien.SinhVienMonHoc.SinhVienMonHoc;

import java.io.Serializable;
import java.util.ArrayList;

public class SinhVien implements Serializable {
    private long msv;
    private String hoTen;
    private String sdt;
    private String queQuan;
    private String khoaHoc;
    private String email;
    private ArrayList<SinhVienMonHoc> monHoc;
    private String anh;
    private String gioiTinh;
    private String ngaySinh;

    public SinhVien(){

    }

    public SinhVien(long msv, String hoTen, String sdt, String queQuan, String khoaHoc, String email, ArrayList<SinhVienMonHoc> monHoc, String anh, String gioiTinh, String ngaySinh) {
        this.msv = msv;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.queQuan = queQuan;
        this.khoaHoc = khoaHoc;
        this.email = email;
        this.monHoc = monHoc;
        this.anh = anh;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public long getMsv() {
        return msv;
    }

    public void setMsv(long msv) {
        this.msv = msv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(String khoaHoc) {
        this.khoaHoc = khoaHoc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<SinhVienMonHoc> getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(ArrayList<SinhVienMonHoc> monHoc) {
        this.monHoc = monHoc;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
