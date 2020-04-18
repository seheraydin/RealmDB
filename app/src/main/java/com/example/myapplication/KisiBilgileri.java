package com.example.myapplication;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

//6:Realm anotationunu tanımla
@RealmClass

//5: oluşturulan KisiBilgileri classı Realm objesinden kalıtılsın
public class KisiBilgileri extends RealmObject {
    //7: xml dosyamızda tanımladıklarımızdan veritabanına eklenecek olanları tanımlayalım:
    private String kullanici;
    private String sifre;
    private String isim;
    private String cinsiyet;


    //8:set ve get metotlarını ooluştur

    public String getKullanici() {
        return kullanici;
    }

    public void setKullanici(String kullanici) {
        this.kullanici = kullanici;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }


    //9: log ekranında verilerin görüntülenebilmesi için toString metodunu oluştur

    @Override
    public String toString() {
        return "KisiBilgileri{" +
                "kullanici='" + kullanici + '\'' +
                ", sifre='" + sifre + '\'' +
                ", isim='" + isim + '\'' +
                '}';
    }
}
