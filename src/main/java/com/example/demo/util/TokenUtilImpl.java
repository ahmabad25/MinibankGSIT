package com.example.demo.util;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Token;

@Component
public class TokenUtilImpl implements TokenUtil {
	
	@Autowired
	Token token;
	
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
	public Token getToken(String id) {
		Token token = new Token();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT ID_PENGGUNA, TOKEN, TO_CHAR(EXPIRE_TIME, 'yyyy-MM-dd hh24:mi:ss') FROM ONLINE_SESSION WHERE ID_PENGGUNA=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			token.setIdKlien(rs.getString(1));
			token.setToken(rs.getString(2));
			token.setExpireTime(rs.getString(3));
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	@Override
	public void insertToken(Token token) {
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ONLINE_SESSION (TOKEN, EXPIRE_TIME, ID_PENGGUNA) VALUES (?, TO_TIMESTAMP(?, 'yyyy-MM-dd hh24:mi:ss'), ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, token.getToken());
			ps.setString(2, token.getExpireTime());
			ps.setString(3, token.getIdKlien());
			int res = ps.executeUpdate();
			
			if (res > 0) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime ldt = LocalDateTime.parse(token.getExpireTime(), dtf).minusMinutes(5);
				String loginTime = dtf.format(ldt);
				
				String sql1 = "INSERT INTO HISTORI_SESSION(ID, ID_PENGGUNA, LOGIN_TIME, EXPIRE_TIME) VALUES(?, ?, TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss'), TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss'))";
				ps = conn.prepareStatement(sql1);
				
				int newId = idOtomatis();
				
				ps.setString(1, newId+"");
				ps.setString(2, token.getIdKlien());
				ps.setString(3, loginTime);
				ps.setString(4, token.getExpireTime());
				ps.executeUpdate();
				System.out.println("LOG :: TOKEN ["+token.getToken()+"] ASSIGNED FOR USER ["+token.getIdKlien()+"]");
			}else {
				System.out.println("LOG :: FAILED TO ASSIGN TOKEN TO ["+token.getIdKlien()+"]");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteToken(Token token) {
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String sql = "DELETE FROM ONLINE_SESSION WHERE ID_PENGGUNA=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, token.getIdKlien());
			int res = ps.executeUpdate();
			
			if(res > 0) {
				System.out.println("LOG :: REMOVED ["+token.getIdKlien()+"] FROM SESSION");
			}else {
				System.out.println("LOG :: THERE IS NO ["+token.getIdKlien()+"] FROM SESSION");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, String> generateToken(String idKlien) {
		Map<String, String> generatedToken = new HashMap<String, String>();
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sKey = new SecretKeySpec(idKlien.getBytes(), "HmacSHA256");
			mac.init(sKey);
			
			String dateTimeNow = LocalDateTime.now().toString();
			String newToken = String.format("%032x", new BigInteger(1, mac.doFinal(dateTimeNow.getBytes())));
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String expired = dtf.format(LocalDateTime.now().plusMinutes(10));
			
			generatedToken.put("token", newToken);
			generatedToken.put("expired", expired);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedToken;
	}
	
	@Override
	public boolean cekSession(String id) {
		boolean auth = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT COUNT(ID_PENGGUNA) FROM ONLINE_SESSION WHERE ID_PENGGUNA = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
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
	public boolean validateToken(Token token) {
		boolean auth = false;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT ID_PENGGUNA, TOKEN, EXPIRE_TIME FROM ONLINE_SESSION WHERE ID_PENGGUNA = ? AND TOKEN = ? AND EXPIRE_TIME = TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss')";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, token.getIdKlien());
			ps.setString(2, token.getToken());
			ps.setString(3, token.getExpireTime());
			
			ResultSet rs = ps.executeQuery();
			rs.next();

			if(rs.getRow() > 0) {
				auth = true;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return auth;
	}
}
