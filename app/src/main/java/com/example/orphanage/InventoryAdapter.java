package com.example.orphanage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private ArrayList<InventoryItem> inventoryList;

    public InventoryAdapter(ArrayList<InventoryItem> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        InventoryItem item = inventoryList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity;

        public InventoryViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
