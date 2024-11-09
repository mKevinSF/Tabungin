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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable Edge-to-Edge
        setContentView(R.layout.activity_input);

        // Initialize views
        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);

        // Navigation icons
        profileIcon = findViewById(R.id.profileIcon);
        cameraIcon = findViewById(R.id.cameraIcon);
        homeIcon = findViewById(R.id.homeIcon);

        // Set up navigation icons
        setupNavigation();

        // Fill spinner with categories from resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Setup RecyclerView
        expenseList = new ArrayList<>();
        //data
        expenseAdapter = new ExpenseAdapter(expenseList);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpenses.setAdapter(expenseAdapter);

        // Request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Button click action
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

                // Create a new Expense object and update RecyclerView
                Expense newExpense = new Expense(itemName, price, category);
                expenseList.add(newExpense);
                expenseAdapter.notifyDataSetChanged();

                // Save data to CSV
                saveToCSV(newExpense);

                // Clear input fields after saving
                etItemName.setText("");
                etPrice.setText("");
                spinnerCategory.setSelection(0);
            }
        });
    }
    private void saveToCSV(Expense expense) {
        // Buat file CSV di penyimpanan eksternal
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(directory, "WOI.txt");

        // Jika file tidak ada, buat file baru dan tambahkan header
        boolean fileCreated = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append("Item Name,Price,Category\n"); // Header CSV
                writer.flush();
                writer.close();
                fileCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Tambahkan data expense ke file CSV
        try {
            FileWriter writer = new FileWriter(file, true); // true untuk append
            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        }
    }

//    private void saveToCSV(Expense expense) {
//        // Pastikan memiliki izin untuk menulis ke penyimpanan eksternal
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//            return; // Tunggu sampai izin diberikan
//        }
//
//        // Menggunakan direktori publik
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        if (!directory.exists()) {
//            directory.mkdirs(); // Buat direktori jika tidak ada
//        }
//        File file = new File(directory, "test.txt");
//
//        try {
//            FileWriter writer = new FileWriter(file, true); // true untuk menambah
//            if (file.length() == 0) {
//                writer.append("Item Name,Price,Category\n"); // Tambah header jika file baru
//            }
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//
//            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
//            Log.d("FileLocation", "File disimpan di: " + file.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("FileWriteError", "Error writing file: " + e.getMessage());
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void saveToCSV(Expense expense) {
//        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS); // gunakan getExternalFilesDir
//        if (!directory.exists()) {
//            directory.mkdirs(); // Buat direktori jika tidak ada
//        }
//        File file = new File(directory, "test.txt");
//
//        try {
//            FileWriter writer = new FileWriter(file, true); // true untuk menambah
//            if (!file.exists()) {
//                writer.append("Item Name,Price,Category\n"); // Tambah header jika file baru
//            }
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("FileWriteError", "Error writing file: " + e.getMessage());
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }


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
}
