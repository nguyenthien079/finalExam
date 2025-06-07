package com.example.finalexam.Activity;



import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalexam.Adapter.ItemListCategoryAdapter;
import com.example.finalexam.Domain.ItemsModel;
import com.example.finalexam.ViewModel.MainViewModel;
import com.example.finalexam.databinding.ActivityItemsListBinding;

import java.util.List;

public class ItemsListActivity extends AppCompatActivity {

    private ActivityItemsListBinding binding;
    private final MainViewModel viewModel = new MainViewModel();
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundles();
        initList();
    }

    private void getBundles() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            String title = getIntent().getStringExtra("title");
            if (title != null) {
                binding.categoryTxt.setText(title);
            }
        }
    }

    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.loadItems(id).observe(this, items -> {
            binding.listView.setLayoutManager(new GridLayoutManager(ItemsListActivity.this, 2));
            binding.listView.setAdapter(new ItemListCategoryAdapter(items));
            binding.progressBar.setVisibility(View.GONE);
        });

        binding.backBtn.setOnClickListener(view -> finish());
    }
}
