package com.example.imagetotextapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SeeSpending extends AppCompatActivity {

    private TableLayout tableLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spending);

        tableLayout = findViewById(R.id.tableLayout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserTransactions();
    }

    private void loadUserTransactions() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Transactions")
                .whereEqualTo("itemInputBy", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                String itemName = document.getString("itemName");
                                Double itemPrice = document.getDouble("itemPrice");
                                String itemCategory = document.getString("itemCategory");

                                addTableRow(itemName, itemPrice, itemCategory);
                            }
                        } else {
                            Toast.makeText(SeeSpending.this, "Tidak ada data transaksi.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SeeSpending.this, "Gagal memuat data transaksi.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTableRow(String itemName, Double itemPrice, String itemCategory) {
        TableRow tableRow = new TableRow(this);

        TextView itemNameView = new TextView(this);
        itemNameView.setText(itemName);
        itemNameView.setPadding(8, 8, 8, 8);

        TextView itemPriceView = new TextView(this);
        itemPriceView.setText(String.valueOf(itemPrice));
        itemPriceView.setPadding(8, 8, 8, 8);

        TextView itemCategoryView = new TextView(this);
        itemCategoryView.setText(itemCategory);
        itemCategoryView.setPadding(8, 8, 8, 8);

        tableRow.addView(itemNameView);
        tableRow.addView(itemPriceView);
        tableRow.addView(itemCategoryView);

        tableLayout.addView(tableRow);
    }
}

//ini dari csv
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.IOException;
//
//public class SeeSpending extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;
//    private TableLayout tableLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_see_spending);
//
//        // Get the TableLayout reference
//        tableLayout = findViewById(R.id.tableLayout);
//
//        // Open file picker to select CSV file
//        openFilePicker();
//    }
//
//    // Open the file picker for selecting a CSV file
//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("*/*");  // Allow any file type to be selected
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
//            // Get the Uri of the selected file
//            Uri fileUri = data.getData();
//
//            if (fileUri != null) {
//                String mimeType = getContentResolver().getType(fileUri);
//                // Check if the selected file is a CSV file
//                if (mimeType != null && mimeType.equals("text/csv")) {
//                    loadCSVData(fileUri);
//                } else {
//                    Toast.makeText(this, "Silakan pilih file CSV", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Method to read and display CSV data
//    private void loadCSVData(Uri fileUri) {
//        ContentResolver resolver = getContentResolver();
//
//        try (InputStream inputStream = resolver.openInputStream(fileUri)) {
//            if (inputStream == null) {
//                Toast.makeText(this, "Gagal membuka file", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            boolean isFirstLine = true;
//
//            // Read the file line by line
//            while ((line = reader.readLine()) != null) {
//                Log.d("SeeSpending", "Reading line: " + line);  // Log each line
//
//                // Skip header
//                if (isFirstLine) {
//                    isFirstLine = false;
//                    continue;
//                }
//
//                // Split the line by commas
//                String[] data = line.split(",");
//
//                // Create a new TableRow for each line of data
//                TableRow tableRow = new TableRow(this);
//
//                // Add TextViews to TableRow for each column (Nama Item, Harga, Kategori, Tanggal)
//                for (String value : data) {
//                    TextView textView = new TextView(this);
//                    textView.setText(value.trim());
//                    textView.setPadding(8, 8, 8, 8);  // Padding for readability
//                    tableRow.addView(textView);
//                }
//
//                // Add TableRow to TableLayout
//                tableLayout.addView(tableRow);
//            }
//
//            reader.close();
//            Log.d("SeeSpending", "CSV data loaded successfully");
//            Toast.makeText(this, "Data berhasil dimuat", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal membaca file CSV", Toast.LENGTH_SHORT).show();
//            Log.e("SeeSpending", "Error reading CSV file", e);
//        }
//    }
//}


