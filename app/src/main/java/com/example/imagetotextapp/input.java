package com.example.imagetotextapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class input extends AppCompatActivity {
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
    private Map<String, Double> categoryAllocation; // Menyimpan alokasi kategori

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //29/12
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fetchCategoryAllocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(v -> {
            Log.d("INFO_BUTTON", "Button clicked");
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Informasi");
                builder.setMessage("Seluruh transaksi anda akan kami simpan pada file dengan format file csv " +
                        "yang ada pada folder Documents.");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("INFO_BUTTON", "Dialog displayed");
            } catch (Exception e) {
                Log.e("INFO_BUTTON", "Error: " + e.getMessage());
            }
        });

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

//         Button click action to save data
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemName = etItemName.getText().toString().trim();
//                String priceText = etPrice.getText().toString().trim();
//                String category = spinnerCategory.getSelectedItem().toString();
//
//                // Input validation
//                if (itemName.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
//                    Toast.makeText(input.this, "Nama Item, Harga, dan kategori wajib diisi", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Parse price to double
//                double price = Double.parseDouble(priceText);
//
//                // Warning jika kategori tidak memiliki alokasi
//                if (!categoryAllocation.containsKey(category)) {
//                    Snackbar.make(v, "Anda belum melakukan alokasi anggaran", Snackbar.LENGTH_SHORT).show();
//                }
//
//                // Warning jika nilai transaksi > alokasi
//                Double allocatedBudget = categoryAllocation.get(category);
//                if (allocatedBudget != null && price > allocatedBudget) {
//                    Snackbar.make(v, "Nilai transaksi melebihi alokasi kategori!", Snackbar.LENGTH_SHORT).show();
//                }
//
//                // Create a new Expense object
//                Expense newExpense = new Expense(itemName, price, category);
//
//                // Get today's date in the required format
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//                String currentDate = sdf.format(new Date());
//
//                // Save data to Firestore
//                String userId = mAuth.getCurrentUser ().getUid();
//                Map<String, Object> barangData = new HashMap<>();
//                barangData.put("itemName", itemName);
//                barangData.put("itemPrice", price);
//                barangData.put("itemCategory", category);
//                barangData.put("itemInputBy", userId);  // Use userId as itemInputBy
//                barangData.put("itemDate", currentDate); // Add the current date to Firestore
//
//                // Add new document to Firestore (this prevents overwriting data)
//                db.collection("Transactions").add(barangData)
//                        .addOnSuccessListener(documentReference -> {
//                            // Success: Data is saved in Firestore
//                            Toast.makeText(input.this, "Data berhasil disimpan ke Firestore", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            // Failure: Something went wrong
//                            Toast.makeText(input.this, "Gagal menyimpan data ke Firestore", Toast.LENGTH_SHORT).show();
//                        });
//
//                // Optionally, save the same data to CSV
////                saveToCSV(newExpense);
//                reinsertCategoryAllocationToFirestore(category, price);
//
//                // Clear input fields after saving
//                etItemName.setText("");
//                etPrice.setText("");
//                spinnerCategory.setSelection(0);
//            }
//        });
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

                // Cek apakah alokasi dana tersedia
                Double allocatedBudget = categoryAllocation != null ? categoryAllocation.get(category) : null;

                if (allocatedBudget == null) {
                    Snackbar.make(v, "Anda belum melakukan alokasi dana", Snackbar.LENGTH_SHORT).show();
                } else if (price > allocatedBudget) {
                    Snackbar.make(v, "Nilai transaksi melebihi alokasi kategori!", Snackbar.LENGTH_SHORT).show();
                }

                // Simpan data ke Firestore
                String userId = mAuth.getCurrentUser().getUid();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = sdf.format(new Date());

                Map<String, Object> barangData = new HashMap<>();
                barangData.put("itemName", itemName);
                barangData.put("itemPrice", price);
                barangData.put("itemCategory", category);
                barangData.put("itemInputBy", userId);
                barangData.put("itemDate", currentDate);

                db.collection("Transactions").add(barangData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(input.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(input.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        });

                // Jika ada alokasi dana, kurangi alokasinya
                if (allocatedBudget != null) {
                    reinsertCategoryAllocationToFirestore(category, price);
                }

                // Clear input fields setelah menyimpan
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

    private void fetchCategoryAllocation() {
        String userId = mAuth.getCurrentUser().getUid();

        // Ambil userName berdasarkan userId
        db.collection("users").document(userId).get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        String userName = userDocument.getString("userName");
                        android.util.Log.d("CATEGORY_ALLOCATION", "userName yang diambil: " + userName);

                        // Query untuk mengambil data terbaru
                        db.collection("managemoney")
                                .whereEqualTo("userName", userName)
                                .orderBy("date", Query.Direction.DESCENDING) // Urutkan berdasarkan date, terbaru di atas
                                .limit(1) // Ambil hanya satu dokumen
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot latestDocument = queryDocumentSnapshots.getDocuments().get(0);
                                        categoryAllocation = new HashMap<>();

                                        for (String key : latestDocument.getData().keySet()) {
                                            if (!key.equals("userName") && !key.equals("date")) {
                                                Object value = latestDocument.get(key);
                                                if (value instanceof Number) {
                                                    categoryAllocation.put(key, ((Number) value).doubleValue());
                                                    android.util.Log.d("CATEGORY_ALLOCATION", "Kategori: " + key + ", Alokasi: " + value);
                                                }
                                            }
                                        }

                                        // Log dan feedback pengguna
                                        android.util.Log.d("CATEGORY_ALLOCATION", "Data terbaru berhasil diambil: " + latestDocument.getData());
//                                        Toast.makeText(this, "Data terbaru berhasil diambil.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        android.util.Log.d("CATEGORY_ALLOCATION", "Tidak ada data ditemukan.");
                                        Toast.makeText(this, "Kamu belum melakukan alokasi dana", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    android.util.Log.e("CATEGORY_ALLOCATION", "Gagal mengambil data", e);
                                    Toast.makeText(this, "Gagal mengambil data terbaru.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        android.util.Log.e("CATEGORY_ALLOCATION", "User tidak ditemukan");
                        Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("CATEGORY_ALLOCATION", "Gagal mengambil data user", e);
                    Toast.makeText(this, "Gagal mengambil data user.", Toast.LENGTH_SHORT).show();
                });
    }


    private void reinsertCategoryAllocationToFirestore(String category, double price) {
        // Cek apakah categoryAllocation ada datanya
        if (categoryAllocation == null || categoryAllocation.isEmpty()) {
            Toast.makeText(this, "Tidak ada data untuk dimasukkan kembali.", Toast.LENGTH_SHORT).show();
//            return;
        }

        // Cek apakah kategori yang dimaksud ada dalam alokasi
        if (!categoryAllocation.containsKey(category)) {
            Toast.makeText(this, "Kategori tidak ditemukan dalam alokasi.", Toast.LENGTH_SHORT).show();
//            return;
        }

        // Kurangi alokasi kategori dengan harga
        double currentAllocation = categoryAllocation.get(category);
        if (currentAllocation < price) {
            Toast.makeText(this, "Alokasi kategori tidak cukup untuk transaksi ini.", Toast.LENGTH_SHORT).show();

        }

        categoryAllocation.put(category, currentAllocation - price);

        // Dapatkan userName dari Firebase Authentication
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        String userName = userDocument.getString("userName");

                        // Siapkan data baru untuk dimasukkan
                        Map<String, Object> newData = new HashMap<>();
                        newData.put("userName", userName);

                        // Tambahkan alokasi kategori yang sudah dikurangi ke data baru
                        for (Map.Entry<String, Double> entry : categoryAllocation.entrySet()) {
                            newData.put(entry.getKey(), entry.getValue());
                        }

                        // Tambahkan field date dengan timestamp terbaru
                        newData.put("date", new com.google.firebase.Timestamp(new Date()));

                        // Simpan data baru ke Firestore
                        db.collection("managemoney").add(newData)
                                .addOnSuccessListener(documentReference -> {
//                                    Toast.makeText(this, "Data berhasil dimasukkan kembali dengan pengurangan alokasi.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Gagal memasukkan data baru.", Toast.LENGTH_SHORT).show();
//                                    Log.e("FIREBASE_INSERT", "Error: " + e.getMessage(), e);
                                });
                    } else {
                        Toast.makeText(this, "User tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil data user.", Toast.LENGTH_SHORT).show();
                    Log.e("FIREBASE_USER_FETCH", "Error: " + e.getMessage(), e);
                });
    }

}