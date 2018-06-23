package com.dreamteam.service;

import java.util.List;

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

public interface ComboService
{
	public List<CategoriaBean> readCategoriaActiva();
	public List<CategoriaBean> readCategoria();
	public List<MarcaBean> readMarca();
	public List<ColorBean> readColor();
	public List<EstadoCivilBean> readEstadoCivil();
	public List<TipoDocumentoBean> readTipoDocumento();
	public List<PaisBean> readPais();
	public List<DepartamentoBean> readDepartamentoPorPais(int idPais);
	public List<ProvinciaBean> readProvinciaPorDepartamento(int idDepartamento);
	public List<DistritoBean> readDistritoPorProvincia(int idProvincia);
	public List<RolBean> readRol();
}