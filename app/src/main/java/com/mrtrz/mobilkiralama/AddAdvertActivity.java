package com.mrtrz.mobilkiralama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.mrtrz.mobilkiralama.databinding.ActivityAddAdvertBinding;
import com.mrtrz.mobilkiralama.databinding.ActivityMainBinding;

public class AddAdvertActivity extends AppCompatActivity {

    private ActivityAddAdvertBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdvertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
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