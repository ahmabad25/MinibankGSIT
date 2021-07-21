package com.example.demo.util;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Token;

@Repository
public interface TokenUtil {
	
	public int idOtomatis();
	public Token getToken(String id);
	public void insertToken(Token token);
	public void deleteToken(Token token);
	public Map<String, String> generateToken(String idKlien);
	public boolean cekSession(String id);
	public boolean validateToken(Token token);
}
