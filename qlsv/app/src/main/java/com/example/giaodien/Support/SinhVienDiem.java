package com.example.giaodien.Support;

import java.io.Serializable;

public class SinhVienDiem implements Serializable {
    private long msv;
    private Diem diem;

    public SinhVienDiem(){

    }

    public SinhVienDiem(long msv, Diem diem) {
        this.msv = msv;
        this.diem = diem;
    }

    public long getMsv() {
        return msv;
    }

    public void setMsv(long msv) {
        this.msv = msv;
    }

    public Diem getDiem() {
        return diem;
    }

    public void setDiem(Diem diem) {
        this.diem = diem;
    }
}
