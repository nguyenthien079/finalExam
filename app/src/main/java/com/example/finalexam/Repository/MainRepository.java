package com.example.finalexam.Repository;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.finalexam.Domain.BannerModel;
import com.example.finalexam.Domain.CategoryModel;
import com.example.finalexam.Domain.ItemsModel;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<List<BannerModel>> loadBanner() {
        final MutableLiveData<List<BannerModel>> listData = new MutableLiveData<>();
        firebaseDatabase.getReference("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<BannerModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
        return listData;
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        final MutableLiveData<List<CategoryModel>> listData = new MutableLiveData<>();
        firebaseDatabase.getReference("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CategoryModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
        return listData;
    }

    public LiveData<List<ItemsModel>> loadPopular() {
        final MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();
        firebaseDatabase.getReference("Popular").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
        return listData;
    }

    public LiveData<List<ItemsModel>> loadItemCategory(String categoryId) {
        final MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
        Query query = firebaseDatabase.getReference("Items").orderByChild("categoryId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                itemsLiveData.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
        return itemsLiveData;
    }
}
