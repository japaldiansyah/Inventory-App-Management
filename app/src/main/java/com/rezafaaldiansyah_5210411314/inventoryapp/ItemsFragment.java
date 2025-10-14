package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    public static final String url = "http://192.168.26.36/AutoDiesel/select.php";
    private static final String CHANNEL_ID = "low_stock_alert_channel"; // ID Channel Notifikasi

    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<>();
    BarangAdapter adapter;
    SearchView searchView;

    ImageView scanQrCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_items, container, false);

        searchView = view.findViewById(R.id.searchView2);
        swipe = view.findViewById(R.id.swipe);
        list = view.findViewById(R.id.listView);

        scanQrCode=view.findViewById(R.id.scanQrCode);
        scanQrCode.setOnClickListener(v -> scanKode());

        itemList = new ArrayList<>();
        adapter = new BarangAdapter(getActivity(), itemList);
        list.setAdapter(adapter);

        // Buat Notification Channel
        createNotificationChannel();

        // Setup SwipeRefreshLayout
        swipe.setOnRefreshListener(() -> {
            itemList.clear();
            adapter.notifyDataSetChanged();
            callVolley();
        });

        swipe.post(() -> {
            swipe.setRefreshing(true);
            itemList.clear();
            adapter.notifyDataSetChanged();
            callVolley();
        });

        // Setup SearchView
        searchView.clearFocus();
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

        // Setup FloatingActionButton
        FloatingActionButton fab = view.findViewById(R.id.btn_reg);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterItemsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Low Stock Alerts";
            String description = "Channel untuk notifikasi stok barang menipis";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void filterList(String newText) {
        List<Data> filteredList = new ArrayList<>();
        for (Data data : itemList) {
            if (data.getTxtKodeBarang().toLowerCase().contains(newText.toLowerCase()) ||
                    data.getTxtNamaBarang().toLowerCase().contains(newText.toLowerCase())
                    || data.getTxtHargaBarang().toLowerCase().contains(newText.toLowerCase())
                    || data.getTxtHargaBeli().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(data);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "Barang tidak ditemukan", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();
                        item.setTxtKodeBarang(obj.getString("kode_barang"));
                        item.setTxtNamaBarang(obj.getString("nama_barang"));
                        item.setTxtHargaBarang(obj.getString("harga_barang"));
                        item.setTxtHargaBeli(obj.getString("harga_beli"));
                        item.setTxtStockBarang(obj.getString("stock_barang"));

                        itemList.add(item);

                        // Cek stok barang
                        int stock = Integer.parseInt(obj.getString("stock_barang"));
                        if (stock < 5) { // Misalnya, stok < 10 dianggap menipis
                            sendLowStockNotification(item.getTxtNamaBarang(), stock);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(requireContext());
        mRequestQueue.add(jArr);
    }

    private void sendLowStockNotification(String itemName, int stock) {
        // Pesan notifikasi
        String message = "Stok barang " + itemName + " menipis. Tersisa " + stock + " unit.";

        // Buat notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Peringatan Stok Menipis")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Kirim notifikasi
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(itemName.hashCode(), builder.build());
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
}
