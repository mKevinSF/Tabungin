//package com.example.imagetotextapp;
//
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
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Register extends AppCompatActivity {
//    private FirebaseAuth mAuth;
//    private EditText userNameEditText, emailEditText, passwordEditText;
//    private Button signUpButton;
//    private TextView loginText;
//    private FirebaseFirestore db;
//    String userId, TAG;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        userNameEditText = findViewById(R.id.userName);
//        emailEditText = findViewById(R.id.email);
//        passwordEditText = findViewById(R.id.password);
//        signUpButton = findViewById(R.id.signUpButton);
//        loginText = findViewById(R.id.loginText);
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userName = userNameEditText.getText().toString();
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//
//                boolean hasError = false;
//
//                if (TextUtils.isEmpty(userName)) {
//                    userNameEditText.setError("Username is required");
//                    hasError = true;
//                }
//
//                if (TextUtils.isEmpty(email)) {
//                    emailEditText.setError("Email is required");
//                    hasError = true;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    passwordEditText.setError("Password is required");
//                    hasError = true;
//                }
//
//                if (password.length() < 6) {
//                    passwordEditText.setError("Password Must Be >= 6 Characters");
//                    hasError = true;
//                }
//
//                if (hasError) {
//                    return; // Stop execution if any error is found
//                }
//
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful()){
//                                    FirebaseUser fUser = mAuth.getCurrentUser();
//                                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(Register.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                    userId = mAuth.getCurrentUser().getUid();
//                                    DocumentReference documentReference = db.collection("users").document(userId);
//                                    Map<String, Object> userData = new HashMap<>();
//                                    userData.put("userName", userName);
//                                    userData.put("email", email);
//                                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Log.d(TAG, "onSuccess: User Profile is created for "+ userId);
//                                        }
//                                    });
//                                    startActivity(new Intent(getApplicationContext(), Login.class));
//                                } else {
//                                    Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//
//        loginText.setOnClickListener(v -> {
//            Intent intent = new Intent(Register.this, Login.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//}

package com.example.imagetotextapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText userNameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView loginText;
    private String TAG = "RegisterActivity";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameEditText = findViewById(R.id.userName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.loginText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                boolean hasError = false;

                if (TextUtils.isEmpty(userName)) {
                    userNameEditText.setError("Username harus diisi");
                    hasError = true;
                }

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email harus diisi");
                    hasError = true;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password harus diisi");
                    hasError = true;
                }

                if (password.length() < 6) {
                    passwordEditText.setError("Password Harus >= 6 Karakter");
                    hasError = true;
                }

                if (hasError) {
                    return;
                }

                checkUniqueUser(userName, email, password);
            }
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUniqueUser(String userName, String email, String password) {
        db.collection("users")
                .whereEqualTo("userName", userName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        userNameEditText.setError("Username sudah digunakan");
                    } else {
                        db.collection("users")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                                        emailEditText.setError("Email sudah digunakan");
                                    } else {
                                        registerUser(userName, email, password);
                                    }
                                });
                    }
                });
    }

    private void registerUser(String userName, String email, String password) {
        db.collection("users")
                .whereEqualTo("userName", userName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Username sudah digunakan
                        Toast.makeText(Register.this, "Username sudah digunakan. Silakan gunakan yang lain.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Periksa email
                        db.collection("users")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
                                        // Email sudah digunakan
                                        Toast.makeText(Register.this, "Email sudah digunakan. Silakan gunakan yang lain.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Username dan email unik, lanjutkan registrasi
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(authTask -> {
                                                    if (authTask.isSuccessful()) {
                                                        FirebaseUser fUser = mAuth.getCurrentUser();
                                                        if (fUser != null) {
                                                            // Kirim email verifikasi
                                                            fUser.sendEmailVerification().addOnSuccessListener(unused ->
                                                                    Toast.makeText(Register.this, "Verification Email sudah dikirimkan", Toast.LENGTH_SHORT).show()
                                                            );

                                                            // Simpan data ke Firestore
                                                            String userId = fUser.getUid();
                                                            DocumentReference documentReference = db.collection("users").document(userId);
                                                            Map<String, Object> userData = new HashMap<>();
                                                            userData.put("userName", userName);
                                                            userData.put("email", email);
                                                            documentReference.set(userData).addOnSuccessListener(unused ->
                                                                    Log.d(TAG, "onSuccess: User Profile is created for " + userId)
                                                            );

                                                            Toast.makeText(Register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                                        }
                                                    } else {
                                                        Toast.makeText(Register.this, "Error ! Tolong ubah Username / Emailmu.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}
