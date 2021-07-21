package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Pegawai;

@Repository
public interface PegawaiDAO {
	public List<Pegawai> getAll();
	public Pegawai getOne(String idPegawai);
	public String delete(String idPegawai);
	public String insert(Pegawai pegawai);
	public String update(Pegawai pegawai);
	public String generateId();
	public String ambilTanggal();
	public List<Pegawai> getUserPegawai();
}
