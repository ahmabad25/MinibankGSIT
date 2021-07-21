package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Pegawai {

	private String idPegawai;
	private String firstName;
	private String lastName;
	private char jenisKelamin;
	private String agama;
	private String phoneNumber;
	private String alamat;
	private String idJabatan;
	private String tanggalMasuk;
	private String tempatLahir;
	private String tanggalLahir;
	
	public String getIdPegawai() {
		return idPegawai;
	}
	public void setIdPegawai(String idPegawai) {
		this.idPegawai = idPegawai;
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
	public char getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(char jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getIdJabatan() {
		return idJabatan;
	}
	public void setIdJabatan(String idJabatan) {
		this.idJabatan = idJabatan;
	}
	public String getTanggalMasuk() {
		return tanggalMasuk;
	}
	public void setTanggalMasuk(String tanggalMasuk) {
		this.tanggalMasuk = tanggalMasuk;
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
