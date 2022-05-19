package com.mrtrz.mobilkiralama.model;

import java.util.ArrayList;

public class House implements Entity{
    public String documentId;
    public String downloadUrl;
    public String aciklama;
    public long fiyat;
    public long metrekare;
    public String odaSayisi;
    public long kat;
    public long binaYasi;
    public String tapuDurumu;


    public House(String documentId, String downloadUrl, String aciklama, long fiyat) {
        this.documentId = documentId;
        this.downloadUrl = downloadUrl;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
    }

    public House(String downloadUrl, String aciklama, long fiyat, long metrekare, String odaSayisi, long kat, long binaYasi, String tapuDurumu) {
        this.downloadUrl = downloadUrl;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
        this.metrekare = metrekare;
        this.odaSayisi = odaSayisi;
        this.kat = kat;
        this.binaYasi = binaYasi;
        this.tapuDurumu = tapuDurumu;
    }
}
