package com.rezafaaldiansyah_5210411314.inventoryapp;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button btnLogout;

    private TextView tvProfileName, tvProfileEmail;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        btnLogout = findViewById(R.id.btnLogout);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfileName = findViewById(R.id.tvProfileName);

        btnLogout.setOnClickListener(v -> logout());

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        tvProfileName.setText(name != null ? name : "User");
                        tvProfileEmail.setText(email != null ? email : "User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void logout() {
        firebaseAuth.signOut();
        Toast.makeText(ProfileActivity.this, "log out berhasil!", Toast.LENGTH_SHORT).show();

        // Redirect to login screen after logout
        Intent intent = new Intent(ProfileActivity.this, FirstMenuActivty.class);
        startActivity(intent);
        finish();
    }
}