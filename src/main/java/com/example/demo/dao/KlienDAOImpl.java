package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Klien;

@Component
public class KlienDAOImpl implements KlienDAO {

	@Autowired
	Klien kln;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public List<Klien> getAll() {
		List<Klien> allkln = new ArrayList<Klien>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_klien, username, id_tabungan FROM KLIEN ORDER BY id_klien DESC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Klien klien = new Klien(); 
				klien.setIdKlien(rs.getString(1));
				klien.setUsername(rs.getString(2));
				klien.setIdTabungan(rs.getString(3));
				allkln.add(klien);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allkln;
	}
	
	@Override
	public Klien getOne(String idKln) {
		Klien klien = new Klien();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_klien, username, password, id_tabungan FROM klien WHERE id_klien = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, idKln);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			klien.setIdKlien(rs.getString(1));
			klien.setUsername(rs.getString(2));
			klien.setPassword(rs.getString(3));
			klien.setIdTabungan(rs.getString(4));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klien;
	}

	@Override
	public String insert(String idKln, Klien klien) {
		String msg = null;
		boolean result = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String cekUsername = "SELECT COUNT(USERNAME) FROM KLIEN WHERE USERNAME=?";
			PreparedStatement stmt = conn.prepareStatement(cekUsername);
			stmt.setString(1, klien.getUsername());
			ResultSet rs = stmt.executeQuery();
			rs.next();
			if(rs.getInt(1) > 0) {
			msg = "USERNAME YANG DIMASUKKAN TELAH DIGUNAKAN. SILAHKAN GUNAKAN USERNAME LAIN";
			 }else {
				    String query = "INSERT INTO klien (id_klien, username, password, id_tabungan) VALUES (?, ?, ?, ?)";
				    stmt = conn.prepareStatement(query);
				    stmt.setString(1, idKln);
				    stmt.setString(2, klien.getUsername());
				    stmt.setString(3, klien.getPassword());
				    stmt.setString(4, klien.getIdTabungan());
				    result = stmt.execute();
				    
				if (result == false) {
					msg = "BERHASIL MENAMBAHKAN DENGAN ID-" + klien.getIdKlien() + " KE DATA KLIEN";
				} else {
					msg = "GAGAL MENAMBAHKAN DENGAN ID-" + klien.getIdKlien() + " KE DATA KLIEN";
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String update(Klien klien) {
		String upd = null;
		int res = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "UPDATE klien SET username=?, password=? WHERE id_klien=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, klien.getUsername());
			stmt.setString(2, klien.getPassword());
			stmt.setString(3, klien.getIdKlien());
			res = stmt.executeUpdate();
			if(res > 0) {
				upd = "BERHASIL MEMPERBAHARUI DATA KLIEN DENGAN ID-" +klien.getIdKlien();
			}else {
				upd = "GAGAL MEMPERBAHARUI DATA KLIEN DENGAN ID-" +klien.getIdKlien();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return upd;
	}

	@Override
	public String delete(String idKln) {
		String del = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM KLIEN WHERE ID_KLIEN = '"+ idKln +"'";
			result = stmt.executeUpdate(query);
			
			if(result !=0) {
			del="KLIEN DENGAN ID-" + idKln + " BERHASIL DIHAPUS";
			}else {
			del="KLIEN DENGAN ID-" + idKln + " GAGAL DIHAPUS";
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	@Override
	public String generateId() {
		String newId = null;
		try {
		   DataSource ds = jdbc.getDataSource();
		   Connection conn = ds.getConnection();
		   String cQuery = "SELECT COUNT(ID_KLIEN) FROM KLIEN ORDER BY ID_KLIEN DESC";
		   PreparedStatement cStmt = conn.prepareStatement(cQuery);
		   ResultSet cRes = cStmt.executeQuery();
		   cRes.next();
		   if (cRes.getInt(1) > 0) {
			   String query = "SELECT ID_KLIEN FROM KLIEN ORDER BY ID_KLIEN DESC";
			   PreparedStatement stmt = conn.prepareStatement(query);
			   ResultSet res = stmt.executeQuery();
			   res.next();
			    
			   String lastId = res.getString(1);
			   int plusOne = (Integer.parseInt(lastId.substring(1, 4)) + 1);
			   if (plusOne < 100) {
				   if (plusOne < 10) {
					   newId = "K00"+plusOne;
				   }else {
					   newId = "K0"+plusOne;
				   }
			   }else {
				   newId = "K"+plusOne;
			   }
		   }else {
			   newId = "K001";
		   }
		conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}
	
	@Override
	public int autentikasi(Klien klien) {
		int auth = 2;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String q1 = "SELECT COUNT(USERNAME) FROM KLIEN WHERE USERNAME = ?";
			PreparedStatement ps1 = conn.prepareStatement(q1);
			ps1.setString(1, klien.getUsername());
			
			ResultSet rs1 = ps1.executeQuery();
			rs1.next();
			
			if(rs1.getInt(1) < 1) {
				auth = 2;
				System.out.println("LOG :: LOGIN FAILED [CAUSE : USERNAME IS NOT REGISTERED]");
			}else {
				String q2 = "SELECT ID_KLIEN, KLIEN.ID_TABUNGAN, STATUS FROM KLIEN JOIN TABUNGAN ON KLIEN.ID_TABUNGAN = TABUNGAN.ID_TABUNGAN WHERE USERNAME = ? AND PASSWORD = ?";
				PreparedStatement ps2 = conn.prepareStatement(q2);
				ps2.setString(1, klien.getUsername());
				ps2.setString(2, klien.getPassword());
				
				ResultSet rs2 = ps2.executeQuery();
				rs2.next();
				
				if(rs2.getRow() > 0) {
					if(rs2.getString(3).equals("AKTIF")) {
						klien.setIdKlien(rs2.getString(1));
						klien.setIdTabungan(rs2.getString(2));
						auth = 1;
						System.out.println("LOG :: LOGIN SUCCEED [USER : "+klien.getUsername()+"]");
					}else {
						auth = 4;
						System.out.println("LOG :: LOGIN FAILED [CAUSE : USER IS INACTIVE]");
					}
				}else {
					auth = 3;
					System.out.println("LOG :: LOGIN FAILED [CAUSE : WRONG PASSWORD ENTERED]");
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return auth;
	}
}