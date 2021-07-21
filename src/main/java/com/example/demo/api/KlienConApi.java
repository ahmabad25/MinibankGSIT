package com.example.demo.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.KlienDAO;
import com.example.demo.dao.NasabahDAO;
import com.example.demo.dao.TabunganDao;
import com.example.demo.dao.TransaksiDao;
import com.example.demo.model.Klien;
import com.example.demo.model.Nasabah;
import com.example.demo.model.Tabungan;
import com.example.demo.model.Token;
import com.example.demo.model.Transaksi;
import com.example.demo.util.TokenUtil;

@RestController
@RequestMapping("api")
public class KlienConApi {
	
	@Autowired
	KlienDAO klienDao;
	
	@Autowired
	NasabahDAO nasabahDao;
	
	@Autowired
	TabunganDao tabunganDao;
	
	@Autowired
	TokenUtil tkUtil;
	
	@Autowired
	TransaksiDao transDao;
	
		private Map<String, Object> success(Klien klien) {
			Map<String, Object> success = new HashMap<String, Object>();
			
			// Generate Token
			Map<String, String> token = tkUtil.generateToken(klien.getIdKlien());
			
			// Insert Token to Table
			Token tkn = new Token();
			tkn.setIdKlien(klien.getIdKlien());
			tkn.setToken(token.get("token"));
			tkn.setExpireTime(token.get("expired"));
			tkUtil.insertToken(tkn);
			
			// Get User
			Map<String, String> user = new HashMap<String, String>();
			Tabungan tb = tabunganDao.getOne(klien.getIdTabungan());
			Nasabah nb = nasabahDao.getOne(tb.getIdNasabah());
			user.put("idKlien", klien.getIdKlien());
			user.put("idTabungan", tb.getIdTabungan());
			user.put("noRekening", tb.getNoRekening()+"");
			user.put("idNasabah", nb.getIdNasabah());
			user.put("nama", nb.getFirstName()+" "+nb.getLastName());
			user.put("status", "200");
			
			// Put together
			success.put("token", token);
			success.put("user", user);
			
			return success;
		}
	
		private Map<String, Object> cekUser(String tokenUser) {
			String[] enkripsi = tokenUser.split(" ");
			String kode = enkripsi[1];
			byte[] byteKode = Base64.getDecoder().decode(kode); 
			String decoded = new String(byteKode);
			
			String[] userPass = decoded.split(":"); 
			Klien klien = new Klien();
			klien.setUsername(userPass[0]);
			klien.setPassword(userPass[1]);
			
			int izin = klienDao.autentikasi(klien);
			Map<String, Object> hasil = new HashMap<String, Object>();
			switch(izin) {
				case 1 : 
					boolean session = tkUtil.cekSession(klien.getIdKlien());
					if(session) {
						System.out.println("LOG :: USER ["+klien.getIdKlien()+"] IS ALREADY IN SESSION");
						hasil.put("klien", klien);
						hasil.put("izin", 3);
						return hasil;
					}else {
						hasil.put("klien", klien);
						hasil.put("izin", 1);
						return hasil;
					}
				case 3 : 
					hasil.put("klien", klien);
					hasil.put("izin", 2);
					hasil.put("pesan", "Password yang anda masukkan salah.");
					return hasil;
				case 4 :

					hasil.put("klien", klien);
					hasil.put("izin", 2);
					hasil.put("pesan", "Tabungan anda ber-status non-aktif.");
					return hasil;
				default :
					case 2 :
						hasil.put("klien", klien);
						hasil.put("izin", 2);
						hasil.put("pesan", "Username yang anda masukkan salah atau tidak terdaftar.");
						return hasil;
			}
		}
		
