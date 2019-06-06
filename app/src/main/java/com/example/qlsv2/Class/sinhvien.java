package com.example.qlsv2.Class;


public class sinhvien {
    String idSV;
    String mssv;
    String hotenSV;
    String msLopCN;
    String cohoc;
    String tongbuoi;
     Boolean  check;

    public sinhvien(String idSV, String mssv, String hotenSV, String msLopCN, String cohoc, String tongbuoi, Boolean check) {
        this.idSV = idSV;
        this.mssv = mssv;
        this.hotenSV = hotenSV;
        this.msLopCN = msLopCN;
        this.cohoc = cohoc;
        this.tongbuoi = tongbuoi;
        this.check = check;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getCohoc() {
        return cohoc;
    }

    public void setCohoc(String cohoc) {
        this.cohoc = cohoc;
    }

    public String getTongbuoi() {
        return tongbuoi;
    }

    public void setTongbuoi(String tongbuoi) {
        this.tongbuoi = tongbuoi;
    }

    public String getIdSV() {
        return idSV;
    }

    public void setIdSV(String idSV) {
        this.idSV = idSV;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getHotenSV() {
        return hotenSV;
    }

    public void setHotenSV(String hotenSV) {
        this.hotenSV = hotenSV;
    }


    public String getMsLopCN() {
        return msLopCN;
    }

    public void setMsLopCN(String msLopCN) {
        this.msLopCN = msLopCN;
    }

    public Boolean isChecked() {
        return check;
    }
}
