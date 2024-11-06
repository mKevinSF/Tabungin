package com.example.imagetotextapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class input extends AppCompatActivity {
    // Navigation Icons
    private ImageView profileIcon;
    private ImageView cameraIcon;
    private ImageView homeIcon;

    private EditText etItemName, etPrice;
    private Spinner spinnerCategory;
    private Button btnSave;
    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize your views here
        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);

        // Initialize navigation icons
        profileIcon = findViewById(R.id.profileIcon);
        cameraIcon = findViewById(R.id.cameraIcon);
        homeIcon = findViewById(R.id.homeIcon);

        // Set up spinner for categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Setup RecyclerView
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpenses.setAdapter(expenseAdapter);

        // Request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Button click action to save data
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString().trim();
                String priceText = etPrice.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                // Input validation
                if (itemName.isEmpty() || priceText.isEmpty()) {
                    Toast.makeText(input.this, "Nama Item dan Harga wajib diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse price to double
                double price = Double.parseDouble(priceText);

                // Get userName from Firestore based on the userId
                String userId = mAuth.getCurrentUser().getUid();
                db.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String userName = documentSnapshot.getString("userName");

                                // Create a new Expense object
                                Expense newExpense = new Expense(itemName, price, category);

                                // Save data to Firestore with userName as "itemInputBy"
                                Map<String, Object> barangData = new HashMap<>();
                                barangData.put("itemName", itemName);
                                barangData.put("itemPrice", price);
                                barangData.put("itemCategory", category);
                                barangData.put("itemInputBy", userName);  // Use userName instead of userId

                                // Add new document to Firestore (this prevents overwriting data)
                                db.collection("Transactions").add(barangData)
                                        .addOnSuccessListener(documentReference -> {
                                            // Success: Data is saved in Firestore
                                            Toast.makeText(input.this, "Data berhasil disimpan ke Firestore", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failure: Something went wrong
                                            Toast.makeText(input.this, "Gagal menyimpan data ke Firestore", Toast.LENGTH_SHORT).show();
                                        });

                                // Optionally, save the same data to CSV
                                saveToCSV(newExpense);

                                // Clear input fields after saving
                                etItemName.setText("");
                                etPrice.setText("");
                                spinnerCategory.setSelection(0);
                            } else {
                                Toast.makeText(input.this, "Gagal mendapatkan nama pengguna", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Error retrieving userName
                            Toast.makeText(input.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Navigation icons setup
        setupNavigation();
    }

    private void setupNavigation() {
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(input.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(input.this, profilepage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveToCSV(Expense expense) {
        // Save the expense data to a CSV file in external storage
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(directory, "WOI.txt");

        // If the file doesn't exist, create it and add a header
        boolean fileCreated = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append("Item Name,Price,Category\n"); // CSV header
                writer.flush();
                writer.close();
                fileCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Append data to the file
        try {
            FileWriter writer = new FileWriter(file, true); // true to append
            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        }
    }
}