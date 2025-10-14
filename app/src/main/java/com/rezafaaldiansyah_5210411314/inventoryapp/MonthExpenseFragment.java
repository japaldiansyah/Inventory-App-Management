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

public class MonthExpenseFragment extends Fragment {

    private TextView tvMonthExpenseTotalAmount, tvMonthExpenseMasukAmount, tvMonthExpenseKeluarAmount;
    private static final String TRANSACTION_URL = "http://192.168.26.36/AutoDiesel/transaction.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_expense, container, false);

        // Initialize TextViews
        tvMonthExpenseTotalAmount = view.findViewById(R.id.tvMonthExpenseTotalAmount);
        tvMonthExpenseMasukAmount = view.findViewById(R.id.tvMonthExpenseMasukAmount);
        tvMonthExpenseKeluarAmount = view.findViewById(R.id.tvMonthExpenseKeluarAmount);

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

                                // Filter by current month
                                if (isCurrentMonth(tanggal)) {
                                    total += jumlah;
                                    if ("masuk".equals(jenisTransaksi)) {
                                        masuk += jumlah;
                                    } else if ("keluar".equals(jenisTransaksi)) {
                                        keluar += jumlah;
                                    }
                                }
                            }

                            // Update TextViews
                            tvMonthExpenseTotalAmount.setText(String.valueOf(total));
                            tvMonthExpenseMasukAmount.setText(String.valueOf(masuk));
                            tvMonthExpenseKeluarAmount.setText(String.valueOf(keluar));

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

    private boolean isCurrentMonth(String tanggal) {
        // Assuming the format of `tanggal` is "yyyy-MM-dd"
        try {
            String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
            return tanggal.startsWith(currentMonth);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
