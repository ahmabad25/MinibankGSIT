package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Transaksi;

@Repository
public interface TransaksiDao {

	public String Transfer(Transaksi transaksi);
	public String Setor(Transaksi transaksi);
	public String TransferbyAdmin(Transaksi transaksi);
	
	public String IdMasuk ();
	public String IdKeluar ();
	public String IdHistori ();
	
	public List<Transaksi> MutasiRek(String id);
	public List<Transaksi> MutasiRekApi(String id, String tglAwal, String tglAkhir);
	public List<Transaksi> Setor();
	public List<Transaksi> Transfer();
	
	public Map<String, String> CekSaldo(String idTabungan);
	public Map<String, String> akumulasi(String idTabungan, String tgl);

	String countPegawai();
	String countTransaksi();
	String countNasabah();
	int countSaldo();
	
	public void CekTabungan();
	
}
