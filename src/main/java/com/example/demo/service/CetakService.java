package com.example.demo.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CetakDataNasabah;
import com.example.demo.dao.CetakJabatan;
import com.example.demo.dao.CetakKlien;
import com.example.demo.dao.CetakMutasi;
import com.example.demo.dao.CetakPegawai;
import com.example.demo.dao.CetakSetoranNasabah;
import com.example.demo.dao.CetakTabungan;
import com.example.demo.dao.CetakTransfer;
import com.example.demo.dao.CetakTransfer;
import com.example.demo.dao.CetakUserPegawai;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class CetakService {

 @Autowired
 private CetakMutasi cetakDao;
 
 @Autowired
 private CetakTabungan cetakTabunganDao;
 
 @Autowired
 private CetakPegawai cetakPegawaiDao;
 
 @Autowired
 private CetakDataNasabah cetakNasabahDao;
 
 @Autowired
 private CetakKlien cetakKlienDao;
 
 @Autowired
 private CetakJabatan cetakJabatanDao;
 
 @Autowired
 private CetakUserPegawai  cetakUserPegawaiDao;
 
 @Autowired
 private CetakSetoranNasabah cetakSetoranNasabahDao;
 
 @Autowired
 private CetakTransfer cetaktransferDao;
 
	public JasperPrint cetakMutasi() throws SQLException, JRException, IOException {
  return cetakDao.exportPdfFile();
  
 }
	public JasperPrint cetakTabungan() throws SQLException, JRException, IOException {
		  return cetakTabunganDao.exportPdfFile();
}
	public JasperPrint cetakPegawai() throws SQLException, JRException, IOException {
		  return cetakPegawaiDao.exportPdfFile();
}
	public JasperPrint cetakNasabah() throws SQLException, JRException, IOException {
		  return cetakNasabahDao.exportPdfFile();
}
	public JasperPrint cetakKlien() throws SQLException, JRException, IOException {
		  return cetakKlienDao.exportPdfFile();
	}
	public JasperPrint cetakJabatan() throws SQLException, JRException, IOException {
		  return cetakJabatanDao.exportPdfFile();
	}
	public JasperPrint cetakUserPegawai() throws SQLException, JRException, IOException {
		  return cetakUserPegawaiDao.exportPdfFile();
	}
	public JasperPrint cetakSetoranNasabah() throws SQLException, JRException, IOException {
		  return cetakSetoranNasabahDao.exportPdfFile();
	}
	public JasperPrint cetakTransfer() throws SQLException, JRException, IOException {
		  return cetaktransferDao.exportPdfFile();
	}
}