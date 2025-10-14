package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String url = "http://192.168.26.36/AutoDiesel/transaction.php"; // URL untuk mengambil riwayat transaksi
    ListView listView;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Data> transactionList = new ArrayList<>();
    TransactionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the adapter and other setup if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        listView = view.findViewById(R.id.listViewTransaksi);
        swipeRefreshLayout = view.findViewById(R.id.swipeRiwayatTransaksi);

        LinearLayout layoutStockMasukKeluar = view.findViewById(R.id.layoutStockMasukKeluar);

        // Set up the adapter
        adapter = new TransactionAdapter(getActivity(), transactionList);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                transactionList.clear();
                adapter.notifyDataSetChanged();
                fetchTransactionHistory();
            }
        });

        layoutStockMasukKeluar.setOnClickListener(v -> {
            BottomDialogBlankFragment bottomDialogBlankFragment = new BottomDialogBlankFragment();
            bottomDialogBlankFragment.show(getParentFragmentManager(), bottomDialogBlankFragment.getTag());
        });

        return view;
    }
    private String formatTanggal(String tanggalAsli) {
        try {
            // Format awal sesuai dengan data dari server
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());

            Date date = inputFormat.parse(tanggalAsli);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            // Jika terjadi error, kembalikan tanggal asli
            return tanggalAsli;
        }
    }

    private void fetchTransactionHistory() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Data transaction = new Data();

                        transaction.setTxtKodeBarang(jsonObject.getString("kode_barang"));
                        transaction.setTxtJumlah(jsonObject.getString("jumlah"));
                        transaction.setTxtCatatan(jsonObject.getString("catatan"));

                        String tanggalAsli = jsonObject.getString("tanggal");
                        String tanggalFormatted = formatTanggal(tanggalAsli);
                        transaction.setTxtTanggal(tanggalFormatted);

                        transaction.setTxtJenisTransaksi(jsonObject.getString("jenis_transaksi"));

                        transactionList.add(transaction);
                    }

                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        transactionList.clear();
        adapter.notifyDataSetChanged();
        fetchTransactionHistory();
    }
}
