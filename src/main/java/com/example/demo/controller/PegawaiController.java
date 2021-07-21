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

import com.example.demo.dao.JabatanDAO;
import com.example.demo.dao.PegawaiDAO;
import com.example.demo.model.Jabatan;
import com.example.demo.model.Pegawai;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("pegawai")
public class PegawaiController {
	
	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	PegawaiDAO pegawaiDao;
	
	@Autowired
	JabatanDAO jabatanDao;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getAll(HttpSession session, Model model) {
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
				List<Pegawai> pegawai = pegawaiDao.getAll();
				model.addAttribute("data", pegawai);
				model.addAttribute("id", id);
				return "Pegawai/allPegawai";
			}
		}
	}
	
	@RequestMapping(path= "/{idPegawai}", method = RequestMethod.GET)
	public String getOne(@PathVariable(name = "idPegawai")String idPegawai, HttpSession session, Model model) {
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
				Pegawai pgw = pegawaiDao.getOne(idPegawai);
				Jabatan jbt = jabatanDao.getOne(Integer.parseInt(pgw.getIdJabatan()));
				model.addAttribute("pegawai", pgw);
				model.addAttribute("jabatan", jbt);
				model.addAttribute("id", id);
				return "Pegawai/getOne";
			}
		}
	}
	
	@RequestMapping(path="/delete/{idPegawai}", method = RequestMethod.GET)
	public String delete(@PathVariable(name = "idPegawai") String idPegawai, HttpSession session, Model model) {
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
				String delete = pegawaiDao.delete(idPegawai);
				model.addAttribute("id", id);
				System.out.println(delete);
				return "redirect:/pegawai";
			}
		}
	}
	
	@RequestMapping(path = "/insert", method = RequestMethod.GET)
	public String formAdd(HttpSession session, Model model) {
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
				Pegawai Pegawai = new Pegawai();
				String newId = pegawaiDao.generateId();
				String tglMasuk = pegawaiDao.ambilTanggal();
				List<Jabatan> daftarJabatan = jabatanDao.getAll();
				model.addAttribute("pegawai", Pegawai);
				model.addAttribute("newId", newId);
				model.addAttribute("tglMasuk", tglMasuk);
				model.addAttribute("daftarJabatan", daftarJabatan);
				model.addAttribute("id", id);
				return "Pegawai/formAdd";
			}
		}
	}

	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute Pegawai pegawai, HttpSession session, Model model) {
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
				String result = pegawaiDao.insert(pegawai);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/pegawai";
			}
		}
	}
	
	@RequestMapping(path = "/update/{idPegawai}", method = RequestMethod.GET)
	public String formUpdate(@PathVariable(name = "idPegawai") String idPegawai, HttpSession session, Model model) {
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
				Pegawai pgw = pegawaiDao.getOne(idPegawai);
				List<Jabatan> daftarJabatan = jabatanDao.getAll();
				model.addAttribute("pegawai", pgw);
				model.addAttribute("daftarJabatan", daftarJabatan);
				model.addAttribute("id", id);
				return "Pegawai/formUpdate";
			}
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute Pegawai pegawai, HttpSession session, Model model) {
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
				String result = pegawaiDao.update(pegawai);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/pegawai";
			}
		}
	}
}