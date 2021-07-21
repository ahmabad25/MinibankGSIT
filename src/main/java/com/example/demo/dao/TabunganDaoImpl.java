package com.example.demo.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Tabungan;

@Component
public class TabunganDaoImpl implements TabunganDao {

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Tabungan> getAll() {
		List<Tabungan> result = new ArrayList<Tabungan>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT tab.id_tabungan,nas.first_name||' '||nas.last_name name,tab.no_rekening,"
					+ "TO_CHAR(tab.tanggal_pembukaan, 'dd MONTH yyyy'),tab.status\r\n" + 
					"FROM tabungan tab INNER JOIN nasabah nas on tab.id_nasabah = nas.id_nasabah ORDER BY id_tabungan DESC";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Tabungan tabungan = new Tabungan();
				tabungan.setIdTabungan(rs.getString(1));
				tabungan.setNama(rs.getString(2));
				tabungan.setNoRekening(rs.getInt(3));
				tabungan.setTanggalPembukaan(rs.getString(4));
				tabungan.setStatus(rs.getString(5));
				result.add(tabungan);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Tabungan getOne(String tabungan) {
		Tabungan getOne = new Tabungan();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_tabungan,no_rekening,pin,tanggal_pembukaan,status,id_nasabah FROM Tabungan where Id_tabungan =?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, tabungan);
			ResultSet rs = ps.executeQuery();
			rs.next();

			getOne.setIdTabungan(rs.getString(1));
			getOne.setNoRekening(rs.getInt(2));
			getOne.setPin(rs.getString(3));
			getOne.setTanggalPembukaan(rs.getString(4));
			getOne.setStatus(rs.getString(5));
			getOne.setIdNasabah(rs.getString(6));
		
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getOne;
	}

	@Override
	public String insert(Tabungan tabungan) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query =  "INSERT ALL into tabungan (id_tabungan,no_rekening,pin,tanggal_pembukaan,status,id_nasabah) values (?,?,?,TO_DATE(?, 'yyyy/mm/dd HH24:mi:ss'),?,?) "
				     + "into tabungan_detail (id_tabungan,Saldo_masuk,total_Saldo,tanggal_transaksi,keterangan,id_histori  ) values (?,?,?,TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), 'Setor',?)"
				     + "into transaksi_masuk (id_transaksi_masuk, Id_tabungan, Nominal, tanggal_masuk) values (?,?,?,TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss')) "
				     + "into histori_transaksi (id_histori, id_transaksi_masuk) values (?,?) "
				     + "SELECT * FROM dual";;
			PreparedStatement stmt = conn.prepareStatement(query);
					   stmt.setString(1, tabungan.getIdTabungan());
					   stmt.setInt(2, tabungan.getNoRekening());
					   stmt.setString(3, tabungan.getPin());
					   stmt.setString(4, tabungan.getTanggalPembukaan());
					   stmt.setString(5, tabungan.getStatus());
					   stmt.setString(6, tabungan.getIdNasabah());
					   
					   stmt.setString(7, tabungan.getIdTabungan());
					   stmt.setInt(8, tabungan.getNominal());
					   stmt.setInt(9, tabungan.getNominal());
					   stmt.setString(10, tabungan.getTanggalPembukaan());
					   stmt.setString(11, tabungan.getIdHistori());
					   
					   stmt.setString(12, tabungan.getIdTransaksiMasuk());
					   stmt.setString(13, tabungan.getIdTabungan());
					   stmt.setInt(14, tabungan.getNominal());
					   stmt.setString(15, tabungan.getTanggalPembukaan());
					   
