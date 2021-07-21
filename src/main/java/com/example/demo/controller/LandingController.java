package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dao.JabatanDAO;
import com.example.demo.dao.PegawaiDAO;
import com.example.demo.dao.TransaksiDao;
import com.example.demo.dao.UserPegawaiDAO;
import com.example.demo.model.Jabatan;
import com.example.demo.model.Pegawai;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("")
public class LandingController {
	
	@Autowired
	PegawaiDAO pegawaiDao;
	
	@Autowired
	JabatanDAO jabatanDao;
	
	@Autowired
	UserPegawaiDAO userPegawaiDao;
	
	@Autowired
	TransaksiDao transaksiDao;
	
	@Autowired
	SessionUtil sessionUtil;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String panggilHalamanLogin(HttpSession session, HttpServletRequest request, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "FormLogin";
		}else {
			boolean auth = sessionUtil.validateSession(id);
			if (auth == false) {
				UserPegawai user = new UserPegawai();
				model.addAttribute("user", user);
				return "FormLogin";
			}else {
				return "redirect:/Home";
			}
		}
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public String autentikasiLogin(@ModelAttribute UserPegawai user, HttpServletRequest request, BindingResult bindingResult, Model model) {
		Map<String, String> kode = userPegawaiDao.autentikasi(user);
		boolean auth = Boolean.parseBoolean(kode.get("izin"));
		String hasil = kode.get("pesan");
		
		Map<String, String> id = new HashMap<String, String>();
		if(auth) {
			boolean cekSesi = sessionUtil.cekSession(user.getIdUser());
			if(cekSesi == false) {
				id.put("idUser", user.getIdUser());
				id.put("idPegawai", user.getIdPegawai());				
				Pegawai pgw = pegawaiDao.getOne(user.getIdPegawai());
				id.put("idJabatan", pgw.getIdJabatan());
				sessionUtil.insertSession(id);
				
				request.getSession().setAttribute("mySession", id);
				System.out.println(hasil);
				return "redirect:/Home";
			}else {
				Map<String, String> retrievedSession = sessionUtil.getSession(user.getIdUser());
				boolean validasi = sessionUtil.validateSession(retrievedSession);
				if(validasi == false) {
					sessionUtil.deleteSession(retrievedSession);
					id.put("idUser", user.getIdUser());
					id.put("idPegawai", user.getIdPegawai());				
					Pegawai pgw = pegawaiDao.getOne(user.getIdPegawai());
					id.put("idJabatan", pgw.getIdJabatan());
					sessionUtil.insertSession(id);
					
					request.getSession().setAttribute("mySession", id);
					System.out.println(hasil);
					return "redirect:/Home";
				}else {
					System.out.println("A SESSION BY ["+user.getIdUser()+"] IS CURRENTLY ACTIVE");
					return "redirect:/";
				}
			}
		}else {
			System.out.println(hasil);
			return "redirect:/";
		}
	}

	@GetMapping("/Home")
	public String getHome(HttpSession session, HttpServletRequest request, Model model) {
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
				Pegawai data = pegawaiDao.getOne(id.get("idPegawai"));
				String pegawai = transaksiDao.countPegawai();
				String nasabah = transaksiDao.countNasabah();
				String transaksi = transaksiDao.countTransaksi();
				int totalSaldo = transaksiDao.countSaldo();
				model.addAttribute("totalPegawai", pegawai);
				model.addAttribute("totalNasabah", nasabah);
				model.addAttribute("totalTransaksi", transaksi);
				model.addAttribute("totalSaldo", totalSaldo);
				model.addAttribute("pegawai", data);
				model.addAttribute("id", id);
				return "Home";
			}
		}
	}
	
	@RequestMapping(path="/logout", method=RequestMethod.GET)
	public String destroySession(HttpSession session, HttpServletRequest request, Model model) {
		@SuppressWarnings("unchecked")
		Map<String, String> id = (Map<String, String>) session.getAttribute("mySession");
		if(id == null) {
			UserPegawai user = new UserPegawai();
			model.addAttribute("user", user);
			return "redirect:/";
		}else {
			sessionUtil.deleteSession(id);
			request.getSession().invalidate();
			return "redirect:/";
		}
	}
	
	@GetMapping("/profile")
	public String getProfile(HttpSession session, Model model) {
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
				Pegawai pegawai = pegawaiDao.getOne(id.get("idPegawai"));
				Jabatan jabatan = jabatanDao.getOne(Integer.parseInt(pegawai.getIdJabatan()));
				model.addAttribute("pegawai", pegawai);
				model.addAttribute("jabatan", jabatan);
				model.addAttribute("id", id);
				return "profile"; 
			}
		}
	}
}