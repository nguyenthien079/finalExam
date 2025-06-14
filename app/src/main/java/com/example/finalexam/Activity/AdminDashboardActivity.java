package com.example.finalexam.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalexam.R;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    ListView listFlaggedListings, listReportedUsers;

    ArrayList<String> flaggedListings = new ArrayList<>();
    ArrayList<String> reportedUsers = new ArrayList<>();

    ArrayAdapter<String> adapterListings, adapterUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        listFlaggedListings = findViewById(R.id.listFlaggedListings);
        listReportedUsers = findViewById(R.id.listReportedUsers);

        // Fake data demo
        flaggedListings.add("Listing: 'iPhone 14' flagged - Scam suspected");
        flaggedListings.add("Listing: 'Used Laptop' flagged - Inappropriate image");

        reportedUsers.add("User: john_doe - Reported 3 times");
        reportedUsers.add("User: scammer123 - Fraud reports");

        adapterListings = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flaggedListings);
        adapterUsers = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportedUsers);

        listFlaggedListings.setAdapter(adapterListings);
        listReportedUsers.setAdapter(adapterUsers);

        listFlaggedListings.setOnItemClickListener((parent, view, position, id) -> {
            String selected = flaggedListings.get(position);
            showAdminActionsDialog(selected, "listing");
        });

        listReportedUsers.setOnItemClickListener((parent, view, position, id) -> {
            String selected = reportedUsers.get(position);
            showAdminActionsDialog(selected, "user");
        });
    }

    private void showAdminActionsDialog(String item, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manage " + (type.equals("user") ? "User" : "Listing"))
                .setMessage(item)
                .setPositiveButton("Delete", (dialog, which) -> {
                    Toast.makeText(this, "Deleted " + type, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Suspend", (dialog, which) -> {
                    Toast.makeText(this, "Suspended " + type, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Warn", (dialog, which) -> {
                    Toast.makeText(this, "Warning sent to " + type, Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
