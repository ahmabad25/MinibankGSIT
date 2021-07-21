package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Transaksi {
	private String idTransaksiMasuk;
	private String idTabungan;
	private int nominal;
	private String tanggalMasuk;
	private String keterangan;
	private String idTransaksiKeluar;
	private String pengirim;
	private String penerima;
	private String tanggalKeluar;
	private String idHistori;
	private int saldoMasuk;
	private int saldoKeluar;
	private int totalSaldo;
	private String tanggalTransaksi;
	private String beritaTransfer;
	
	public String getIdTransaksiMasuk() {
		return idTransaksiMasuk;
	}
	public void setIdTransaksiMasuk(String idTransaksiMasuk) {
		this.idTransaksiMasuk = idTransaksiMasuk;
	}
	public String getIdTabungan() {
		return idTabungan;
	}
	public void setIdTabungan(String idTabungan) {
		this.idTabungan = idTabungan;
	}
	public int getNominal() {
		return nominal;
	}
	public void setNominal(int nominal) {
		this.nominal = nominal;
	}
	public String getTanggalMasuk() {
		return tanggalMasuk;
	}
	public void setTanggalMasuk(String tanggalMasuk) {
		this.tanggalMasuk = tanggalMasuk;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getIdTransaksiKeluar() {
		return idTransaksiKeluar;
	}
	public void setIdTransaksiKeluar(String idTransaksiKeluar) {
		this.idTransaksiKeluar = idTransaksiKeluar;
	}
	public String getPengirim() {
		return pengirim;
	}
	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}
	public String getPenerima() {
		return penerima;
	}
	public void setPenerima(String penerima) {
		this.penerima = penerima;
	}
	public String getTanggalKeluar() {
		return tanggalKeluar;
	}
	public void setTanggalKeluar(String tanggalKeluar) {
		this.tanggalKeluar = tanggalKeluar;
	}
	public String getIdHistori() {
		return idHistori;
	}
	public void setIdHistori(String idHistori) {
		this.idHistori = idHistori;
	}
	public int getSaldoMasuk() {
		return saldoMasuk;
	}
	public void setSaldoMasuk(int saldoMasuk) {
		this.saldoMasuk = saldoMasuk;
	}
	public int getSaldoKeluar() {
		return saldoKeluar;
	}
	public void setSaldoKeluar(int saldoKeluar) {
		this.saldoKeluar = saldoKeluar;
	}
	public int getTotalSaldo() {
		return totalSaldo;
	}
	public void setTotalSaldo(int totalSaldo) {
		this.totalSaldo = totalSaldo;
	}
	public String getTanggalTransaksi() {
		return tanggalTransaksi;
	}
	public void setTanggalTransaksi(String tanggalTransaksi) {
		this.tanggalTransaksi = tanggalTransaksi;
	}
	public String getBeritaTransfer() {
		return beritaTransfer;
	}
	public void setBeritaTransfer(String beritaTransfer) {
		this.beritaTransfer = beritaTransfer;
	}
}
