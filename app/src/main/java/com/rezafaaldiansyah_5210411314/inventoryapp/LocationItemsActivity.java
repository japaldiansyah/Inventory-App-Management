package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationItemsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Item> itemList;
    private ItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_items);

        // Initialize UI components
        listView = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout); // SwipeRefreshLayout
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(this, itemList);
        listView.setAdapter(adapter);

        // Handle swipe-to-refresh action
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String lokasi = getIntent().getStringExtra("lokasi");
                if (lokasi != null) {
                    loadItemsByLocation(lokasi); // Reload items when swipe-to-refresh is triggered
                } else {
                    Toast.makeText(LocationItemsActivity.this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String lokasi = getIntent().getStringExtra("lokasi");
        if (lokasi != null) {
            loadItemsByLocation(lokasi); // Initial data load
        } else {
            Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadItemsByLocation(String lokasi) {
        String url = "http://192.168.26.36/AutoDiesel/lokasi_barang.php?lokasi=" + lokasi.replace(" ", "%20");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        itemList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String kode = jsonObject.getString("kode_barang");
                                String nama = jsonObject.getString("nama_barang");
                                double harga = jsonObject.getDouble("harga_barang");
                                int stock = jsonObject.getInt("stock_barang");
                                double hargaBeli = jsonObject.getDouble("harga_beli");

                                itemList.add(new Item(kode, nama, harga, stock, hargaBeli));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(LocationItemsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        } finally {
                            swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LocationItemsActivity.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }
}