					   stmt.setString(16, tabungan.getIdHistori());
					   stmt.setString(17, tabungan.getIdTransaksiMasuk());
			int hasil = stmt.executeUpdate();
			if (hasil > 0) {
				msg = "BERHASIL UBAH " + tabungan.getIdTabungan() + " !";
			} else {
				msg = "GAGAL UBAH " + tabungan.getIdTabungan() + " !";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String update(Tabungan tabungan) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "update Tabungan set pin=? where id_tabungan=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, tabungan.getPin());
			stmt.setString(2, tabungan.getIdTabungan());
			int hasil = stmt.executeUpdate();
			if (hasil > 0) {
				msg = "BERHASIL UBAH TABUNGAN " + tabungan.getIdTabungan() + " !";
			} else {
				msg = "GAGAL UBAH TABUNGAN " + tabungan.getIdTabungan() + " !";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String NonAktif(String idTabungan) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "update Tabungan set status='NON-AKTIF' where id_tabungan = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, idTabungan);
			int hasil = stmt.executeUpdate();
			if (hasil > 0) {
				msg = "BERHASIL MENONAKTIFKAN TABUNGAN " + idTabungan + " !";
			} else {
				msg = "GAGAL MENONAKTIFKAN TABUNGAN " + idTabungan + " !";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String Aktif(String idTabungan) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "update Tabungan set status='AKTIF' where id_tabungan = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, idTabungan);
			int hasil = stmt.executeUpdate();
			if (hasil > 0) {
				msg = "BERHASIL MENGAKTIFKAN TABUNGAN " + idTabungan + " !";
			} else {
				msg = "GAGAL MENGAKTIFKAN TABUNGAN" + idTabungan + " !";
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
			String cQuery = "SELECT COUNT(ID_TABUNGAN) FROM TABUNGAN ORDER BY ID_TABUNGAN DESC";
			PreparedStatement cStmt = conn.prepareStatement(cQuery);
			ResultSet cRes = cStmt.executeQuery();
			cRes.next();
			if (cRes.getInt(1) > 0) {
				String query = "SELECT ID_TABUNGAN FROM TABUNGAN ORDER BY ID_TABUNGAN DESC";
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				res.next();

				String lastId = res.getString(1);
				int plusOne = (Integer.parseInt(lastId.substring(1, 4)) + 1);
				if (plusOne < 100) {
					if (plusOne < 10) {
						newId = "T00" + plusOne;
					} else {
						newId = "T0" + plusOne;
					}
				} else {
					newId = "T" + plusOne;
				}
			} else {
				newId = "T001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}

	@Override
	public String generateNorek() {
		String newRek = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String cQuery = "SELECT COUNT(NO_REKENING) FROM TABUNGAN ORDER BY NO_REKENING DESC";
			PreparedStatement cStmt = conn.prepareStatement(cQuery);
			ResultSet cRes = cStmt.executeQuery();
			cRes.next();
			if (cRes.getInt(1) > 0) {
				String query = "SELECT NO_REKENING FROM TABUNGAN ORDER BY NO_REKENING DESC";
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				res.next();

				String lastId = res.getString(1);
				int plusOne = (Integer.parseInt(lastId.substring(5, 8)) + 1);
				if (plusOne < 100) {
					if (plusOne < 10) {
						newRek = "2651700" + plusOne;
					} else {
						newRek = "265170" + plusOne;
					}
				} else {
					newRek = "26517" + plusOne;
				}
			} else {
				newRek = "26517001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newRek;
	}

	@Override
	public String ambilTanggal() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		String tanggal = dtf.format(ldt);
		return tanggal;
	}

	@Override
	public List<Tabungan> getKlienTabungan() {
		List<Tabungan> result = new ArrayList<Tabungan>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_tabungan,no_rekening,pin,TO_CHAR(tanggal_pembukaan, 'dd MONTH yyyy'),status, (nasabah.first_name||' '||nasabah.last_name) FROM Tabungan JOIN nasabah ON tabungan.id_nasabah = nasabah.id_nasabah WHERE status = 'AKTIF' AND tabungan.id_tabungan NOT IN (SELECT id_tabungan FROM klien) ORDER BY id_tabungan DESC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Tabungan tabungan = new Tabungan();
				tabungan.setIdTabungan(rs.getString(1));
				tabungan.setNoRekening(rs.getInt(2));
				tabungan.setPin(rs.getString(3));
				tabungan.setTanggalPembukaan(rs.getString(4));
				tabungan.setStatus(rs.getString(5));
				tabungan.setIdNasabah(rs.getString(6));
				result.add(tabungan);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getIdTabungan(int noRek) {
		String id = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "SELECT ID_TABUNGAN FROM TABUNGAN WHERE NO_REKENING = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, noRek);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			id = rs.getString(1);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id; 
	}
	
	@Override
	public String getIdNasabah(int noRek) {
		String id = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "SELECT ID_NASABAH FROM TABUNGAN WHERE NO_REKENING = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, noRek);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			id = rs.getString(1);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id; 
	}
	
	@Override
	public boolean cekPin(String encPin, String idKlien, String idTabungan) {
		boolean hasil = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "SELECT PIN FROM TABUNGAN WHERE ID_TABUNGAN = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, idTabungan);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			String pin = rs.getString(1);
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sKey = new SecretKeySpec(idKlien.getBytes(), "HmacSHA256");
			mac.init(sKey);
			String kode = String.format("%032x", new BigInteger(1, mac.doFinal(pin.getBytes())));
			
			if(encPin.equals(kode)) {
				hasil = true;
			}else {
				hasil = false;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasil;
	}
}
