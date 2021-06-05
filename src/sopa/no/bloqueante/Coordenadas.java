package sopa.no.bloqueante;

import java.io.Serializable;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Coordenadas implements Serializable
{
    private String palabra;
    
    private int inicio_x;
    private int inicio_y;
    
    private int final_x;
    private int final_y;
    
    
    //private int orientation;
    
    public Coordenadas() {
    }
    
    public Coordenadas(String palabra, int x, int y, int x2, int y2) {
    
        this.palabra = palabra;
        
        this.inicio_x = x;
        this.inicio_y = y;
        
        this.final_x = x2;
        this.final_y = y2;
        
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int getInicio_x() {
        return inicio_x;
    }

    public void setInicio_x(int inicio_x) {
        this.inicio_x = inicio_x;
    }

    public int getInicio_y() {
        return inicio_y;
    }

    public void setInicio_y(int inicio_y) {
        this.inicio_y = inicio_y;
    }

    public int getFinal_x() {
        return final_x;
    }

    public void setFinal_x(int final_x) {
        this.final_x = final_x;
    }

    public int getFinal_y() {
        return final_y;
    }

    public void setFinal_y(int final_y) {
        this.final_y = final_y;
    }
    
}
