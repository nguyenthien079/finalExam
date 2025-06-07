package com.example.finalexam.Activity;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalexam.Adapter.CartAdapter;
import com.example.finalexam.Helper.ChangeNumberItemsListener;
import com.example.finalexam.Helper.ManagmentCart;
import com.example.finalexam.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        calculateCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        binding.listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.listView.setAdapter(new CartAdapter(
                managmentCart.getListCart(),
                this,
                this::calculateCart
        ));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void calculateCart() {
        double percentTax = 0.02;
        int delivery = 15;
        double itemTotal = managmentCart.getTotalFee();
        double tax = Math.round(itemTotal * percentTax * 100.0) / 100.0;
        double total = Math.round((itemTotal + tax + delivery) * 100.0) / 100.0;
        itemTotal = Math.round(itemTotal * 100.0) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.totalTaxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}
