package com.mrtrz.mobilkiralama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mrtrz.mobilkiralama.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(MainActivity.this, AnasayfaActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void girisKayit(View view){
        Intent intent = new Intent(this, GirisKayitOl.class);
        startActivity(intent);
        finish();
    }

    public void kayitsiz(View view){
        Intent intent = new Intent(this, AnasayfaActivity.class);
        startActivity(intent);
        finish();
    }
}