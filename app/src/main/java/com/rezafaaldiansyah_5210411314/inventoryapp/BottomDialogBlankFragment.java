package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomDialogBlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomDialogBlankFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BottomDialogBlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottomDialogBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomDialogBlankFragment newInstance(String param1, String param2) {
        BottomDialogBlankFragment fragment = new BottomDialogBlankFragment();
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
        View view = inflater.inflate(R.layout.fragment_bottom_dialog_blank, container, false);

        // Find LinearLayout by ID
        LinearLayout layoutBarangMasuk = view.findViewById(R.id.layoutBarangMasuk);
        LinearLayout layoutBarangKeluar = view.findViewById(R.id.layoutBarangKeluar);

        layoutBarangMasuk.setOnClickListener(v -> {
            // Buat Intent untuk berpindah ke BarangKeluarActivity
            Intent intent = new Intent(getActivity(), BarangMasuk.class);
            startActivity(intent); // Mulai Activity baru

        });

        layoutBarangKeluar.setOnClickListener(v -> {
            // Buat Intent untuk berpindah ke BarangKeluarActivity
            Intent intent = new Intent(getActivity(), BarangKeluar.class);
            startActivity(intent); // Mulai Activity baru

        });
        return view;
    }
}