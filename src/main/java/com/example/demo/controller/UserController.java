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

import com.example.demo.dao.PegawaiDAO;
import com.example.demo.dao.UserPegawaiDAO;
import com.example.demo.model.Pegawai;
import com.example.demo.model.UserPegawai;
import com.example.demo.util.SessionUtil;

@Controller
@RequestMapping("UserPegawai")
public class UserController {
	
	@Autowired
	SessionUtil sessionUtil;
	
	@Autowired
	UserPegawaiDAO userPegawaiDAO;
	
	@Autowired
	PegawaiDAO pegawaiDao;

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
				List<UserPegawai> allUser = userPegawaiDAO.getAll();
				model.addAttribute("user", allUser);
				model.addAttribute("id", id);
				return "User/allUser";
			}
		}
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String getOne(@PathVariable(name = "id") String idUser, String idPegawai, HttpSession session, Model model) {
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
				UserPegawai user = userPegawaiDAO.getOne(idUser);
				model.addAttribute("user", user);
				model.addAttribute("id", id);
				return "User/getOne";
			}
		}
	}

	@RequestMapping(path = "/del/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable(name = "id") String idUser, HttpSession session, Model model) {
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
				String delete = userPegawaiDAO.delete(idUser);
				model.addAttribute("id", id);
				System.out.println(delete);
				return "redirect:/UserPegawai";
			}
		}
	}
	
	@RequestMapping(path="/edit/{id}", method = RequestMethod.GET)
	public String callEdit(@PathVariable(name="id") String idUser, HttpSession session, Model model) {
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
				UserPegawai userPegawai = userPegawaiDAO.getOne(idUser);
				model.addAttribute("userSelected", userPegawai);
				model.addAttribute("userId", idUser);
				model.addAttribute("id", id);
				return "User/formEdit";
			}
		}
	}
	
	@RequestMapping(path="/edit/{id}", method = RequestMethod.POST)
	public String doEdit(@ModelAttribute UserPegawai editedEmp, HttpSession session, Model model) {
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
				String result = userPegawaiDAO.update(editedEmp);
				model.addAttribute("id", id);
				System.out.println(result);
				return "redirect:/UserPegawai";
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
				UserPegawai newUser = new UserPegawai();
				String newId = userPegawaiDAO.generateId();
				List<Pegawai> pegawai = pegawaiDao.getUserPegawai();
				model.addAttribute("newId", newId+"");
				model.addAttribute("newUser", newUser);
				model.addAttribute("pegawai", pegawai);
				model.addAttribute("id", id);
				return "User/formAdd";
			}
		}
	}
	
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String addNew(@ModelAttribute UserPegawai newUser,@RequestParam(name="idUser")String userId, HttpSession session, Model model) {
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
				Pegawai pegawai = pegawaiDao.getOne(newUser.getIdPegawai());
				String newUsername = userPegawaiDAO.generateUsername(Integer.parseInt(pegawai.getIdJabatan()));
				
				newUser.setUsername(newUsername);
				String result = userPegawaiDAO.insert(userId, newUser);
				model.addAttribute("id", id);
				System.out.println(result);
			}
			return "redirect:/UserPegawai";
		}
	}	
}