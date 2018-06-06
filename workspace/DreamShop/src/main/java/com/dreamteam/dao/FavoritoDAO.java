package com.dreamteam.dao;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.FavoritoBean;

public interface FavoritoDAO
{
	public Map<String, Object> createFavorito(FavoritoBean favoritoBean);
	public List<FavoritoBean>  readFavoritoPorCliente(int idCliente);
	public Map<String, Object> deleteFavorito(int idCliente, int idProducto, int idColor);
}