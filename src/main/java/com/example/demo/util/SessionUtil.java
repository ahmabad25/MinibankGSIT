package com.example.demo.util;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface SessionUtil {
	
	public int idOtomatis();
	public Map<String, String> getSession(String idUser);
	public void insertSession(Map<String, String> id);
	public void deleteSession(Map<String, String> id);
	public boolean cekSession(String idUser);
	public boolean validateSession(Map<String, String> id);
}
