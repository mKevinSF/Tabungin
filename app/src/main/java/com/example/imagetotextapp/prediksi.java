package com.example.imagetotextapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
//
//public class prediksi extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState); // Pastikan memanggil super.onCreate()
//        setContentView(R.layout.activity_prediksi); // Ganti "activity_prediksi" dengan nama layout Anda
//
//        // Mengatur tanggal saat ini
//        Calendar calendar = Calendar.getInstance();
//        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//
//        // Temukan TextView dan set teksnya
//        TextView dateText = findViewById(R.id.dateText);
//        dateText.setText(currentDate);
//
//        ImageView profileIcon = findViewById(R.id.profileIcon);
//        ImageView cameraIcon = findViewById(R.id.cameraIcon);
//        ImageView homeIcon = findViewById(R.id.homeIcon);
//
//        homeIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, profilepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        cameraIcon.setOnClickListener(new View.OnClickListener() { // Add this block
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, input.class); // Change to your input activity name
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//}
//
//package com.example.imagetotextapp;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class prediksi extends AppCompatActivity {
//    private TextView dateText, suddenExpenseLabel, suddenExpenseValue;
//    private static final String API_URL = "https://getpredict2-497063330583.asia-southeast2.run.app/predict"; // Replace with your API URL
//    private static final String TAG = "PrediksiActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_prediksi);
//
//        // Initialize UI components
//        dateText = findViewById(R.id.dateText);
//        suddenExpenseLabel = findViewById(R.id.suddenExpenseLabel);
//        suddenExpenseValue = findViewById(R.id.suddenExpenseValue);
//
//        // Load CSV and predict
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(directory, "WOI.csv");
//
//        if (file.exists()) {
//            try {
//                List<String[]> csvData = readCSV(file);
//                sendPredictionRequest(csvData);
//            } catch (Exception e) {
//                Log.e(TAG, "Error reading CSV file", e);
//            }
//        } else {
//            Log.e(TAG, "File not found: " + file.getAbsolutePath());
//        }
//    }
//
//    private List<String[]> readCSV(File file) throws Exception {
//        List<String[]> data = new ArrayList<>();
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] values = line.split(","); // Assuming CSV is comma-separated
//            data.add(values);
//        }
//        br.close();
//        return data;
//    }
//
//    private void sendPredictionRequest(List<String[]> csvData) {
//        try {
//            JSONObject jsonRequest = new JSONObject();
//            JSONArray csvArray = new JSONArray();
//
//            for (String[] row : csvData) {
//                JSONArray jsonRow = new JSONArray();
//                for (String value : row) {
//                    jsonRow.put(value);
//                }
//                csvArray.put(jsonRow);
//            }
//
//            jsonRequest.put("csv_data", csvArray);
//            jsonRequest.put("days", 7); // Example parameter
//
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(
//                    jsonRequest.toString(),
//                    MediaType.parse("application/json; charset=utf-8")
//            );
//
//            Request request = new Request.Builder()
//                    .url(API_URL)
//                    .post(body)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e(TAG, "API request failed", e);
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        try {
//                            String responseData = response.body().string();
//                            JSONObject jsonResponse = new JSONObject(responseData);
//
//                            String unexpectedExpenses = jsonResponse.getString("unexpected_expenses");
//                            JSONArray futurePredictions = jsonResponse.getJSONArray("future_predictions");
//
//                            runOnUiThread(() -> {
//                                // Update UI
//                                suddenExpenseValue.setText("Rp. " + unexpectedExpenses);
//
//                                // Display predictions safely
//                                for (int i = 0; i < futurePredictions.length(); i++) {
//                                    try {
//                                        if (!futurePredictions.isNull(i)) {
//                                            double predictionValue = futurePredictions.getDouble(i);
//                                            Log.d(TAG, "Day " + (i + 1) + ": " + predictionValue);
//                                        } else {
//                                            Log.e(TAG, "Invalid value at position " + i);
//                                        }
//                                    } catch (JSONException e) {
//                                        Log.e(TAG, "Error parsing prediction at position " + i, e);
//                                    }
//                                }
//                            });
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Error parsing API response", e);
//                        }
//                    } else {
//                        Log.e(TAG, "API response error: " + response.code());
//                    }
//                }
//
//            });
//        } catch (JSONException e) {
//            Log.e(TAG, "Error creating JSON request", e);
//        }
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class prediksi extends AppCompatActivity {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_prediksi);
//
//        // Mendapatkan data yang dikirimkan dari halaman sebelumnya (API result)
//        Intent intent = getIntent();
//        String apiResult = intent.getStringExtra("api_result");
//
//        try {
//            // Parse JSON hasil dari API
//            JSONObject jsonObject = new JSONObject(apiResult);
//
//            // Mengambil data dari response JSON
//            String originalData = jsonObject.getString("original_data");
//            JSONArray testPredictions = new JSONArray(jsonObject.getString("test_predictions"));
//            JSONArray futurePredictions = new JSONArray(jsonObject.getString("future_predictions"));
//            String unexpectedExpenses = jsonObject.getString("unexpected_expenses");
//
//            // Menampilkan data pada UI
//            TextView originalDataText = findViewById(R.id.originalDataText);
//            originalDataText.setText("Original Data: \n" + originalData);
//
//            TextView testPredictionsText = findViewById(R.id.testPredictionsText);
//            testPredictionsText.setText("Test Predictions: \n" + testPredictions.toString());
//
//            TextView futurePredictionsText = findViewById(R.id.futurePredictionsText);
//            futurePredictionsText.setText("Future Predictions: \n" + futurePredictions.toString());
//
//            TextView unexpectedExpensesText = findViewById(R.id.unexpectedExpensesText);
//            unexpectedExpensesText.setText("Unexpected Expenses: \n" + unexpectedExpenses);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Set up navigation buttons
//        ImageView homeIcon = findViewById(R.id.homeIcon);
//        ImageView profileIcon = findViewById(R.id.profileIcon);
//        ImageView cameraIcon = findViewById(R.id.cameraIcon);
//
//        homeIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, profilepage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        cameraIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(prediksi.this, input.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediksi);

        LineChart originalDataChart = findViewById(R.id.original_data_chart);
        LineChart testPredictionsChart = findViewById(R.id.test_predictions_chart);
        LineChart futurePredictionsChart = findViewById(R.id.future_predictions_chart);
        TableLayout unexpectedExpensesTable = findViewById(R.id.unexpected_expenses_table);

        enableChartScrollAndZoom(originalDataChart);
        enableChartScrollAndZoom(testPredictionsChart);
        enableChartScrollAndZoom(futurePredictionsChart);

        // Get the API result from the intent
        String apiResult = getIntent().getStringExtra("api_result");

        try {
            JSONObject jsonObject = new JSONObject(apiResult);
            JSONObject predictions = jsonObject.getJSONObject("predictions");

            // Plot Original Data
            if (predictions.has("original_data")) {
                JSONArray originalDataArray = new JSONArray(predictions.getString("original_data"));
                ArrayList<Entry> originalDataEntries = new ArrayList<>();
                for (int i = 0; i < originalDataArray.length(); i++) {
                    JSONObject dataPoint = originalDataArray.getJSONObject(i);
                    String date = dataPoint.getString("Date"); // Format: "dd/MM/yyyy"
                    float amount = (float) dataPoint.getDouble("Amount");
                    originalDataEntries.add(new Entry(i, amount));
                }
                plotData(originalDataChart, originalDataEntries, "Original Data");
            }

            // Plot Test Predictions
            if (predictions.has("test_predictions")) {
                JSONArray testPredictionsArray = predictions.getJSONArray("test_predictions");
                ArrayList<Entry> testPredictionsEntries = new ArrayList<>();
                for (int i = 0; i < testPredictionsArray.length(); i++) {
                    float prediction = (float) testPredictionsArray.getDouble(i);
                    testPredictionsEntries.add(new Entry(i, prediction));
                }
                plotData(testPredictionsChart, testPredictionsEntries, "Test Predictions");
            }

            // Plot Future Predictions
            if (predictions.has("future_predictions")) {
                JSONArray futurePredictionsArray = predictions.getJSONArray("future_predictions");
                ArrayList<Entry> futurePredictionsEntries = new ArrayList<>();
                for (int i = 0; i < futurePredictionsArray.length(); i++) {
                    float prediction = (float) futurePredictionsArray.getDouble(i);
                    futurePredictionsEntries.add(new Entry(i, prediction));
                }
                plotData(futurePredictionsChart, futurePredictionsEntries, "Future Predictions");
            }

            // Populate Unexpected Expenses Table
            if (predictions.has("unexpected_expenses")) {
                JSONArray unexpectedExpensesArray = predictions.getJSONArray("unexpected_expenses");
                for (int i = 0; i < unexpectedExpensesArray.length(); i++) {
                    JSONObject expense = unexpectedExpensesArray.getJSONObject(i);
                    String month = expense.getString("Month");
                    int count = expense.getInt("Count");
                    JSONArray amountsArray = expense.getJSONArray("Amounts");

                    // Create a new row for each item in the JSON array
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    // Month Column
                    TextView monthText = new TextView(this);
                    monthText.setText(month);
                    monthText.setPadding(10, 10, 10, 10); // Optional padding for readability
                    row.addView(monthText);

                    // Count Column
                    TextView countText = new TextView(this);
                    countText.setText(String.valueOf(count));
                    countText.setPadding(30, 10, 10, 10);
                    row.addView(countText);

                    // Calculate the Total Unexpected Value
                    double totalAmount = 0;
                    for (int j = 0; j < amountsArray.length(); j++) {
                        totalAmount += amountsArray.getDouble(j);
                    }

                    // Total Unexpected Value Column
                    TextView totalText = new TextView(this);
                    totalText.setText(String.format("%.2f", totalAmount)); // Format to show two decimal places
                    totalText.setPadding(10, 10, 10, 10);
                    row.addView(totalText);

                    // Add the row to the table layout
                    unexpectedExpensesTable.addView(row);
                }
            }
        } catch (Exception e) {
            Log.e("JSONParsingError", "Error: " + e.getMessage());
        }

        // Set up zoom and scroll for the charts
        enableChartScrollAndZoom(originalDataChart);
        enableChartScrollAndZoom(testPredictionsChart);
        enableChartScrollAndZoom(futurePredictionsChart);

        // Set up navigation buttons
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView cameraIcon = findViewById(R.id.cameraIcon);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(prediksi.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(prediksi.this, profilepage.class);
                startActivity(intent);
                finish();
            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(prediksi.this, input.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to enable scroll and zoom on the chart
    private void enableChartScrollAndZoom(LineChart chart) {
        chart.setDragEnabled(true);  // Enable dragging
        chart.setScaleEnabled(true); // Enable scaling (zoom)
        chart.setPinchZoom(true);    // Enable pinch zoom
    }

    private void plotData(LineChart chart, ArrayList<Entry> entries, String label) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setLineWidth(2f);
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        Description description = new Description();
        description.setText(label);
        chart.setDescription(description);
        chart.invalidate(); // Refresh chart
    }
}

