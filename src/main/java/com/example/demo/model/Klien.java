package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Klien {
	
	private String idKlien;
	private String username;
	private String password;
	private String idTabungan;
	
	public String getIdKlien() {
		return idKlien;
	}
	public void setIdKlien(String idKlien) {
		this.idKlien = idKlien;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdTabungan() {
		return idTabungan;
	}
	public void setIdTabungan(String idTabungan) {
		this.idTabungan = idTabungan;
	}
}
