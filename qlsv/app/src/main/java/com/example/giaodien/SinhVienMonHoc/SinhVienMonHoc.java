package com.example.giaodien.SinhVienMonHoc;

import java.io.Serializable;

public class SinhVienMonHoc implements Serializable {
    private String maMon;
    private String tenMon;
    private double diemHe10;
    private int soTinChi;
    private double hocPhi;

    public SinhVienMonHoc(){

    }

    public SinhVienMonHoc(String maMon, String tenMon, double diemHe10, int soTinChi, double hocPhi) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.diemHe10 = diemHe10;
        this.soTinChi = soTinChi;
        this.hocPhi = hocPhi;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public double getDiemHe10() {
        return diemHe10;
    }

    public void setDiemHe10(double diemHe10) {
        this.diemHe10 = diemHe10;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public double getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(double hocPhi) {
        this.hocPhi = hocPhi;
    }
}
