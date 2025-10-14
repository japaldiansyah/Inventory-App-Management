package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText etNama, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi UI
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Tombol Register
        btnRegister.setOnClickListener(v -> {
            String name = etNama.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();


            // Validasi input
            if (TextUtils.isEmpty(name)) {
                etNama.setError("Name is required!");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required!");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters!");
                return;
            }

            // Mendaftarkan pengguna
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();

                                // Simpan nama dan email ke database
                                User newUser = new User(name, email);
                                databaseReference.child(userId).setValue(newUser)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                                                // Pindah ke FirstMenuActivity
                                                Intent intent = new Intent(RegisterActivity.this, FirstMenuActivty.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Gagal menyimpan data pengguna!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });



        // Pindah ke halaman login
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Model class untuk data pengguna
    public static class User {
        public String name;
        public String email;

        public User() {
            // Default constructor (diperlukan oleh Firebase)
        }

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
