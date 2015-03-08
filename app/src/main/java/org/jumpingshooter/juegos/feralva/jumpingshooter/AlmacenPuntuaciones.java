package org.jumpingshooter.juegos.feralva.jumpingshooter;

import java.util.Vector;
/**
 * Created by Packard Bell on 08/03/2015.
 */
public interface AlmacenPuntuaciones {
    public void guardarPuntuacion(double puntos,String nombre );
    public Vector<String> listaPuntuaciones(int Cantdad);
}

