package com.example.fetchrewardsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    List<Item> itemList;
    Context context;

    public void setFilteredList(List<Item> filteredList){
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    public ItemAdapter(List<Item> itemList, Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item  = itemList.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tvListId;
        TextView tvId;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvListId = itemView.findViewById(R.id.tvlistId);
            name = itemView.findViewById(R.id.tvName);
            tvId = itemView.findViewById(R.id.tvId);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Item item) {
            tvListId.setText(String.valueOf(item.getListId()));
            name.setText(item.getName());
            tvId.setText(String.valueOf(item.getId()));
            cardView.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.anim_one));

        }
    }
}
