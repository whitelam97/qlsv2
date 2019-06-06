package com.example.qlsv2.Class;

public class tkb_tuan {
    private String tenlopHP;
    private String tenPhong;
    private String thu;
    private String tietBD;
    private String sotiet;
    private String ht;

    public tkb_tuan(String tenlopHP, String tenPhong,String thu, String tietBD, String sotiet,String ht) {
        this.tenlopHP = tenlopHP;
        this.tenPhong = tenPhong;
        this.thu=thu;
        this.tietBD = tietBD;
        this.sotiet = sotiet;
        this.ht = ht;

    }

    public String getHt() {
        return ht;
    }

    public void setHt(String ht) {
        this.ht = ht;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getTenlopHP() {
        return tenlopHP;
    }

    public void setTenlopHP(String tenlopHP) {
        this.tenlopHP = tenlopHP;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getTietBD() {
        return tietBD;
    }

    public void setTietBD(String tietBD) {
        this.tietBD = tietBD;
    }

    public String getSotiet() {
        return sotiet;
    }

    public void setSotiet(String sotiet) {
        this.sotiet = sotiet;
    }
}
