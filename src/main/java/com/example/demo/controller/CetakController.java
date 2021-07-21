package com.example.demo.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.CetakService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("cetak")
public class CetakController {

 @Autowired
 private CetakService userService;


 @RequestMapping(value = "/cetakMutasi", method = RequestMethod.GET)
 public void export(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"Mutasi.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakMutasi();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 
 @RequestMapping(value = "/cetakTabungan", method = RequestMethod.GET)
 public void cetakTabungan(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakTabungan.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakTabungan();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 
 @RequestMapping(value = "/cetakPegawai", method = RequestMethod.GET)
 public void cetakPegawai(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakPegawai.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakPegawai();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 
 @RequestMapping(value = "/cetakNasabah", method = RequestMethod.GET)
 public void cetakNasabah(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakNasabah.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakNasabah();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 
 @RequestMapping(value = "/cetakKlien", method = RequestMethod.GET)
 public void cetakKlien(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakKlien.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakKlien();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 @RequestMapping(value = "/cetakJabatan", method = RequestMethod.GET)
 public void cetakJabatan(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakJabatan.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakJabatan();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 @RequestMapping(value = "/cetakUserPegawai", method = RequestMethod.GET)
 public void cetakUserPegawai(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakUserpegawai.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakUserPegawai();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 @RequestMapping(value = "/cetakSetoranNasabah", method = RequestMethod.GET)
 public void cetakSetoranNasabah(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"CetakSetoranNasabah.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakSetoranNasabah();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
 @RequestMapping(value = "/cetakTransfer", method = RequestMethod.GET)
 public void cetakTransfer(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
  JasperPrint jasperPrint = null;

  response.setContentType("application/x-download");
  response.setHeader("Content-Disposition", String.format("attachment; filename=\"TransferNasabah.pdf\""));

  OutputStream out = response.getOutputStream();
  jasperPrint = userService.cetakTransfer();
  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
 }
}