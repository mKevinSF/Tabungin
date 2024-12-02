//package com.example.imagetotextapp;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.nfc.Tag;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.content.Intent;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.Firebase;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.w3c.dom.Text;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class input extends AppCompatActivity {
//    // Navigation Icons
//    private ImageView profileIcon;
//    private ImageView cameraIcon;
//    private ImageView homeIcon;
//
//    private EditText etItemName, etPrice;
//    private Spinner spinnerCategory;
//    private Button btnSave;
//    private RecyclerView recyclerViewExpenses;
//    private ExpenseAdapter expenseAdapter;
//    private List<Expense> expenseList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        FirebaseAuth mAuth;
//        FirebaseFirestore db;
//        TextView namabarang, hargabarang;
//        Spinner jenisbarang;
//        String userId;
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        namabarang = findViewById(R.id.etItemName);
//        hargabarang = findViewById(R.id.etPrice);
//        jenisbarang = findViewById(R.id.spinnerCategory);
//        btnSave = findViewById(R.id.btnSave);
//
//        String barang = etItemName.getText().toString();
//        String harga = etPrice.getText().toString();
//        String jenis = spinnerCategory.getSelectedItem().toString();
//        userId = mAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = db.collection("Transactions").document(userId);
//        Map<String, Object> barangData = new HashMap<>();
//        barangData.put("itemName", barang);
//        barangData.put("itemPrice", harga);
//        barangData.put("itemCategory", jenis);
//        barangData.put("itemInputBy", userId);
//
//
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this); // Enable Edge-to-Edge
//        setContentView(R.layout.activity_input);
//
//        // Initialize views
//        etItemName = findViewById(R.id.etItemName);
//        etPrice = findViewById(R.id.etPrice);
//        spinnerCategory = findViewById(R.id.spinnerCategory);
//        btnSave = findViewById(R.id.btnSave);
//        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
//
//        // Navigation icons
//        profileIcon = findViewById(R.id.profileIcon);
//        cameraIcon = findViewById(R.id.cameraIcon);
//        homeIcon = findViewById(R.id.homeIcon);
//
//        // Set up navigation icons
//        setupNavigation();
//
//        // Fill spinner with categories from resources
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.category_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategory.setAdapter(adapter);
//
//        // Setup RecyclerView
//        expenseList = new ArrayList<>();
//        //data
//        expenseAdapter = new ExpenseAdapter(expenseList);
//        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewExpenses.setAdapter(expenseAdapter);
//
//        // Request storage permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//
//        // Button click action
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemName = etItemName.getText().toString().trim();
//                String priceText = etPrice.getText().toString().trim();
//                String category = spinnerCategory.getSelectedItem().toString();
//
//                // Input validation
//                if (itemName.isEmpty() || priceText.isEmpty()) {
//                    Toast.makeText(input.this, "Nama Item dan Harga wajib diisi", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Parse price to double
//                double price = Double.parseDouble(priceText);
//
//                // Create a new Expense object and update RecyclerView
//                Expense newExpense = new Expense(itemName, price, category);
//                expenseList.add(newExpense);
//                expenseAdapter.notifyDataSetChanged();
//
//                // Save data to CSV
//                saveToCSV(newExpense);
//
//                // Clear input fields after saving
//                etItemName.setText("");
//                etPrice.setText("");
//                spinnerCategory.setSelection(0);
//            }
//        });
//    }
//    private void saveToCSV(Expense expense) {
//        // Buat file CSV di penyimpanan eksternal
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(directory, "WOI.txt");
////change 1
//        // Jika file tidak ada, buat file baru dan tambahkan header
//        boolean fileCreated = false;
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // Header CSV
//                writer.flush();
//                writer.close();
//                fileCreated = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Tambahkan data expense ke file CSV
//        try {
//            FileWriter writer = new FileWriter(file, true); // true untuk append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void setupNavigation() {
//        homeIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(input.this, homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(input.this, profilepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//}
//package com.example.imagetotextapp;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class input extends AppCompatActivity {
//    // Navigation Icons
//    private ImageView profileIcon;
//    private ImageView cameraIcon;
//    private ImageView homeIcon;
//
//    private EditText etItemName, etPrice;
//    private Spinner spinnerCategory;
//    private Button btnSave;
//    private RecyclerView recyclerViewExpenses;
//    private ExpenseAdapter expenseAdapter;
//    private List<Expense> expenseList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this); // Enable Edge-to-Edge
//        setContentView(R.layout.activity_input);
//
//        // Firebase Initialization
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Initialize views
//        etItemName = findViewById(R.id.etItemName);
//        etPrice = findViewById(R.id.etPrice);
//        spinnerCategory = findViewById(R.id.spinnerCategory);
//        btnSave = findViewById(R.id.btnSave);
//        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
//
//        // Navigation icons
//        profileIcon = findViewById(R.id.profileIcon);
//        cameraIcon = findViewById(R.id.cameraIcon);
//        homeIcon = findViewById(R.id.homeIcon);
//
//        // Set up navigation icons
//        setupNavigation();
//
//        // Fill spinner with categories from resources
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.category_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategory.setAdapter(adapter);
//
//        // Setup RecyclerView
//        expenseList = new ArrayList<>();
//        expenseAdapter = new ExpenseAdapter(expenseList);
//        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewExpenses.setAdapter(expenseAdapter);
//
//        // Request storage permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//
//        // Button click action
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemName = etItemName.getText().toString().trim();
//                String priceText = etPrice.getText().toString().trim();
//                String category = spinnerCategory.getSelectedItem().toString();
//
//                // Input validation
//                if (itemName.isEmpty() || priceText.isEmpty()) {
//                    Toast.makeText(input.this, "Nama Item dan Harga wajib diisi", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Parse price to double
//                double price = Double.parseDouble(priceText);
//
//                // Get user ID from Firebase Authentication
//                String userId = mAuth.getCurrentUser().getUid();
//
//                // Create map for Firestore
//                Map<String, Object> barangData = new HashMap<>();
//                barangData.put("itemName", itemName);
//                barangData.put("itemPrice", price);
//                barangData.put("itemCategory", category);
//                barangData.put("itemInputBy", userId);
//
//                // Save data to Firestore
//                DocumentReference documentReference = db.collection("Transactions").document(userId);
//                documentReference.set(barangData)
//                        .addOnSuccessListener(aVoid -> {
//                            // Data successfully saved to Firestore
//                            Toast.makeText(input.this, "Data berhasil disimpan ke Firestore", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            // Failed to save data
//                            Toast.makeText(input.this, "Gagal menyimpan data ke Firestore", Toast.LENGTH_SHORT).show();
//                        });
//
//                // Create a new Expense object and update RecyclerView
//                Expense newExpense = new Expense(itemName, price, category);
//                expenseList.add(newExpense);
//                expenseAdapter.notifyDataSetChanged();
//
//                // Save data to CSV
//                saveToCSV(newExpense);
//
//                // Clear input fields after saving
//                etItemName.setText("");
//                etPrice.setText("");
//                spinnerCategory.setSelection(0);
//            }
//        });
//    }
//
//    private void saveToCSV(Expense expense) {
//        // Buat file CSV di penyimpanan eksternal
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(directory, "WOI.txt");
//
//        // Jika file tidak ada, buat file baru dan tambahkan header
//        boolean fileCreated = false;
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // Header CSV
//                writer.flush();
//                writer.close();
//                fileCreated = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Tambahkan data expense ke file CSV
//        try {
//            FileWriter writer = new FileWriter(file, true); // true untuk append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void setupNavigation() {
//        homeIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(input.this, homepage.class);
//            startActivity(intent);
//            finish();
//        });
//
//        profileIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(input.this, profilepage.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//}
//package com.example.imagetotextapp;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class input extends AppCompatActivity {
//    // Navigation Icons
//    private ImageView profileIcon;
//    private ImageView cameraIcon;
//    private ImageView homeIcon;
//
//    private EditText etItemName, etPrice;
//    private Spinner spinnerCategory;
//    private Button btnSave;
//    private RecyclerView recyclerViewExpenses;
//    private ExpenseAdapter expenseAdapter;
//    private List<Expense> expenseList;
//
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private String userId, userName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this); // Enable Edge-to-Edge
//        setContentView(R.layout.activity_input);
//
//        // Firebase Initialization
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize views
//        etItemName = findViewById(R.id.etItemName);
//        etPrice = findViewById(R.id.etPrice);
//        spinnerCategory = findViewById(R.id.spinnerCategory);
//        btnSave = findViewById(R.id.btnSave);
//        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
//
//        // Navigation icons
//        profileIcon = findViewById(R.id.profileIcon);
//        cameraIcon = findViewById(R.id.cameraIcon);
//        homeIcon = findViewById(R.id.homeIcon);
//
//        // Set up navigation icons
//        setupNavigation();
//
//        // Fill spinner with categories from resources
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.category_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategory.setAdapter(adapter);
//
//        // Setup RecyclerView
//        expenseList = new ArrayList<>();
//        expenseAdapter = new ExpenseAdapter(expenseList);
//        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewExpenses.setAdapter(expenseAdapter);
//
//        // Request storage permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//
//        // Get userId and userName from Firestore
//        userId = mAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = db.collection("users").document(userId);
//        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
//            if (e != null) {
//                return;
//            }
//            if (documentSnapshot != null && documentSnapshot.exists()) {
//                userName = documentSnapshot.getString("userName");
//            }
//        });
//
//        // Button click action
//        btnSave.setOnClickListener(v -> {
//            String itemName = etItemName.getText().toString().trim();
//            String priceText = etPrice.getText().toString().trim();
//            String category = spinnerCategory.getSelectedItem().toString();
//
//            // Input validation
//            if (itemName.isEmpty() || priceText.isEmpty()) {
//                Toast.makeText(input.this, "Nama Item dan Harga wajib diisi", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Parse price to double
//            double price = Double.parseDouble(priceText);
//
//            // Create map for Firestore with username for itemInputBy
//            Map<String, Object> barangData = new HashMap<>();
//            barangData.put("itemName", itemName);
//            barangData.put("itemPrice", price);
//            barangData.put("itemCategory", category);
//            barangData.put("itemInputBy", userName); // Use userName instead of userId
//
//            // Save data to Firestore
//            db.collection("Transactions").add(barangData)
//                    .addOnSuccessListener(documentReference -> {
//                        // Data berhasil disimpan ke dokumen baru
//                        Toast.makeText(input.this, "Data berhasil disimpan ke Firestore", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        // Gagal menyimpan data
//                        Toast.makeText(input.this, "Gagal menyimpan data ke Firestore", Toast.LENGTH_SHORT).show();
//                    });
//
//            // Create a new Expense object and update RecyclerView
//            Expense newExpense = new Expense(itemName, price, category);
//            expenseList.add(newExpense);
//            expenseAdapter.notifyDataSetChanged();
//
//            // Save data to CSV
//            saveToCSV(newExpense);
//
//            // Clear input fields after saving
//            etItemName.setText("");
//            etPrice.setText("");
//            spinnerCategory.setSelection(0);
//        });
//    }
//
//    private void saveToCSV(Expense expense) {
//        // Buat file CSV di penyimpanan eksternal
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(directory, "WOI.txt");
//
//        // Jika file tidak ada, buat file baru dan tambahkan header
//        boolean fileCreated = false;
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // Header CSV
//                writer.flush();
//                writer.close();
//                fileCreated = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Tambahkan data expense ke file CSV
//        try {
//            FileWriter writer = new FileWriter(file, true); // true untuk append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan ke CSV", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void setupNavigation() {
//        homeIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(input.this, homepage.class);
//            startActivity(intent);
//            finish();
//        });
//
//        profileIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(input.this, profilepage.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//}
package com.example.imagetotextapp;
//ini hasil 6 november 2024 coy
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
                builder.setMessage("Seluruh transaksi anda akan kami simpan pada file bernama TR4NSAKSIMU dengan format file csv " +
                        "yang ada pada folder Documents di folder aplikasi kami.");
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
                if (itemName.isEmpty() || priceText.isEmpty()) {
                    Toast.makeText(input.this, "Nama Item dan Harga wajib diisi", Toast.LENGTH_SHORT).show();
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
//        // Get the app's internal storage directory
//        File directory = getFilesDir();  // Access the app's internal storage directory
//        File file = new File(directory, "transaksimu.csv");
//
//        // Log the directory and file path
//        Log.d("CSVFilePath", "File is being saved to: " + file.getAbsolutePath());
//
//        // If the file doesn't exist, create it and add a header
//        boolean fileCreated = false;
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // CSV header
//                writer.flush();
//                writer.close();
//                fileCreated = true;
//
//                // Log when the file is created
//                Log.d("CSVFileCreation", "File created and header added.");
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CSVFileCreationError", "Error creating file or adding header: " + e.getMessage());
//            }
//        }
//
//        // Append data to the file
//        try {
//            FileWriter writer = new FileWriter(file, true); // true to append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//
//            // Log when data is successfully appended
//            Log.d("CSVFileAppend", "Data appended to file: " + expense.getItemName() + ", " + expense.getPrice() + ", " + expense.getCategory());
//
//            // Show success toast
//            Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("CSVFileAppendError", "Error appending data to file: " + e.getMessage());
//
//            // Show failure toast
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void saveToCSV(Expense expense) {
//        // Save the expense data to a CSV file in external storage
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(directory, "transaksimu.csv");
//
//        // If the file doesn't exist, create it and add a header
//        boolean fileCreated = false;
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // CSV header
//                writer.flush();
//                writer.close();
//                fileCreated = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Append data to the file
//        try {
//            FileWriter writer = new FileWriter(file, true); // true to append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }


//private void saveToCSV(Expense expense) {
//    // Get the app's external storage directory (app-specific)
//    File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//    if (directory != null) {
//        Log.d("FilePath", "Saving to: " + directory.getAbsolutePath());
//
//        File file = new File(directory, "transaksimu.csv");
//
//        try {
//            // If file doesn't exist, create it and write header
//            if (!file.exists()) {
//                file.createNewFile();
//                FileWriter writer = new FileWriter(file);
//                writer.append("Item Name,Price,Category\n"); // CSV header
//                writer.flush();
//                writer.close();
//            }
//
//            // Append data to the file
//            FileWriter writer = new FileWriter(file, true); // true to append
//            writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
//            writer.flush();
//            writer.close();
//            Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        }
//    }
//}


    private void saveToCSV(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "TR4NSAKSIMU.csv");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS); // Save to "Documents"

        // Get the content resolver
        ContentResolver resolver = getContentResolver();
        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

        if (uri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                if (outputStream != null) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.append("Item Name,Price,Category\n"); // CSV header
                    writer.append(expense.getItemName() + "," + expense.getPrice() + "," + expense.getCategory() + "\n");
                    writer.flush();
                    writer.close();
                    Toast.makeText(this, "Data disimpan !!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
