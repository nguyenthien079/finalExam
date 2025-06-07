package com.example.finalexam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalexam.Domain.ItemsModel;
import com.example.finalexam.Helper.ChangeNumberItemsListener;
import com.example.finalexam.Helper.ManagmentCart;
import com.example.finalexam.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private ArrayList<ItemsModel> listItemSelected;
    private ManagmentCart managmentCart;
    private ChangeNumberItemsListener changeNumberItemListener;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemListener) {
        this.listItemSelected = listItemSelected;
        this.managmentCart = new ManagmentCart(context);
        this.changeNumberItemListener = changeNumberItemListener;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ItemsModel item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + (item.getNumberInCart() * item.getPrice()));
        holder.binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.binding.picCart);

        holder.binding.plusBtn.setOnClickListener(view -> {
            managmentCart.plusItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemListener != null) {
                    changeNumberItemListener.onChanged();
                }
            });
        });

        holder.binding.minusBtn.setOnClickListener(view -> {
            managmentCart.minusItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemListener != null) {
                    changeNumberItemListener.onChanged();
                }
            });
        });

        holder.binding.removeItemBtn.setOnClickListener(view -> {
            managmentCart.removeItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemListener != null) {
                    changeNumberItemListener.onChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }
}