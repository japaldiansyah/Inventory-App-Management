package com.rezafaaldiansyah_5210411314.inventoryapp;

public class Data {
    private String txtKodeBarang, txtNamaBarang, txtHargaBarang, txtHargaBeli, txtStockBarang;
    private String txtJumlah, txtCatatan, txtTanggal, txtJenisTransaksi;

    // Konstruktor tanpa argumen
    public Data() {
    }

    // Konstruktor dengan parameter untuk barang dan transaksi
    public Data(String txtKodeBarang, String txtNamaBarang, String txtHargaBarang,
                String txtHargaBeli, String txtStockBarang, String txtJumlah,
                String txtCatatan, String txtTanggal, String txtJenisTransaksi) {
        this.txtKodeBarang = txtKodeBarang;
        this.txtNamaBarang = txtNamaBarang;
        this.txtHargaBarang = txtHargaBarang;
        this.txtHargaBeli = txtHargaBeli;
        this.txtStockBarang = txtStockBarang;
        this.txtJumlah = txtJumlah;
        this.txtCatatan = txtCatatan;
        this.txtTanggal = txtTanggal;
        this.txtJenisTransaksi = txtJenisTransaksi;
    }

    // Getter dan Setter untuk setiap properti

    public String getTxtKodeBarang() {
        return txtKodeBarang;
    }

    public void setTxtKodeBarang(String txtKodeBarang) {
        this.txtKodeBarang = txtKodeBarang;
    }

    public String getTxtNamaBarang() {
        return txtNamaBarang;
    }

    public void setTxtNamaBarang(String txtNamaBarang) {
        this.txtNamaBarang = txtNamaBarang;
    }

    public String getTxtHargaBarang() {
        return txtHargaBarang;
    }

    public void setTxtHargaBarang(String txtHargaBarang) {
        this.txtHargaBarang = txtHargaBarang;
    }

    public String getTxtHargaBeli() {
        return txtHargaBeli;
    }

    public void setTxtHargaBeli(String txtHargaBeli) {
        this.txtHargaBeli = txtHargaBeli;
    }

    public String getTxtStockBarang() {
        return txtStockBarang;
    }

    public void setTxtStockBarang(String txtStockBarang) {
        this.txtStockBarang = txtStockBarang;
    }

    // Getter dan Setter untuk transaksi
    public String getTxtJumlah() {
        return txtJumlah;
    }

    public void setTxtJumlah(String txtJumlah) {
        this.txtJumlah = txtJumlah;
    }

    public String getTxtCatatan() {
        return txtCatatan;
    }

    public void setTxtCatatan(String txtCatatan) {
        this.txtCatatan = txtCatatan;
    }

    public String getTxtTanggal() {
        return txtTanggal;
    }

    public void setTxtTanggal(String txtTanggal) {
        this.txtTanggal = txtTanggal;
    }

    public String getTxtJenisTransaksi() {
        return txtJenisTransaksi;
    }

    public void setTxtJenisTransaksi(String txtJenisTransaksi) {
        this.txtJenisTransaksi = txtJenisTransaksi;
    }
}
