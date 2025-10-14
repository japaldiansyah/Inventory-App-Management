package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StorageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StorageFragment() {
        // Required empty public constructor
    }

    public static StorageFragment newInstance(String param1, String param2) {
        StorageFragment fragment = new StorageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout lokasi1 = view.findViewById(R.id.lokasi1);
        LinearLayout lokasi2 = view.findViewById(R.id.lokasi2);
        LinearLayout lokasi3 = view.findViewById(R.id.lokasi3);

        lokasi1.setOnClickListener(v -> openLocationActivity("Lokasi 1"));
        lokasi2.setOnClickListener(v -> openLocationActivity("Lokasi 2"));
        lokasi3.setOnClickListener(v -> openLocationActivity("Lokasi 3"));
    }

    private void openLocationActivity(String lokasi) {
        Intent intent = new Intent(getActivity(), LocationItemsActivity.class);
        intent.putExtra("lokasi", lokasi);
        startActivity(intent);
    }
}
