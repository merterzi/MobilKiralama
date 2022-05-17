package com.mrtrz.mobilkiralama.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtrz.mobilkiralama.CarAdvertDetailActivity;
import com.mrtrz.mobilkiralama.databinding.RecyclerRowBinding;
import com.mrtrz.mobilkiralama.model.Car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder> {

    ArrayList<Car> carArrayList;

    public CarAdapter(ArrayList<Car> carArrayList) {
        this.carArrayList = carArrayList;
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CarHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerViewFiyatEditText.setText(String.valueOf(carArrayList.get(position).fiyat));
        holder.recyclerRowBinding.recyclerViewAciklamaEditText.setText(carArrayList.get(position).aciklama);
        Picasso.get().load(carArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerViewImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), CarAdvertDetailActivity.class);
                intent.putExtra("document", carArrayList.get(position).documentId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carArrayList.size();
    }

    class CarHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public CarHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
