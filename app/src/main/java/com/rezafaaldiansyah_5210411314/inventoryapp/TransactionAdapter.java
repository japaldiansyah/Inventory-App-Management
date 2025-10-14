package com.rezafaaldiansyah_5210411314.inventoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private List<Data> dataList;

    public TransactionAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list2, parent, false);
        }

        Data data = dataList.get(position);

        TextView kodeBarang = convertView.findViewById(R.id.txtKodeBarang);
        TextView jumlah = convertView.findViewById(R.id.txtJumlah);
        TextView catatan = convertView.findViewById(R.id.txtCatatan);
        TextView tanggal = convertView.findViewById(R.id.txtTanggal);
        TextView jenisTransaksi = convertView.findViewById(R.id.txtJenisTransaksi);

        kodeBarang.setText(data.getTxtKodeBarang());
        jumlah.setText(data.getTxtJumlah());
        catatan.setText(data.getTxtCatatan());
        tanggal.setText(data.getTxtTanggal());
        jenisTransaksi.setText(data.getTxtJenisTransaksi());

        return convertView;
    }
}
