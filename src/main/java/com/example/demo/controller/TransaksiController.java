package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dao.TransaksiDao;
import com.example.demo.model.Transaksi;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("transaksi")
public class TransaksiController {

	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	TransaksiDao transaksiDao;
	
	@RequestMapping(path = "/setor", method = RequestMethod.GET)
	public String setor(HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				Transaksi transaksi = new Transaksi();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime ldt = LocalDateTime.now();
				String hireDate = dtf.format(ldt);
				String idMasuk = transaksiDao.IdMasuk();
				String idHistori = transaksiDao.IdHistori();
				model.addAttribute("idmasuk", idMasuk);
				model.addAttribute("idhistori", idHistori);
				model.addAttribute("date", hireDate);
				model.addAttribute("transaksi", transaksi);
				model.addAttribute("id", id);
				return "Transaksi/setor";
			}
		}
	}
	
	@RequestMapping(path = "/setor", method = RequestMethod.POST)
	public String insertsetor(@ModelAttribute Transaksi transaksi, HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				transaksiDao.Setor(transaksi);
				System.out.println("Berhasil Setor ke no Rekening  :: "+transaksi.getIdTabungan());
				model.addAttribute("id", id);
				return "redirect:/transaksi/data/setor";
			}
		}
	}
	
	@RequestMapping(path = "/transfer", method = RequestMethod.GET)
	public String Transfer (HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				Transaksi transfer = new Transaksi();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime ldt = LocalDateTime.now();
				String hireDate = dtf.format(ldt);
				String idMasuk = transaksiDao.IdMasuk();
				String idKeluar = transaksiDao.IdKeluar();
				String idHistori = transaksiDao.IdHistori();
				model.addAttribute("idmasuk",idMasuk);
				model.addAttribute("idkeluar", idKeluar);
				model.addAttribute("idhistori", idHistori);
				model.addAttribute("date", hireDate);
				model.addAttribute("transfer", transfer);
				model.addAttribute("id", id);
				return "Transaksi/transfer";
			}
		}
	}
	
	@RequestMapping(path = "/transfer", method = RequestMethod.POST)
	public String insertTransfer(@ModelAttribute Transaksi transfer, HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				String msg = transaksiDao.Transfer(transfer);
				model.addAttribute("id", id);
				System.out.println(msg);
				System.out.println("Berhasil Transfer ke-Rek:"+transfer.getPenerima());
				return "redirect:/transaksi/transfer";
			}
		}
	}
	

	@RequestMapping(path="/data/setor", method = RequestMethod.GET)
	public String HistoriSetor(HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				List<Transaksi> setor = transaksiDao.Setor();
				model.addAttribute("setor", setor);
				model.addAttribute("id", id);
				return "Transaksi/setorAll";
			}
		}
	}
	@RequestMapping(path = "/data/transfer", method = RequestMethod.GET)
	public String HistoriTransfer(HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "redirect:/";
			}else {
				List<Transaksi> transfer = transaksiDao.Transfer();
				model.addAttribute("transfer", transfer);
				model.addAttribute("id", id);
				return "Transaksi/transferAll";
			}
		}
	}
}