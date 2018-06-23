package com.dreamteam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.bean.ColorBean;
import com.dreamteam.bean.DepartamentoBean;
import com.dreamteam.bean.DistritoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.PaisBean;
import com.dreamteam.bean.ProvinciaBean;
import com.dreamteam.bean.RolBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.service.ComboService;

@RestController
@RequestMapping("/combo")
public class ComboController
{
	@Autowired
	private ComboService comboService;
	
	@RequestMapping(value = "/readCategoria", method = RequestMethod.GET)
	@ResponseBody public List<CategoriaBean> readCategoria() {return comboService.readCategoria();}
	
	@RequestMapping(value = "/readCategoriaActiva", method = RequestMethod.GET)
	@ResponseBody public List<CategoriaBean> readCategoriaActiva() {return comboService.readCategoriaActiva();}
	
	@RequestMapping(value = "/readMarca", method = RequestMethod.GET)
	@ResponseBody public List<MarcaBean> readMarca() {return comboService.readMarca();}
	
	@RequestMapping(value = "/readColor", method = RequestMethod.GET)
	@ResponseBody public List<ColorBean> readColor() {return comboService.readColor();}
	
	@RequestMapping(value = "/readEstadoCivil", method = RequestMethod.GET)
	@ResponseBody public List<EstadoCivilBean> readEstadoCivil() {return comboService.readEstadoCivil();}
	
	@RequestMapping(value = "/readTipoDocumento", method = RequestMethod.GET)
	@ResponseBody public List<TipoDocumentoBean> readTipoDocumento() {return comboService.readTipoDocumento();}
	
	@RequestMapping(value = "/readPais", method = RequestMethod.GET)
	@ResponseBody public List<PaisBean> readPais() {return comboService.readPais();}
	
	@RequestMapping(value = "/readDepartamentoPorPais/{idPais}", method = RequestMethod.GET)
	@ResponseBody public List<DepartamentoBean> readDepartamentoPorPais(@PathVariable int idPais) {return comboService.readDepartamentoPorPais(idPais);}
	
	@RequestMapping(value = "/readProvinciaPorDepartamento/{idDepartamento}", method = RequestMethod.GET)
	@ResponseBody public List<ProvinciaBean> readProvinciaPorDepartamento(@PathVariable int idDepartamento) {return comboService.readProvinciaPorDepartamento(idDepartamento);}
	
	@RequestMapping(value = "/readDistritoPorProvincia/{idProvincia}", method = RequestMethod.GET)
	@ResponseBody List<DistritoBean> readDistritoPorProvincia(@PathVariable int idProvincia) {return comboService.readDistritoPorProvincia(idProvincia);}
	
	@RequestMapping(value = "/readRol", method = RequestMethod.GET)
	@ResponseBody public List<RolBean> readRol(){return  comboService.readRol();}
}