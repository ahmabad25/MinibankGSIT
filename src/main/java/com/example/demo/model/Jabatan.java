package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Jabatan {
	private int idJabatan;
	private String namaJabatan;
	
	public int getIdJabatan() {
		return idJabatan;
	}
	public void setIdJabatan(int idJabatan) {
		this.idJabatan = idJabatan;
	}
	public String getNamaJabatan() {
		return namaJabatan;
	}
	public void setNamaJabatan(String namaJabatan) {
		this.namaJabatan = namaJabatan;
	}
}
