package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.example.demo.model.UserPegawai;

@Component
public class UserPegawaiDAOImpl implements UserPegawaiDAO {

	@Autowired
	UserPegawai userpegawai;

	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public List<UserPegawai> getAll() {
		List<UserPegawai> allUser = new ArrayList<UserPegawai>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT up.id_user, p.first_name||' '||p.last_name name, up.username\r\n" + 
					"FROM user_pegawai up INNER JOIN pegawai p on up.id_pegawai = p.id_pegawai ORDER BY id_user DESC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				UserPegawai user = new UserPegawai();
				user.setIdUser(rs.getString(1));
				user.setNama(rs.getString(2));
				user.setUsername(rs.getString(3));
				allUser.add(user);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allUser;
	}

	@Override
	public UserPegawai getOne(String idUser) {
		UserPegawai userpegawai = new UserPegawai();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "SELECT up.id_user, p.first_name||' '||p.last_name name, up.username "
					+ "FROM user_pegawai up INNER JOIN pegawai p on up.id_pegawai = p.id_pegawai "
					+ "WHERE id_user = '" + idUser + "'";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			userpegawai.setIdUser(rs.getString(1));
			userpegawai.setNama(rs.getString(2));
			userpegawai.setUsername(rs.getString(3));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userpegawai;
	}

	@Override
	public String delete(String id) {
		String msg = null;
		int syn = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM user_pegawai WHERE id_user = '" + id + "'";
			syn = stmt.executeUpdate(query);

			if (syn > 0) {
				msg = "USER PEGAWAI DENGAN ID-" + id + " BERHASIL DIHAPUS";
			} else {
				msg = "USER PEGAWAI DENGAN ID-" + id + " GAGAL DIHAPUS";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String insert(String userId, UserPegawai userPegawai) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = ("INSERT INTO user_pegawai (id_user, username, password, id_pegawai) VALUES (?, ?, ?, ?)");
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userId);
			stmt.setString(2, userPegawai.getUsername());
			stmt.setString(3, userPegawai.getPassword());
			stmt.setString(4, userPegawai.getIdPegawai());
			int result = stmt.executeUpdate();
			
			if(result > 0) {
				msg = "BERHASIL MENAMBAHKAN DATA DENGAN ID-"+userPegawai.getIdPegawai()+" KE DATA USER PEGAWAI";
			}else {
				msg = "GAGAL MENAMBAHKAN DATA DENGAN ID-"+userPegawai.getIdPegawai()+" KE DATA USER PEGAWAI";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	@Override
	public String update(UserPegawai userpegawai) {
		String msg = null;
		int res = 0;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "UPDATE user_pegawai SET  password=? WHERE id_user=?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userpegawai.getPassword());
			stmt.setString(2, userpegawai.getIdUser());
			res = stmt.executeUpdate();
			if (res > 0) {
				msg = "BERHASIL MEMPERBAHARUI USER PEGAWAI DENGAN ID-"+ userpegawai.getIdUser();
			} else {
				msg = "GAGAL MEMPERBAHARUI USER PEGAWAI DENGAN ID-"+ userpegawai.getIdUser();
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
	   String cQuery = "SELECT COUNT(ID_USER) FROM USER_PEGAWAI ORDER BY ID_USER DESC";
	   PreparedStatement cStmt = conn.prepareStatement(cQuery);
	   ResultSet cRes = cStmt.executeQuery();
	   cRes.next();
	   if (cRes.getInt(1) > 0) {
	    String query = "SELECT ID_USER FROM USER_PEGAWAI ORDER BY ID_USER DESC";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    ResultSet res = stmt.executeQuery();
	    res.next();
	    
	    String lastId = res.getString(1);
	    int plusOne = (Integer.parseInt(lastId.substring(1, 4)) + 1);
	    if (plusOne < 100) {
	     if (plusOne < 10) {
	      newId = "U00"+plusOne;
	     }else {
	      newId = "U0"+plusOne;
	     }
	    }else {
	     newId = "U"+plusOne;
	    }
	   }else {
	    newId = "U001";
	   }
	   conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return newId;
	 }

	@Override
	public Map<String, String> autentikasi(UserPegawai userpegawai) {
		Map<String, String> dataLogin = new HashMap<String, String>();
	  	try {
		   DataSource ds = jdbc.getDataSource();
		   Connection conn = ds.getConnection();

		   String cekUserSql = "SELECT COUNT(username) FROM user_pegawai WHERE username = ?";
		   PreparedStatement ps = conn.prepareStatement(cekUserSql);
		   ps.setString(1, userpegawai.getUsername());
		   ResultSet rs = ps.executeQuery();

		   rs.next();
		   if (rs.getInt(1) < 1) {
			    dataLogin.put("pesan", "USER : ["+userpegawai.getUsername()+"] GAGAL LOGIN - USERNAME TIDAK ADA DALAM DATA!");
			    dataLogin.put("izin", "false");
		   } else {
			    String cekUserIdSql = "SELECT ID_USER, ID_PEGAWAI FROM user_pegawai WHERE username = ? AND password = ?";
			    ps = conn.prepareStatement(cekUserIdSql);
			    ps.setString(1, userpegawai.getUsername());
			    ps.setString(2, userpegawai.getPassword());
			    rs = ps.executeQuery();
	
			    rs.next();
			    if (rs.getRow() < 1) {
			    	dataLogin.put("pesan", "USER : ["+userpegawai.getUsername()+"] GAGAL LOGIN - PASSWORD YANG DIMASUKKAN SALAH!");
			    	dataLogin.put("izin", "false");
			    } else {
				     userpegawai.setIdUser(rs.getString(1));
				     userpegawai.setIdPegawai(rs.getString(2));
				     dataLogin.put("pesan", "USER : ["+userpegawai.getUsername()+"] BERHASIL LOGIN!");
				     dataLogin.put("izin", "true");
			    }
		   }
		conn.close();
	  	} catch (SQLException e) {
	  		e.printStackTrace();
	  	}
	  	return dataLogin;
	}
	
	@Override
	public String generateUsername(int idJabatan) {
		String keyword = null;
		String usernameBaru = null;
		switch(idJabatan) {
			case 10 :
				keyword = "UCS";
				break;
			case 20 :
				keyword = "UTL";
				break;
			case 30 :
				keyword = "UMA";
				break;
		}
		
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			
			String sql = "SELECT USERNAME FROM USER_PEGAWAI WHERE USERNAME LIKE ? ORDER BY USERNAME DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, keyword+"%");
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			if(rs.getRow() > 0) {
				String username = rs.getString(1);
				int noUrut = (Integer.parseInt(username.substring(3, 6)) + 1);
				if(noUrut < 100) {
					if(noUrut > 10) {
						usernameBaru = keyword+"0"+noUrut;
					}else {
						usernameBaru = keyword+"00"+noUrut;
					}
				}else {
					usernameBaru = keyword+noUrut;
				}
			}else {
				usernameBaru = keyword+"001";
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usernameBaru;
	}
}