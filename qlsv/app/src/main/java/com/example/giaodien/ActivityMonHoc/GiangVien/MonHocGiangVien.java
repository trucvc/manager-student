package com.example.giaodien.ActivityMonHoc.GiangVien;

import com.example.giaodien.GiangVien.GiangVien;

import java.io.Serializable;

public class MonHocGiangVien implements Serializable {
    private Long MaGV;

    private GiangVien giangVien;

    private String maMon;

    private Long maKhoa;

    public Long getMaGV() {
        return MaGV;
    }

    public void setMaGV(Long maGV) {
        MaGV = maGV;
    }

    public GiangVien getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
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
}
