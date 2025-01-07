package com.example.imagetotextapp;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class SeeSpending extends AppCompatActivity {
//
//    private TableLayout tableLayout;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_see_spending);
//
//        tableLayout = findViewById(R.id.tableLayout);
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        loadUserTransactions();
//    }
//
//    private void loadUserTransactions() {
//        String userId = mAuth.getCurrentUser().getUid();
//
//        db.collection("Transactions")
//                .whereEqualTo("itemInputBy", userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot querySnapshot = task.getResult();
//
//                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                            for (DocumentSnapshot document : querySnapshot) {
//                                String itemName = document.getString("itemName");
//                                Double itemPrice = document.getDouble("itemPrice");
//                                String itemCategory = document.getString("itemCategory");
//
//                                addTableRow(itemName, itemPrice, itemCategory);
//                            }
//                        } else {
//                            Toast.makeText(SeeSpending.this, "Tidak ada data transaksi.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(SeeSpending.this, "Gagal memuat data transaksi.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void addTableRow(String itemName, Double itemPrice, String itemCategory) {
//        TableRow tableRow = new TableRow(this);
//
//        TextView itemNameView = new TextView(this);
//        itemNameView.setText(itemName);
//        itemNameView.setPadding(8, 8, 8, 8);
//
//        TextView itemPriceView = new TextView(this);
//        itemPriceView.setText(String.valueOf(itemPrice));
//        itemPriceView.setPadding(8, 8, 8, 8);
//
//        TextView itemCategoryView = new TextView(this);
//        itemCategoryView.setText(itemCategory);
//        itemCategoryView.setPadding(8, 8, 8, 8);
//
//        tableRow.addView(itemNameView);
//        tableRow.addView(itemPriceView);
//        tableRow.addView(itemCategoryView);
//
//        tableLayout.addView(tableRow);
//    }
//}
//
////ini dari csv

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SeeSpending extends AppCompatActivity {

    private TableLayout tableLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button btnDownloadCSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spending);

        tableLayout = findViewById(R.id.tableLayout);
        btnDownloadCSV = findViewById(R.id.btnDownloadCSV);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView cameraIcon = findViewById(R.id.cameraIcon);
        ImageView homeIcon = findViewById(R.id.homeIcon);

        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SeeSpending.this, homepage.class);
            startActivity(intent);
            finish();
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SeeSpending.this, profilepage.class);
            startActivity(intent);
            finish();
        });

        cameraIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SeeSpending.this, input.class);
            startActivity(intent);
            finish();
        });

        loadUserTransactions();

        btnDownloadCSV.setOnClickListener(v -> exportToCSV());
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
                                String itemDate = document.getString("itemDate");

                                addTableRow(itemName, itemPrice, itemCategory, itemDate);
                            }
                        } else {
                            Toast.makeText(SeeSpending.this, "Tidak ada data transaksi.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SeeSpending.this, "Gagal memuat data transaksi.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTableRow(String itemName, Double itemPrice, String itemCategory, String itemDate) {
        TableRow tableRow = new TableRow(this);

        TextView itemNameView = new TextView(this);
        if (itemName != null && itemName.length() > 15) {
            itemNameView.setText(itemName.substring(0, 15) + "…"); // Tambahkan ellipsis jika terlalu panjang
        } else {
            itemNameView.setText(itemName);
        }
        itemNameView.setPadding(8, 8, 8, 8);
//        itemNameView.setText(itemName);
//        itemNameView.setPadding(8, 8, 8, 8);

        TextView itemPriceView = new TextView(this);
        itemPriceView.setText(String.valueOf(itemPrice));
        itemPriceView.setPadding(8, 8, 8, 8);

        TextView itemCategoryView = new TextView(this);
        itemCategoryView.setText(itemCategory);
        itemCategoryView.setPadding(8, 8, 8, 8);

        TextView itemDateView = new TextView(this);
        itemDateView.setText(itemDate);
        itemDateView.setPadding(8, 8, 8, 8);

        tableRow.addView(itemNameView);
        tableRow.addView(itemPriceView);
        tableRow.addView(itemCategoryView);
        tableRow.addView(itemDateView);

        tableLayout.addView(tableRow);
    }

    private void exportToCSV() {
        // Generate the filename based on the current date
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        String currentDate = sdf.format(new Date());
        String fileName = "pengeluaran_" + currentDate + ".csv";

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

        ContentResolver resolver = getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");

        // Always create a new file (overwrite)
        Uri uri = resolver.insert(contentUri, values);

        if (uri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(uri, "w");
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

                // Write the header
                writer.append("Item Name,Amount,Category,Date\n");

                // Iterate through table rows
                for (int i = 0; i < tableLayout.getChildCount(); i++) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    if (i == 0) continue; // Skip header row in the table layout

                    String itemName = ((TextView) tableRow.getChildAt(0)).getText().toString();
                    String itemPrice = ((TextView) tableRow.getChildAt(1)).getText().toString();
                    String itemCategory = ((TextView) tableRow.getChildAt(2)).getText().toString();
                    String itemDate = ((TextView) tableRow.getChildAt(3)).getText().toString();

                    writer.append(itemName).append(",")
                            .append(itemPrice).append(",")
                            .append(itemCategory).append(",")
                            .append(itemDate).append("\n");
                }

                writer.flush();
                Toast.makeText(this, "CSV berhasil disimpan dengan nama: " + fileName, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan CSV.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
