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

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.service.CategoriaService;

@RestController
@RequestMapping("/categoria")
public class CategoriaController
{
    @Autowired
    CategoriaService categoriaService;
    
    @RequestMapping(value = "/createCategoria", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createCategoria(@RequestBody CategoriaBean categoriaBean)
    {return categoriaService.createCategoria(categoriaBean);}
    
    @RequestMapping(value = "/readCategoria", method = RequestMethod.GET)
    @ResponseBody public List<CategoriaBean> readCategoria()
    { return categoriaService.readCategoria(); }
    
    @RequestMapping(value = "/updateCategoria", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateCategoria(@RequestBody CategoriaBean categoriaBean)
    {return categoriaService.updateCategoria(categoriaBean);}
    
    @RequestMapping(value = "/deleteCategoria/{idCategoria}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteCategoria(@PathVariable int idCategoria)
    {return categoriaService.deleteCategoria(idCategoria);}
    
    @RequestMapping(value = "/readCategoriaActiva", method = RequestMethod.GET)
	@ResponseBody public List<CategoriaBean> readCategoriaActiva() {return categoriaService.readCategoriaActiva();}
}