package com.rezafaaldiansyah_5210411314.inventoryapp;

public class Item {
    private String kode;
    private String nama;
    private double harga;
    private int stock;
    private double hargaBeli;

    public Item(String kode, String nama, double harga, int stock, double hargaBeli) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stock = stock;
        this.hargaBeli = hargaBeli;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStock() { return stock; }
    public double getHargaBeli() { return hargaBeli; }
}

