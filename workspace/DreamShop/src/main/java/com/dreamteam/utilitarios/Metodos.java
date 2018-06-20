package com.dreamteam.utilitarios;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Metodos
{
	public static void mensaje(String m){JOptionPane.showMessageDialog(null, m);}
	public static void imprimir(String m){System.out.println(m);}
	public static void cerrar(FileOutputStream fos)
	{
		if(fos != null)
		{
			try {fos.close();}
			catch (IOException e) {e.printStackTrace();}
		}
	}
}