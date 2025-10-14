package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CatatanActivity extends AppCompatActivity {
    private EditText editTextNote;
    private Button buttonSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan);

        editTextNote = findViewById(R.id.editTextNote);
        buttonSave = findViewById(R.id.buttonSave);

        SharedPreferences sharedPreferences = getSharedPreferences("InventoryApp", MODE_PRIVATE);

        // Load saved note if it exists
        String savedNote = sharedPreferences.getString("catatan", "");
        editTextNote.setText(savedNote);

        buttonSave.setOnClickListener(v -> {
            String catatan = editTextNote.getText().toString().trim();
            // Kirim data kembali ke Fragment menggunakan Intent
            Intent resultIntent = new Intent();
            resultIntent.putExtra("CATATAN", catatan);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void saveNote() {
        String note = editTextNote.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("catatan", note);
        editor.apply(); // Save the note
    }
}
