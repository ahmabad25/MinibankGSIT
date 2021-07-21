package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Klien;

@Repository
public interface KlienDAO {
	
	public List<Klien> getAll ();
	public String insert(String idKlien, Klien kln);
	public String update(Klien kln);
	public String delete(String id);
	public Klien getOne(String id);
	public String generateId();

	public int autentikasi(Klien klien);

}
