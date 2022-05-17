package com.mrtrz.mobilkiralama;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;
import com.mrtrz.mobilkiralama.adapter.HouseAdapter;
import com.mrtrz.mobilkiralama.databinding.FragmentHouseAdvertBinding;
import com.mrtrz.mobilkiralama.model.House;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HouseAdvertFragment extends Fragment {

    private FragmentHouseAdvertBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayList<House> houseArrayList;
    HouseAdapter houseAdapter;
    String enAzYas, enFazlaYas, enAzFiyat, enFazlaFiyat, enAzKat, enFazlaKat, enAzMetrekare, enFazlaMetrekare;

    public HouseAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        houseArrayList = new ArrayList<>();
        getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHouseAdvertBinding.inflate(inflater, container, false);
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

        binding.houseSiralamaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOrder(view);
            }
        });

        binding.houseTarihYeniRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.DESCENDING);
                binding.houseSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.houseTarihEskiRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.ASCENDING);
                binding.houseSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.houseFiyatAzalanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.DESCENDING);
                binding.houseSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.houseFiyatArtanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.ASCENDING);
                binding.houseSiralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.houseFiltreleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterButton();
            }
        });

        binding.yasAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yasAraButton();
            }
        });

        binding.katAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                katAraButton();
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

        binding.filtreleriTemizleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilters();
            }
        });
    }

    private void getData(){
        houseArrayList.clear();
        firestore.collection("Houses").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void chooseOrder(View view){
        binding.houseSiralamaSecenekleri.setVisibility(View.VISIBLE);
        binding.houseFiyatArtanRadioButton.setChecked(false);
        binding.houseFiyatAzalanRadioButton.setChecked(false);
        binding.houseTarihEskiRadioButton.setChecked(false);
        binding.houseTarihYeniRadioButton.setChecked(false);
    }

    private void getOrderingData(String field, Query.Direction direction){
        houseArrayList.clear();
        firestore.collection("Houses").orderBy(field, direction).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getFilterData(String field, long enAz, long enFazla){
        houseArrayList.clear();
        firestore.collection("Houses").whereGreaterThan(field, enAz)
                .whereLessThan(field, enFazla).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void filterButton(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.filtrelemeLinearLayout.setVisibility(View.VISIBLE);
    }

    private void yasAraButton(){
        if(!(binding.enAzYasText.getText().toString().matches("") && binding.enFazlaYasText.getText().toString().matches(""))){
            enAzYas = binding.enAzYasText.getText().toString();
            enFazlaYas = binding.enFazlaYasText.getText().toString();
            getFilterData("binayasi", Long.parseLong(enAzYas), Long.parseLong(enFazlaYas));
        }
        else{
            houseArrayList.clear();
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
            houseArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void katAraButton(){
        if(!(binding.enAzKatText.getText().toString().matches("") && binding.enFazlaKatText.getText().toString().matches(""))){
            enAzKat = binding.enAzKatText.getText().toString();
            enFazlaKat = binding.enFazlaKatText.getText().toString();
            getFilterData("kat", Long.parseLong(enAzKat), Long.parseLong(enFazlaKat));
        }
        else{
            houseArrayList.clear();
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
            houseArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void clearFilters(){
        getData();

        binding.enAzMetrekareText.setText("");
        binding.enAzKatText.setText("");
        binding.enAzFiyatText.setText("");
        binding.enAzYasText.setText("");
        binding.enFazlaMetrekareText.setText("");
        binding.enFazlaKatText.setText("");
        binding.enFazlaFiyatText.setText("");
        binding.enFazlaYasText.setText("");

        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }
}








