package com.mrtrz.mobilkiralama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mrtrz.mobilkiralama.databinding.ActivityAddAdvertBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityMainBinding;

public class AddAdvertActivity extends AppCompatActivity {

    private ActivityAddAdvertBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdvertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
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
            Intent intentToAddAdvert = new Intent(AddAdvertActivity.this, AddAdvertActivity.class);
            startActivity(intentToAddAdvert);
        }
        else if(item.getItemId() == R.id.anasayfa){
            Intent intentToAnasayfa = new Intent(AddAdvertActivity.this, AnasayfaActivity.class);
            startActivity(intentToAnasayfa);
        }
        else if(item.getItemId() == R.id.profil){
            Intent intentToProfil = new Intent(AddAdvertActivity.this, ProfilActivity.class);
            startActivity(intentToProfil);
        }
        else if(item.getItemId() == R.id.kaydedilen_ilanlar){
            Intent intentToSavedAdverts = new Intent(AddAdvertActivity.this, SavedAdvertsActivity.class);
            startActivity(intentToSavedAdverts);
        }
        else if(item.getItemId() == R.id.cikis_yap){
            auth.signOut();

            Intent intentToSignOut = new Intent(AddAdvertActivity.this, MainActivity.class);
            startActivity(intentToSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToHouse(View view){

        HouseFragment houseFragment = new HouseFragment();
        fragmentTransaction.add(R.id.frgament_linearlayout, houseFragment).commit();

        binding.arabaButton.setVisibility(View.GONE);
        binding.konutButton.setVisibility(View.GONE);
        binding.dukkanButton.setVisibility(View.GONE);
    }
    public void goToCar(View view){

        CarFragment carFragment = new CarFragment();
        fragmentTransaction.add(R.id.frgament_linearlayout, carFragment).commit();

        binding.arabaButton.setVisibility(View.GONE);
        binding.konutButton.setVisibility(View.GONE);
        binding.dukkanButton.setVisibility(View.GONE);

    }
    public void goToShop(View view){

        ShopFragment shopFragment = new ShopFragment();
        fragmentTransaction.add(R.id.frgament_linearlayout, shopFragment).commit();

        binding.arabaButton.setVisibility(View.GONE);
        binding.konutButton.setVisibility(View.GONE);
        binding.dukkanButton.setVisibility(View.GONE);
    }
}