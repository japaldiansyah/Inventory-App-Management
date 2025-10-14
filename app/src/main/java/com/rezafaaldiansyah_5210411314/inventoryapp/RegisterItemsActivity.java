package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.Map;


public class RegisterItemsActivity extends AppCompatActivity {

    public static final String url = "http://192.168.101.89AutoDiesel/insert.php";
    ImageView imgScanKode;
    EditText etKodeBarang, etNamaBarang, etHargaBarang, etHargaBeli, etLokasi;
    TextView txtKet;
    Button btnSimpan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_items);
        imgScanKode = findViewById(R.id.scanQrCode);
        etKodeBarang = findViewById(R.id.etKodeBarang);
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etHargaBarang = findViewById(R.id.etHargaBarang);
        etHargaBeli = findViewById(R.id.etHargaBeli);
        etLokasi = findViewById(R.id.etLokasi);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        imgScanKode.setOnClickListener(v -> scanKode());
    }

    private void scanKode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("push up volume to flashlight");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CameraActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            etKodeBarang.setText(result.getContents());
        }
    });

    void inputData() {
        String kode_barang = etKodeBarang.getText().toString();
        String nama_barang = etNamaBarang.getText().toString();
        String harga_barang = etHargaBarang.getText().toString();
        String harga_beli = etHargaBeli.getText().toString();
        String lokasi = etLokasi.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tampilkan popup jika berhasil
                        showPopup("Success", "Data berhasil disimpan");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Tampilkan popup jika error
                showPopup("Error", "Tidak dapat diproses");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_barang", kode_barang);
                params.put("nama_barang", nama_barang);
                params.put("harga_barang", harga_barang);
                params.put("harga_beli", harga_beli);
                params.put("lokasi", lokasi);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    private void showPopup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Kosongkan jika tidak perlu aksi tambahan
                    }
                })
                .show();
    }
}
