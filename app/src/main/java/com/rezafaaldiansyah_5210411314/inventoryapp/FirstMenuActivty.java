package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class FirstMenuActivty extends AppCompatActivity {
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_menu_activty);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Mengarahkan ke LoginActivity saat tombol Login ditekan
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(FirstMenuActivty.this, LoginActivity.class));
        });

        // Mengarahkan ke RegisterActivity saat tombol Register ditekan
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(FirstMenuActivty.this, RegisterActivity.class));
        });
    }
}