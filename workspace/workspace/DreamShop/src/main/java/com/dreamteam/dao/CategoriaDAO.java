package com.dreamteam.dao;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.CategoriaBean;

public interface CategoriaDAO
{
	public Map<String, Object> createCategoria(CategoriaBean categoriaBean);
	public List<CategoriaBean> readCategoria();
	public Map<String, Object> updateCategoria(CategoriaBean categoriaBean);
	public Map<String, Object> deleteCategoria(int idCategoria);
	public List<CategoriaBean> readCategoriaActiva();
}