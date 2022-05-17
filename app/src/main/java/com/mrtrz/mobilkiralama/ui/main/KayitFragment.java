package com.mrtrz.mobilkiralama.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtrz.mobilkiralama.AnasayfaActivity;
import com.mrtrz.mobilkiralama.GirisKayitOl;
import com.mrtrz.mobilkiralama.MainActivity;
import com.mrtrz.mobilkiralama.R;
import com.mrtrz.mobilkiralama.databinding.FragmentGirisBinding;
import com.mrtrz.mobilkiralama.databinding.FragmentKayitBinding;

import java.util.HashMap;

public class KayitFragment extends Fragment {

    PageViewModel pageViewModel;
    private FragmentKayitBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;

    public static KayitFragment newInstance(){
        return new KayitFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = new ViewModelProvider(requireActivity()).get(PageViewModel.class);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKayitBinding.inflate(inflater, container, false);
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

        //Kayıt Olma İşlevi
        binding.buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kayitOl(view);
            }
        });

    }

    public void kayitOl(View view){
        String ad = binding.adEditText.getText().toString();
        String soyad = binding.soyadEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String telefon = binding.telefonEditText2.getText().toString();
        String sifre = binding.sifreEditText.getText().toString();

        if(ad.equals("") || soyad.equals("") || email.equals("") || sifre.equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Alanları boş bırakmayınız", Toast.LENGTH_LONG).show();
        }
        else{
            auth.createUserWithEmailAndPassword(email, sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("ad", ad);
                    userData.put("soyad", soyad);
                    userData.put("telefon", telefon);
                    userData.put("email", email);

                    firestore.collection("Users").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), AnasayfaActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}














