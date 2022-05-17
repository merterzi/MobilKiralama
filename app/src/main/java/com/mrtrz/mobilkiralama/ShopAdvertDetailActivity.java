package com.mrtrz.mobilkiralama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.databinding.ActivityHouseAdvertDetailBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityShopAdvertDetailBinding;
import com.mrtrz.mobilkiralama.model.SaveAdvert;
import com.mrtrz.mobilkiralama.model.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopAdvertDetailActivity extends AppCompatActivity {

    private ActivityShopAdvertDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;
    String documentId;
    String email;
    ArrayList<SaveAdvert> saveAdvertArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopAdvertDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        documentId = intent.getStringExtra("document");

        getData();

        binding.ilanKaydetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAdvert(view);
            }
        });

        saveAdvertArrayList = new ArrayList<>();

        saveAdsControl();


    }

    private void getData(){
        firestore.collection("Shops").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ShopAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        long metrekare = (Long) data.get("metrekare");
                        long binaYasi = (Long) data.get("binayasi");
                        long fiyat = (Long) data.get("fiyat");
                        String aciklama = (String) data.get("aciklama");
                        String email = (String) data.get("email");
                        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    Toast.makeText(ShopAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }

                                if(value != null){
                                    for(DocumentSnapshot documentSnapshot2 : value.getDocuments()){
                                        Map<String, Object> userData = documentSnapshot2.getData();
                                        String telefon = (String) userData.get("telefon");
                                        String ad = (String) userData.get("ad");
                                        String soyad = (String) userData.get("soyad");

                                        binding.telefonTextView.setText(telefon);
                                        binding.adSoyadTextView.setText(ad + " " + soyad);
                                    }
                                }
                            }
                        });

                        Shop shop = new Shop(documentId, downloadUrl, fiyat, aciklama, metrekare, binaYasi);

                        binding.metrekareTextView.setText(String.valueOf(shop.metrekare));
                        binding.binaYasiTextView.setText(String.valueOf(shop.binaYasi));
                        binding.fiyatTextView.setText(String.valueOf(shop.fiyat));
                        binding.aciklamaText.setText(shop.aciklama);
                        Picasso.get().load(downloadUrl).into(binding.shopImagView);

                    }
                }
            }
        });
    }

    private void saveAdvert(View view){
        user = auth.getCurrentUser();
        email = user.getEmail();

        HashMap<String, Object> advertData = new HashMap<>();
        advertData.put("email", email);
        advertData.put("documentid", documentId);

        firestore.collection("SaveAds").add(advertData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ShopAdvertDetailActivity.this, "İlan Kaydedildi", Toast.LENGTH_SHORT).show();
                binding.ilanKaydetButton.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShopAdvertDetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAdsControl(){
        saveAdvertArrayList.clear();
        user = auth.getCurrentUser();
        firestore.collection("SaveAds").whereEqualTo("documentid", documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ShopAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if(value != null){
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String documentId2 = (String) data.get("documentid");
                        String email2 = (String) data.get("email");
                        SaveAdvert saveAdvert = new SaveAdvert(documentId2, email2);
                        saveAdvertArrayList.add(saveAdvert);
                    }
                }

                for(SaveAdvert saveAdvert : saveAdvertArrayList){
                    if(saveAdvert.email.equals(user.getEmail())){
                        binding.ilanKaydetButton.setText("İlan Zaten Kayıtlı");
                        binding.ilanKaydetButton.setEnabled(false);
                        break;
                    }
                }
            }
        });


    }
}









