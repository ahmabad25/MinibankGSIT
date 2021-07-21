package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Nasabah;

@Repository
public interface NasabahDAO {
	
	public List<Nasabah> getAll();
	public Nasabah getOne(String id);
	public String insert(Nasabah nasabah);
	public String update(Nasabah nasabah);
	public String delete(String id);
	public String generateId();
}
