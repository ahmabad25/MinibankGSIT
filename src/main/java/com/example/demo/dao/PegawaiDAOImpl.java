package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Pegawai;

@Component
public class PegawaiDAOImpl implements PegawaiDAO{
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	Pegawai pegawai;

	@Override
	public List<Pegawai> getAll() {
		List<Pegawai> result = new ArrayList<Pegawai>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT id_pegawai, first_name, last_name, jenis_kelamin, agama, no_telepon, alamat_lengkap, id_jabatan, tanggal_masuk, tempat_lahir, tanggal_lahir FROM pegawai ORDER BY id_pegawai DESC";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Pegawai pgw = new Pegawai();
				pgw.setIdPegawai(rs.getString(1));
				pgw.setFirstName(rs.getString(2));
				pgw.setLastName(rs.getString(3));
				pgw.setJenisKelamin((rs.getString(4).charAt(0)));
				pgw.setAgama(rs.getString(5));
				pgw.setPhoneNumber(rs.getString(6));
				pgw.setAlamat(rs.getString(7));
				pgw.setIdJabatan(rs.getString(8));
				pgw.setTanggalMasuk(rs.getString(9));
				pgw.setTempatLahir(rs.getString(10));
				pgw.setTanggalLahir(rs.getString(11));
				result.add(pgw);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Pegawai getOne(String idPegawai) {
		Pegawai pgw = new Pegawai();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_pegawai, first_name, last_name, jenis_kelamin, no_telepon, agama, alamat_lengkap, id_jabatan, TO_CHAR(tanggal_masuk, 'DD MONTH YYYY'), tempat_lahir, TO_CHAR(tanggal_lahir,'DD MONTH YYYY') FROM pegawai WHERE id_pegawai = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idPegawai);
			ResultSet rs = ps.executeQuery();
			rs.next();

				pgw.setIdPegawai(rs.getString(1));
				pgw.setFirstName(rs.getString(2));
				pgw.setLastName(rs.getString(3));
				pgw.setJenisKelamin((rs.getString(4).charAt(0)));
				pgw.setPhoneNumber(rs.getString(5));
				pgw.setAgama(rs.getString(6));
				pgw.setAlamat(rs.getString(7));
				pgw.setIdJabatan(rs.getString(8));
				pgw.setTanggalMasuk(rs.getString(9));
				pgw.setTempatLahir(rs.getString(10));
				pgw.setTanggalLahir(rs.getString(11));
				conn.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pgw;
	}

