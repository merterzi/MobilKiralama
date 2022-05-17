package com.mrtrz.mobilkiralama.model;

public class Shop implements Entity{
    public String downloadUrl;
    public long fiyat;
    public String aciklama;
    public long metrekare;
    public long binaYasi;
    public String documentId;

    public Shop(String documentId, String downloadUrl, long fiyat, String aciklama) {
        this.documentId = documentId;
        this.downloadUrl = downloadUrl;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
    }

    public Shop(String documentId, String downloadUrl, long fiyat, String aciklama, long metrekare, long binaYasi) {
        this.downloadUrl = downloadUrl;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.metrekare = metrekare;
        this.binaYasi = binaYasi;
        this.documentId = documentId;
    }
}
