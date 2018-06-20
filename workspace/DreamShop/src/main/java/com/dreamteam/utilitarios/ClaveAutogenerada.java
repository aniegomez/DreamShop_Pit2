package com.dreamteam.utilitarios;

import java.util.Locale;
import java.util.Random;

import lombok.Getter;

public class ClaveAutogenerada
{	
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;
    
    @Getter private String clave;
    
    private Random random;
    
    public ClaveAutogenerada()
    {
        clave = "";
        random = new Random();
        for (int i = 0; i < 10; i++)
            clave += alphanum.charAt(random.nextInt(alphanum.length()));
    }
    
    public ClaveAutogenerada(int tamanio)
    {
        clave = "";
        random = new Random();
        for (int i = 0; i < tamanio; i++)
            clave += alphanum.charAt(random.nextInt(alphanum.length()));
    }
}