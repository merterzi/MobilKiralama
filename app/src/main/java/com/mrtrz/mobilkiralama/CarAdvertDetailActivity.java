package com.mrtrz.mobilkiralama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
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
import com.mrtrz.mobilkiralama.databinding.ActivityCarAdvertDetailBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityHouseAdvertDetailBinding;
import com.mrtrz.mobilkiralama.model.Car;
import com.mrtrz.mobilkiralama.model.SaveAdvert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarAdvertDetailActivity extends AppCompatActivity {

    private ActivityCarAdvertDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    String email;
    String documentId;
    ArrayList<SaveAdvert> saveAdvertArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarAdvertDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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
            Intent intentToAddAdvert = new Intent(CarAdvertDetailActivity.this, AddAdvertActivity.class);
            startActivity(intentToAddAdvert);
        }
        else if(item.getItemId() == R.id.anasayfa){
            Intent intentToAnasayfa = new Intent(CarAdvertDetailActivity.this, AnasayfaActivity.class);
            startActivity(intentToAnasayfa);
        }
        else if(item.getItemId() == R.id.profil){
            Intent intentToProfil = new Intent(CarAdvertDetailActivity.this, ProfilActivity.class);
            startActivity(intentToProfil);
        }
        else if(item.getItemId() == R.id.kaydedilen_ilanlar){
            Intent intentToSavedAdverts = new Intent(CarAdvertDetailActivity.this, SavedAdvertsActivity.class);
            startActivity(intentToSavedAdverts);
        }
        else if(item.getItemId() == R.id.cikis_yap){
            auth.signOut();

            Intent intentToSignOut = new Intent(CarAdvertDetailActivity.this, MainActivity.class);
            startActivity(intentToSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        firestore.collection("Cars").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(CarAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        String aciklama = (String) data.get("aciklama");
                        long fiyat = (Long) data.get("fiyat");
                        long modelYili = (Long) data.get("modelyili");
                        long km = (Long) data.get("km");
                        long motorGucu = (Long) data.get("motorgucu");
                        long motorHacmi = (Long) data.get("motorhacmi");
                        String marka = (String) data.get("marka");
                        String yakit = (String) data.get("yakit");
                        String email = (String) data.get("email");

                        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    Toast.makeText(CarAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }

                                if(value != null){
                                    for (DocumentSnapshot documentSnapshot2 : value.getDocuments()){
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

                        Car car = new Car(downloadUrl, aciklama, fiyat, modelYili, km, motorGucu, motorHacmi, marka, yakit);
                        binding.aciklamaText.setText(car.aciklama);
                        binding.fiyatTextView.setText(String.valueOf(car.fiyat));
                        binding.kmTextView.setText(String.valueOf(car.km));
                        binding.modelYiliTextView.setText(String.valueOf(car.modelYili));
                        binding.motorGCTextView.setText(String.valueOf(car.motorGucu));
                        binding.motorHacmiTextView.setText(String.valueOf(car.motorHacmi));
                        binding.markaTextView.setText(car.marka);
                        binding.yakitTextView.setText(car.yakit);

                        Picasso.get().load(downloadUrl).into(binding.carImagView);
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
                Toast.makeText(CarAdvertDetailActivity.this, "İlan Kaydedildi", Toast.LENGTH_SHORT).show();
                binding.ilanKaydetButton.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CarAdvertDetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CarAdvertDetailActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
















