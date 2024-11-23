//package com.example.imagetotextapp;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Button;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.ListenerRegistration;
//
//import java.text.DateFormat;
//import java.util.Calendar;
//
//public class profilepage extends AppCompatActivity {
//
//    TextView userName, email;
//    FirebaseAuth mAuth;
//    FirebaseFirestore db;
//    Button logOutButton, changeProfile;
//    ImageView profileImage;
//    String userId;
//    ListenerRegistration listenerRegistration;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        userName = findViewById(R.id.greetUserName);
//        email = findViewById(R.id.email);
//        profileImage = findViewById(R.id.profileImage);
//        logOutButton = findViewById(R.id.logout);
//        changeProfile = findViewById(R.id.changeProfile);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        userId = mAuth.getCurrentUser().getUid();
//        Calendar calendar = Calendar.getInstance();
//        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView dateText = findViewById(R.id.dateText);
//        dateText.setText(currentDate);
//
//        DocumentReference documentReference = db.collection("users").document(userId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if(e != null){
//                    return;
//                }
//                userName.setText(documentSnapshot.getString("userName"));
//                email.setText(documentSnapshot.getString("email"));
//            }
//        });
//
//        changeProfile.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), editProfile.class);
//                i.putExtra("userName", userName.getText().toString());
//                i.putExtra("email", email.getText().toString());
//                startActivity(i);
//            }
////            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////            startActivityForResult(openGalleryIntent, 1000);
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(listenerRegistration != null) {
//            listenerRegistration.remove();
//        }
//    }
//
//    public void logout (View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(), LoginRegister.class));
//        finish();
//    }
//
//}
package com.example.imagetotextapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.DateFormat;
import java.util.Calendar;

public class profilepage extends AppCompatActivity {
    ImageView profileIcon, cameraIcon, homeIcon;
    TextView userName, email;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button logOutButton, changeProfile;
    ImageView profileImage;
    String userId;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi view
        profileIcon = findViewById(R.id.profileIcon);
        cameraIcon = findViewById(R.id.cameraIcon);
        homeIcon = findViewById(R.id.homeIcon);
        userName = findViewById(R.id.greetUserName);
        email = findViewById(R.id.email);
        profileImage = findViewById(R.id.profileImage);
        logOutButton = findViewById(R.id.logout);
        changeProfile = findViewById(R.id.changeProfile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(currentDate);

        // Listener untuk ikon home
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilepage.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });

        // Listener untuk ikon profile
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilepage.this, profilepage.class);
                startActivity(intent);
                finish();
            }
        });

        // Listener untuk ikon kamera
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilepage.this, input.class); // Ubah sesuai nama Activity Anda
                startActivity(intent);
                finish();
            }
        });

        DocumentReference documentReference = db.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    userName.setText(documentSnapshot.getString("userName"));
                    email.setText(documentSnapshot.getString("email"));
                }
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), editProfile.class);
                i.putExtra("userName", userName.getText().toString());
                i.putExtra("email", email.getText().toString());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginRegister.class));
        finish();
    }
}