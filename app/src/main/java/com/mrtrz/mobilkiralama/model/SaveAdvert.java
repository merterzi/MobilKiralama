package com.mrtrz.mobilkiralama.model;

public class SaveAdvert {

    public String documentId;
    public String email;

    public SaveAdvert(String documentId, String email) {
        this.documentId = documentId;
        this.email = email;
    }
    public SaveAdvert(String documentId) {
        this.documentId = documentId;
    }
}
