package com.example.imagetotextapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class homepage extends AppCompatActivity {

    private static final int PICK_CSV_FILE_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView cameraIcon = findViewById(R.id.cameraIcon);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        Button prediksibutton = findViewById(R.id.prediksiButton);
        Button manageButton = findViewById(R.id.manageButton);


        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(currentDate);

        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, homepage.class);
            startActivity(intent);
            finish();
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, profilepage.class);
            startActivity(intent);
            finish();
        });

        cameraIcon.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, input.class);
            startActivity(intent);
            finish();
        });

        manageButton.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, ManageMoneyy.class);
            startActivity(intent);
            finish();
        });

        prediksibutton.setOnClickListener(v -> selectCSVFile());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("userName");
                TextView userNameTextView = findViewById(R.id.greetingText);
                userNameTextView.setText("Welcome Back, " + userName);
            }
        });
    }

    private void selectCSVFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Filter untuk file CSV
        intent.addCategory(Intent.CATEGORY_OPENABLE); // Hanya file yang bisa dibuka
        startActivityForResult(intent, PICK_CSV_FILE_REQUEST_CODE);
    }

    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri uri = data.getData();
//                Log.d(TAG, "Selected file URI: " + uri.toString());
//
//                // Unggah file CSV ke API
//                uploadCSVToAPI(uri);
//            }
//        }
//    }
    //1212
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d("TAG", "Selected file URI: " + uri.toString());

                // Dapatkan nama file
                String fileName = getFileName(uri);

                // Validasi apakah file adalah CSV
                if (!fileName.toLowerCase().endsWith(".csv")) {
                    Toast.makeText(this, "File yang dipilih bukan file CSV. Silakan pilih file dengan format .csv.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Unggah file CSV ke API
                uploadCSVToAPI(uri);
            }
        }
    }


    private void uploadCSVToAPI(Uri fileUri) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        View progressOverlay = findViewById(R.id.progressOverlay);
        progressOverlay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1200, TimeUnit.SECONDS)  // Waktu tunggu koneksi
            .writeTimeout(1200, TimeUnit.SECONDS)    // Waktu tunggu penulisan
            .readTimeout(1200, TimeUnit.SECONDS)    // Waktu tunggu membaca respons
            .build();;

        try {
            // Gunakan ContentResolver untuk membuka InputStream
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            if (inputStream == null) {
                Log.e(TAG, "Failed to open file input stream.");
                runOnUiThread(() -> {
                    progressOverlay.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                });
                return;
            }

            // Dapatkan nama file dari URI
            String fileName = getFileName(fileUri);
            if (fileName == null) {
                Log.e(TAG, "Failed to get file name.");
                runOnUiThread(() -> {
                    progressOverlay.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                });
                return;
            }

            // Baca isi file ke dalam string
            String fileContent = readInputStream(inputStream);
            if (!hasRequiredColumns(fileContent)) {
                runOnUiThread(() -> {
                    progressOverlay.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Format file yang kamu upload tidak sesuai", Toast.LENGTH_LONG).show();
                });
                return;
            }

            // Hitung jumlah baris pada file CSV
            int lineCount = countLines(fileContent);
            if (lineCount < 100) {
                runOnUiThread(() -> {
                    progressOverlay.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Datanya masih kurang biar prediksinya akurat. Input lebih banyak data yuk, minimal 100 data", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Siapkan RequestBody untuk file
            RequestBody fileRequestBody = RequestBody.create(
                    MediaType.parse("text/csv"),
                    fileContent
            );

            // Bangun MultipartBody
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, fileRequestBody)
                    .addFormDataPart("days", "7") // Tambahkan parameter days
                    .build();

            // Bangun request API
            Request request = new Request.Builder()
                    .url("https://getprediction2-1063878325210.asia-southeast2.run.app/predict") // Ganti dengan endpoint API Anda
                    .post(requestBody)
                    .build();

            // Kirim request secara asynchronous
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "API call failed: " + e.getMessage());
                    runOnUiThread(() -> {
                        progressOverlay.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        progressOverlay.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    });
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Log.d(TAG, "API Response: " + responseData);

                        Intent intent = new Intent(homepage.this, prediksi.class);
                        intent.putExtra("api_result", responseData);
                        startActivity(intent);

                        // Tambahkan logika untuk menangani respons sukses
                    } else {
                        Log.e(TAG, "API Error: " + response.code() + ", Message: " + response.message());
                    }
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + e.getMessage());
            runOnUiThread(() -> {
                progressOverlay.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    // Fungsi untuk menghitung jumlah baris dalam file CSV
    private int countLines(String fileContent) {
        String[] lines = fileContent.split("\n");
        return lines.length;
    }

    // Helper method untuk membaca InputStream ke dalam String
    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    // Helper method untuk mendapatkan nama file dari URI
    private String getFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }
        return fileName != null ? fileName : "default.csv"; // Nama file default jika tidak ditemukan
    }
    private boolean hasRequiredColumns(String fileContent) {
        String[] lines = fileContent.split("\n");
        if (lines.length > 0) {
            String headerLine = lines[0];
            String[] columns = headerLine.split(",");

            boolean hasAmount = false;
            boolean hasDate = false;

            // Iterate through columns to check if both 'Amount' and 'Date' are present
            for (String column : columns) {
                column = column.trim(); // Remove extra spaces around column names
                if (column.equalsIgnoreCase("Amount")) {
                    hasAmount = true;
                } else if (column.equalsIgnoreCase("Date")) {
                    hasDate = true;
                }
            }

            // Return true only if both columns are found
            return hasAmount && hasDate;
        }
        return false;
    }

}
