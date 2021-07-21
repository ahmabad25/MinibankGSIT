package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Token {
	
	private String idKlien;
	private String token;
	private String expireTime;
	
	public String getIdKlien() {
		return idKlien;
	}
	public void setIdKlien(String idKlien) {
		this.idKlien = idKlien;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
}
