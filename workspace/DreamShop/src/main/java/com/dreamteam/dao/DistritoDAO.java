package com.dreamteam.dao;

import java.util.List;

import com.dreamteam.bean.DistritoBean;

public interface DistritoDAO
{
	public List<DistritoBean> readDistritoPorProvincia(int idProvincia);
}
