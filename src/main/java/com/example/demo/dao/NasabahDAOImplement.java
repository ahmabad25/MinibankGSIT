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

import com.example.demo.model.Nasabah;

@Component
public class NasabahDAOImplement implements NasabahDAO {
	
	@Autowired
	Nasabah nasabah;
	
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Nasabah> getAll() {
		List<Nasabah> nasabah = new ArrayList<Nasabah>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT ID_NASABAH, FIRST_NAME, LAST_NAME, ALAMAT_LENGKAP, NO_TELEPON, AGAMA, JENIS_KELAMIN, NIK, NAMA_IBU_KANDUNG, NPWP, PEKERJAAN, TEMPAT_LAHIR, TO_CHAR(TANGGAL_LAHIR, 'dd MONTH yyyy') FROM NASABAH ORDER BY ID_NASABAH DESC";
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				Nasabah nsbh = new Nasabah();
				nsbh.setIdNasabah(res.getString(1));
				nsbh.setFirstName(res.getString(2));
				nsbh.setLastName(res.getString(3));
				nsbh.setAlamat(res.getString(4));
				nsbh.setNoTelepon(res.getString(5));
				nsbh.setAgama(res.getString(6));
				nsbh.setJenisKelamin(res.getString(7).charAt(0));
				nsbh.setNIK(res.getString(8));
				nsbh.setNamaIbu(res.getString(9));
				nsbh.setNpwp(res.getString(10));
				nsbh.setPekerjaan(res.getString(11));
				nsbh.setTempatLahir(res.getString(12));
				nsbh.setTanggalLahir(res.getString(13));
				nasabah.add(nsbh);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nasabah;
	}

	@Override
	public Nasabah getOne(String id) {
		Nasabah nasabah = new Nasabah();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT ID_NASABAH, FIRST_NAME, LAST_NAME, ALAMAT_LENGKAP, NO_TELEPON, AGAMA, JENIS_KELAMIN, NIK, NAMA_IBU_KANDUNG, NPWP, PEKERJAAN, TEMPAT_LAHIR, TO_CHAR(TANGGAL_LAHIR, 'dd MONTH yyyy') FROM NASABAH WHERE ID_NASABAH = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet res = stmt.executeQuery();
			res.next();
			
			nasabah.setIdNasabah(res.getString(1));
			nasabah.setFirstName(res.getString(2));
			nasabah.setLastName(res.getString(3));
			nasabah.setAlamat(res.getString(4));
			nasabah.setNoTelepon(res.getString(5));
			nasabah.setAgama(res.getString(6));
			nasabah.setJenisKelamin(res.getString(7).charAt(0));
			nasabah.setNIK(res.getString(8));
			nasabah.setNamaIbu(res.getString(9));
			nasabah.setNpwp(res.getString(10));
			nasabah.setPekerjaan(res.getString(11));
			nasabah.setTempatLahir(res.getString(12));
			nasabah.setTanggalLahir(res.getString(13));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nasabah;
	}

	@Override
	public String insert(Nasabah nasabah) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "INSERT INTO NASABAH (ID_NASABAH, FIRST_NAME, LAST_NAME, ALAMAT_LENGKAP, NO_TELEPON, AGAMA, JENIS_KELAMIN, NIK, NAMA_IBU_KANDUNG, NPWP, PEKERJAAN, TEMPAT_LAHIR, TANGGAL_LAHIR) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY/MM/DD'))";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nasabah.getIdNasabah());
			stmt.setString(2, nasabah.getFirstName());
			stmt.setString(3, nasabah.getLastName());
			stmt.setString(4, nasabah.getAlamat());
			stmt.setString(5, nasabah.getNoTelepon());
			stmt.setString(6, nasabah.getAgama());
			stmt.setString(7, nasabah.getJenisKelamin()+"");
			stmt.setString(8, nasabah.getNIK());
			stmt.setString(9, nasabah.getNamaIbu());
			stmt.setString(10, nasabah.getNpwp());
			stmt.setString(11, nasabah.getPekerjaan());
			stmt.setString(12, nasabah.getTempatLahir());
			stmt.setString(13, nasabah.getTanggalLahir());
			
			int syn = stmt.executeUpdate();
			if (syn > 0) {
				msg = "BERHASIL MENAMBAHKAN '"+nasabah.getFirstName()+" "+nasabah.getLastName()+"' KE DATA NASABAH";
			}else {
				msg = "GAGAL MENAMBAHKAN '"+nasabah.getFirstName()+" "+nasabah.getLastName()+"' KE DATA NASABAH";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String update(Nasabah nasabah) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "UPDATE NASABAH SET ALAMAT_LENGKAP = ?, NO_TELEPON = ?, NPWP = ?, PEKERJAAN = ? WHERE ID_NASABAH = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nasabah.getAlamat());
			stmt.setString(2, nasabah.getNoTelepon());
			stmt.setString(3, nasabah.getNpwp());
			stmt.setString(4, nasabah.getPekerjaan());
			stmt.setString(5, nasabah.getIdNasabah());
			
			int syn = stmt.executeUpdate();
			if (syn > 0) {
				msg = "BERHASIL MEMPERBAHARUI DATA NASABAH DENGAN ID-"+nasabah.getIdNasabah();
			}else {
				msg = "GAGAL MEMPERBAHARUI DATA NASABAH DENGAN ID-"+nasabah.getIdNasabah();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String delete(String id) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "DELETE FROM NASABAH WHERE ID_NASABAH = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			int syn = stmt.executeUpdate();
			if (syn > 0) {
				msg = "NASABAH DENGAN ID-"+id+" BERHASIL DIHAPUS";
			}else {
				msg = "NASABAH DENGAN ID-"+id+" GAGAL DIHAPUS";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String generateId() {
		String newId = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String cQuery = "SELECT COUNT(ID_NASABAH) FROM NASABAH ORDER BY ID_NASABAH DESC";
			PreparedStatement cStmt = conn.prepareStatement(cQuery);
			ResultSet cRes = cStmt.executeQuery();
			cRes.next();
			if (cRes.getInt(1) > 0) {
				String query = "SELECT ID_NASABAH FROM NASABAH ORDER BY ID_NASABAH DESC";
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				res.next();
				
				String lastId = res.getString(1);
				int plusOne = (Integer.parseInt(lastId.substring(1, 4)) + 1);
				if (plusOne < 100) {
					if (plusOne < 10) {
						newId = "N00"+plusOne;
					}else {
						newId = "N0"+plusOne;
					}
				}else {
					newId = "N"+plusOne;
				}
			}else {
				newId = "N001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}
}
