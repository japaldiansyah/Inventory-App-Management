package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        Item currentItem = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvStock = convertView.findViewById(R.id.tvItemStock);

        tvName.setText(currentItem.getNama());
        tvStock.setText("Stock: " + currentItem.getStock());

        return convertView;
    }
}
