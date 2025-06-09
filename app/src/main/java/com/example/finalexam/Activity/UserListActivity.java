package com.example.finalexam.Activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexam.Adapter.UserAdapter;
import com.example.finalexam.Domain.UserModel;
import com.google.firebase.database.*;
import com.example.finalexam.R;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<UserModel> userList;
    private UserAdapter userAdapter;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist); // layout bạn sẽ tạo bên dưới

        recyclerView = findViewById(R.id.userRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        userList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList, user -> {
            Intent intent = new Intent(UserListActivity.this, UserProfileActivity.class);
            intent.putExtra("userId", user.getUid());
            startActivity(intent);
        });
        recyclerView.setAdapter(userAdapter);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        loadUsers();
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel user = data.getValue(UserModel.class);
                    if (user != null) {
                        user.setUid(data.getKey()); // gán ID nếu cần
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
