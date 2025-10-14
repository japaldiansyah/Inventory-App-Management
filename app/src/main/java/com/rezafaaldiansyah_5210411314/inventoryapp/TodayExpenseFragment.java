package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayExpenseFragment extends Fragment {

    private TextView tvToDayExpenseTotalAmount, tvToDayExpenseMasukAmount, tvToDayExpenseKeluarAmount;
    private static final String TRANSACTION_URL = "http://192.168.26.36/AutoDiesel/transaction.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_expense, container, false);

        // Initialize TextViews
        tvToDayExpenseTotalAmount = view.findViewById(R.id.tvTodayExpenseTotalAmount);
        tvToDayExpenseMasukAmount = view.findViewById(R.id.tvTodayExpenseMasukAmount);
        tvToDayExpenseKeluarAmount = view.findViewById(R.id.tvTodayExpenseKeluarAmount);

        // Load transaction data
        loadTransactionData();

        return view;
    }

    private void loadTransactionData() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, TRANSACTION_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int total = 0;
                            int masuk = 0;
                            int keluar = 0;

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject transaction = response.getJSONObject(i);

                                String jenisTransaksi = transaction.getString("jenis_transaksi");
                                int jumlah = transaction.getInt("jumlah");
                                String tanggal = transaction.getString("tanggal");

                                // Filter by current day
                                if (isToday(tanggal)) {
                                    total += jumlah;
                                    if ("masuk".equals(jenisTransaksi)) {
                                        masuk += jumlah;
                                    } else if ("keluar".equals(jenisTransaksi)) {
                                        keluar += jumlah;
                                    }
                                }
                            }

                            // Update TextViews
                            tvToDayExpenseTotalAmount.setText(String.valueOf(total));
                            tvToDayExpenseMasukAmount.setText(String.valueOf(masuk));
                            tvToDayExpenseKeluarAmount.setText(String.valueOf(keluar));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private boolean isToday(String tanggal) {
        try {
            // Parsing tanggal dari server (format datetime)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(tanggal);

            // Format tanggal hari ini
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = outputFormat.format(date);

            // Bandingkan dengan tanggal hari ini
            String currentDate = outputFormat.format(new Date());
            return formattedDate.equals(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
