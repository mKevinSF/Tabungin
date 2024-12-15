package com.example.imagetotextapp;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        Button btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(v -> {
            Log.d("INFO_BUTTON", "Button clicked");
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Informasi");
                builder.setMessage("Seluruh transaksi anda akan kami simpan pada file bernama pengeluaranmu dengan format file csv " +
                        "yang ada pada folder Documents.");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("INFO_BUTTON", "Dialog displayed");
            } catch (Exception e) {
                Log.e("INFO_BUTTON", "Error: " + e.getMessage());
            }
        });

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
                if (itemName.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                    Toast.makeText(input.this, "Nama Item, Harga, dan kategori wajib diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse price to double
                double price = Double.parseDouble(priceText);

                // Create a new Expense object
                Expense newExpense = new Expense(itemName, price, category);

                // Save data to Firestore
                String userId = mAuth.getCurrentUser().getUid();
                Map<String, Object> barangData = new HashMap<>();
                barangData.put("itemName", itemName);
                barangData.put("itemPrice", price);
                barangData.put("itemCategory", category);
                barangData.put("itemInputBy", userId);  // Use userId as itemInputBy

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

//    private void saveToCSV(Expense expense) {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "pengeluaranmu.csv");
//        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
//        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS); // Save to "Documents"
//
//        // Get the content resolver
//        ContentResolver resolver = getContentResolver();
//        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);
//
//        if (uri != null) {
//            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
//                if (outputStream != null) {
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//                    writer.append("Item Name,Price,Category\n"); // CSV header
//                    writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//                    writer.flush();
//                    writer.close();
//                    Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void saveToCSV(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "pengeluaranmu.csv");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS); // Save to "Documents"

        // Get the content resolver
        ContentResolver resolver = getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");

        // Check if file already exists
        String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[] { "pengeluaranmu.csv" };
        Cursor cursor = resolver.query(contentUri, null, selection, selectionArgs, null);

        Uri uri = null;
        if (cursor != null && cursor.moveToFirst()) {
            // File exists, get the Uri
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            long id = cursor.getLong(idColumn);
            uri = ContentUris.withAppendedId(contentUri, id);
        } else {
            // File doesn't exist, create a new one
            uri = resolver.insert(contentUri, values);
        }

        if (uri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(uri, "wa")) { // "wa" = append mode
                if (outputStream != null) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

                    // Get today's date in the required format
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String currentDate = sdf.format(new Date());

                    // Write header and data
                    if (cursor == null || cursor.getCount() == 0) {
                        writer.append("Item Name,Amount,Category,Date\n"); // Write header if new file
                    }
                    writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "," + currentDate + "\n");

                    writer.flush();
                    writer.close();

                    Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }


}
