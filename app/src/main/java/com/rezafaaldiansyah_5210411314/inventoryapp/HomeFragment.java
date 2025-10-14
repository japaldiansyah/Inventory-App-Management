package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private ExpensePagerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView tvUserName;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Mengambil referensi ke LinearLayout setelah inflating layout
        LinearLayout daftarBarang = view.findViewById(R.id.DaftarBarang);
        daftarBarang.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterItemsActivity.class);
            startActivity(intent);
        });

        LinearLayout laporanBarang = view.findViewById(R.id.LaporanBarang);
        laporanBarang.setOnClickListener(v -> exportDaftarBarangToPdf());

        return view; // Mengembalikan view yang di-inflate
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi Firebase dan referensi database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Inisialisasi UI
        tvUserName = view.findViewById(R.id.tvUserName);
        ImageView ivProfile = view.findViewById(R.id.ivProfile);

        ivProfile.setOnClickListener(v -> openProfile());

        // Inisialisasi ViewPager2 dan Adapter
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new ExpensePagerAdapter(getActivity());
        viewPager.setAdapter(adapter);

        // Ambil data pengguna dari Firebase
        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("Firebase", "Current User ID: " + userId);

            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        Log.d("Firebase", "User Name: " + name); // Debugging log

                        tvUserName.setText(name != null ? name : "User");
                    } else {
                        Log.d("Firebase", "Snapshot does not exist for User ID: " + userId);
                        tvUserName.setText("User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to read user data: " + error.getMessage());
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("Firebase", "CurrentUser is null");
            tvUserName.setText("Guest");
        }
    }
    private void exportDaftarBarangToPdf() {
        String url = "http://192.168.26.36/AutoDiesel/laporan_barang.php"; // Ganti dengan URL asli

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Lokasi penyimpanan di folder Downloads
                        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        if (!downloadsDir.exists()) {
                            downloadsDir.mkdirs(); // Buat folder jika belum ada
                        }
                        File file = new File(downloadsDir, "DaftarBarang.pdf");

                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(file));
                        document.open();

                        // Font
                        Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
                        Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

                        // Judul
                        Paragraph title = new Paragraph("LAPORAN DAFTAR BARANG", titleFont);
                        title.setAlignment(Element.ALIGN_CENTER);
                        title.setSpacingAfter(15f);
                        document.add(title);

                        // Tanggal di kiri atas
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        Paragraph date = new Paragraph("Tanggal: " + currentDate, dateFont);
                        date.setAlignment(Element.ALIGN_LEFT);
                        date.setSpacingAfter(10f);
                        document.add(date);

                        // Tabel
                        PdfPTable table = new PdfPTable(6);
                        table.setWidthPercentage(100);
                        table.setSpacingBefore(5f);

                        table.addCell("Kode");
                        table.addCell("Nama");
                        table.addCell("Stok");
                        table.addCell("Harga Barang");
                        table.addCell("Harga Beli");
                        table.addCell("Lokasi");

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);
                            table.addCell(item.getString("kode_barang"));
                            table.addCell(item.getString("nama_barang"));
                            table.addCell(item.getString("stock_barang"));
                            table.addCell(item.getString("harga_barang"));
                            table.addCell(item.getString("harga_beli"));
                            table.addCell(item.getString("lokasi"));
                        }

                        document.add(table);
                        document.close();

                        Toast.makeText(getContext(), "PDF disimpan di: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Gagal membuat PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }


    private void openProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }
}
