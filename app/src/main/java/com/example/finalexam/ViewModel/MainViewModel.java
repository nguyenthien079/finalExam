package com.example.finalexam.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalexam.Domain.BannerModel;
import com.example.finalexam.Domain.CategoryModel;
import com.example.finalexam.Domain.ItemsModel;
import com.example.finalexam.Repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private MainRepository repository = new MainRepository();

    public LiveData<List<BannerModel>> loadBanner() {
        return repository.loadBanner();
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        return repository.loadCategory();
    }

    public LiveData<List<ItemsModel>> loadPopular() {
        return repository.loadPopular();
    }

    public LiveData<List<ItemsModel>> loadItems(String categoryId) {
        return repository.loadItemCategory(categoryId);
    }
}