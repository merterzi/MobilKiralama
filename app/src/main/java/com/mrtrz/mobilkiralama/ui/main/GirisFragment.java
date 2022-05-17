package com.mrtrz.mobilkiralama.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderKt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mrtrz.mobilkiralama.AnasayfaActivity;
import com.mrtrz.mobilkiralama.GirisKayitOl;
import com.mrtrz.mobilkiralama.MainActivity;
import com.mrtrz.mobilkiralama.R;
import com.mrtrz.mobilkiralama.databinding.ActivityMainBinding;
import com.mrtrz.mobilkiralama.databinding.FragmentGirisBinding;

public class GirisFragment extends Fragment {

    PageViewModel pageViewModel;
    private FragmentGirisBinding binding;
    private FirebaseAuth auth;

    public static GirisFragment newInstance(){
        return new GirisFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = new ViewModelProvider(requireActivity()).get(PageViewModel.class);

        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGirisBinding.inflate(inflater, container, false);
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

        //Verileri alma işlemi
        binding.buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girisYap(view);
            }
        });

    }

    public void girisYap(View view){

        String email = binding.emailEditText.getText().toString();
        String sifre = binding.sifreEditText.getText().toString();

        if(email.equals("") || sifre.equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Alanları boş bırakmayınız", Toast.LENGTH_LONG).show();
        }
        else{
            auth.signInWithEmailAndPassword(email, sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
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


    }


}