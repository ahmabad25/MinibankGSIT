package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Tabungan {
	
	private String idTabungan;
	private int noRekening;
	private String pin;
	private String tanggalPembukaan;
	private String status;
	private String idNasabah;
	private String nama;
	private int nominal;
	private String idTransaksiMasuk;
	private String idHistori;
	
	public String getIdTabungan() {
		return idTabungan;
	}
	public void setIdTabungan(String idTabungan) {
		this.idTabungan = idTabungan;
	}
	public int getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(int noRekening) {
		this.noRekening = noRekening;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getTanggalPembukaan() {
		return tanggalPembukaan;
	}
	public void setTanggalPembukaan(String tanggalPembukaan) {
		this.tanggalPembukaan = tanggalPembukaan;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIdNasabah() {
		return idNasabah;
	}
	public void setIdNasabah(String idNasabah) {
		this.idNasabah = idNasabah;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public int getNominal() {
		return nominal;
	}
	public void setNominal(int nominal) {
		this.nominal = nominal;
	}
	public String getIdTransaksiMasuk() {
		return idTransaksiMasuk;
	}
	public void setIdTransaksiMasuk(String idTransaksiMasuk) {
		this.idTransaksiMasuk = idTransaksiMasuk;
	}
	public String getIdHistori() {
		return idHistori;
	}
	public void setIdHistori(String idHistori) {
		this.idHistori = idHistori;
	}
	
	
}
