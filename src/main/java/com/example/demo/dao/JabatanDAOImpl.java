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

import com.example.demo.model.Jabatan;

@Component
public class JabatanDAOImpl implements JabatanDAO {

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Jabatan> getAll() {
		List<Jabatan> result = new ArrayList<Jabatan>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT id_jabatan, nama_jabatan FROM jabatan ORDER BY id_jabatan DESC";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Jabatan jabatan = new Jabatan();
				jabatan.setIdJabatan(rs.getInt(1));
				jabatan.setNamaJabatan(rs.getString(2));
				result.add(jabatan);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Jabatan getOne(int idJabatan) {
		Jabatan jabatan = new Jabatan();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT id_jabatan, nama_jabatan FROM jabatan WHERE id_jabatan = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idJabatan);
			ResultSet rs = ps.executeQuery();
			rs.next();

			jabatan.setIdJabatan(rs.getInt(1));
			jabatan.setNamaJabatan(rs.getString(2));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jabatan;
	}

	@Override
	public String delete(int idJabatan) {
		String msg = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "DELETE FROM jabatan WHERE id_jabatan = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idJabatan);
			result = ps.executeUpdate();

			if (result > 0) {
				msg = "JABATAN DENGAN ID-" + idJabatan + " BERHASIL DIHAPUS";
			} else {
				msg = "JABATAN DENGAN ID-" + idJabatan + " GAGAL DIHAPUS";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String insert(Jabatan jabatan) {
		String msg = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "INSERT INTO jabatan (id_jabatan, nama_jabatan) VALUES (?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, jabatan.getIdJabatan());
			ps.setString(2, jabatan.getNamaJabatan());
			result = ps.executeUpdate();

			if (result > 0) {
				msg = "BERHASIL MENAMBAHKAN ID-" + jabatan.getIdJabatan() + " KE DATA JABATAN";
			} else {
				msg = "GAGAL MENAMBAHKAN ID-" + jabatan.getIdJabatan() + " KE DATA JABATAN";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String update(Jabatan jabatan) {
		String msg = null;
		int result = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "UPDATE jabatan SET nama_jabatan = ? WHERE id_jabatan = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(2, jabatan.getIdJabatan());
			ps.setString(1, jabatan.getNamaJabatan());
			result = ps.executeUpdate();

			if (result > 0) {
				msg = "BERHASIL MEMPERBAHARUI JABATAN DENGAN ID-" + jabatan.getIdJabatan();
			} else {
				msg = "GAGAL MEMPERBAHARUI JABATAN DENGAN ID-" + jabatan.getIdJabatan();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public int genereteId() {
		int newId = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String cQuery = "SELECT COUNT(ID_JABATAN) FROM JABATAN ORDER BY ID_JABATAN DESC";
			PreparedStatement cStmt = conn.prepareStatement(cQuery);
			ResultSet cRes = cStmt.executeQuery();
			cRes.next();

			if (cRes.getInt(1) > 0) {
				String query = "SELECT ID_JABATAN FROM JABATAN ORDER BY ID_JABATAN DESC";
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				res.next();

				int lastId = res.getInt(1);
				newId = lastId + 10;
			} else {
				newId = 10;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}

}
