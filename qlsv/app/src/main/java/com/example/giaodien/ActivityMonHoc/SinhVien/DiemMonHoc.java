package com.example.giaodien.ActivityMonHoc.SinhVien;

import java.io.Serializable;

public class DiemMonHoc implements Serializable {
    private Double DiemCC;
    private Double DiemGK;
    private Double DiemCK;
    private Double DiemHe10;

    private Long maKhoa;
    private String maMonHoc;
    private Long maSinhVien;

    public DiemMonHoc() {
    }

    public DiemMonHoc(Double diemCC, Double diemGK, Double diemCK, Double diemHe10) {
        DiemCC = diemCC;
        DiemGK = diemGK;
        DiemCK = diemCK;
        DiemHe10 = diemHe10;
    }

    public Double getDiemCC() {
        return DiemCC;
    }

    public void setDiemCC(Double diemCC) {
        DiemCC = diemCC;
    }

    public Double getDiemGK() {
        return DiemGK;
    }

    public void setDiemGK(Double diemGK) {
        DiemGK = diemGK;
    }

    public Double getDiemCK() {
        return DiemCK;
    }

    public void setDiemCK(Double diemCK) {
        DiemCK = diemCK;
    }

    public Double getDiemHe10() {
        return DiemHe10;
    }

    public void setDiemHe10(Double diemHe10) {
        DiemHe10 = diemHe10;
    }

    public Long getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(Long maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public Long getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(Long maSinhVien) {
        this.maSinhVien = maSinhVien;
    }
}
