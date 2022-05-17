package com.mrtrz.mobilkiralama;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.adapter.ShopAdapter;
import com.mrtrz.mobilkiralama.databinding.FragmentCarAdvertBinding;
import com.mrtrz.mobilkiralama.databinding.FragmentShopAdvertBinding;
import com.mrtrz.mobilkiralama.model.Shop;

import java.util.ArrayList;
import java.util.Map;

public class ShopAdvertFragment extends Fragment {

    private FragmentShopAdvertBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayList<Shop> shopArrayList;
    ShopAdapter shopAdapter;
    String enAzYas, enFazlaYas, enAzFiyat, enFazlaFiyat, enAzMetrekare, enFazlaMetrekare;

    public ShopAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        shopArrayList = new ArrayList<>();

        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopAdvertBinding.inflate(inflater, container, false);
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
        shopAdapter = new ShopAdapter(shopArrayList);
        binding.recyclerView.setAdapter(shopAdapter);

        binding.shopSiralamaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOrder(view);
            }
        });

        binding.shopFiyatArtanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.ASCENDING);
                binding.shopSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.shopFiyatAzalanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.DESCENDING);
                binding.shopSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.shopTarihEskiRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.ASCENDING);
                binding.shopSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.shopTarihYeniRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.DESCENDING);
                binding.shopSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.shopFiltreleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterButton();
            }
        });

        binding.fiyatAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiyatAraButton();
            }
        });

        binding.metrekareAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metrekareAraButton();
            }
        });

        binding.yasAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yasAraButton();
            }
        });

        binding.filtreleriTemizleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilters();
            }
        });
    }

    private void getData(){
        shopArrayList.clear();
        firestore.collection("Shops").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(error != null){
                   Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
               }

               if(value != null){
                   for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                       Map<String, Object> data = documentSnapshot.getData();
                       String downloadUrl = (String) data.get("downloadurl");
                       long fiyat = (Long) data.get("fiyat");
                       String aciklama = (String) data.get("aciklama");
                       String documentId = documentSnapshot.getId();

                       Shop shop = new Shop(documentId, downloadUrl, fiyat, aciklama);
                       shopArrayList.add(shop);
                   }
                   shopAdapter.notifyDataSetChanged();
               }
            }
        });
    }

    private void chooseOrder(View view){
        binding.shopSiralamaSecenekleri.setVisibility(View.VISIBLE);
        binding.shopFiyatArtanRadioButton.setChecked(false);
        binding.shopFiyatAzalanRadioButton.setChecked(false);
        binding.shopTarihYeniRadioButton.setChecked(false);
        binding.shopTarihEskiRadioButton.setChecked(false);
    }

    private void getOrderingData(String field, Query.Direction direction){
        shopArrayList.clear();
        firestore.collection("Shops").orderBy(field, direction).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        long fiyat = (Long) data.get("fiyat");
                        String aciklama = (String) data.get("aciklama");
                        String documentId = documentSnapshot.getId();

                        Shop shop = new Shop(documentId, downloadUrl, fiyat, aciklama);
                        shopArrayList.add(shop);
                    }
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void filterButton(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.filtrelemeLinearLayout.setVisibility(View.VISIBLE);
    }

    private void getFilterData(String field, long enAz, long enFazla){
        shopArrayList.clear();
        firestore.collection("Shops").whereGreaterThan(field, enAz)
                .whereLessThan(field, enFazla).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Map<String, Object> data = documentSnapshot.getData();
                        String downloadUrl = (String) data.get("downloadurl");
                        long fiyat = (Long) data.get("fiyat");
                        String aciklama = (String) data.get("aciklama");
                        String documentId = documentSnapshot.getId();

                        Shop shop = new Shop(documentId, downloadUrl, fiyat, aciklama);
                        shopArrayList.add(shop);
                    }
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void yasAraButton(){
        if(!(binding.enAzYasText.getText().toString().matches("") && binding.enFazlaYasText.getText().toString().matches(""))){
            enAzYas = binding.enAzYasText.getText().toString();
            enFazlaYas = binding.enFazlaYasText.getText().toString();
            getFilterData("binayasi", Long.parseLong(enAzYas), Long.parseLong(enFazlaYas));
        }
        else{
            shopArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void fiyatAraButton(){
        if(!(binding.enAzFiyatText.getText().toString().matches("") && binding.enFazlaFiyatText.getText().toString().matches(""))){
            enAzFiyat = binding.enAzFiyatText.getText().toString();
            enFazlaFiyat = binding.enFazlaFiyatText.getText().toString();
            getFilterData("fiyat", Long.parseLong(enAzFiyat), Long.parseLong(enFazlaFiyat));
        }
        else{
            shopArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void metrekareAraButton(){
        if(!(binding.enAzMetrekareText.getText().toString().matches("") && binding.enFazlaMetrekareText.getText().toString().matches(""))){
            enAzMetrekare = binding.enAzMetrekareText.getText().toString();
            enFazlaMetrekare = binding.enFazlaMetrekareText.getText().toString();
            getFilterData("metrekare", Long.parseLong(enAzMetrekare), Long.parseLong(enFazlaMetrekare));
        }
        else{
            shopArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void clearFilters(){
        getData();

        binding.enAzMetrekareText.setText("");
        binding.enAzFiyatText.setText("");
        binding.enAzYasText.setText("");
        binding.enFazlaMetrekareText.setText("");
        binding.enFazlaFiyatText.setText("");
        binding.enFazlaYasText.setText("");

        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }
}