	@Override
	public String delete(String idPegawai) {
		String msg = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "DELETE FROM pegawai WHERE id_pegawai = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idPegawai);
			result  = ps.executeUpdate();
			
			if(result>0) {
				msg = "PEGAWAI DENGAN ID-" + idPegawai+" BERHASIL DIHAPUS";
			} else {
				msg = "PEGAWAI DENGAN ID-" + idPegawai+" GAGAL DIHAPUS";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String insert(Pegawai pegawai) {
		String msg = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "INSERT INTO pegawai (id_pegawai, first_name, last_name, jenis_kelamin, no_telepon, agama, alamat_lengkap, id_jabatan, tanggal_masuk, tempat_lahir, tanggal_lahir) VALUES (?,?,?,?,?,?,?,?,TO_DATE(?,'yyyy-MM-dd HH24:mi:ss'),?,TO_DATE(?,'yyyy-MM-dd'))";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, pegawai.getIdPegawai());
			ps.setString(2, pegawai.getFirstName());
			ps.setString(3, pegawai.getLastName());
			ps.setString(4, pegawai.getJenisKelamin()+"");
			ps.setString(5, pegawai.getPhoneNumber());
			ps.setString(6, pegawai.getAgama());
			ps.setString(7, pegawai.getAlamat());
			ps.setString(8, pegawai.getIdJabatan());
			ps.setString(9, pegawai.getTanggalMasuk());
			ps.setString(10, pegawai.getTempatLahir());
			ps.setString(11, pegawai.getTanggalLahir());
			result = ps.executeUpdate();
			
			if (result > 0) {
				msg = "BERHASIL MENAMBAHKAN DATA PEGAWAI DENGAN ID-" + pegawai.getIdPegawai()+" KE DATA PEGAWAI";
			} else {
				msg = "GAGAL MENAMBAHKAN DATA PEGAWAI DENGAN ID-" + pegawai.getIdPegawai()+" KE DATA PEGAWAI";
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
		  String cQuery = "SELECT COUNT(ID_PEGAWAI) FROM PEGAWAI ORDER BY ID_PEGAWAI DESC";
		  PreparedStatement cStmt = conn.prepareStatement(cQuery);
		  ResultSet cRes = cStmt.executeQuery();
		  cRes.next();
		  
		  	if (cRes.getInt(1) > 0) {
		  		String query = "SELECT ID_PEGAWAI FROM PEGAWAI ORDER BY ID_PEGAWAI DESC";
		  		PreparedStatement stmt = conn.prepareStatement(query);
		  		ResultSet res = stmt.executeQuery();
		  		res.next();
	    
	    String lastId = res.getString(1);
	    int plusOne = (Integer.parseInt(lastId.substring(1, 4)) + 1);
	    	if (plusOne < 100) {
	    		if (plusOne < 10) {
	    			newId = "P00"+plusOne;
	    		}else {
	    			newId = "P0"+plusOne;
	    		}
	    	}else {
	    		newId = "P"+plusOne;
	    	}
		  	}else {
		  		newId = "P001";
		  	}
		  	conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return newId;
	 }

	@Override
	public String update(Pegawai pegawai) {
			String msg = null;
			int result = 0;
			try {
				DataSource ds = jdbc.getDataSource();
				Connection conn = ds.getConnection();
				String query = "UPDATE pegawai SET no_telepon = ?, alamat_lengkap = ?, id_jabatan = ? WHERE id_pegawai = ?";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, pegawai.getPhoneNumber());
				ps.setString(2, pegawai.getAlamat());
				ps.setString(3, pegawai.getIdJabatan());
				ps.setString(4, pegawai.getIdPegawai());
				result = ps.executeUpdate();
				
				if (result > 0) {
					msg = "BERHASIL MEMPERBAHARUI PEGAWAI DENGAN ID-" + pegawai.getIdPegawai();
				} else {
					msg = "GAGAL MEMPERBAHARUI PEGAWAI DENGAN ID-" + pegawai.getIdPegawai();
				}
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return msg;
		}

	@Override
	public String ambilTanggal() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		String tanggal = dtf.format(ldt);
		return tanggal;
	}

	@Override
	public List<Pegawai> getUserPegawai() {
		List<Pegawai> result = new ArrayList<Pegawai>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT id_pegawai, first_name, last_name, jenis_kelamin, no_telepon, agama, alamat_lengkap, id_jabatan, tanggal_masuk, tempat_lahir, tanggal_lahir FROM PEGAWAI WHERE PEGAWAI.ID_PEGAWAI NOT IN (SELECT ID_PEGAWAI FROM USER_PEGAWAI) ORDER BY FIRST_NAME ASC";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Pegawai pgw = new Pegawai();
				pgw.setIdPegawai(rs.getString(1));
				pgw.setFirstName(rs.getString(2));
				pgw.setLastName(rs.getString(3));
				pgw.setJenisKelamin((rs.getString(4).charAt(0)));
				pgw.setAgama(rs.getString(5));
				pgw.setPhoneNumber(rs.getString(6));
				pgw.setAlamat(rs.getString(7));
				pgw.setIdJabatan(rs.getString(8));
				pgw.setTanggalMasuk(rs.getString(9));
				pgw.setTempatLahir(rs.getString(10));
				pgw.setTanggalLahir(rs.getString(11));
				result.add(pgw);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
