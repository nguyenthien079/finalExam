package com.example.finalexam.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finalexam.Domain.UserModel;
import com.example.finalexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView displayNameTextView, ratingTextView;
    private EditText bioEditText, contactEditText;
    private Button updateProfileButton, deactivateAccountButton, deleteAccountButton;

    private DatabaseReference userRef;
    private String currentUserId;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        profileImageView = findViewById(R.id.profileImageView);
        displayNameTextView = findViewById(R.id.displayNameTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        bioEditText = findViewById(R.id.bioEditText);
        contactEditText = findViewById(R.id.contactEditText);

        updateProfileButton = findViewById(R.id.updateProfileButton);
        deactivateAccountButton = findViewById(R.id.deactivateAccountButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);

            loadUserProfile();

            // Sự kiện nút cập nhật thông tin
            updateProfileButton.setOnClickListener(v -> updateUserProfile());

            // Vô hiệu hóa tài khoản (set deactivated = true trong database)
            deactivateAccountButton.setOnClickListener(v -> deactivateAccount());

            // Xoá tài khoản (Firebase Auth + Database)
            deleteAccountButton.setOnClickListener(v -> deleteAccount());

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserProfile() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    if (user != null) {
                        Log.d("UserProfileActivity", "User data loaded: " + user.getUsername());
                        displayNameTextView.setText(user.getUsername());
                        ratingTextView.setText("Rating: " + user.getRating());
                        bioEditText.setText(user.getBio());
                        contactEditText.setText(user.getContact());

                        Glide.with(UserProfileActivity.this)
                                .load(user.getProfilePic())
                                .placeholder(R.drawable.profile)
                                .into(profileImageView);

                    } else {
                        Log.d("UserProfileActivity", "UserModel is null");
                    }
                } else {
                    Log.d("UserProfileActivity", "Snapshot does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String updatedBio = bioEditText.getText().toString().trim();
        String updatedContact = contactEditText.getText().toString().trim();

        // Cập nhật 2 trường bio và contact
        userRef.child("bio").setValue(updatedBio);
        userRef.child("contact").setValue(updatedContact).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deactivateAccount() {
        // Đặt trường deactivated = true trong database
        userRef.child("deactivated").setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfileActivity.this, "Account deactivated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Deactivation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAccount() {
        // Xoá dữ liệu user trong database trước
        userRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xoá user trên Firebase Authentication
                currentUser.delete().addOnCompleteListener(deleteTask -> {
                    if (deleteTask.isSuccessful()) {
                        Toast.makeText(UserProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                        finish(); // thoát activity sau khi xoá
                    } else {
                        Toast.makeText(UserProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(UserProfileActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
