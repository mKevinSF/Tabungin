package com.example.imagetotextapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class ManageMoneyy extends AppCompatActivity {

    private EditText etIncome, resultTextView;
    private Button btnAutoAllocate, btnAddCategory;
    private LinearLayout categoryContainer;

    // Fixed category values
    private static final double ListrikAirLimit = 250000;
    private static final double InternetLimit = 200000;

    // Default categories and their allocation percentages
    private String[] defaultCategories = {
            "Tempat Tinggal", "Transportasi", "Listrik & Air", "Internet", "Makan", "Keinginan", "Tabungan", "Lain-lain"
    };

    // Default allocations for each category
    private double[] defaultAllocations = {1500000, 500000, ListrikAirLimit, InternetLimit, 350000, 0, 0, 0}; // Keinginan, Tabungan, and Lain-lain will be calculated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_moneyy);

        etIncome = findViewById(R.id.etIncome);
//        resultTextView = findViewById(R.id.resultTextView);
        btnAutoAllocate = findViewById(R.id.btnAutoAllocate);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        categoryContainer = findViewById(R.id.categoryContainer);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView cameraIcon = findViewById(R.id.cameraIcon);
        ImageView homeIcon = findViewById(R.id.homeIcon);

        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoneyy.this, homepage.class);
            startActivity(intent);
            finish();
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoneyy.this, profilepage.class);
            startActivity(intent);
            finish();
        });

        cameraIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoneyy.this, input.class);
            startActivity(intent);
            finish();
        });

        btnAutoAllocate.setOnClickListener(v -> {
            String incomeText = etIncome.getText().toString();
            if (!incomeText.isEmpty()) {
                double income = Double.parseDouble(incomeText);
                autoAllocate(income);
            } else {
                Toast.makeText(ManageMoneyy.this, "Masukkan jumlah pemasukan terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddCategory.setOnClickListener(v -> addCategory("Kategori Baru", 0));
    }

    // Function to perform auto allocation based on percentages
    private void autoAllocate(double income) {
        // Clear previous categories from the container to avoid duplication
        categoryContainer.removeAllViews();

        double[] percentages = {0.4, 0.3, 0.2, 0.1}; // 40%, 30%, 20%, 10%
        StringBuilder result = new StringBuilder();

        // Calculate the total allocation amounts
        double kebutuhanPokok = income * percentages[0]; // 40% for Kebutuhan Pokok
        double keinginan = income * percentages[1]; // 30% for Keinginan
        double tabungan = income * percentages[2]; // 20% for Tabungan
        double lainLain = income * percentages[3]; // 10% for Lain-lain

        // Fixed costs for Listrik and Listrik & Air
        double listrik = Math.min(income / 50, 150000);  // Listrik becomes 1/50 of income, capped at 150k
        double listrikAir = Math.min(income / 25, 250000);  // Listrik & Air becomes 1/25 of income, capped at 250k

        // Calculate remaining income after deducting fixed costs
        double remainingKebutuhanPokok = kebutuhanPokok - listrik - listrikAir;

        // If remaining income is less than 0, adjust Listrik and Listrik & Air
        if (remainingKebutuhanPokok < 0) {
            // If the remaining amount is negative, reduce Listrik & Listrik & Air first
            double totalDeficit = -remainingKebutuhanPokok;  // How much we're short
            double reducedListrikAir = Math.max(0, listrikAir - totalDeficit);
            double reducedListrik = Math.max(0, listrik - (totalDeficit - (listrikAir - reducedListrikAir)));

            // Recalculate remaining after adjustments
            remainingKebutuhanPokok = kebutuhanPokok - reducedListrik - reducedListrikAir;

            // Update Listrik and Listrik & Air values
            listrik = reducedListrik;
            listrikAir = reducedListrikAir;
        }

        // Allocate the remaining amount to Tempat Tinggal, Makan, and Transportasi
        double tempatTinggal = remainingKebutuhanPokok * 0.5;  // 50% of remaining Kebutuhan Pokok for Tempat Tinggal
        double makan = remainingKebutuhanPokok * 0.3;  // 30% of remaining Kebutuhan Pokok for Makan
        double transportasi = remainingKebutuhanPokok * 0.2;  // 20% of remaining Kebutuhan Pokok for Transportasi

        // Ensure Tempat Tinggal has a higher value than the others
        if (tempatTinggal < makan) {
            double diff = makan - tempatTinggal;
            tempatTinggal += diff;  // Increase Tempat Tinggal
            makan -= diff;  // Decrease Makan
        }

        if (tempatTinggal < transportasi) {
            double diff = transportasi - tempatTinggal;
            tempatTinggal += diff;  // Increase Tempat Tinggal
            transportasi -= diff;  // Decrease Transportasi
        }

        // Now, check if any of the categories are zero and redistribute the values
        if (tempatTinggal == 0 || makan == 0) {
            // If either Tempat Tinggal or Makan is zero, use Tabungan and Lain-lain to adjust
            double availableAmount = tabungan + lainLain;

            if (tempatTinggal == 0) {
                tempatTinggal = availableAmount > 0 ? Math.max(tabungan, lainLain) : 0;
                if (tempatTinggal == 0) {
                    makan += availableAmount;
                }
            } else if (makan == 0) {
                makan = availableAmount > 0 ? Math.max(tabungan, lainLain) : 0;
                if (makan == 0) {
                    tempatTinggal += availableAmount;
                }
            }
        }

        // Append the calculated allocations to the result
//        result.append("Kebutuhan Pokok (40%): ").append(String.format("%.2f", kebutuhanPokok)).append("\n");
//        result.append("Tempat Tinggal: ").append(String.format("%.2f", tempatTinggal)).append("\n");
//        result.append("Makan: ").append(String.format("%.2f", makan)).append("\n");
//        result.append("Transportasi: ").append(String.format("%.2f", transportasi)).append("\n");
//        result.append("Internet: ").append(String.format("%.2f", listrik)).append("\n");
//        result.append("Listrik & Air: ").append(String.format("%.2f", listrikAir)).append("\n");
//
//        result.append("\n");
//
//        // Append the Keinginan, Tabungan, and Lain-lain
//        result.append("Keinginan (30%): ").append(String.format("%.2f", keinginan)).append("\n");
//        result.append("Tabungan (20%): ").append(String.format("%.2f", tabungan)).append("\n");
//        result.append("Lain-lain (10%): ").append(String.format("%.2f", lainLain)).append("\n");

        // Set the result in the resultTextView
//        resultTextView.setText(result.toString());
        Toast.makeText(this, "Pemasukan dialokasikan sesuai 40-30-20-10", Toast.LENGTH_SHORT).show();

        // Add categories to the container
        addCategory("Tempat Tinggal", tempatTinggal);
        addCategory("Makan", makan);
        addCategory("Transportasi", transportasi);
        addCategory("Internet", listrik);
        addCategory("Listrik & Air", listrikAir);
        addCategory("Keinginan", keinginan);
        addCategory("Tabungan", tabungan);
        addCategory("Lain-lain", lainLain);
    }


    // Function to add a category dynamically to the UI
    private void addCategory(String categoryName, double nominal) {
        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add EditText for category name
        EditText categoryNameText = new EditText(this);
        categoryNameText.setText(categoryName);
        categoryNameText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Add EditText for nominal value
        EditText nominalField = new EditText(this);
        nominalField.setId(View.generateViewId());
        nominalField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        nominalField.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        nominalField.setText(String.format("%.2f", nominal));

        categoryLayout.addView(categoryNameText);
        categoryLayout.addView(nominalField);

        // Add category to the container
        categoryContainer.addView(categoryLayout);
    }
}
