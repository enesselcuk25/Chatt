package com.enes.chattapp.models;

public class kullanicilar {


    private String dogumtarih;
    private String egitim;
    private  String hakkinda;
    private String isim ;
    private String resim;
    private Object state;

    public kullanicilar(){

    }



    public kullanicilar(String dogumtarih, String egitim, String hakkımda, String isim, String resim, Object state) {
        this.dogumtarih = dogumtarih;
        this.egitim = egitim;
        this.hakkinda = hakkımda;
        this.isim = isim;
        this.resim = resim;
        this.state = state;
    }

    public String getDogumtarih() {
        return dogumtarih;
    }

    public void setDogumtarih(String dogumtarih) {
        this.dogumtarih = dogumtarih;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkinda() {
        return hakkinda;
    }

    public void setHakkinda(String hakkinda) {
        this.hakkinda = hakkinda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "kullanicilar{" +
                "dogumtarih='" + dogumtarih + '\'' +
                ", egitim='" + egitim + '\'' +
                ", hakkinda='" + hakkinda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }
}
