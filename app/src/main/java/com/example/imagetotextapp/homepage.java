//package com.example.imagetotextapp;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import android.widget.TextView;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class homepage extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_homepage);
//
//        ImageView profileIcon = findViewById(R.id.profileIcon);
//        ImageView cameraIcon = findViewById(R.id.cameraIcon);
//        ImageView homeIcon = findViewById(R.id.homeIcon);
//        Button prediksibutton = findViewById(R.id.prediksiButton);
//
//        homeIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(homepage.this, homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(homepage.this, profilepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        cameraIcon.setOnClickListener(new View.OnClickListener() { // Add this block
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(homepage.this, input.class); // Change to your input activity name
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        prediksibutton.setOnClickListener(new View.OnClickListener() { // Add this block
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(homepage.this, prediksi.class); // Change to your input activity name
////                startActivity(intent);
//                selectCSVFile();
//            }
//        });
//
////        prediksibutton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                // Run API call in background thread
////                Log.d(TAG, "Button clicked"); // Debug line
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Log.d(TAG, "Button clicked in background");
////                        uploadCSVToAPI();  // Call the function to upload CSV
////                    }
////                }).start();
////            }
////        });
//
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userId = mAuth.getCurrentUser().getUid();
//
//        DocumentReference documentReference = db.collection("users").document(userId);
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    String userName = documentSnapshot.getString("userName");
//                    TextView userNameTextView = findViewById(R.id.greetingText); // Sesuaikan ID TextView Anda di homepage
//                    userNameTextView.setText("Welcome Back, " + userName);
//                }
//            }
//        });
//
//    }
////    private void uploadCSVToAPI() {
////        ExecutorService executorService = Executors.newSingleThreadExecutor();
////        Callable<String> task = new Callable<String>() {
////            @Override
////            public String call() throws Exception {
////                String result = "";
////                try {
////                    // Check if file exists
////                    File file = new File(getFilesDir(), "personal_transactions.csv");
////                    if (!file.exists()) {
////                        return "File not found";  // Handle file not found error
////                    }
////
////                    // Read file content
////                    StringBuilder stringBuilder = new StringBuilder();
////                    try (FileInputStream fis = openFileInput("personal_transactions.csv");
////                         BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
////                        String line;
////                        while ((line = reader.readLine()) != null) {
////                            stringBuilder.append(line).append("\n");
////                        }
////                    }
////
////                    String csvData = stringBuilder.toString();
////
////                    // Set request data
////                    JSONObject requestBody = new JSONObject();
////                    requestBody.put("csv_data", csvData);
////                    requestBody.put("days", 7);
////
////                    // API connection
////                    URL url = new URL("https://getpredict2-497063330583.asia-southeast2.run.app/predict");
////                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////                    connection.setRequestMethod("POST");
////                    connection.setDoOutput(true);
////                    connection.setRequestProperty("Content-Type", "application/json");
////
////                    try (OutputStream os = connection.getOutputStream()) {
////                        os.write(requestBody.toString().getBytes());
////                    }
////
////                    // Read API response
////                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                    StringBuilder response = new StringBuilder();
////                    String line;
////                    while ((line = responseReader.readLine()) != null) {
////                        response.append(line);
////                    }
////                    result = response.toString();
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    result = "Error: " + e.getMessage();
////                }
////                return result;
////            }
////        };
////
////        Future<String> futureResult = executorService.submit(task);
////        try {
////            String result = futureResult.get();
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    Intent intent = new Intent(homepage.this, prediksi.class);
////                    intent.putExtra("api_result", result);
////                    startActivity(intent);
////                }
////            });
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            executorService.shutdown();
////        }
////    }
////private void uploadCSVToAPI() {
////    ExecutorService executorService = Executors.newSingleThreadExecutor();
////    Callable<String> task = new Callable<String>() {
////        @Override
////        public String call() throws Exception {
////            String result = "";
////            try {
////                // Check if file exists
////                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
////                File file = new File(directory, "personal_transactions.csv");
////                if (!file.exists()) {
////                    Log.e("uploadCSVToAPI", "File not found at: " + file.getAbsolutePath());  // Log error with file path
////                    return "File not found";  // Handle file not found error
////                }
////                Log.d("uploadCSVToAPI", "File found: " + file.getAbsolutePath());  // Log file path
////
////                // Read file content
////                StringBuilder stringBuilder = new StringBuilder();
////                try (FileInputStream fis = new FileInputStream(file);
////                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
////                    String line;
////                    while ((line = reader.readLine()) != null) {
////                        stringBuilder.append(line).append("\n");
////                    }
////                }
////                String csvData = stringBuilder.toString();
////                Log.d("uploadCSVToAPI", "CSV Data Read: " + csvData); // Log CSV content
////
////// Set request data
////                JSONObject requestBody = new JSONObject();
////                requestBody.put("csv_data", csvData);
////                requestBody.put("days", 7);  // Example additional parameter
////                Log.d("uploadCSVToAPI", "Request body created: " + requestBody.toString()); // Log request data
////
////                // API connection
////                URL url = new URL("https://getpredict2-497063330583.asia-southeast2.run.app/predict");
////                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////                connection.setRequestMethod("POST");
////                connection.setDoOutput(true);
////                connection.setRequestProperty("Content-Type", "application/json");
////
////                // Send request
////                try (OutputStream os = connection.getOutputStream()) {
////                    os.write(requestBody.toString().getBytes());
////                    Log.d("uploadCSVToAPI", "Request sent to API");  // Log when request is sent
////                }
////
////                // Read API response
////                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                StringBuilder response = new StringBuilder();
////                String line;
////                while ((line = responseReader.readLine()) != null) {
////                    response.append(line);
////                }
////                result = response.toString();
////                Log.d("uploadCSVToAPI", "API Response: " + result);  // Log API response
////
////                // Handle potential API errors or unexpected responses
////                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
////                    Log.e("uploadCSVToAPI", "Error: Response code " + connection.getResponseCode());
////                    result = "API error: " + connection.getResponseMessage();
////                }
////
////            } catch (Exception e) {
////                e.printStackTrace();
////                result = "Error: " + e.getMessage();
////                Log.e("uploadCSVToAPI", "Error: " + e.getMessage());  // Log any error
////            }
////            return result;
////        }
////    };
////
////    Future<String> futureResult = executorService.submit(task);
////    try {
////        String result = futureResult.get();
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                Intent intent = new Intent(homepage.this, prediksi.class);
////                intent.putExtra("api_result", result);
////                Log.d("uploadCSVToAPI", "Result passed to next activity: " + result);  // Log result passing to next activity
////                startActivity(intent);
////            }
////        });
////    } catch (Exception e) {
////        e.printStackTrace();
////        Log.e("uploadCSVToAPI", "Exception: " + e.getMessage());  // Log exception if occurs
////    } finally {
////        executorService.shutdown();
////    }
//
//
//private static final int PICK_CSV_FILE_REQUEST_CODE = 1001;
//
//    private void selectCSVFile() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("*/*"); // Filter for CSV files
//        intent.addCategory(Intent.CATEGORY_OPENABLE); // Allow the user to select the file
//        startActivityForResult(intent, PICK_CSV_FILE_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri uri = data.getData();
//                Log.d(TAG, "Selected file URI: " + uri.toString());
//
//                // You can now use this URI to copy the file to internal storage or upload it directly to the API
//                uploadCSVToAPI(uri);
//            }
//        }
//    }
//
//    private void uploadCSVToAPI(Uri fileUri) {
//        OkHttpClient client = new OkHttpClient();
//
//        try {
//            // Use ContentResolver to open the file input stream
//            InputStream inputStream = getContentResolver().openInputStream(fileUri);
//
//            if (inputStream == null) {
//                Log.e(TAG, "Failed to open file input stream.");
//                return;
//            }
//
//            // Get the file name from the URI
//            String fileName = getFileName(fileUri); // Helper method to extract the file name
//
//            // Prepare the file part using the input stream
//            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/csv"), inputStream.toString());
//            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileRequestBody);
//
//            // Prepare the 'days' parameter
//            RequestBody daysRequestBody = RequestBody.create(MediaType.parse("text/plain"), "7");
//
//            // Build the multipart request
//            MultipartBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addPart(filePart) // Add the file
//                    .addPart(MultipartBody.Part.createFormData("days", null, daysRequestBody)) // Add the 'days' field
//                    .build();
//
//            // Make the request
//            Request request = new Request.Builder()
//                    .url("https://getpredict2-497063330583.asia-southeast2.run.app/predict")  // Replace with your API endpoint
//                    .post(requestBody)
//                    .build();
//
//            // Execute the request asynchronously
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e(TAG, "API call failed: " + e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String responseData = response.body().string();
//                        Log.d(TAG, "API Response: " + responseData);
//                    } else {
//                        Log.e(TAG, "API Error: " + response.code());
//                    }
//                }
//            });
//
//        } catch (IOException e) {
//            Log.e(TAG, "Error reading file: " + e.getMessage());
//        }
//    }
//
//    // Helper method to extract the file name from URI
//    private String getFileName(Uri uri) {
//        String fileName = null;
//        String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//            fileName = cursor.getString(columnIndex);
//            cursor.close();
//        }
//        return fileName != null ? fileName : "personal_transaction.csv";  // Return a default name if not found
//    }
//
//}

//24/11 disini

//package com.example.imagetotextapp;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class homepage extends AppCompatActivity {
//
//    private static final int PICK_CSV_FILE_REQUEST_CODE = 1001;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_homepage);
//
//        ImageView profileIcon = findViewById(R.id.profileIcon);
//        ImageView cameraIcon = findViewById(R.id.cameraIcon);
//        ImageView homeIcon = findViewById(R.id.homeIcon);
//        Button prediksibutton = findViewById(R.id.prediksiButton);
//
//        homeIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(homepage.this, homepage.class);
//            startActivity(intent);
//            finish();
//        });
//
//        profileIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(homepage.this, profilepage.class);
//            startActivity(intent);
//            finish();
//        });
//
//        cameraIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(homepage.this, input.class);
//            startActivity(intent);
//            finish();
//        });
//
//        prediksibutton.setOnClickListener(v -> selectCSVFile());
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userId = mAuth.getCurrentUser().getUid();
//
//        DocumentReference documentReference = db.collection("users").document(userId);
//        documentReference.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot != null && documentSnapshot.exists()) {
//                String userName = documentSnapshot.getString("userName");
//                TextView userNameTextView = findViewById(R.id.greetingText);
//                userNameTextView.setText("Welcome Back, " + userName);
//            }
//        });
//    }
//
//    private void selectCSVFile() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("*/*"); // Filter for CSV files
//        intent.addCategory(Intent.CATEGORY_OPENABLE); // Allow the user to select the file
//        startActivityForResult(intent, PICK_CSV_FILE_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri uri = data.getData();
//                Log.d(TAG, "Selected file URI: " + uri.toString());
//
//                // You can now use this URI to copy the file to internal storage or upload it directly to the API
//                uploadCSVToAPI(uri);
//            }
//        }
//    }
//
//    private void uploadCSVToAPI(Uri fileUri) {
//        OkHttpClient client = new OkHttpClient();
//
//        try {
//            // Use ContentResolver to open the file input stream
//            InputStream inputStream = getContentResolver().openInputStream(fileUri);
//
//            if (inputStream == null) {
//                Log.e(TAG, "Failed to open file input stream.");
//                return;
//            }
//
//            // Get the file name from the URI
//            String fileName = getFileName(fileUri); // Helper method to extract the file name
//
//            // Prepare the file part using the input stream
//            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/csv"), inputStream.toString());
//            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileRequestBody);
//
//            // Prepare the 'days' parameter
//            RequestBody daysRequestBody = RequestBody.create(MediaType.parse("text/plain"), "7");
//
//            // Build the multipart request
//            MultipartBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addPart(filePart) // Add the file
//                    .addPart(MultipartBody.Part.createFormData("days", null, daysRequestBody)) // Add the 'days' field
//                    .build();
//
//            // Make the request
//            Request request = new Request.Builder()
//                    .url("https://getpredict2-497063330583.asia-southeast2.run.app/predict")  // Replace with your API endpoint
//                    .post(requestBody)
//                    .build();
//
//            // Execute the request asynchronously
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e(TAG, "API call failed: " + e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String responseData = response.body().string();
//                        Log.d(TAG, "API Response: " + responseData);
//                    } else {
//                        Log.e(TAG, "API Error: " + response.code());
//                    }
//                }
//            });
//
//        } catch (IOException e) {
//            Log.e(TAG, "Error reading file: " + e.getMessage());
//        }
//    }
//
//    // Helper method to extract the file name from URI
//    private String getFileName(Uri uri) {
//        String fileName = null;
//        String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//            fileName = cursor.getString(columnIndex);
//            cursor.close();
//        }
//        return fileName != null ? fileName : "personal_transaction.csv";  // Return a default name if not found
//    }
//}
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d(TAG, "Selected file URI: " + uri.toString());

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
                    .url("https://getpredict2-497063330583.asia-southeast2.run.app/predict") // Ganti dengan endpoint API Anda
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
}
