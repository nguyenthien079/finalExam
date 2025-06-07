package com.example.finalexam.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.finalexam.Adapter.CategoryAdapter;
import com.example.finalexam.Adapter.PopularAdapter;
import com.example.finalexam.ViewModel.MainViewModel;
import com.example.finalexam.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final MainViewModel viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        initBottomMenu();
    }

    private void initBottomMenu() {
        binding.cartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        viewModel.loadPopular().observe(this, popularList -> {
            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            binding.recyclerViewPopular.setAdapter(new PopularAdapter(popularList));
            binding.progressBarPopular.setVisibility(View.GONE);
        });
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observe(this, categoryList -> {
            binding.categoryView.setLayoutManager(
                    new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
            );
            binding.categoryView.setAdapter(new CategoryAdapter(categoryList));
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        viewModel.loadBanner().observe(this, bannerList -> {
            Glide.with(MainActivity.this)
                    .load(bannerList.get(0).getUrl())
                    .into(binding.banner);
            binding.progressBarBanner.setVisibility(View.GONE);
        });
    }
}