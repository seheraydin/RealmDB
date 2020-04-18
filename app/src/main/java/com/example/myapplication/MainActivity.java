package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmResults;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //1: realm nesnesi oluştur
    Realm realm;

    //10 : View nesnelerimizi oluşturalım
    EditText kullaniciAdi, sifre, isim;
    RadioGroup cinsiyetGrup;
    //55: guncelle butonunun nesnesi oluştur
    Button button, guncelleBtn;
    ListView listView;
    //58: pozisyon oluştur
    Integer positionG = 0;

    //60:devam.. fonksiyon içerisindekilerin nesnesini oluştur
    String isimText, kullaniciAdiText, sifreText, cinsiyetText;

    //59: devam ..listemizi tanımlayalım
    RealmResults<KisiBilgileri> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //3:realm fonk. çağır
        realmTanimla();
        //12: tanimla fonk.
        tanimla();
        //21: DB ye ekleme fonk
        ekle();
        //34: goster
        goster();
        //37: listview da tıklanan item in pozisyonunu bul
        pozisyonBul();

    }

    //NOT:EĞER REALM KISMINDA HATA ÇIKARSA
    //MyApplicatioN.java DOSYASINDA REALM VERİTABANININ NAME İNİ DEĞİŞTİR

    //2: Realmi tanımlayalım
    public void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    //11: view tanımlamarını yapalım
    public void tanimla() {
        kullaniciAdi = findViewById(R.id.idTxtKadi);
        sifre = findViewById(R.id.idTxtSifre);
        isim = findViewById(R.id.idTxtIsim);
        cinsiyetGrup = findViewById(R.id.idRdnGrpCinsiyet);
        button = findViewById(R.id.idBtnKayitOl);
        //56: güncelle butonu tanıma
        guncelleBtn = findViewById(R.id.idBtnGuncelle);
        listView = findViewById(R.id.idLstView);
    }

    public void ekle() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bilgileriAl();
                //20: yaz fonk çalıştır
                yaz(cinsiyetText, isimText, kullaniciAdiText, sifreText);
                goster();

                //24:butona tıklayınca textlerin içerisi temizlensin
                kullaniciAdi.setText("");
                isim.setText("");
                sifre.setText("");


            }
        });


        //57: güncelle butonuna tıklandığında hangi (DB)pozisyona tıklandığını bulur ve setini yapalım
        guncelleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriTabanindanListeGetir();
                final KisiBilgileri kisi = list.get(positionG);
                bilgileriAl();
                //62:şimdiset etme işlemini(db deki elemanların get ile alınmasından sonra viewlara yazılma işlemine set denir)yapalım
                //set işlemini transaction içerisine yazalım ki bir bütün olarak set edilsin
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //set
                        kisi.setKullanici(kullaniciAdiText);
                        kisi.setSifre(sifreText);
                        kisi.setIsim(isimText);
                        kisi.setCinsiyet(cinsiyetText);

                    }
                });
                goster();


            }
        });
    }

    //19:set işlemlerine göre bu fonksiyon 4 parametre alır
    public void yaz(final String cinsiyet, final String isim, final String kullaniciAdi, final String sifre) {
        //15: onsucces ve onerror metotlarını ekle başarılı kayıt olursa ve olmazsa ona göre toast basmak için
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //17:tablo classımızın nesnesini oluşturalım
                KisiBilgileri kisiBilgileri = realm.createObject(KisiBilgileri.class);
                //18:set işlmelerini yap, parantez içini boş bırak
                //parantez içi 19 numaralı işlemden sonra yaz
                kisiBilgileri.setCinsiyet(cinsiyet);
                kisiBilgileri.setIsim(isim);
                kisiBilgileri.setSifre(sifre);
                kisiBilgileri.setKullanici(kullaniciAdi);
            }
            //16: köşeli parantezden sonra virgül koyunca new dedikten sonra gerekli şeyle çıkar
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "başarılı", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "başarısız", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //22: eklenen elemanları gössterelim
    public void goster() {
        veriTabanindanListeGetir();
        //33: listemizi şartla gösterebiliriz
        if (list.size() >= 0) {
            adapter adapter = new adapter(list, getApplicationContext());
            listView.setAdapter(adapter);
        }
    }

    //35: Listedeki elemanın pozisyon değerini bulma ve silme
    //listede tıklanan itemdan ilgili pozisyondaki elemanı bulsun
    public void pozisyonBul() {
        //52: 52 numaradan itibaren güncelleme işlemleri
        //52:veri tabanımızdaki kayıtların bulunduğu listemizi oluşturalım
        veriTabanindanListeGetir();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //36: pozisyonu log ekranında bastıralım
                // Log.i("pozisyonGelen",""+position); //string gibi okunması için + ile str birleştirme yaptık
                //39 : ilgili pozisyondaki elemanı silelim
                //sil(position);
                //50: hangi item a tıklarsak alert dialog açılacak
                ac(position);

                //53: veri tabanından edit textlerimize verileri set edelim(get getir-oku, set yaz)
                //list.get(position).getKullanici()-->veritabanından bu bilgileri getir
                //kullaniciAdi.setText-->kullanıcı adına view ına ekle
                //Not: eğer veri tabanından değer getirmek stiyorsam get etmem lazım
                //eğer ekrandaki araçlara değerleri almak istiyorsam set etmem lazım
                kullaniciAdi.setText(list.get(position).getKullanici());
                sifre.setText(list.get(position).getSifre());
                isim.setText(list.get(position).getIsim());

                if (list.get(position).getCinsiyet().equals("Erkek")) {
                    ((RadioButton) cinsiyetGrup.getChildAt(0)).setChecked(true);
                } else {
                    ((RadioButton) cinsiyetGrup.getChildAt(1)).setChecked(true);
                }
                //58:listview'ın hangi item ına tıklandıysa bunun posiitionuna ata
                positionG = position;


            }

        });
    }

    //38 listeden elemanı sil
    public void sil(final int position) {
        //kontrol: pozisyon değerlerini basalım
        //Log.i("name",""+position);

        //40: DB deki kayıtları bir tane sonuç listesine atmamız lazım
        veriTabanindanListeGetir();

        //kontrol:listede tıklanan elemanın kullanıcı adını basalım
        //Log.i("name","liste eleman sayısı: "+gelenList.get(position).getKullanici());

        //41: gelenList içerisindeki seçilen pozisyonu başka bir nesneye at ve bbu nesneyi de realm den sil
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                KisiBilgileri kisi = list.get(position);
                kisi.deleteFromRealm();

                //42: goster fonksiyonunda listenin son halini gösteriyor.
                //silme işleminden sonra liste tekrar eleman sayısını hesaplasın ki son hali görünsün
                //o yüzden goster metodunu tekrar çağırdık
                goster();

                kullaniciAdi.setText("");
                isim.setText("");
                sifre.setText("");
                ((RadioButton) cinsiyetGrup.getChildAt(0)).setChecked(false);
                ((RadioButton) cinsiyetGrup.getChildAt(1)).setChecked(false);
            }
        });
    }

    //44: silmek istediğinizden emin misiniz diye bir tane alert dialog oluşturalım
    public void ac(final int position) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alert, null);

        //46 View larımızı tanımlayalım
        Button btnEvet = view.findViewById(R.id.idBtnEvet);
        Button btnHayir = view.findViewById(R.id.idBtnHayir);
        //45:aşağıdaki kod iç içe class kullanacağımızı belirtir
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //47: alert nesnemize bir tane view set ermeliyiz ve viewın seçili olmamasını sağlayalım
        alert.setView(view);
        alert.setCancelable(false);

        //48: alert dialoğumuzu gerçekleştirelim
        final AlertDialog dialog = alert.create();
        btnEvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //49: sil fonk. çağır ve dialoğu kapat
                sil(position);
                dialog.cancel();

            }
        });
        //51: hayıra basınca dialog kapansın
        btnHayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    //TEKRARLAYAN İŞLEMLERİ FONKSİYON OLARAK YAPALIM
    public void bilgileriAl() {
        //60: view elemanından verileri alalım
        isimText = isim.getText().toString();
        kullaniciAdiText = kullaniciAdi.getText().toString();
        sifreText = sifre.getText().toString();
        //61: cinsiyet text ini almamız gerekir bunun için hangi radio butona tıklanmış onun id sini almalıyız
        Integer id = cinsiyetGrup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(id);
        cinsiyetText = radioButton.getText().toString();

    }

    //TEKRARLAYAN İŞLEMLERİ FONKSİYON OLARAK YAPALIM
    public void veriTabanindanListeGetir() {
        //59:güncelle butonuna tıklayınca bizim veri tabanındaki kayıtları bir listeye atsın
        list = realm.where(KisiBilgileri.class).findAll();
    }


}
