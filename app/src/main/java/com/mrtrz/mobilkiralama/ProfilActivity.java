package com.mrtrz.mobilkiralama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.adapter.CarAdapter;
import com.mrtrz.mobilkiralama.adapter.HouseAdapter;
import com.mrtrz.mobilkiralama.adapter.ShopAdapter;
import com.mrtrz.mobilkiralama.databinding.ActivityAnasayfaBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityProfilBinding;
import com.mrtrz.mobilkiralama.model.Car;
import com.mrtrz.mobilkiralama.model.House;
import com.mrtrz.mobilkiralama.model.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    private ActivityProfilBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<House> houseArrayList;
    ArrayList<Car> carArrayList;
    ArrayList<Shop> shopArrayList;
    HouseAdapter houseAdapter;
    CarAdapter carAdapter;
    ShopAdapter shopAdapter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        houseArrayList = new ArrayList<>();
        carArrayList = new ArrayList<>();
        shopArrayList = new ArrayList<>();

        getUserData();
        getHouseData();
        getCarData();
        getShopData();

        binding.profilKonutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        houseAdapter = new HouseAdapter(houseArrayList);
        binding.profilKonutRecyclerView.setAdapter(houseAdapter);

        binding.profilAracRecylerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(carArrayList);
        binding.profilAracRecylerView.setAdapter(carAdapter);

        binding.profilDukkanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new ShopAdapter(shopArrayList);
        binding.profilDukkanRecyclerView.setAdapter(shopAdapter);
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
            Intent intentToAddAdvert = new Intent(ProfilActivity.this, AddAdvertActivity.class);
            startActivity(intentToAddAdvert);
        }
        else if(item.getItemId() == R.id.anasayfa){
            Intent intentToAnasayfa = new Intent(ProfilActivity.this, AnasayfaActivity.class);
            startActivity(intentToAnasayfa);
        }
        else if(item.getItemId() == R.id.profil){
            Intent intentToProfil = new Intent(ProfilActivity.this, ProfilActivity.class);
            startActivity(intentToProfil);
        }
        else if(item.getItemId() == R.id.kaydedilen_ilanlar){
            Intent intentToSavedAdverts = new Intent(ProfilActivity.this, SavedAdvertsActivity.class);
            startActivity(intentToSavedAdverts);
        }
        else if(item.getItemId() == R.id.cikis_yap){
            auth.signOut();

            Intent intentToSignOut = new Intent(ProfilActivity.this, MainActivity.class);
            startActivity(intentToSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserData(){
        firestore.collection("Users").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ProfilActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for(DocumentSnapshot documentSnapshot2 : value.getDocuments()){
                        Map<String, Object> userData = documentSnapshot2.getData();
                        String telefon = (String) userData.get("telefon");
                        String ad = (String) userData.get("ad");
                        String soyad = (String) userData.get("soyad");

                        binding.profilAdSoyadTextView.setText(ad + " " + soyad);
                        binding.profilTelefonTextView.setText(telefon);
                    }
                }
            }
        });
    }

    private void getHouseData(){

        firestore.collection("Houses").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ProfilActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot: value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        String aciklama = (String) data.get("aciklama");
                        long fiyat = (Long) data.get("fiyat");
                        String documentId = documentSnapshot.getId();

                        House house = new House(documentId, downloadUrl, aciklama, fiyat);
                        houseArrayList.add(house);

                    }
                    houseAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getCarData(){
        firestore.collection("Cars").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ProfilActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot: value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        String aciklama = (String) data.get("aciklama");
                        long fiyat = (Long) data.get("fiyat");
                        String documentId = documentSnapshot.getId();

                        Car car = new Car(documentId, downloadUrl, aciklama, fiyat);
                        carArrayList.add(car);

                    }
                    carAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getShopData(){
        firestore.collection("Shops").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(ProfilActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot: value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        String aciklama = (String) data.get("aciklama");
                        long fiyat = (Long) data.get("fiyat");
                        String documentId = documentSnapshot.getId();

                        Shop shop = new Shop(documentId, downloadUrl, fiyat, aciklama);
                        shopArrayList.add(shop);

                    }
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
























