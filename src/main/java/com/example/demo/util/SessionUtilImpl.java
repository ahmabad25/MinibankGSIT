package com.example.demo.util;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionUtilImpl implements SessionUtil{
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public int idOtomatis() {
		int noUrut = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "SELECT ID FROM HISTORI_SESSION ORDER BY ID DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			if(rs.getRow() > 0) {
				noUrut = rs.getInt(1) + 1;
			}else {
				noUrut = 1;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noUrut;
	}

	@Override
	public Map<String, String> getSession(String idUser) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "SELECT ID_PENGGUNA, TOKEN, TO_CHAR(EXPIRE_TIME, 'yyyy-MM-dd hh24:mi:ss') FROM ONLINE_SESSION WHERE ID_PENGGUNA=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			map.put("idUser", rs.getString(1));
			map.put("token", rs.getString(2));
			map.put("expired", rs.getString(3));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void insertSession(Map<String, String> id) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sKey = new SecretKeySpec(id.get("idUser").getBytes(), "HmacSHA256");
			mac.init(sKey);
			
			LocalDateTime ldt = LocalDateTime.now();
			String newToken = String.format("%032x", new BigInteger(1, mac.doFinal(ldt.toString().getBytes())));
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String expired = dtf.format(LocalDateTime.now().plusMinutes(15));
			
			id.put("token", newToken);
			id.put("expired", expired);
			
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String createSession = "INSERT INTO ONLINE_SESSION (ID_PENGGUNA, TOKEN, EXPIRE_TIME) VALUES (?, ?, TO_TIMESTAMP(?, 'yyyy-MM-dd hh24:mi:ss'))";
			PreparedStatement ps = conn.prepareStatement(createSession);
			ps.setString(1, id.get("idUser"));
			ps.setString(2, id.get("token"));
			ps.setString(3, id.get("expired"));
			int res = ps.executeUpdate();
			
			if(res > 0) {
				String loginTime = dtf.format(ldt);
				String createTimeStamp = "INSERT INTO HISTORI_SESSION(ID, ID_PENGGUNA, LOGIN_TIME, EXPIRE_TIME) VALUES (?, ? , TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss'), TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss'))";
				ps = conn.prepareStatement(createTimeStamp);
				
				int newId = idOtomatis();
				ps.setString(1, newId+"");
				ps.setString(2, id.get("idUser"));
				ps.setString(3, loginTime);
				ps.setString(4, expired);
				ps.executeUpdate();
				System.out.println("USER ["+id.get("idUser")+"] HAS START A SESSION!");
			}else {
				System.out.println("FAILED TO CREATE SESSION FOR ["+id.get("idUser")+"]");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSession(Map<String, String> id) {
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "DELETE FROM ONLINE_SESSION WHERE ID_PENGGUNA=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id.get("idUser"));
			int res = ps.executeUpdate();
			
			if(res > 0) {
				System.out.println("USER ["+id.get("idUser")+"] HAS LOGOUT!");
			}else {
				System.out.println("NO SESSION FOUND UNDER USER ["+id.get("idUser")+"]");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean cekSession(String idUser) {
		boolean auth = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT COUNT(ID_PENGGUNA) FROM ONLINE_SESSION WHERE ID_PENGGUNA = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idUser);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			if(rs.getInt(1) > 0) {
				auth = true;
			}else {
				auth = false;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return auth;
	}

	@Override
	public boolean validateSession(Map<String, String> id) {
		boolean auth = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT ID_PENGGUNA, TOKEN, EXPIRE_TIME FROM ONLINE_SESSION WHERE ID_PENGGUNA = ? AND TOKEN = ? AND EXPIRE_TIME = TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss')";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id.get("idUser"));
			ps.setString(2, id.get("token"));
			ps.setString(3, id.get("expired"));
			
			ResultSet rs = ps.executeQuery();
			rs.next();

			if(rs.getRow() > 0) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime awal = LocalDateTime.now();
				LocalDateTime akhir = LocalDateTime.parse(id.get("expired"), dtf);
				int jarak = (int) ChronoUnit.MINUTES.between(awal, akhir);
				if (jarak < 0) {
					auth = false;
				}else {
					auth = true;
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return auth;
	}
}
