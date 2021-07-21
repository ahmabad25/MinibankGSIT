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

import com.example.demo.dao.KlienDAO;
import com.example.demo.dao.TabunganDao;
import com.example.demo.model.Klien;
import com.example.demo.model.Tabungan;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("klien")
public class KlienController {
	
	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	KlienDAO klienDAO;
	
	@Autowired
	TabunganDao tabunganDao;
	
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
				List<Klien> allkln = klienDAO.getAll();
				model.addAttribute("klien", allkln);
				model.addAttribute("id", id);
				return "Klien/allKlien";
			}
		}
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String getOne(@PathVariable(name = "id")String idKlien, HttpSession session, Model model) {
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
				Klien kln = klienDAO.getOne(idKlien);
				model.addAttribute("klien", kln);
				model.addAttribute("id", id);
				return "Klien/getOne";
			}
		}
	}
	
	@RequestMapping(path = "/del/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable(name="id")String idKlien, HttpSession session, Model model) {
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
				String delete = klienDAO.delete(idKlien);
				model.addAttribute("id", id);
				System.out.println(delete);
				return "redirect:/klien";
			}
		}
	}
	
	@RequestMapping(path="/edit/{id}", method = RequestMethod.GET)
	public String callEdit(@PathVariable(name="id")String idKlien, HttpSession session, Model model) {
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
				Klien kln = klienDAO.getOne(idKlien);
				model.addAttribute("klnSelected", kln);
				model.addAttribute("newId", id+"");
				model.addAttribute("id", id);
				return "Klien/formEdit";
			}
		}
	}
	
	@RequestMapping(path="/edit/{id}", method = RequestMethod.POST)
	public String doEdit(@ModelAttribute Klien editedKln, HttpSession session, Model model) {
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
				String result = klienDAO.update(editedKln);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/klien";
			}
		}
	}
	
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String callForm(HttpSession session, Model model) {
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
				String newId = klienDAO.generateId();
				Klien newkln = new Klien();
				List<Tabungan> listTabungan = tabunganDao.getKlienTabungan(); 
				model.addAttribute("newkln", newkln);
				model.addAttribute("newId", newId);
				model.addAttribute("tabungan", listTabungan);
				model.addAttribute("id", id);
				return "Klien/formAdd";
			}
		}
	}
	
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String addNew(@ModelAttribute Klien newkln,@RequestParam(name="idKlien")String idKlien, HttpSession session, Model model) {
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
				String result = klienDAO.insert(idKlien, newkln);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/klien";
			}
		}
	}
}