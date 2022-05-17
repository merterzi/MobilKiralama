package com.mrtrz.mobilkiralama;

import android.os.Binder;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.adapter.CarAdapter;
import com.mrtrz.mobilkiralama.databinding.FragmentCarAdvertBinding;
import com.mrtrz.mobilkiralama.databinding.FragmentCarBinding;
import com.mrtrz.mobilkiralama.databinding.FragmentHouseAdvertBinding;
import com.mrtrz.mobilkiralama.model.Car;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class CarAdvertFragment extends Fragment {

    private FragmentCarAdvertBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayList<Car> carArrayList;
    CarAdapter carAdapter;
    String enAzFiyat, enFazlaFiyat, enAzKm, enFazlaKm, enAzModel, enFazlaModel, enAzMotorGucu, enFazlaMotorGucu, enAzMotorHacmi, enFazlaMotorHacmi;

    public CarAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        carArrayList = new ArrayList<>();

        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCarAdvertBinding.inflate(inflater, container, false);
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
        carAdapter = new CarAdapter(carArrayList);
        binding.recyclerView.setAdapter(carAdapter);

        binding.siralamaButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOrder(view);
            }
        });

        binding.fiyatArtanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.ASCENDING);
                binding.siralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.fiyatAzalanRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("fiyat", Query.Direction.DESCENDING);
                binding.siralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.tarihEskiRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.ASCENDING);
                binding.siralamaSecenekleri.setVisibility(View.GONE);
            }
        });
        binding.tarihYeniRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOrderingData("tarih", Query.Direction.DESCENDING);
                binding.siralamaSecenekleri.setVisibility(View.GONE);
            }
        });

        binding.filtreleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterButton(view);
            }
        });

        binding.fiyatAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiyatAraButton(view);
            }
        });

        binding.kmAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kmAraButton(view);
            }
        });

        binding.modelAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelAraButton(view);
            }
        });

        binding.motorGucuAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motorGucuAraButton(view);
            }
        });

        binding.motorHacmiAraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motorHacmiAraButton(view);
            }
        });

        binding.filtreleriTemizleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilters();
            }
        });
    }

    private void getData() {
        carArrayList.clear();
        firestore.collection("Cars").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
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

    private void chooseOrder(View view) {
        binding.siralamaSecenekleri.setVisibility(view.VISIBLE);
        binding.fiyatAzalanRadioButton.setChecked(false);
        binding.fiyatArtanRadioButton.setChecked(false);
        binding.tarihYeniRadioButton.setChecked(false);
        binding.tarihEskiRadioButton.setChecked(false);
    }

    private void getOrderingData(String field, Query.Direction direction) {
        carArrayList.clear();
        firestore.collection("Cars").orderBy(field, direction).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
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

    private void filterButton(View view) {
        binding.filtrelemeLinearLayout.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void getFilterData(String field, long enAzDeger, long enFazlaDeger) {
        carArrayList.clear();
        firestore.collection("Cars").whereGreaterThan(field, enAzDeger)
                .whereLessThan(field, enFazlaDeger)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }

                        if (value != null) {
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
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

    private void fiyatAraButton(View view) {
        if (!(binding.enAzFiyatText.getText().toString().matches("") && binding.enFazlaFiyatText.getText().toString().matches(""))) {
            enAzFiyat = binding.enAzFiyatText.getText().toString();
            enFazlaFiyat = binding.enFazlaFiyatText.getText().toString();
            getFilterData("fiyat", Long.parseLong(enAzFiyat), Long.parseLong(enFazlaFiyat));
        }
        else{
            carArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void kmAraButton(View view){
        if(!(binding.enAzKmText.getText().toString().matches("") && binding.enFazlaKmText.getText().toString().matches(""))){
            enAzKm = binding.enAzKmText.getText().toString();
            enFazlaKm = binding.enFazlaKmText.getText().toString();
            getFilterData("km", Long.parseLong(enAzKm), Long.parseLong(enFazlaKm));
        }
        else{
            carArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void modelAraButton(View view){
        if(!(binding.enAzModelText.getText().toString().matches("") && binding.enFazlaModelText.getText().toString().matches(""))){
            enAzModel = binding.enAzModelText.getText().toString();
            enFazlaModel = binding.enFazlaModelText.getText().toString();
            getFilterData("modelyili", Long.parseLong(enAzModel), Long.parseLong(enFazlaModel));
        }
        else{
            carArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void motorGucuAraButton(View view){
        enAzMotorGucu = binding.enAzMotorGucuText.getText().toString();
        enFazlaMotorGucu = binding.enFazlaMotorGucuText.getText().toString();
        if(!(binding.enAzMotorGucuText.getText().toString().matches("") && binding.enFazlaMotorGucuText.getText().toString().matches(""))){
            enAzModel = binding.enAzMotorGucuText.getText().toString();
            enFazlaModel = binding.enFazlaMotorGucuText.getText().toString();
            getFilterData("motorgucu", Long.parseLong(enAzMotorGucu), Long.parseLong(enFazlaMotorGucu));
        }
        else{
            carArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void motorHacmiAraButton(View view){
        enAzMotorHacmi = binding.enAzMotorHacmiText.getText().toString();
        enFazlaMotorHacmi = binding.enFazlaMotorHacmiText.getText().toString();
        if(!(binding.enAzMotorHacmiText.getText().toString().matches("") && binding.enFazlaMotorHacmiText.getText().toString().matches(""))){
            enAzModel = binding.enAzMotorHacmiText.getText().toString();
            enFazlaModel = binding.enFazlaMotorHacmiText.getText().toString();
            getFilterData("motorhacmi", Long.parseLong(enAzMotorHacmi), Long.parseLong(enFazlaMotorHacmi));
        }
        else{
            carArrayList.clear();
            getData();
            Toast.makeText(getActivity().getApplicationContext(), "Alanlar boş bırakıldığı için filtreleme yapılamadı!!!", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }

    private void clearFilters(){
        getData();

        binding.enAzFiyatText.setText("");
        binding.enAzKmText.setText("");
        binding.enAzMotorHacmiText.setText("");
        binding.enAzModelText.setText("");
        binding.enAzMotorGucuText.setText("");
        binding.enFazlaMotorHacmiText.setText("");
        binding.enFazlaModelText.setText("");
        binding.enFazlaMotorGucuText.setText("");
        binding.enFazlaKmText.setText("");
        binding.enFazlaFiyatText.setText("");

        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.filtrelemeLinearLayout.setVisibility(View.GONE);
    }
}














