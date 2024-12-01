package com.example.imagetotextapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManageMoneyy extends AppCompatActivity {

    private EditText etIncome;
    private Button btnAutoAllocate, btnSave, btnAddCategory, btnRemoveCategory;
    private LinearLayout categoryContainer;
    private int categoryCount = 0; // Keep track of the number of categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_moneyy);

        etIncome = findViewById(R.id.etIncome);
        btnAutoAllocate = findViewById(R.id.btnAutoAllocate);
        btnSave = findViewById(R.id.btnSave);
        btnAddCategory = findViewById(R.id.btnAddCategory); // Add a button to add categories
        btnRemoveCategory = findViewById(R.id.btnRemoveCategory); // Add a button to remove categories
        categoryContainer = findViewById(R.id.categoryContainer);

        // Add a new category (input field)
        btnAddCategory.setOnClickListener(v -> {
            addCategory();
        });

        // Remove the last added category
        btnRemoveCategory.setOnClickListener(v -> {
            removeCategory();
        });

        // Auto Allocate Button Click
        btnAutoAllocate.setOnClickListener(v -> {
            String incomeText = etIncome.getText().toString();
            if (!incomeText.isEmpty()) {
                double income = Double.parseDouble(incomeText);
                allocateMoney(income);
            } else {
                Toast.makeText(ManageMoneyy.this, "Masukkan jumlah pemasukan terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        // Save Button Click
        btnSave.setOnClickListener(v -> {
            // Here you can implement saving logic (e.g., saving the data to a database)
            Toast.makeText(ManageMoneyy.this, "Data Disimpan", Toast.LENGTH_SHORT).show();
        });
    }

    // Method to allocate money to categories
    private void allocateMoney(double income) {
        // Implement the allocation logic here (same as before)
    }

    // Method to add a category
    private void addCategory() {
        categoryCount++;
        EditText newCategory = new EditText(this);
        newCategory.setHint("Category " + categoryCount);
        newCategory.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        newCategory.setPadding(10, 10, 10, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newCategory.setLayoutParams(params);
        categoryContainer.addView(newCategory);
    }

    // Method to remove a category
    private void removeCategory() {
        if (categoryCount > 0) {
            categoryContainer.removeViewAt(categoryCount - 1);
            categoryCount--;
        }
    }
}
