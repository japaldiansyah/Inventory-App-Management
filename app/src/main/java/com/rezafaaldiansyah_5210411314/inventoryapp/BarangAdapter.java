package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BarangAdapter extends BaseAdapter {

    Activity activity;

    private LayoutInflater inflater;
    public List<Data> items;

    public BarangAdapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }
    public void setFilteredList(List<Data> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    public void updateList(List<Data> filteredList) {
        items.clear();
        items.addAll(filteredList);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) convertView = inflater.inflate(R.layout.list, null);
        TextView namaBarang = (TextView) convertView.findViewById(R.id.txtNamaBarang);
        TextView hargaBarang = (TextView) convertView.findViewById(R.id.txtHargaBarang);
        TextView hargaBeli = (TextView) convertView.findViewById(R.id.txtHargaBeli);
        TextView stockBarang = (TextView) convertView.findViewById(R.id.txtStockBarang);

        Data data = items.get(position);

        namaBarang.setText(data.getTxtNamaBarang());
        hargaBarang.setText(data.getTxtHargaBarang());
        hargaBeli.setText(data.getTxtHargaBeli());
        stockBarang.setText(data.getTxtStockBarang());

        return convertView;

    }
}
