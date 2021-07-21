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
import com.example.demo.model.Jabatan;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("jabatan")
public class JabatanController {
	
	@Autowired
	SessionUtil sessionUtil;
	
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
				List<Jabatan> jabatan = jabatanDao.getAll();
				model.addAttribute("data", jabatan);
				model.addAttribute("id", id);
				return "Jabatan/jabatanAll";
			}
		}
	}
	
	
	@RequestMapping(path= "/{idJabatan}", method = RequestMethod.GET)
	public String getOne(@PathVariable(name = "idJabatan")int idJabatan, HttpSession session, Model model) {
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
				Jabatan jbt = jabatanDao.getOne(idJabatan);
				model.addAttribute("jabatan", jbt);
				model.addAttribute("id", id);
				return "Jabatan/getOne";
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
				Jabatan jabatan = new Jabatan();
				int newId = jabatanDao.genereteId();
				model.addAttribute("jabatan", jabatan);
				model.addAttribute("newId", newId);
				model.addAttribute("id", id);
				return "Jabatan/formAdd";
			}
		}
	}

	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute Jabatan jabatan, HttpSession session, Model model) {
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
				String result = jabatanDao.insert(jabatan);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/jabatan";
			}
		}
	}
	
	@RequestMapping(path = "/delete/{idJabatan}", method = RequestMethod.GET)
	public String delete(@PathVariable(name = "idJabatan") int idJabatan, HttpSession session, Model model) {
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
				String delete = jabatanDao.delete(idJabatan);
				model.addAttribute("id", id);
				System.out.println(delete);
				return "redirect:/jabatan";
			}
		}
	}
	
	@RequestMapping(path = "/update/{idJabatan}", method = RequestMethod.GET)
	public String formUpdate(@PathVariable(name = "idJabatan") int idJabatan, HttpSession session, Model model) {
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
				Jabatan jabatan = jabatanDao.getOne(idJabatan);
				model.addAttribute("jabatan", jabatan);
				model.addAttribute("id", id);
				return "Jabatan/formUpdate";
			}
		}
	}

	@RequestMapping(value = "/update/{idJabatan}", method = RequestMethod.POST)
	public String update(@ModelAttribute Jabatan jabatan, HttpSession session, Model model) {
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
				String result = jabatanDao.update(jabatan);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/jabatan";
			}
		}
	}
}