package com.mrtrz.mobilkiralama.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtrz.mobilkiralama.ShopAdvertDetailActivity;
import com.mrtrz.mobilkiralama.databinding.RecyclerRowBinding;
import com.mrtrz.mobilkiralama.model.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {

    ArrayList<Shop> shopArrayList;

    public ShopAdapter(ArrayList<Shop> shopArrayList) {
        this.shopArrayList = shopArrayList;
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShopHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerViewAciklamaEditText.setText(shopArrayList.get(position).aciklama);
        holder.recyclerRowBinding.recyclerViewFiyatEditText.setText(String.valueOf(shopArrayList.get(position).fiyat));
        Picasso.get().load(shopArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerViewImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ShopAdvertDetailActivity.class);
                intent.putExtra("document", shopArrayList.get(position).documentId);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }

    class ShopHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public ShopHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
