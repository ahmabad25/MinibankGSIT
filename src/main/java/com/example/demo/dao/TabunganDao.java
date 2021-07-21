package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Tabungan;

@Repository
public interface TabunganDao {
	
	public List<Tabungan> getAll();
	public Tabungan getOne(String tabungan);
	public String insert (Tabungan tabungan);
	public String update (Tabungan tabungan);
	public String NonAktif(String idTabungan);
	public String Aktif(String idTabungan);
	public String generateId();
	public String generateNorek();
	public String ambilTanggal();
	public List<Tabungan> getKlienTabungan();
	
	public String getIdTabungan(int noRek);
	public String getIdNasabah(int noRek);
	public boolean cekPin(String encPin, String idKlien, String idTabungan);
}
