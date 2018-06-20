package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.dao.CategoriaDAO;
import com.dreamteam.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService
{
	@Autowired
	CategoriaDAO categoriaDAO;
	
	@Override
	public Map<String, Object> createCategoria(CategoriaBean categoriaBean)
	{return categoriaDAO.createCategoria(categoriaBean);}
	
	@Override
	public List<CategoriaBean> readCategoria()
	{return categoriaDAO.readCategoria();}
	
	@Override
	public Map<String, Object> updateCategoria(CategoriaBean categoriaBean)
	{return categoriaDAO.updateCategoria(categoriaBean);}
	
	@Override
	public Map<String, Object> deleteCategoria(int idCategoria)
	{return categoriaDAO.deleteCategoria(idCategoria);}
	
	@Override
	public List<CategoriaBean> readCategoriaActiva()
	{return categoriaDAO.readCategoriaActiva();}
}