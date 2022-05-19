package com.mrtrz.mobilkiralama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mrtrz.mobilkiralama.adapter.HouseAdapter;
import com.mrtrz.mobilkiralama.databinding.ActivityAnasayfaBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityHouseAdvertDetailBinding;
import com.mrtrz.mobilkiralama.model.House;
import com.mrtrz.mobilkiralama.model.SaveAdvert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HouseAdvertDetailActivity extends AppCompatActivity {

    private ActivityHouseAdvertDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    String documentId;
    String email;
    House house;
    ArrayList<SaveAdvert> saveAdvertArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHouseAdvertDetailBinding.inflate(getLayoutInflater());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.ilan_ekle){
            Intent intentToAddAdvert = new Intent(HouseAdvertDetailActivity.this, AddAdvertActivity.class);
            startActivity(intentToAddAdvert);
        }
        else if(item.getItemId() == R.id.anasayfa){
            Intent intentToAnasayfa = new Intent(HouseAdvertDetailActivity.this, AnasayfaActivity.class);
            startActivity(intentToAnasayfa);
        }
        else if(item.getItemId() == R.id.profil){
            Intent intentToProfil = new Intent(HouseAdvertDetailActivity.this, ProfilActivity.class);
            startActivity(intentToProfil);
        }
        else if(item.getItemId() == R.id.kaydedilen_ilanlar){
            Intent intentToSavedAdverts = new Intent(HouseAdvertDetailActivity.this, SavedAdvertsActivity.class);
            startActivity(intentToSavedAdverts);
        }
        else if(item.getItemId() == R.id.cikis_yap){
            auth.signOut();

            Intent intentToSignOut = new Intent(HouseAdvertDetailActivity.this, MainActivity.class);
            startActivity(intentToSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        firestore.collection("Houses").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(HouseAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        long metrekare = (Long) data.get("metrekare");
                        String odaSayisi = (String) data.get("odasayisi");
                        long kat = (Long) data.get("kat");
                        long binaYasi = (Long) data.get("binayasi");
                        String tapuDurumu = (String) data.get("tapu");
                        long fiyat = (Long) data.get("fiyat");
                        String aciklama = (String) data.get("aciklama");
                        String email = (String) data.get("email");

                        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(HouseAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }

                                if (value != null) {
                                    for (DocumentSnapshot documentSnapshot2 : value.getDocuments()) {
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


                        house = new House(downloadUrl, aciklama, fiyat, metrekare, odaSayisi, kat, binaYasi, tapuDurumu);

                        binding.aciklamaText.setText(house.aciklama);
                        binding.binaYasiTextView.setText(String.valueOf(house.binaYasi));
                        binding.fiyatTextView.setText(String.valueOf(house.fiyat));
                        binding.katTextView.setText(String.valueOf(house.kat));
                        binding.metrekareTextView.setText(String.valueOf(house.metrekare));
                        binding.odaSayisiTextView.setText(house.odaSayisi);
                        binding.tapuTextView.setText(house.tapuDurumu);
                        Picasso.get().load(downloadUrl).into(binding.houseImagView);

                        //tarih kısmını eklemeyi unutma

                    }


                }
            }
        });
    }

    private void saveAdvert(View view) {
        user = auth.getCurrentUser();
        email = user.getEmail();

        HashMap<String, Object> advertData = new HashMap<>();
        advertData.put("email", email);
        advertData.put("documentid", documentId);

        firestore.collection("SaveAds").add(advertData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(HouseAdvertDetailActivity.this, "İlan Kaydedildi", Toast.LENGTH_SHORT).show();
                binding.ilanKaydetButton.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HouseAdvertDetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAdsControl() {
        saveAdvertArrayList.clear();
        user = auth.getCurrentUser();
        firestore.collection("SaveAds").whereEqualTo("documentid", documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(HouseAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String documentId2 = (String) data.get("documentid");
                        String email2 = (String) data.get("email");
                        SaveAdvert saveAdvert = new SaveAdvert(documentId2, email2);
                        saveAdvertArrayList.add(saveAdvert);
                    }
                }

                for (SaveAdvert saveAdvert : saveAdvertArrayList) {
                    if (saveAdvert.email.equals(user.getEmail())) {
                        binding.ilanKaydetButton.setText("İlan Zaten Kayıtlı");
                        binding.ilanKaydetButton.setEnabled(false);
                        break;
                    }
                }
            }
        });
    }
}
















