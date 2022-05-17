package com.mrtrz.mobilkiralama;

import androidx.annotation.FloatRange;
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
import com.mrtrz.mobilkiralama.databinding.ActivityAnasayfaBinding;

public class AnasayfaActivity extends AppCompatActivity {

    private ActivityAnasayfaBinding binding;
    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnasayfaBinding.inflate(getLayoutInflater());
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
            Intent intentToAddAdvert = new Intent(AnasayfaActivity.this, AddAdvertActivity.class);
            startActivity(intentToAddAdvert);
        }
        else if(item.getItemId() == R.id.anasayfa){
            Intent intentToAnasayfa = new Intent(AnasayfaActivity.this, AnasayfaActivity.class);
            startActivity(intentToAnasayfa);
        }
        else if(item.getItemId() == R.id.profil){
            Intent intentToProfil = new Intent(AnasayfaActivity.this, ProfilActivity.class);
            startActivity(intentToProfil);
        }
        else if(item.getItemId() == R.id.kaydedilen_ilanlar){
            Intent intentToSavedAdverts = new Intent(AnasayfaActivity.this, SavedAdvertsActivity.class);
            startActivity(intentToSavedAdverts);
        }
        else if(item.getItemId() == R.id.cikis_yap){
            auth.signOut();

            Intent intentToSignOut = new Intent(AnasayfaActivity.this, MainActivity.class);
            startActivity(intentToSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToHouse(View view){
        HouseAdvertFragment houseAdvertFragment = new HouseAdvertFragment();
        fragmentTransaction.replace(R.id.adverts_fragment, houseAdvertFragment).commit();

        binding.konutImageView.setVisibility(View.GONE);
        binding.arabaImageView.setVisibility(View.GONE);
        binding.dukkanImageView.setVisibility(View.GONE);
        binding.aramaImageView.setVisibility(View.GONE);
    }
    public void goToCar(View view){
        CarAdvertFragment carAdvertFragment = new CarAdvertFragment();
        fragmentTransaction.replace(R.id.adverts_fragment, carAdvertFragment).commit();

        binding.konutImageView.setVisibility(View.GONE);
        binding.arabaImageView.setVisibility(View.GONE);
        binding.dukkanImageView.setVisibility(View.GONE);
        binding.aramaImageView.setVisibility(View.GONE);
    }
    public void goToShop(View view){
        ShopAdvertFragment shopAdvertFragment = new ShopAdvertFragment();
        fragmentTransaction.replace(R.id.adverts_fragment, shopAdvertFragment).commit();

        binding.konutImageView.setVisibility(View.GONE);
        binding.arabaImageView.setVisibility(View.GONE);
        binding.dukkanImageView.setVisibility(View.GONE);
        binding.aramaImageView.setVisibility(View.GONE);
    }
    public void goToSearch(View view){
        SearchFragment searchFragment = new SearchFragment();
        fragmentTransaction.replace(R.id.adverts_fragment, searchFragment).commit();

        binding.konutImageView.setVisibility(View.GONE);
        binding.arabaImageView.setVisibility(View.GONE);
        binding.dukkanImageView.setVisibility(View.GONE);
        binding.aramaImageView.setVisibility(View.GONE);
    }
}
























