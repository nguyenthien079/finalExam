package com.example.finalexam.Activity;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finalexam.Domain.ItemsModel;
import com.example.finalexam.Helper.ManagmentCart;
import com.example.finalexam.R;
import com.example.finalexam.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private ItemsModel item;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        bundle();
        initSizeList();
    }

    private void initSizeList() {
        binding.smallBtn.setOnClickListener(view -> {
            binding.smallBtn.setBackgroundResource(R.drawable.brown_storke_bg);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(0);
        });

        binding.mediumBtn.setOnClickListener(view -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(R.drawable.brown_storke_bg);
            binding.largeBtn.setBackgroundResource(0);
        });

        binding.largeBtn.setOnClickListener(view -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(R.drawable.brown_storke_bg);
        });
    }

    @SuppressLint("SetTextI18n")
    private void bundle() {
        item = (ItemsModel) getIntent().getSerializableExtra("object");

        Glide.with(this)
                .load(item.getPicUrl().get(0))
                .into(binding.picMain);

        binding.titleTxt.setText(item.getTitle());
        binding.descriptionTxt.setText(item.getDescription());
        binding.priceTxt.setText("$" + item.getPrice());
        binding.ratingTxt.setText(String.valueOf(item.getRating()));
        binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart()));

        binding.addToCartBtn.setOnClickListener(view -> {
            int number = Integer.parseInt(binding.numberInCartTxt.getText().toString());
            item.setNumberInCart(number);
            managmentCart.insertItems(item);
        });

        binding.backBtn.setOnClickListener(view -> finish());

        binding.plusBtn.setOnClickListener(view -> {
            item.setNumberInCart(item.getNumberInCart() + 1);
            binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart()));
        });

        binding.minusBtn.setOnClickListener(view -> {
            if (item.getNumberInCart() > 0) {
                item.setNumberInCart(item.getNumberInCart() - 1);
                binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart()));
            }
        });
    }
}

