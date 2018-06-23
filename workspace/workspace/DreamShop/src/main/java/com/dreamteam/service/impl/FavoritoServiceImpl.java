package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.FavoritoBean;
import com.dreamteam.dao.FavoritoDAO;
import com.dreamteam.service.FavoritoService;

@Service
public class FavoritoServiceImpl implements FavoritoService
{
	@Autowired
	FavoritoDAO favoritoDAO;

	@Override
	public Map<String, Object> createFavorito(FavoritoBean favoritoBean)
	{return favoritoDAO.createFavorito(favoritoBean);}
	
	@Override
	public List<FavoritoBean>  readFavoritoPorCliente(int idCliente)
	{return favoritoDAO.readFavoritoPorCliente(idCliente);}
	
	@Override
	public Map<String, Object> deleteFavorito(int idCliente, int idProducto, int idColor)
	{return favoritoDAO.deleteFavorito(idCliente, idProducto, idColor);}
}