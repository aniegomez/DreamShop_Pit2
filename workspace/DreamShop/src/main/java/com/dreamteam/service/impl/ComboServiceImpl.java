package com.dreamteam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.dreamteam.dao.CategoriaDAO;
import com.dreamteam.dao.ColorDAO;
import com.dreamteam.dao.DepartamentoDAO;
import com.dreamteam.dao.DistritoDAO;
import com.dreamteam.dao.EstadoCivilDAO;
import com.dreamteam.dao.MarcaDAO;
import com.dreamteam.dao.PaisDAO;
import com.dreamteam.dao.ProvinciaDAO;
import com.dreamteam.dao.RolDAO;
import com.dreamteam.dao.TipoDocumentoDAO;
import com.dreamteam.service.ComboService;

@Service
public class ComboServiceImpl implements ComboService
{
	@Autowired private CategoriaDAO categoriaDAO;	
	@Autowired private MarcaDAO marcaDAO;
	@Autowired private ColorDAO colorDAO;
	@Autowired private EstadoCivilDAO estadoCivilDAO;	
	@Autowired private TipoDocumentoDAO tipoDocumentoDAO;
	@Autowired private PaisDAO paisDAO;	
	@Autowired private DepartamentoDAO departamentoDAO;	
	@Autowired private ProvinciaDAO provinciaDAO;	
	@Autowired private DistritoDAO distritoDAO;
	@Autowired private RolDAO rolDAO;
	
	@Override public List<CategoriaBean> readCategoria() {return categoriaDAO.readCategoria();}
	@Override public List<CategoriaBean> readCategoriaActiva()  {return categoriaDAO.readCategoriaActiva();}
	@Override public List<MarcaBean> readMarca() {return marcaDAO.readMarca();}
	@Override public List<ColorBean> readColor() {return colorDAO.readColor();}	
	@Override public List<EstadoCivilBean> readEstadoCivil() {return estadoCivilDAO.readEstadoCivil();}	
	@Override public List<TipoDocumentoBean> readTipoDocumento() {return tipoDocumentoDAO.readTipoDocumento();}	
	@Override public List<PaisBean> readPais() {return paisDAO.readPais();}	
	@Override public List<DepartamentoBean> readDepartamentoPorPais(int idPais) {return departamentoDAO.readDepartamentoPorPais(idPais);}	
	@Override public List<ProvinciaBean> readProvinciaPorDepartamento(int idDepartamento) {return provinciaDAO.readProvinciaPorDepartamento(idDepartamento);}	
	@Override public List<DistritoBean> readDistritoPorProvincia(int idProvincia) {return distritoDAO.readDistritoPorProvincia(idProvincia);}	
	@Override public List<RolBean> readRol(){return  rolDAO.readRol();}
}