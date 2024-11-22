package com.example.giaodien.Support;

import java.io.Serializable;

public class Diem implements Serializable {
    private double diemCC;
    private double diemGK;
    private double diemCK;
    private double diemHe10;

    public Diem(){

    }

    public Diem(double diemCC, double diemGK, double diemCK, double diemHe10) {
        this.diemCC = diemCC;
        this.diemGK = diemGK;
        this.diemCK = diemCK;
        this.diemHe10 = diemHe10;
    }

    public double getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(double diemCC) {
        this.diemCC = diemCC;
    }

    public double getDiemGK() {
        return diemGK;
    }

    public void setDiemGK(double diemGK) {
        this.diemGK = diemGK;
    }

    public double getDiemCK() {
        return diemCK;
    }

    public void setDiemCK(double diemCK) {
        this.diemCK = diemCK;
    }

    public double getDiemHe10() {
        return diemHe10;
    }

    public void setDiemHe10(double diemHe10) {
        this.diemHe10 = diemHe10;
    }
}
