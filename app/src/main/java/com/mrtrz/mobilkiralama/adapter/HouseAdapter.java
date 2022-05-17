package com.mrtrz.mobilkiralama.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrtrz.mobilkiralama.HouseAdvertDetailActivity;
import com.mrtrz.mobilkiralama.databinding.RecyclerRowBinding;
import com.mrtrz.mobilkiralama.model.House;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseHolder> {

    private ArrayList<House> houseArrayList;

    public HouseAdapter(ArrayList<House> houseArrayList) {
        this.houseArrayList = houseArrayList;
    }

    @NonNull
    @Override
    public HouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HouseHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerViewAciklamaEditText.setText(houseArrayList.get(position).aciklama);
        holder.recyclerRowBinding.recyclerViewFiyatEditText.setText(String.valueOf(houseArrayList.get(position).fiyat));
        Picasso.get().load(houseArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerViewImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), HouseAdvertDetailActivity.class);
                intent.putExtra("document", houseArrayList.get(position).documentId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houseArrayList.size();
    }

    class HouseHolder extends RecyclerView.ViewHolder {

        RecyclerRowBinding recyclerRowBinding;

        public HouseHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }

}
