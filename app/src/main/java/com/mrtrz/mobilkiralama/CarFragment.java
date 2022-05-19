package com.mrtrz.mobilkiralama;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mrtrz.mobilkiralama.databinding.FragmentCarBinding;

import java.util.HashMap;
import java.util.UUID;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;
    private Uri imageData;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;

    public CarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerLauncher();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCarBinding.inflate(inflater, container, false);
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

        ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.araba_marka_array, android.R.layout.simple_spinner_item);
        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.markaSpinner.setAdapter(adapterBrand);

        ArrayAdapter<CharSequence> adapterFuel = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.yakit_tipi_array, android.R.layout.simple_spinner_item);
        adapterFuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yakitSpinner.setAdapter(adapterFuel);



        binding.arabaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCarImage(view);
            }
        });

        binding.ilanEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAdvert(view);
            }
        });
    }

    public void addAdvert(View view){
        if(imageData != null){
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = storage.getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            long modelYili = Long.parseLong(binding.modelYiliEditText.getText().toString());
                            long km = Long.parseLong(binding.kmEditText.getText().toString());
                            long motorGucu = Long.parseLong(binding.motorGCEditText.getText().toString());
                            long motorHacmi = Long.parseLong(binding.motorHacmiEditText.getText().toString());
                            String marka = binding.markaSpinner.getSelectedItem().toString();
                            String yakit = binding.yakitSpinner.getSelectedItem().toString();
                            long fiyat = Long.parseLong(binding.fiyatEditText.getText().toString());
                            String aciklama = binding.aciklamaEditTextTextMultiLine.getText().toString();
                            String keyWord = binding.keyWordEditText.getText().toString();
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();

                            HashMap<String, Object> advertData = new HashMap<>();
                            advertData.put("downloadurl", downloadUrl);
                            advertData.put("modelyili", modelYili);
                            advertData.put("km", km);
                            advertData.put("motorgucu", motorGucu);
                            advertData.put("motorhacmi", motorHacmi);
                            advertData.put("marka", marka);
                            advertData.put("yakit", yakit);
                            advertData.put("fiyat", fiyat);
                            advertData.put("aciklama", aciklama);
                            advertData.put("keyword", keyWord);
                            advertData.put("tarih", FieldValue.serverTimestamp());
                            advertData.put("email", email);

                            firestore.collection("Cars").add(advertData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), AnasayfaActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

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

    public void addCarImage(View view){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Galeriye gitmek için izin gerekli", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }
    }

    private void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult =result.getData();
                    if(intentFromResult != null){
                        imageData = intentFromResult.getData();
                        binding.arabaImageView.setImageURI(imageData);
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "İzin Gerekli", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



















