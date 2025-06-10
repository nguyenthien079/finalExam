package com.example.finalexam.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.finalexam.Domain.UserModel;
import com.example.finalexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE_PICK = 1001;

    private ImageView profileImageView;
    private TextView displayNameTextView, ratingTextView;
    private EditText bioEditText, contactEditText;
    private Button updateProfileButton, deactivateAccountButton, deleteAccountButton;

    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        // Ánh xạ view
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

            profileImageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            });

            updateProfileButton.setOnClickListener(v -> showUpdateDialog());
            deactivateAccountButton.setOnClickListener(v -> deactivateAccount());
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
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
                    displayNameTextView.setText(user.getUsername());
                    ratingTextView.setText("Rating: " + user.getRating());
                    bioEditText.setText(user.getBio());
                    contactEditText.setText(user.getContact());

                    Glide.with(UserProfileActivity.this)
                            .load(user.getProfilePic())
                            .placeholder(R.drawable.profile)
                            .into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_updateprofile, null);
        EditText editUsername = view.findViewById(R.id.editUsername);
        EditText editBio = view.findViewById(R.id.editBio);
        EditText editContact = view.findViewById(R.id.editContact);

        editUsername.setText(displayNameTextView.getText().toString());
        editBio.setText(bioEditText.getText().toString());
        editContact.setText(contactEditText.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Update Profile")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    performProfileUpdate(
                            editUsername.getText().toString().trim(),
                            editBio.getText().toString().trim(),
                            editContact.getText().toString().trim()
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performProfileUpdate(String username, String bio, String contact) {
        userRef.child("username").setValue(username);
        userRef.child("bio").setValue(bio);
        userRef.child("contact").setValue(contact).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                displayNameTextView.setText(username);
                bioEditText.setText(bio);
                contactEditText.setText(contact);
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deactivateAccount() {
        showEmailConfirmDialog(() -> {
            userRef.child("deactivated").setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Account deactivated", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void deleteAccount() {
        showEmailConfirmDialog(() -> {
            userRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentUser.delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        });
    }

    private void showEmailConfirmDialog(Runnable onConfirmed) {
        View view = getLayoutInflater().inflate(R.layout.dialog_confirmemail, null);
        EditText emailInput = view.findViewById(R.id.emailInput);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Email")
                .setView(view)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String enteredEmail = emailInput.getText().toString().trim();
                    if (enteredEmail.equals(currentUser.getEmail())) {
                        onConfirmed.run();
                    } else {
                        Toast.makeText(this, "Email does not match", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            uploadToCloudinary(selectedImage);
        }
    }

    private void uploadToCloudinary(Uri uri) {
        String filePath = getPathFromUri(uri);
        if (filePath == null) {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
            return;
        }

        MediaManager.get().upload(filePath)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = (String) resultData.get("secure_url");
                        userRef.child("profilePic").setValue(url);
                        Glide.with(UserProfileActivity.this).load(url).into(profileImageView);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(UserProfileActivity.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                })
                .dispatch();
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            try {
                int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(colIndex);
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }
}
