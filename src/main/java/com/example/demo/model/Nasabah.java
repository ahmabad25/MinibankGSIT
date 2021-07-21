package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Nasabah {
	private String idNasabah;
	private String firstName;
	private String lastName;
	private String alamat;
	private String noTelepon;
	private String agama;
	private char jenisKelamin;
	private String NIK;
	private String namaIbu;
	private String npwp;
	private String pekerjaan;
	private String tempatLahir;
	private String tanggalLahir;
	
	public String getIdNasabah() {
		return idNasabah;
	}
	public void setIdNasabah(String idNasabah) {
		this.idNasabah = idNasabah;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getNoTelepon() {
		return noTelepon;
	}
	public void setNoTelepon(String noTelepon) {
		this.noTelepon = noTelepon;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	public char getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(char jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	public String getNIK() {
		return NIK;
	}
	public void setNIK(String nIK) {
		NIK = nIK;
	}
	public String getNamaIbu() {
		return namaIbu;
	}
	public void setNamaIbu(String namaIbu) {
		this.namaIbu = namaIbu;
	}
	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}
	public String getPekerjaan() {
		return pekerjaan;
	}
	public void setPekerjaan(String pekerjaan) {
		this.pekerjaan = pekerjaan;
	}
	public String getTempatLahir() {
		return tempatLahir;
	}
	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
	public String getTanggalLahir() {
		return tanggalLahir;
	}
	public void setTanggalLahir(String tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
}
