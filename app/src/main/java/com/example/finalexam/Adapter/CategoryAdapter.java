package com.example.finalexam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexam.Activity.ItemsListActivity;
import com.example.finalexam.Domain.CategoryModel;
import com.example.finalexam.R;
import com.example.finalexam.databinding.ViewholderCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    private List<CategoryModel> items;
    private Context context;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public CategoryAdapter(List<CategoryModel> items) {
        this.items = items;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(inflater, parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, @SuppressLint("RecyclerView") int position) {
        CategoryModel item = items.get(position);
        holder.binding.titleCat.setText(item.getTitle());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) return; // item đã bị xóa hoặc không hợp lệ

                lastSelectedPosition = selectedPosition;
                selectedPosition = currentPosition;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                // Lấy lại item đúng vị trí cập nhật
                CategoryModel currentItem = items.get(currentPosition);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, ItemsListActivity.class);
                        intent.putExtra("id", String.valueOf(currentItem.getId()));
                        intent.putExtra("title", currentItem.getTitle());
                        ContextCompat.startActivity(context, intent, null);
                    }
                }, 500);
            }
        });

        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_full_corner_bg);
            holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_full_corner_bg);
            holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.darkBrown));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}