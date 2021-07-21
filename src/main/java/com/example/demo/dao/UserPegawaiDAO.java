package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.example.demo.model.UserPegawai;

@Repository
public interface UserPegawaiDAO {
	public List<UserPegawai> getAll();
	public UserPegawai getOne(String idUser);
	public String delete(String id);
	public String insert(String userId, UserPegawai userPegawai);
	public String update(UserPegawai userpegawai);
	public String generateId();
	public Map<String,String> autentikasi(UserPegawai userPegawai);
	public String generateUsername(int idJabatan);
}
