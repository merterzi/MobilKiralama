package com.mrtrz.mobilkiralama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.mrtrz.mobilkiralama.databinding.ActivitySavedAdvertsBinding;
import com.mrtrz.mobilkiralama.model.Car;
import com.mrtrz.mobilkiralama.model.Entity;
import com.mrtrz.mobilkiralama.model.House;
import com.mrtrz.mobilkiralama.model.SaveAdvert;
import com.mrtrz.mobilkiralama.model.Shop;

import java.util.ArrayList;
import java.util.Map;

public class SavedAdvertsActivity extends AppCompatActivity {

    private ActivitySavedAdvertsBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<House> houseArrayList;
    ArrayList<Car> carArrayList;
    ArrayList<Shop> shopArrayList;
    ArrayList<SaveAdvert> saveAdvertArrayList;
    HouseAdapter houseAdapter;
    CarAdapter carAdapter;
    ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedAdvertsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        saveAdvertArrayList = new ArrayList<>();
        houseArrayList = new ArrayList<>();
        carArrayList = new ArrayList<>();
        shopArrayList = new ArrayList<>();

        getSavedHouseAdverts();
        getSavedShopAdverts();
        getSavedCarAdverts();

        binding.kayitliKonutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        houseAdapter = new HouseAdapter(houseArrayList);
        binding.kayitliKonutRecyclerView.setAdapter(houseAdapter);

        binding.kayitliAracRecylerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(carArrayList);
        binding.kayitliAracRecylerView.setAdapter(carAdapter);

        binding.kayitliDukkanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new ShopAdapter(shopArrayList);
        binding.kayitliDukkanRecyclerView.setAdapter(shopAdapter);
    }

    private void getSavedHouseAdverts(){
        firestore.collection("SaveAds").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String documentId = (String) data.get("documentid");
                        firestore.collection("Houses").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!= null){
                                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                                if(value != null){
                                    for(DocumentSnapshot documentSnapshot2 : value.getDocuments()){
                                        Map<String, Object> data = documentSnapshot2.getData();
                                        String downloadUrl = (String) data.get("downloadurl");
                                        String aciklama = (String) data.get("aciklama");
                                        long fiyat = (Long) data.get("fiyat");
                                        String documentId2 = documentSnapshot2.getId();
                                        House house = new House(documentId2, downloadUrl, aciklama, fiyat);
                                        houseArrayList.add(house);
                                    }
                                    houseAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getSavedCarAdverts(){
        firestore.collection("SaveAds").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String documentId = (String) data.get("documentid");
                        firestore.collection("Cars").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!= null){
                                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                                if(value != null){
                                    for(DocumentSnapshot documentSnapshot2 : value.getDocuments()){
                                        Map<String, Object> data = documentSnapshot2.getData();
                                        String downloadUrl = (String) data.get("downloadurl");
                                        String aciklama = (String) data.get("aciklama");
                                        long fiyat = (Long) data.get("fiyat");
                                        String documentId2 = documentSnapshot2.getId();
                                        Car car = new Car(documentId2, downloadUrl, aciklama, fiyat);
                                        carArrayList.add(car);
                                    }
                                    carAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getSavedShopAdverts(){
        firestore.collection("SaveAds").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String documentId = (String) data.get("documentid");
                        firestore.collection("Shops").whereEqualTo(FieldPath.documentId(), documentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!= null){
                                    Toast.makeText(SavedAdvertsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                                if(value != null){
                                    for(DocumentSnapshot documentSnapshot2 : value.getDocuments()){
                                        Map<String, Object> data = documentSnapshot2.getData();
                                        String downloadUrl = (String) data.get("downloadurl");
                                        String aciklama = (String) data.get("aciklama");
                                        long fiyat = (Long) data.get("fiyat");
                                        String documentId2 = documentSnapshot2.getId();
                                        Shop shop = new Shop(documentId2, downloadUrl, fiyat, aciklama);
                                        shopArrayList.add(shop);
                                    }
                                    shopAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}












