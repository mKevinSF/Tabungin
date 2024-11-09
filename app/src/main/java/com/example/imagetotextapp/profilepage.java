package com.example.imagetotextapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView greeting;
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
        greeting = findViewById(R.id.greetUserName);
        profileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(currentDate);

        DocumentReference documentReference = db.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                greeting.setText(documentSnapshot.getString("userName"));
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),)
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public void logout (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginRegister.class));
        finish();
    }
}
