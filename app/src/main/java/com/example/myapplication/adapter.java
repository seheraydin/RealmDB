package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//24: adapter sınıfımız BaseAdapter dan kalıtılmalı
public class adapter extends BaseAdapter {
    //25: KisiBilgileri tipinde 1 tane list lazım,
    List<KisiBilgileri> list;
    //26: oluşturduğumuz layoutu çağırmak için 1 tane context lazım
    Context context;

    //27:constructor oluştur
    public adapter(List<KisiBilgileri> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //28: count listSize döndürmeli
    @Override
    public int getCount() {
        return list.size();
    }

    //29: Item listPosition döndürmeli
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //30: View geriye view döndürmeli
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //31 view ımızı tanımlyalım
        convertView = LayoutInflater.from(context).inflate(R.layout.layout, parent, false);
        TextView isim = convertView.findViewById(R.id.idTxtLKullaniciIsmi);
        TextView sifre = convertView.findViewById(R.id.idTxtLKullaniciSifre);
        TextView kullaniciAdi = convertView.findViewById(R.id.idTxtLKullaniciAdi);
        TextView cinsiyet = convertView.findViewById(R.id.idTxtLKullaniciCinsiyet);

        //32: 31 de tanımladığımız nesnelere listenin döönen elemanlarını teker teker set etmemiz gerek
        isim.setText(list.get(position).getIsim());
        sifre.setText(list.get(position).getSifre());
        kullaniciAdi.setText(list.get(position).getKullanici());
        cinsiyet.setText(list.get(position).getCinsiyet());

        //30: View geriye view döndürmeli
        return convertView;
    }
}
