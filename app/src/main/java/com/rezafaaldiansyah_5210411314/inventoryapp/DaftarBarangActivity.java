package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DaftarBarangActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String url = "http://192.168.26.36/AutoDiesel/select.php";
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    BarangAdapter adapter;
    LayoutInflater inflater;
    String kode_barang, nama_barang, harga_barang, harga_beli;
    ImageView imgScanKode;

    Button btnSelesai;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_barang);
        searchView = findViewById(R.id.searchView2);
        imgScanKode = findViewById(R.id.scanQrCode);
        itemList = new ArrayList<>();
        searchView.clearFocus();
        imgScanKode.setOnClickListener(v -> scanKode());
        Button selesaiButton = findViewById(R.id.btnSelesai);
        selesaiButton.setOnClickListener(v -> {
            Intent intent = new Intent(DaftarBarangActivity.this, BarangMasuk.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.listView);

        adapter = new BarangAdapter(DaftarBarangActivity.this, itemList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data selectedItem = itemList.get(position);
                showInputStockDialog(selectedItem);
            }
        });

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
    }
    private void showInputStockDialog(Data selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Stok Barang");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Hanya input angka
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String stock = input.getText().toString();
            if (!stock.isEmpty()) {
                // Simpan data stok barang
                saveStock(selectedItem, Integer.parseInt(stock));
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Masukkan jumlah stok", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveStock(Data selectedItem, int stock) {
        SharedPreferences sharedPreferences = getSharedPreferences("InventoryApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Simpan data dengan kode barang sebagai kunci
        editor.putInt(selectedItem.getTxtKodeBarang(), stock);
        editor.apply();
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
            // Set hasil pemindaian ke dalam SearchView
            searchView.setQuery(result.getContents(), true);
        }
    });

    private void filterList(String newText) {
        List<Data> fillteredList = new ArrayList<>();
        for (Data data: itemList){
            if (data.getTxtKodeBarang().toLowerCase().contains(newText.toLowerCase()) ||
                    data.getTxtNamaBarang().toLowerCase().contains(newText.toLowerCase())
                    || data.getTxtHargaBarang().toLowerCase().contains(newText.toLowerCase())
                    || data.getTxtHargaBeli().toLowerCase().contains(newText.toLowerCase())){
                fillteredList.add(data);
            }
        }
        if (fillteredList.isEmpty()){
            Toast.makeText(this, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(fillteredList);
        }

    }

    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();

    }

    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setTxtKodeBarang(obj.getString("kode_barang"));
                        item.setTxtNamaBarang(obj.getString("nama_barang"));
                        item.setTxtHargaBarang(obj.getString("harga_barang"));
                        item.setTxtHargaBeli(obj.getString("harga_beli"));
                        item.setTxtStockBarang(obj.getString("stock_barang"));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }

        });

        // menambah request ke request queue
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }
}