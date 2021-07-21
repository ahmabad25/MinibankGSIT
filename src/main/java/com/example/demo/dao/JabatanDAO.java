package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Jabatan;

@Repository
public interface JabatanDAO {
	public List<Jabatan> getAll();
	public Jabatan getOne(int idJabatan);
	int genereteId();
	String insert(Jabatan jabatan);
	String delete(int idJabatan);
	String update(Jabatan jabatan);

}
