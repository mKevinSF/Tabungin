package com.example.imagetotextapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

public class fetchmanage extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetchmanage);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchSavedCategories();
    }

    private void fetchSavedCategories() {
        FirebaseUser  user = auth.getCurrentUser ();
        if (user == null) {
            Log.e("FetchManage", "User  is not authenticated");
            Toast.makeText(this, "User  is not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid(); // Get the userId
        Log.d("FetchManage", "Fetching data for user ID: " + userId);
        CollectionReference userRef = db.collection("managemoney");

        // Query using userId
        userRef.whereEqualTo("userName", userId)
                .orderBy("date", Query.Direction.DESCENDING) // Order by timestamp
                .limit(1) // Fetch only the latest document
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d("FetchManage", "Number of documents fetched: " + task.getResult().size());
                        if (task.getResult().isEmpty()) {
                            Log.d("FetchManage", "No documents found for user ID: " + userId);
                            Toast.makeText(this, "No data found for this user.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    updatePredefinedRows(data); // Update predefined rows
                                    addDynamicCategories(data); // Add dynamic rows
                                }
                            }
                        }
                    } else {
                        Log.e("FetchManage", "Failed to fetch data", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("FetchManage", "Error fetching data: " + e.getMessage()));
    }

    private void updatePredefinedRows(Map<String, Object> data) {
        updateRowValue(R.id.tempatTinggalNominal, data.get("Tempat Tinggal"));
        updateRowValue(R.id.makanNominal, data.get("Makan"));
        updateRowValue(R.id.transportasiNominal, data.get("Transportasi"));
        updateRowValue(R.id.internetNominal, data.get("Internet"));
        updateRowValue(R.id.listrikAirNominal, data.get("Listrik & Air"));
        updateRowValue(R.id.keinginanNominal, data.get("Keinginan"));
        updateRowValue(R.id.tabunganNominal, data.get("Tabungan"));
        updateRowValue(R.id.lainNominal, data.get("Lain-lain"));
    }

    private void updateRowValue(int viewId, Object value) {
        TextView textView = findViewById(viewId);
        if (textView != null && value != null) {
            try {
                if (value instanceof Number) {
                    textView.setText(String.format("%.2f", ((Number) value).doubleValue()));
                } else {
                    textView.setText("N/A"); // Handle non-numeric values
                }
            } catch (Exception e) {
                Log.e("FetchManage", "Error updating row value: " + e.getMessage());
                textView.setText("Error");
            }
        }
    }

    private void addDynamicCategories(Map<String, Object> data) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String categoryName = entry.getKey();

            // Skip predefined categories and fields you don't want to show
            if (categoryName.equals("Tempat Tinggal") ||
                    categoryName.equals("Makan") ||
                    categoryName.equals("Transportasi") ||
                    categoryName.equals("Internet") ||
                    categoryName.equals("Listrik & Air") ||
                    categoryName.equals("Keinginan") ||
                    categoryName.equals("Tabungan") ||
                    categoryName.equals("Lain-lain") ||
                    categoryName.equals("date") || // Skip date field
                    categoryName.equals("userName")) { // Skip username field
                continue;
            }

            TableRow tableRow = new TableRow(this);
            TextView categoryTextView = new TextView(this);
            TextView valueTextView = new TextView(this);

            categoryTextView.setText(categoryName);
            valueTextView.setText(entry.getValue() != null ? entry.getValue().toString() : "N/A");

            tableRow.addView(categoryTextView);
            tableRow.addView(valueTextView);
            tableLayout.addView(tableRow);
        }
    }
}