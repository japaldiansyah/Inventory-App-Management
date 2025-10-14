package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BarangKeluar extends AppCompatActivity {

    public static final String url = "http://192.168.60.172/AutoDiesel/insert_keluar.php";
    LinearLayout layTanggal;
    LinearLayout layoutInputCatatan;
    TextView txtViewDate, etCatatan;
    Button btnSelesai;
    EditText etKodeBarang, etStockBarang;
    ImageView imgScanKode;
    ListView stockListView;
    List<String> stockList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String catatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_keluar);

        imgScanKode = findViewById(R.id.scanQrCode);
        layTanggal = findViewById(R.id.layTanggal);
        layoutInputCatatan = findViewById(R.id.layoutInputCatatan);
        etKodeBarang = findViewById(R.id.etKodeBarang);
        etCatatan = findViewById(R.id.etCatatan);
        etStockBarang = findViewById(R.id.etStockBarang);
        txtViewDate = findViewById(R.id.txtViewDate);
        btnSelesai = findViewById(R.id.btnSimpan);

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        imgScanKode.setOnClickListener(v -> scanKode());

        String currentDate = new SimpleDateFormat("dd MMMM, yyyy HH:mm", Locale.getDefault()).format(new Date());
        txtViewDate.setText(currentDate);

        ActivityResultLauncher<Intent> catatanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        catatan = result.getData().getStringExtra("CATATAN");
                        // Tampilkan catatan di Log atau di UI
                        etCatatan.setText(catatan);
                    }
                }
        );

        layoutInputCatatan.setOnClickListener(v -> {
            Intent intent = new Intent(BarangKeluar.this, CatatanActivity.class);
            catatanLauncher.launch(intent);
        });

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
        String jumlah_keluar = etStockBarang.getText().toString();
        String catatan = etCatatan.getText().toString();
        String tanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

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
                params.put("jumlah_keluar", jumlah_keluar);
                params.put("catatan", catatan);
                params.put("tanggal", tanggal);
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