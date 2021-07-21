package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Nasabah;
import com.example.demo.model.Tabungan;
import com.example.demo.model.Transaksi;

@Component
public class TransaksiDaoImpl implements TransaksiDao {
	
	@Autowired
	NasabahDAO nasabahDao;
	
	@Autowired
	TabunganDao tabunganDao;
	
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public String Transfer(Transaksi transaksi) {
		String msg = null;
		Transaksi tr = new Transaksi();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String getSaldo = "select *from (select total_saldo from tabungan_detail where id_tabungan=? order by tanggal_transaksi desc) where rownum=1";
			PreparedStatement ps = conn.prepareStatement(getSaldo);
			ps.setString(1, transaksi.getPengirim());
			ResultSet rs = ps.executeQuery();
			rs.next();

			tr.setTotalSaldo(rs.getInt(1));
			int hasil = rs.getInt(1);
			if (hasil <= 30000) {
				msg = ("INSUFFICIENT BALANCE : " + hasil);
			} else {
				int nominal = transaksi.getNominal();
				if ((hasil - nominal) <= 30000) {
					msg = ("INSUFFICIENT BALANCE RESULT! | CURRENTLY " + ": " + hasil + " | REMAININGS : "
							+ (hasil - nominal));
				} else {
					msg = ("TRANSACTION MADE!");
					String query = "insert ALL \r\n"
						       + "into tabungan_detail (id_tabungan,Saldo_masuk,total_Saldo,tanggal_transaksi,keterangan,berita_transfer,id_histori ) values (?,?,(select *from(select  total_saldo from tabungan_detail where id_tabungan=? ORDER BY tanggal_transaksi DESC )where rownum = 1)+?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss'),'Transfer',?,?) \r\n"
						       + "into transaksi_masuk (id_transaksi_masuk, Id_tabungan, Nominal, tanggal_masuk ) values (?,?,?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss')) \r\n"
						       + "into tabungan_detail (id_tabungan,Saldo_keluar,total_Saldo,tanggal_transaksi,keterangan,berita_transfer,id_histori ) values (?,?,(select *from(select  total_saldo from tabungan_detail where id_tabungan=? ORDER BY tanggal_transaksi DESC )where rownum = 1)-?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss'),'Transfer',?,?)\r\n"
						       + "into transaksi_keluar (id_transaksi_keluar,pengirim,penerima,nominal,tanggal_keluar) VALUES (?,?,?,?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss')) \r\n"
						       + "into histori_transaksi (id_histori, id_transaksi_masuk,id_transaksi_keluar)VALUES (?,?,?)\r\n"
						       + "SELECT * FROM dual"; 
					PreparedStatement stmt = conn.prepareStatement(query);
					stmt.setString(1, transaksi.getPenerima());
					stmt.setInt(2, transaksi.getNominal());
					stmt.setString(3, transaksi.getPenerima());
					stmt.setInt(4, transaksi.getNominal());
					stmt.setString(5, transaksi.getTanggalTransaksi());
					stmt.setString(6, transaksi.getKeterangan());
					stmt.setString(7, transaksi.getIdHistori());

					stmt.setString(8, transaksi.getIdTransaksiMasuk());
					stmt.setString(9, transaksi.getPenerima());
					stmt.setInt(10, transaksi.getNominal());
					stmt.setString(11, transaksi.getTanggalTransaksi());
					
					stmt.setString(12, transaksi.getPengirim());
					stmt.setInt(13, transaksi.getNominal());
					stmt.setString(14, transaksi.getPengirim());
					stmt.setInt(15, transaksi.getNominal());
					stmt.setString(16, transaksi.getTanggalTransaksi());
					stmt.setString(17, transaksi.getKeterangan());
					stmt.setString(18, transaksi.getIdHistori());					

					stmt.setString(19, transaksi.getIdTransaksiKeluar());
					stmt.setString(20, transaksi.getPengirim());
					stmt.setString(21, transaksi.getPenerima());
					stmt.setInt(22, transaksi.getNominal());
					stmt.setString(23, transaksi.getTanggalTransaksi());
					
					stmt.setString(24, transaksi.getIdHistori());
					stmt.setString(25, transaksi.getIdTransaksiMasuk());
					stmt.setString(26, transaksi.getIdTransaksiKeluar());

					stmt.executeUpdate();
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String Setor (Transaksi transaksi) {
		String msg = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "insert ALL \r\n"
					+ "into tabungan_detail (id_tabungan,Saldo_masuk,total_Saldo,tanggal_transaksi,keterangan, id_histori ) values ((select id_tabungan from tabungan where no_rekening = ?),?,(select *from(select  total_saldo from tabungan_detail where id_tabungan=(select id_tabungan from tabungan where no_rekening = ?) ORDER BY tanggal_transaksi DESC )where rownum = 1)+?,TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), 'Setor',?) \r\n"
					+ "into transaksi_masuk (id_transaksi_masuk, Id_tabungan, Nominal, tanggal_masuk) values (?,(select id_tabungan from tabungan where no_rekening = ?),?,TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss')) \r\n"
					+ "into histori_transaksi (id_histori, id_transaksi_masuk) values (?,?) \r\n"
					+ "SELECT * FROM dual";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, transaksi.getIdTabungan());
			stmt.setInt(2, transaksi.getSaldoMasuk());
			stmt.setString(3, transaksi.getIdTabungan());
			stmt.setInt(4, transaksi.getSaldoMasuk());
			stmt.setString(5, transaksi.getTanggalTransaksi());
			stmt.setString(6, transaksi.getIdHistori());

			stmt.setString(7, transaksi.getIdTransaksiMasuk());
			stmt.setString(8, transaksi.getIdTabungan());
			stmt.setInt(9, transaksi.getSaldoMasuk());
			stmt.setString(10, transaksi.getTanggalTransaksi());

			stmt.setString(11, transaksi.getIdHistori());
			stmt.setString(12, transaksi.getIdTransaksiMasuk());
			stmt.executeUpdate();

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String IdMasuk() {
		String newid = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String mutasi = "select *from(select Id_transaksi_masuk from transaksi_masuk order by Id_transaksi_masuk desc) where rownum=1";
			PreparedStatement stmt = conn.prepareStatement(mutasi);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			if(rs.getRow() > 0) {
				String lastid = rs.getString(1);
				int num = Integer.parseInt(lastid.substring(1, 4)) + 1;
				if (num < 100) {
					if (num < 10) {
						newid = "M00" + num;
					} else {
						newid = "M0" + num;
					}
				} else {
					newid = "M" + num;
				}
			}else {
				newid = "M001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newid;
	}

	@Override
	public String IdKeluar() {
		String newid = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String mutasi = "select *from(select Id_transaksi_keluar from transaksi_keluar order by Id_transaksi_keluar desc) where rownum=1";
			PreparedStatement stmt = conn.prepareStatement(mutasi);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			if(rs.getRow() > 0) {
				String lastid = rs.getString(1);
				int num = Integer.parseInt(lastid.substring(1, 4)) + 1;
				if (num < 100) {
					if (num < 10) {
						newid = "K00" + num;
					} else {
						newid = "K0" + num;
					}
				} else {
					newid = "K" + num;
				}
			}else {
				newid = "K001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newid;
	}

	@Override
	public String IdHistori() {
		String newid = null;
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String mutasi = "select *from(select Id_Histori from histori_transaksi order by Id_histori desc) where rownum=1";
			PreparedStatement stmt = conn.prepareStatement(mutasi);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			if(rs.getRow() > 0) {
				String lastid = rs.getString(1);
				int num = Integer.parseInt(lastid.substring(1, 4)) + 1;
	
				if (num < 100) {
					if (num < 10) {
						newid = "H00" + num;
					} else {
						newid = "H0" + num;
					}
				} else {
					newid = "H" + num;
				}
			}else {
				newid = "H001";
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newid;
	}
	
	@Override
	public List<Transaksi> MutasiRek(String id) {
		List<Transaksi> result = new ArrayList<Transaksi>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String mutasi = "select td.tanggal_transaksi, td.saldo_masuk, td.saldo_keluar,td.total_saldo,tk.pengirim, tk.penerima , td.berita_transfer, keterangan \r\n" + 
							"from tabungan_detail td   \r\n" + 
							"FULL OUTER JOIN transaksi_keluar tk\r\n" + 
							"on td.tanggal_transaksi= tk.tanggal_keluar\r\n" + 
							"where td.id_tabungan =?\r\n" + 
							"order by td.tanggal_transaksi desc";
			
			PreparedStatement stmt = conn.prepareStatement(mutasi);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transaksi transaksi = new Transaksi();
				transaksi.setTanggalTransaksi(rs.getString(1));
				transaksi.setSaldoMasuk(rs.getInt(2));
				transaksi.setSaldoKeluar(rs.getInt(3));
				transaksi.setTotalSaldo(rs.getInt(4));
				if(rs.getString(5) == null) {
					transaksi.setPengirim("-");
				}else {
					transaksi.setPengirim(rs.getString(5));
				}
				if(rs.getString(6) == null) {
					transaksi.setPenerima("-");
				}else {
					transaksi.setPenerima(rs.getString(6));
				}
				if(rs.getString(7) == null) {
					transaksi.setBeritaTransfer("-");
				}else {
					transaksi.setBeritaTransfer(rs.getString(7));
				}
				transaksi.setKeterangan(rs.getString(8));
				result.add(transaksi);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<Transaksi> MutasiRekApi(String id, String tglAwal, String tglAkhir) {
		String batasAwal = tglAwal+" 00:00:00";
		String batasAkhir = tglAkhir+" 23:59:59";
		List<Transaksi> result = new ArrayList<Transaksi>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String mutasi = "select to_char(td.tanggal_transaksi, 'dd MON yyyy hh24:mi:ss'), td.saldo_masuk, td.saldo_keluar,td.total_saldo,tk.pengirim, tk.penerima ,td.keterangan,td.berita_transfer \r\n" + 
							"from tabungan_detail td  \r\n" + 
							"FULL OUTER JOIN transaksi_keluar tk\r\n" + 
							"on td.tanggal_transaksi= tk.tanggal_keluar\r\n" + 
							"where td.id_tabungan = ? and td.tanggal_transaksi between TO_DATE(?, 'dd MON yyyy hh24:mi:ss') and TO_DATE(?, 'dd MON yyyy hh24:mi:ss')\r\n" + 
							"order by td.tanggal_transaksi desc";
			PreparedStatement stmt = conn.prepareStatement(mutasi);
			stmt.setString(1, id);
			stmt.setString(2, batasAwal);
			stmt.setString(3, batasAkhir);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				Transaksi transaksi = new Transaksi();
				i = (i+1);
				transaksi.setIdTabungan(i+"");;
				transaksi.setTanggalTransaksi(rs.getString(1));
				transaksi.setSaldoMasuk(rs.getInt(2));
				transaksi.setSaldoKeluar(rs.getInt(3));
				transaksi.setTotalSaldo(rs.getInt(4));
				
				if(rs.getString(5) == null) {
					transaksi.setPengirim("-");
				}else {
					Tabungan tabPengirim = tabunganDao.getOne(rs.getString(5));
					Nasabah pengirim = nasabahDao.getOne(tabPengirim.getIdNasabah());
					transaksi.setPengirim(pengirim.getFirstName()+" "+pengirim.getLastName());
				}
				
				if(rs.getString(6) == null) {
					transaksi.setPenerima("-");
				}else {
					Tabungan tabPenerima = tabunganDao.getOne(rs.getString(6));
					Nasabah penerima = nasabahDao.getOne(tabPenerima.getIdNasabah());
					transaksi.setPenerima(penerima.getFirstName()+" "+penerima.getLastName());
				}
				
				transaksi.setKeterangan(rs.getString(7));
				transaksi.setBeritaTransfer(rs.getString(8));
				result.add(transaksi);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Transaksi> Setor() {
		List<Transaksi> result = new ArrayList<Transaksi>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "select tabungan.no_rekening, tanggal_transaksi, saldo_masuk,id_histori  from tabungan_detail join tabungan on tabungan_detail.id_tabungan = tabungan.id_tabungan where keterangan='Setor'";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Transaksi transaksi = new Transaksi();
				transaksi.setPenerima(rs.getInt(1)+"");
				transaksi.setTanggalMasuk(rs.getString(2));
				transaksi.setNominal(rs.getInt(3));
				transaksi.setIdHistori(rs.getString(4));
				result.add(transaksi);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Transaksi> Transfer() {
		List<Transaksi> result = new ArrayList<Transaksi>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String query = "select Id_transaksi_keluar, Pengirim, Penerima, Nominal, TANGGAL_Keluar From transaksi_keluar";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Transaksi transaksi = new Transaksi();
				transaksi.setIdTransaksiKeluar(rs.getString(1));
				Tabungan tabPenerima = tabunganDao.getOne(rs.getString(2));
				transaksi.setPengirim(tabPenerima.getNoRekening()+"");
				Tabungan tabPengirim = tabunganDao.getOne(rs.getString(3));
				transaksi.setPenerima(tabPengirim.getNoRekening()+"");
				transaksi.setNominal(rs.getInt(4));
				transaksi.setTanggalMasuk(rs.getString(5));
				result.add(transaksi);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	public Map<String, String> CekSaldo(String idTabungan) {
		Map<String, String> hasil = new HashMap<String, String>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			String getSaldo = "select * from(select total_saldo from  tabungan_detail where id_tabungan=? order by tanggal_transaksi desc)where rownum=1";
			PreparedStatement ps = conn.prepareStatement(getSaldo);
			ps.setString(1, idTabungan);

			ResultSet rs = ps.executeQuery();
			rs.next();
			hasil.put("saldo", rs.getInt(1) + "");

			String getRekening = "select no_rekening from tabungan where id_tabungan=?";
			ps = conn.prepareStatement(getRekening);
			ps.setString(1, idTabungan);
			rs = ps.executeQuery();
			rs.next();
			
			hasil.put("noRekening", rs.getString(1));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hasil;
	}

	@Override
	public String countPegawai() {
	  String totalPegawai = null;
	  try {
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String query = "select COUNT(*)FROM pegawai";
	   PreparedStatement stmt = conn.prepareStatement(query);
	   ResultSet rs = stmt.executeQuery();

	   rs.next();
	   totalPegawai= rs.getString(1);
	   
	   conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return totalPegawai;
	 }
	 
	@Override
	public String countTransaksi() {
	  String totalTransaksi = null;
	  try {
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String query = "select COUNT(*)FROM histori_transaksi";
	   PreparedStatement stmt = conn.prepareStatement(query);
	   ResultSet rs = stmt.executeQuery();

	   rs.next();
	   totalTransaksi= rs.getString(1);
	   
	   conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return totalTransaksi;
	 }
	 
	@Override
	public String countNasabah() {
	  String totalNasabah = null;
	  try {
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String query = "select COUNT(*)FROM nasabah";
	   PreparedStatement stmt = conn.prepareStatement(query);
	   ResultSet rs = stmt.executeQuery();

	   rs.next();
	   totalNasabah= rs.getString(1);
	   
	   conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return totalNasabah;
	 }
 
	@Override
	public int countSaldo() {
	  int total = 0;
	  try {
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String query = "SELECT id_tabungan FROM tabungan WHERE status = 'AKTIF'";
	   PreparedStatement stmt = conn.prepareStatement(query);
	   ResultSet rs = stmt.executeQuery();
	   
	   while (rs.next()) {
	    String sql = "SELECT * FROM (SELECT total_saldo FROM tabungan_detail WHERE id_tabungan = ? ORDER BY tanggal_transaksi DESC) WHERE ROWNUM = 1";
	    PreparedStatement loopStmt = conn.prepareStatement(sql);
	    loopStmt.setString(1, rs.getString(1));
	    ResultSet rsSaldo = loopStmt.executeQuery();
	    rsSaldo.next();
	     total = (total +rsSaldo.getInt(1));
	   }
	   conn.close();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	  return total;
	 }
	 
	@Override
	public void CekTabungan() {
	  try {
	   System.out.println("Cek tabungan.......");
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String query = "SELECT id_tabungan FROM tabungan WHERE status = 'AKTIF'";
	   PreparedStatement stmt = conn.prepareStatement(query);
	   ResultSet rs = stmt.executeQuery();
	   
	   while (rs.next()) {
	    String getSaldo = "SELECT * FROM (SELECT total_saldo FROM tabungan_detail WHERE id_tabungan = ? ORDER BY tanggal_transaksi DESC) WHERE ROWNUM = 1";
	    PreparedStatement Saldo = conn.prepareStatement(getSaldo);
	    Saldo.setString(1, rs.getString(1));
	    ResultSet rsSaldo = Saldo.executeQuery();
	    rsSaldo.next();
	    
	    int saldoTabungan = rsSaldo.getInt(1);
	    
	    if (saldoTabungan< 50000) {
	     String getTanggal = "SELECT * FROM (SELECT to_char(tanggal_transaksi, 'dd/mm/yyyy hh24:mi:ss') FROM tabungan_detail WHERE id_tabungan = ? ORDER BY tanggal_transaksi DESC) WHERE ROWNUM = 1";
	     PreparedStatement tanggal = conn.prepareStatement(getTanggal);
	     tanggal.setString(1, rs.getString(1));
	     ResultSet rsTanggal = tanggal.executeQuery();
	     rsTanggal.next();

	      
	      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	      LocalDateTime awal = LocalDateTime.parse(rsTanggal.getString(1), dtf);
	      LocalDateTime akhir = LocalDateTime.now();
	      int jarak = (int) ChronoUnit.MINUTES.between(awal, akhir);
	      
	      if (jarak > 219000) {
	       String nonAktif = "update Tabungan set status='NON-AKTIF' where id_tabungan = ?";
	       PreparedStatement tabungan =  conn.prepareStatement(nonAktif);
	       tabungan.setString(1,rs.getString(1));
	       tabungan.executeUpdate();
	       
	       System.out.println("nonAktif tabungan :: "+rs.getString(1));
	      }else {
	       
	      }
	     
	    }else {
	     System.out.println("Tabungan :: "+rs.getString(1)+"Valid");
	    }
	   }
	  
	  }catch (SQLException e) {
	   e.printStackTrace();
	  }
	 }
	 
	@Override
	public Map<String, String> akumulasi(String idTabungan, String tgl) {
		String tglAwal = tgl+" 00:00:00";
		String tglAkhir = tgl+" 23:59:59";
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSource ds = jdbc.getDataSource();
			Connection conn = ds.getConnection();
			
			String sqlAkumulasiKeluar = "SELECT COUNT(PENGIRIM) FROM TRANSAKSI_KELUAR WHERE PENGIRIM=? AND TANGGAL_KELUAR BETWEEN TO_DATE(?, 'dd MON yyyy hh24:mi:ss') AND TO_DATE(?, 'dd MON yyyy hh24:mi:ss')";
			PreparedStatement ps = conn.prepareStatement(sqlAkumulasiKeluar);
			ps.setString(1, idTabungan);
			ps.setString(2, tglAwal);
			ps.setString(3, tglAkhir);
			ResultSet rs = ps.executeQuery();
			rs.next();
			map.put("akumulasiKeluar", rs.getString(1));
			
			String sqlAkumulasiMasuk = "SELECT COUNT(ID_TABUNGAN) FROM TRANSAKSI_MASUK WHERE ID_TABUNGAN=? AND TANGGAL_MASUK BETWEEN TO_DATE(?, 'dd MON yyyy hh24:mi:ss') AND TO_DATE(?, 'dd MON yyyy hh24:mi:ss')";
			ps = conn.prepareStatement(sqlAkumulasiMasuk);
			ps.setString(1, idTabungan);
			ps.setString(2, tglAwal);
			ps.setString(3, tglAkhir);
			rs = ps.executeQuery();
			rs.next();
			map.put("akumulasiMasuk", rs.getString(1));
			
			String sqlJumlahKeluar = "SELECT SUM(NOMINAL) FROM TRANSAKSI_KELUAR WHERE PENGIRIM=? AND TANGGAL_KELUAR BETWEEN TO_DATE(?, 'dd MON yyyy hh24:mi:ss') AND TO_DATE(?, 'dd MON yyyy hh24:mi:ss')";
			ps = conn.prepareStatement(sqlJumlahKeluar);
			ps.setString(1, idTabungan);
			ps.setString(2, tglAwal);
			ps.setString(3, tglAkhir);
			rs = ps.executeQuery();
			rs.next();
			map.put("jumlahKeluar", rs.getInt(1)+"");
			
			String sqlJumlahMasuk = "SELECT SUM(NOMINAL) FROM TRANSAKSI_MASUK WHERE ID_TABUNGAN=? AND TANGGAL_MASUK BETWEEN TO_DATE(?, 'dd MON yyyy hh24:mi:ss') AND TO_DATE(?, 'dd MON yyyy hh24:mi:ss')";
			ps = conn.prepareStatement(sqlJumlahMasuk);
			ps.setString(1, idTabungan);
			ps.setString(2, tglAwal);
			ps.setString(3, tglAkhir);
			rs = ps.executeQuery();
			rs.next();
			map.put("jumlahMasuk", rs.getInt(1)+"");
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	 public String TransferbyAdmin (Transaksi transaksi) {
	  String msg = null;
	  Transaksi tr = new Transaksi();
	  try {
	   DataSource ds = jdbc.getDataSource();
	   Connection conn = ds.getConnection();
	   String getSaldo = "select *from (select total_saldo from tabungan_detail where id_tabungan=(select id_tabungan from tabungan where no_rekening = ?) order by tanggal_transaksi desc) where rownum=1";
	   PreparedStatement ps = conn.prepareStatement(getSaldo);
	   ps.setString(1, transaksi.getPengirim());
	   ResultSet rs = ps.executeQuery();
	   rs.next();

	   tr.setTotalSaldo(rs.getInt(1));
	   int hasil = rs.getInt(1);
	   if (hasil <= 30000) {
	    msg = ("INSUFFICIENT BALANCE : " + hasil);
	   } else {
	    int nominal = transaksi.getNominal();
	    if ((hasil - nominal) <= 30000) {
	     msg = ("INSUFFICIENT BALANCE RESULT! | CURRENTLY " + ": " + hasil + " | REMAININGS : "
	       + (hasil - nominal));
	    } else {
	     msg = ("TRANSACTION MADE!");
	     String query = "insert ALL \r\n"
	             + "into tabungan_detail (id_tabungan,Saldo_masuk,total_Saldo,tanggal_transaksi,keterangan,berita_transfer,id_histori ) values ((select id_tabungan from tabungan where no_rekening = ?),?,(select *from(select  total_saldo from tabungan_detail where id_tabungan=(select id_tabungan from tabungan where no_rekening = ?) ORDER BY tanggal_transaksi DESC )where rownum = 1)+?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss'),'Transfer',?,?) \r\n"
	             + "into transaksi_masuk (id_transaksi_masuk, Id_tabungan, Nominal, tanggal_masuk ) values (?,(select id_tabungan from tabungan where no_rekening = ?),?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss')) \r\n"
	             + "into tabungan_detail (id_tabungan,Saldo_keluar,total_Saldo,tanggal_transaksi,keterangan,berita_transfer,id_histori ) values ((select id_tabungan from tabungan where no_rekening = ?),?,(select *from(select  total_saldo from tabungan_detail where id_tabungan=(select id_tabungan from tabungan where no_rekening = ?) ORDER BY tanggal_transaksi DESC )where rownum = 1)-?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss'),'Transfer',?,?)\r\n"
	             + "into transaksi_keluar (id_transaksi_keluar,pengirim,penerima,nominal,tanggal_keluar) VALUES (?,(select id_tabungan from tabungan where no_rekening = ?),(select id_tabungan from tabungan where no_rekening = ?),?,TO_DATE(?, 'yyyy/MM/dd hh24:mi:ss')) \r\n"
	             + "into histori_transaksi (id_histori, id_transaksi_masuk,id_transaksi_keluar)VALUES (?,?,?)\r\n"
	             + "SELECT * FROM dual"; 
	     PreparedStatement stmt = conn.prepareStatement(query);
	     stmt.setString(1, transaksi.getPenerima());
	     stmt.setInt(2, transaksi.getNominal());
	     stmt.setString(3, transaksi.getPenerima());
	     stmt.setInt(4, transaksi.getNominal());
	     stmt.setString(5, transaksi.getTanggalTransaksi());
	     stmt.setString(6, transaksi.getKeterangan());
	     stmt.setString(7, transaksi.getIdHistori());

	     stmt.setString(8, transaksi.getIdTransaksiMasuk());
	     stmt.setString(9, transaksi.getPenerima());
	     stmt.setInt(10, transaksi.getNominal());
	     stmt.setString(11, transaksi.getTanggalTransaksi());
	     
	     stmt.setString(12, transaksi.getPengirim());
	     stmt.setInt(13, transaksi.getNominal());
	     stmt.setString(14, transaksi.getPengirim());
	     stmt.setInt(15, transaksi.getNominal());
	     stmt.setString(16, transaksi.getTanggalTransaksi());
	     stmt.setString(17, transaksi.getKeterangan());
	     stmt.setString(18, transaksi.getIdHistori());     

	     stmt.setString(19, transaksi.getIdTransaksiKeluar());
	     stmt.setString(20, transaksi.getPengirim());
	     stmt.setString(21, transaksi.getPenerima());
	     stmt.setInt(22, transaksi.getNominal());
	     stmt.setString(23, transaksi.getTanggalTransaksi());
	     
	     stmt.setString(24, transaksi.getIdHistori());
	     stmt.setString(25, transaksi.getIdTransaksiMasuk());
	     stmt.setString(26, transaksi.getIdTransaksiKeluar());

	     stmt.executeUpdate();
	    }
	   }
	   conn.close();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return msg;
	 }
}
