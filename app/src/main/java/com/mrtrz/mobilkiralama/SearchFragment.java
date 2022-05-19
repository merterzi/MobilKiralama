package com.mrtrz.mobilkiralama;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.adapter.CarAdapter;
import com.mrtrz.mobilkiralama.adapter.HouseAdapter;
import com.mrtrz.mobilkiralama.adapter.ShopAdapter;
import com.mrtrz.mobilkiralama.databinding.FragmentSearchBinding;
import com.mrtrz.mobilkiralama.model.Car;
import com.mrtrz.mobilkiralama.model.House;
import com.mrtrz.mobilkiralama.model.Shop;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayList<House> houseArrayList;
    ArrayList<Car> carArrayList;
    ArrayList<Shop> shopArrayList;
    CarAdapter carAdapter;
    ShopAdapter shopAdapter;
    HouseAdapter houseAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        houseArrayList = new ArrayList<>();
        carArrayList = new ArrayList<>();
        shopArrayList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        houseAdapter = new HouseAdapter(houseArrayList);
        binding.recyclerView.setAdapter(houseAdapter);


        binding.searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                binding.konutAraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getHouseData(editable.toString());

                        houseAdapter = new HouseAdapter(houseArrayList);
                        binding.recyclerView.setAdapter(houseAdapter);
                    }
                });

                binding.arabaAraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCarData(editable.toString());

                        carAdapter = new CarAdapter(carArrayList);
                        binding.recyclerView.setAdapter(carAdapter);
                    }
                });

                binding.dKkanAraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getShopData(editable.toString());

                        shopAdapter = new ShopAdapter(shopArrayList);
                        binding.recyclerView.setAdapter(shopAdapter);
                    }
                });

            }
        });



    }

    private void getHouseData(String keyWord){
        houseArrayList.clear();
        firestore.collection("Houses").whereEqualTo("keyword", keyWord).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    private void getCarData(String keyWord){
        carArrayList.clear();
        firestore.collection("Cars").whereEqualTo("keyword", keyWord).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    private void getShopData(String keyWord){
        shopArrayList.clear();
        firestore.collection("Shops").whereEqualTo("keyword", keyWord).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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





