		private boolean cekToken(Token token) {
			boolean hasilCek = tkUtil.validateToken(token);
			if (hasilCek) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				LocalDateTime sesiWaktu = LocalDateTime.now();
				LocalDateTime batasWaktu = LocalDateTime.parse(token.getExpireTime(), dtf);
				int jarak = (int) ChronoUnit.SECONDS.between(sesiWaktu, batasWaktu);
				
				if (jarak < 0) {
					tkUtil.deleteToken(token);
					return false;
				}else {
					return true;
				}
			}else {
				tkUtil.deleteToken(token);
				return false;
			}
		}
	
	@GetMapping(path="/sha25", produces="application/json")
	public String getSha25() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println(dtf.format(ldt));
		
		byte[] userPass = (dtf.format(ldt)).getBytes();
		byte[] secretKey = ("gsitMantab").getBytes();
		String kode = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sKey = new SecretKeySpec(secretKey, "HmacSHA256");
			mac.init(sKey);
			kode = String.format("%032x", new BigInteger(1, mac.doFinal(userPass)));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return kode;
	}
	
	@GetMapping(path="/256pin", produces="application/json")
	public String hidePin() {
		String pin = "123456";		
		String idKlien = "key"; 	
		String kode = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec koentji = new SecretKeySpec(idKlien.getBytes(), "HmacSHA256");
			mac.init(koentji);
			kode = String.format("%032x", new BigInteger(1, mac.doFinal(pin.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kode;
	}
	
	@PostMapping(path="/256pin", produces="application/json")
	public String matchPin(@RequestHeader(value="pin")String pin25) {
		String pin = "234789";		
		String idKlien = "K001"; 	
		String kode = null;
		String response = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec koentji = new SecretKeySpec(idKlien.getBytes(), "HmacSHA256");
			mac.init(koentji);
			kode = String.format("%032x", new BigInteger(1, mac.doFinal(pin.getBytes())));
			
			if(pin25.equals(kode)) {
				response = "AUTHORIZED!!";
			}else {
				response = "ERROR!! \nREC. : "+pin25+"\nHID. : "+kode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping(path="/getToken", produces="application/json")
	public String getToken(@RequestHeader(value="username") String username, @RequestHeader(value="password") String password) {
		String kode = null;
		kode = "Basic "+Base64.getEncoder().encodeToString((username+":"+password).getBytes()); 
		return kode;
	}
	
	@PostMapping(path="/getToken", produces="application/json")
	public Map<String, Object> getToken(@RequestHeader(value="Authorization") String auth) {		
		String[] encoded = auth.split(" ");
		String token = encoded[1];
		
		Map<String, String> mps = new HashMap<String, String>();
		mps.put("full", Arrays.toString(encoded));
		mps.put("token", token);
		byte[] encodedArr = Base64.getDecoder().decode(token);
		
		Map<String, Object> ahh = new HashMap<String, Object>();
		ahh.put("before", mps);
		ahh.put("after", new String(encodedArr));
		return ahh;
	}
	

	
	@GetMapping(path="/login", produces="application/json")
	public ResponseEntity<Map<String, Object>> login(@RequestHeader(value="Authorization") String auth){
		
		Map<String, Object> hasilCek = cekUser(auth);
		int authorized = Integer.parseInt(hasilCek.get("izin").toString());
		Klien klien = (Klien) hasilCek.get("klien");
		
		switch(authorized) {
			case 1 : 
				Map<String, Object> success = success(klien);
				return new ResponseEntity<Map<String, Object>>(success, HttpStatus.OK);
			case 3 :
				Token token = tkUtil.getToken(klien.getIdKlien());
				boolean authToken = cekToken(token);
				if(authToken) {
					Map<String, Object> failed = new HashMap<String, Object>();
					
					Map<String, String> message = new HashMap<String, String>();
					message.put("message", "Unauthorized Login");
					message.put("status", "403");
					message.put("cause", "USER ID PENGGUNA SEDANG DIGUNAKAN!");
					
					failed.put("user", message);					
					return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.OK);
				}else {
					Map<String, Object> renew = success(klien);
					return new ResponseEntity<Map<String,Object>>(renew, HttpStatus.OK);
				}
			default : 
				case 2:
					Map<String, Object> failed = new HashMap<String, Object>();
					
					// Message
					Map<String, String> message = new HashMap<String, String>();
					message.put("message", "Unauthorized Login");
					message.put("status", "403");
					message.put("cause", hasilCek.get("pesan")+"");
					
					failed.put("user", message);					
					return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.OK);
		}
	}
	
	@GetMapping(path="/profile", produces="application/json")
	public ResponseEntity<Map<String, Object>> lihatProfil(@RequestHeader(value="idNasabah") String idNasabah, 
															@RequestHeader(value="idKlien") String idKlien, 
															@RequestHeader(value="token") String token, 
															@RequestHeader(value="limit") String limit){
		Token tkn = new Token();
		tkn.setIdKlien(idKlien);
		tkn.setToken(token);
		tkn.setExpireTime(limit);
		
		System.out.println("LOG :: USER ["+tkn.getIdKlien()+"] REQUESTED PROFILE");
		boolean authorized = cekToken(tkn);
		if(authorized) {
			Nasabah nasabah = nasabahDao.getOne(idNasabah);
			
			Map<String, Object> success = new HashMap<String, Object>();
			success.put("dataNasabah", nasabah);
			System.out.println("LOG :: PROFILE ["+tkn.getIdKlien()+"] RESPONSE -> SUCCEED");
			return new ResponseEntity<Map<String,Object>>(success, HttpStatus.OK);
		}else {
			Map<String, Object> failed = new HashMap<String, Object>();
			
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Session Timeout");
			message.put("status", "403");
			failed.put("message", message);
			
			System.out.println("LOG :: PROFILE ["+tkn.getIdKlien()+"] RESPONSE -> FAILED - TIMED OUT");
			return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(path="/cekSaldo", produces="application/json")
	public ResponseEntity<Map<String, Object>> cekSaldo(@RequestHeader(value="idTabungan")String idTabungan,
														@RequestHeader(value="idKlien")String idKlien,
														@RequestHeader(value="token")String token,
														@RequestHeader(value="limit")String limit){
		Token tkn = new Token();
		tkn.setIdKlien(idKlien);
		tkn.setToken(token);
		tkn.setExpireTime(limit);
		
		System.out.println("LOG :: USER ["+tkn.getIdKlien()+"] REQUESTED BALANCE");
		boolean authorized = cekToken(tkn);
		if(authorized) {
			LocalDateTime ldt = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
			String tgl = dtf.format(ldt);
			Map<String, String> cekSaldo = transDao.CekSaldo(idTabungan);
			Map<String, String> akumulasi = transDao.akumulasi(idTabungan, tgl);
			
			Map<String, Object> success = new HashMap<String, Object>();
			success.put("dataTabungan", cekSaldo);
			success.put("dataAkumulasi", akumulasi);
			
			System.out.println("LOG :: BALANCE ["+tkn.getIdKlien()+"] RESPONSE -> SUCCEED");
			return new ResponseEntity<Map<String,Object>>(success, HttpStatus.OK);
		}else {
			Map<String, Object> failed = new HashMap<String, Object>();
			
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Session Timeout");
			message.put("status", "403");
			failed.put("message", message);
			
			System.out.println("LOG :: BALANCE ["+tkn.getIdKlien()+"] RESPONSE -> FAILED - TIMED OUT");
			return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(path="/cekPenerima", produces="application/json")
	public ResponseEntity<Map<String, Object>> cekDataPenerima(@RequestHeader(value="idKlien") String idKlien,
																@RequestHeader(value="token") String token,
																@RequestHeader(value="limit") String limit,
																@RequestBody Map<String, String> param) {
		Token tkn = new Token();
		tkn.setIdKlien(idKlien);
		tkn.setToken(token);
		tkn.setExpireTime(limit);
		
		System.out.println("LOG :: REQUESTING RECEIVER TO TRANSFER BY ["+tkn.getIdKlien()+"]");
		boolean authorized = cekToken(tkn);
		if(authorized) {
			String idPenerima = tabunganDao.getIdNasabah(Integer.parseInt(param.get("rekening")));
			Nasabah nasabah = nasabahDao.getOne(idPenerima);
			
			if(nasabah.getIdNasabah().equals("null")) {
				Map<String, Object> failed = new HashMap<String, Object>();
				
				Map<String, String> message = new HashMap<String, String>();
				message.put("status", "404");
				failed.put("message", message);
				return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.NOT_FOUND);
			}else {
				Map<String, Object> success = new HashMap<String, Object>();
				
				Map<String, String> penerima = new HashMap<String, String>();
				penerima.put("status", "200");
				penerima.put("nama", nasabah.getFirstName()+" "+nasabah.getLastName());
				penerima.put("rekening", new String(param.get("rekening")));
				penerima.put("nominal", new String(param.get("nominal")));
				penerima.put("berita", new String(param.get("berita")));
				penerima.put("pengirim", new String(param.get("pengirim")));
				success.put("penerima", penerima);
				
				System.out.println("LOG :: RESPONSE TO REQUESTED ["+tkn.getIdKlien()+"] -> SUCCEED");
				return new ResponseEntity<Map<String,Object>>(success, HttpStatus.OK);
			}
		}else {
			Map<String, Object> failed = new HashMap<String, Object>();
			
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Session Timeout");
			message.put("status", "403");
			failed.put("message", message);
			
			System.out.println("LOG :: RESPONSE TO REQUESTED ["+tkn.getIdKlien()+"] -> FAILED - TIMED OUT");
			return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(path="/transfer", produces="application/json")
	public ResponseEntity<Map<String, Object>> transfer(@RequestHeader(value="idKlien") String idKlien,
														@RequestHeader(value="token") String token,
														@RequestHeader(value="limit") String limit,
														@RequestBody Map<String, String> param) {
		Token tkn = new Token();
		tkn.setIdKlien(idKlien);
		tkn.setToken(token);
		tkn.setExpireTime(limit);
		
		System.out.println("LOG :: REQUESTING FINALIZED TRANSFER BY ["+new String(param.get("pengirim"))+"]");
		boolean authorized = cekToken(tkn);
		if(authorized) {
			
			System.out.println("LOG :: INITIATING FINALIZED TRANSFER BY ["+new String(param.get("pengirim"))+"]");
			boolean pinAuth = tabunganDao.cekPin(param.get("hex"), tkn.getIdKlien(), param.get("pengirim"));
			if (pinAuth) {
				Transaksi transaksi = new Transaksi();
				transaksi.setIdTransaksiKeluar(transDao.IdKeluar());
				transaksi.setPengirim(param.get("pengirim"));
				transaksi.setPenerima(tabunganDao.getIdTabungan(Integer.parseInt(param.get("rekening"))));
				transaksi.setNominal(Integer.parseInt(param.get("nominal")));
				
				LocalDateTime ldt = LocalDateTime.now();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				DateTimeFormatter dtfOut = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
				String[] tglWkt = dtfOut.format(ldt).split(" ");
				
				String tglTransaksi = dtf.format(ldt);
				transaksi.setTanggalKeluar(tglTransaksi);
				transaksi.setKeterangan(param.get("berita"));
				
				transaksi.setIdTransaksiMasuk(transDao.IdMasuk());
				transaksi.setIdTabungan(tabunganDao.getIdTabungan(Integer.parseInt(param.get("rekening"))));
				transaksi.setTanggalMasuk(tglTransaksi);
				
				transaksi.setIdHistori(transDao.IdHistori());
				transaksi.setSaldoKeluar(Integer.parseInt(param.get("nominal")));
				transaksi.setSaldoMasuk(Integer.parseInt(param.get("nominal")));
				transaksi.setTanggalTransaksi(tglTransaksi);
				
				String hasilTf = transDao.Transfer(transaksi);
				Map<String, Object> success = new HashMap<String, Object>();
				
				Map<String, String> message = new HashMap<String, String>();
				message.put("status", "200");
				message.put("tanggal", tglWkt[0]+" "+tglWkt[1]+" "+tglWkt[2]);
				message.put("waktu", tglWkt[3]);
				message.put("rekening", param.get("rekening"));
				message.put("nominal", transaksi.getNominal()+"");
				message.put("berita", transaksi.getKeterangan());
				
				success.put("msg", message);
				System.out.println("LOG :: TRANSFER BY ["+tkn.getIdKlien()+"] RESULTED IN ["+hasilTf+"]");
				return new ResponseEntity<Map<String,Object>>(success, HttpStatus.OK); 
			}else {
				Map<String, Object> failed = new HashMap<String, Object>();
				
				Map<String, String> message = new HashMap<String, String>();
				message.put("msg", "PIN YANG ANDA MASUKKAN SALAH");
				message.put("status", "401");
				failed.put("message", message);
				
				System.out.println("LOG :: TRANSFER BY ["+tkn.getIdKlien()+"] RESULTED IN -> FAILED - WRONG PIN");
				return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
			}
		}else {
			Map<String, Object> failed = new HashMap<String, Object>();
			
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Session Timeout");
			message.put("status", "403");
			failed.put("message", message);
			
			System.out.println("LOG :: RESPONSE TO FINALIZED TRANSFER BY ["+tkn.getIdKlien()+"] -> FAILED - TIMED OUT");
			return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(path="/mutasi", produces="application/json")
	public ResponseEntity<Map<String, Object>> mutasi(@RequestHeader(value="idKlien") String idKlien,
														@RequestHeader(value="token") String token,
														@RequestHeader(value="limit") String limit,
														@RequestBody Map<String, String> param) {
		Token tkn = new Token();
		tkn.setIdKlien(idKlien);
		tkn.setToken(token);
		tkn.setExpireTime(limit);
		
		System.out.println("LOG :: REQUESTING MUTATION RECORDS OF ["+tkn.getIdKlien()+"]");
		boolean authorized = cekToken(tkn);
		if(authorized) {
			List<Transaksi> mutasi = transDao.MutasiRekApi(param.get("idTabungan"), param.get("tglAwal"), param.get("tglAkhir"));
			
			Map<String, Object> success = new HashMap<String, Object>();
			success.put("mutasi", mutasi);
			
			System.out.println("LOG :: RESPONSE TO REQUESTED MUTATION BY ["+tkn.getIdKlien()+"] -> SUCCESS");
			return new ResponseEntity<Map<String, Object>>(success, HttpStatus.OK);
		}else {
			Map<String, Object> failed = new HashMap<String, Object>();
			
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Session Timeout");
			message.put("status", "403");
			failed.put("message", message);
			
			System.out.println("LOG :: RESPONSE TO REQUESTED MUTATION BY ["+tkn.getIdKlien()+"] -> FAILED - TIMED OUT");
			return new ResponseEntity<Map<String,Object>>(failed, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(path="/logout", produces="application/json")
	public void logout(@RequestHeader(value="idKlien") String idKlien) {
		Token token = new Token();
		token.setIdKlien(idKlien);
		tkUtil.deleteToken(token);
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		System.out.println("LOG :: USER ["+idKlien+"] HAS BEEN LOGOUT ["+dtf.format(ldt)+"]");
	}
}
