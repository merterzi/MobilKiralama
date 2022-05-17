package com.mrtrz.mobilkiralama.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.type.DateTime;

import java.util.Date;

public class Car implements Entity {
    public String documentId;
    public String downloadUrl;
    public String aciklama;
    public long fiyat;
    public long modelYili;
    public long km;
    public long motorGucu;
    public long motorHacmi;
    public String marka;
    public String yakit;



    public Car(String documentId, String downloadUrl, String aciklama, long fiyat) {
        this.documentId = documentId;
        this.downloadUrl = downloadUrl;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
    }

    public Car(String downloadUrl, String aciklama, long fiyat, long modelYili, long km, long motorGucu, long motorHacmi, String marka, String yakit) {
        this.downloadUrl = downloadUrl;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
        this.modelYili = modelYili;
        this.km = km;
        this.motorGucu = motorGucu;
        this.motorHacmi = motorHacmi;
        this.marka = marka;
        this.yakit = yakit;
    }
}
