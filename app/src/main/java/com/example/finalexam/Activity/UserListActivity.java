package com.example.finalexam.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Import ImageView if you also use the back icon
import android.widget.ProgressBar;
import android.widget.TextView; // Make sure TextView is imported
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexam.Adapter.UserAdapter;
import com.example.finalexam.Domain.UserModel;
import com.google.firebase.database.*;
import com.example.finalexam.R; // Make sure R is imported
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView backBtn; // Declare the TextView for the back button
    private ImageView backImageView; // Declare the ImageView for the back icon (optional, if you want it clickable too)

    private List<UserModel> userList;
    private UserAdapter userAdapter;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist); // This correctly links to your XML layout

        // Initialize views
        recyclerView = findViewById(R.id.userRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.backBtn); // Map the backBtn TextView
        backImageView = findViewById(R.id.imageView5); // Map the back ImageView (optional)


        userList = new ArrayList<>();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList, user -> {
            // Handle user item click to go to UserProfileActivity
            Intent intent = new Intent(UserListActivity.this, UserProfileActivity.class);
            intent.putExtra("userId", user.getUid()); // Pass the user ID to the profile activity
            startActivity(intent);
        });
        recyclerView.setAdapter(userAdapter);

        // Set up Firebase Database reference
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        // Load users from Firebase
        loadUsers();

        // --- Back Button Logic ---
        // Set OnClickListener for the backBtn TextView
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(UserListActivity.this, MainActivity.class);
                // These flags ensure that MainActivity is brought to the top
                // and any activities above it in the stack are cleared.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                 // Close the current UserListActivity
            }
        });

        // Optional: Make the back ImageView also clickable for consistency
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Same logic as backBtn
                Intent intent = new Intent(UserListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // Clear existing list before adding new data
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel user = data.getValue(UserModel.class);
                    if (user != null) {
                        user.setUid(data.getKey()); // Set the UID from the Firebase key
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
                progressBar.setVisibility(View.GONE); // Hide progress bar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE); // Hide progress bar even on error
            }
        });
    }
}