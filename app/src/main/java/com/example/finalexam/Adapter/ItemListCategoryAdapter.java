package com.example.finalexam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalexam.Activity.DetailActivity;
import com.example.finalexam.Domain.ItemsModel;
import com.example.finalexam.databinding.ViewholderItemListBinding;

import java.util.List;

public class ItemListCategoryAdapter extends RecyclerView.Adapter<ItemListCategoryAdapter.Viewholder> {

    private List<ItemsModel> items;
    private Context context;

    public ItemListCategoryAdapter(List<ItemsModel> items) {
        this.items = items;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderItemListBinding binding;

        public Viewholder(ViewholderItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderItemListBinding binding = ViewholderItemListBinding.inflate(inflater, parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        ItemsModel item = items.get(position);
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.priceTxt.setText("$" + item.getPrice());
        holder.binding.subtitleTxt.setText(String.valueOf(item.getExtra()));

        Glide.with(context)
                .load(item.getPicUrl().get(0))
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}