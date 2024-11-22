package com.example.giaodien.ActivityMonHoc.SinhVien;

import com.example.giaodien.SinhVien.SinhVien;

import java.io.Serializable;

public class MonHocSinhVien implements Serializable {
    private Long Msv;

    private SinhVien sinhVien;

    private String maMon;

    private Long maKhoa;

    String tenMonHoc;

    Long soTinChi;

    private DiemMonHoc diemMonHocChiTiet;

    public Long getMsv() {
        return Msv;
    }

    public void setMsv(Long msv) {
        Msv = msv;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public DiemMonHoc getDiemMonHocChiTiet() {
        return diemMonHocChiTiet;
    }

    public void setDiemMonHocChiTiet(DiemMonHoc diemMonHocChiTiet) {
        this.diemMonHocChiTiet = diemMonHocChiTiet;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public Long getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(Long maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public Long getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Long soTinChi) {
        this.soTinChi = soTinChi;
    }
}
