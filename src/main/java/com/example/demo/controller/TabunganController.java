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

import com.example.demo.dao.NasabahDAO;
import com.example.demo.dao.TabunganDao;
import com.example.demo.dao.TransaksiDao;
import com.example.demo.model.Nasabah;
import com.example.demo.model.Tabungan;
import com.example.demo.model.Transaksi;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("tabungan")
public class TabunganController {
	
	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	TabunganDao tabunganDao;
	
	@Autowired
	NasabahDAO nasabahDao;
	
	@Autowired
	TransaksiDao transaksiDao;
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public String getAll (HttpSession session ,Model model) {
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
				List<Tabungan> tabungan = tabunganDao.getAll();
				model.addAttribute("data", tabungan);
				model.addAttribute("id", id);
				return "tabungan/tabungan";
			}
		}
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String getOne (@PathVariable(name ="id")String idTabungan, HttpSession session, Model model) {
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
				Tabungan tabungan = tabunganDao.getOne(idTabungan);
				model.addAttribute("tabungan", tabungan);
				model.addAttribute("id", id);
				return "get1tabungan";
			}
		}
	}
	@RequestMapping(path = "add", method = RequestMethod.GET)
	public String addTabungan(HttpSession session, Model model) {
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
				Tabungan tabungan = new Tabungan();
				String newRek = tabunganDao.generateNorek();
				String newId = tabunganDao.generateId();
				String tglBuka = tabunganDao.ambilTanggal();
				List<Nasabah> daftarNasabah = nasabahDao.getAll();
				String idMasuk = transaksiDao.IdMasuk();
				String idHistori = transaksiDao.IdHistori();
				model.addAttribute("tabungan", tabungan);
				model.addAttribute("daftarNasabah", daftarNasabah);
				model.addAttribute("newId", newId);
				model.addAttribute("tglBuka", tglBuka);
				model.addAttribute("newRek", newRek);
				model.addAttribute("idmasuk", idMasuk);
				model.addAttribute("idhistori", idHistori);
				model.addAttribute("id", id);
				return "tabungan/addTabungan";
			}
		}
	}
		
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String insertEmployee(@ModelAttribute Tabungan tabungan, HttpSession session, Model model) {
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
				String result = tabunganDao.insert(tabungan);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/tabungan";
			}
		}
	}
	
	@RequestMapping(path = "update/{id}", method = RequestMethod.GET)
	public String formEdit(@PathVariable(name="id")String idTabungan,HttpSession session,Model model) {
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
				Tabungan tabungan = tabunganDao.getOne(idTabungan);
				model.addAttribute("tabungan", tabungan);
				model.addAttribute("id", id);
				return "tabungan/updateTabungan"; 
			}
		}
	}
	
	@RequestMapping(path = "update/{id}", method = RequestMethod.POST)
	public String Update (@ModelAttribute Tabungan tabungan, HttpSession session, Model model) {
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
				String result = tabunganDao.update(tabungan);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/tabungan";
			}
		}
	}
	
	@RequestMapping(path = "/nonaktif/{idTab}", method = RequestMethod.GET)
	public String nonAktif (@PathVariable(name ="idTab")String idTabungan, HttpSession session, Model model) {
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
				String status = tabunganDao.NonAktif(idTabungan);
				model.addAttribute("id", id);
				System.out.println(status);
				return "redirect:/tabungan";
			}
		}
	}
	@RequestMapping(path = "/aktif/{idTab}", method = RequestMethod.GET)
	public String Aktif (@PathVariable(name ="idTab")String idTabungan, HttpSession session, Model model) {
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
				String status = tabunganDao.Aktif(idTabungan);
				model.addAttribute("id", id);
				System.out.println(status);
				return "redirect:/tabungan";
			}
		}
	}
	
	@RequestMapping(path = "/mutasi/{id}", method = RequestMethod.GET)
	public String Mutasi(@PathVariable(name = "id") String idTabungan, HttpSession session, Model model) {
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
				List <Transaksi> transaksi =transaksiDao.MutasiRek(idTabungan);
				model.addAttribute("transaksi", transaksi);
				model.addAttribute("id", id);
				return "tabungan/mutasi";
			}
		}
	}
}