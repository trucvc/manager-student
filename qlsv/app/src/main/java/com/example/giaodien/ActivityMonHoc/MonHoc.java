package com.example.giaodien.ActivityMonHoc;

import java.io.Serializable;

public class MonHoc implements Serializable {
    private String MaMon;
    private Long SoTinChi;
    private String TenMon;

    private String maKhoa;

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaMon() {
        return MaMon;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public Object getSoTinChi() {
        return SoTinChi;
    }

    public void setSoTinChi(Long soTinChi) {
        SoTinChi = soTinChi;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }
}
