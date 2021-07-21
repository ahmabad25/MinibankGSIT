package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.NasabahDAO;
import com.example.demo.model.Nasabah;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("nasabah")
public class NasabahController {
	
	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	NasabahDAO nasabahDao;
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public String berandaNasabah(HttpSession session, Model model) {
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
				List<Nasabah> nasabah = nasabahDao.getAll();
				model.addAttribute("data", nasabah);
				model.addAttribute("id", id);
				return "nasabah/Beranda";
			}
		}
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String rincianNasabah(@PathVariable(name = "id") String idNasabah, HttpSession session, Model model) {
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
				Nasabah nasabah = nasabahDao.getOne(idNasabah);
				model.addAttribute("nasabah", nasabah);
				model.addAttribute("id", id);
				return "nasabah/Rincian";
			}
		}
	}
	
	@RequestMapping(path = "/tambah", method = RequestMethod.GET)
	public String panggilFormTambah(HttpSession session, Model model) {
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
				Nasabah nasabahBaru = new Nasabah();
				String idBaru = nasabahDao.generateId();
				model.addAttribute("nasabahBaru", nasabahBaru);
				model.addAttribute("idBaru", idBaru);
				model.addAttribute("id", id);
				return "nasabah/TambahBaru";
			}
		}
	}
	
	@RequestMapping(path = "/tambah", method = RequestMethod.POST)
	public String tambahNasabahBaru(@ModelAttribute Nasabah nasabahBaru, @RequestParam(name="idNasabah")String idNasabah, HttpSession session, Model model){
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
				nasabahBaru.setIdNasabah(idNasabah);
				String hasil = nasabahDao.insert(nasabahBaru);
				model.addAttribute("id", id);
				System.out.println(hasil);
				return "redirect:/nasabah";
			}
		}
	}
	
	@RequestMapping(path = "/ubah/{id}", method = RequestMethod.GET)
	public String panggilFormUbah(@PathVariable(name="id")String idNasabah, HttpSession session, Model model) {
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
				Nasabah nasabah = nasabahDao.getOne(idNasabah);
				model.addAttribute("nasabah", nasabah);
				model.addAttribute("id", id);
				return "nasabah/UbahData";
			}
		}
	}
	
	@RequestMapping(path = "/ubah/{id}", method = RequestMethod.POST)
	public String ubahDataNasabah(@ModelAttribute Nasabah nasabah, HttpSession session, Model model) {
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
				String hasil = nasabahDao.update(nasabah);
				model.addAttribute("id", id);
				System.out.println(hasil);
				return "redirect:/nasabah";
			}
		}
	}
	
	@RequestMapping(path = "/hapus/{id}", method = RequestMethod.GET)
	public String hapusDataNasabah(@PathVariable(name="id")String idNasabah, HttpSession session, Model model) {
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
				String hasil = nasabahDao.delete(idNasabah);
				model.addAttribute("id", id);
				System.out.println(hasil);
				return "redirect:/nasabah";
			}
		}
	}
}