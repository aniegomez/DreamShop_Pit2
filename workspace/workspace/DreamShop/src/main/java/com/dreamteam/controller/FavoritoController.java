package com.dreamteam.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.bean.FavoritoBean;
import com.dreamteam.service.FavoritoService;

@RestController
@RequestMapping("/favorito")
public class FavoritoController
{
    @Autowired
    FavoritoService favoritoService;

    @RequestMapping(value = "/createFavorito", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createFavorito(@RequestBody FavoritoBean favoritoBean)
	{return favoritoService.createFavorito(favoritoBean);}
	
    @RequestMapping(value = "/readFavoritoPorCliente/{idCliente}", method = RequestMethod.GET)
    @ResponseBody public List<FavoritoBean>  readFavoritoPorCliente(@PathVariable int idCliente)
	{return favoritoService.readFavoritoPorCliente(idCliente);}
	
    @RequestMapping(value = "/deleteFavorito/{idCliente}/{idProducto}/{idColor}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteFavorito(@PathVariable int idCliente, @PathVariable int idProducto, @PathVariable int idColor)
	{return favoritoService.deleteFavorito(idCliente, idProducto, idColor);}
}