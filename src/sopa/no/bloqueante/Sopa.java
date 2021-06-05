/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sopa.no.bloqueante;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Sopa implements Serializable{
    
    private char [][] sopa = new char[16][16];
    private ArrayList <Coordenadas> palabras;
    
    public Sopa() {
    }
    
    public Sopa(char [][] sopaN, ArrayList <Coordenadas> palabrasN){
        this.sopa = sopaN;
        this.palabras = palabrasN;
    }

    public char[][] getSopa() {
        return sopa;
    }

    public void setSopa(char[][] sopa) {
        this.sopa = sopa;
    }

    public ArrayList<Coordenadas> getPalabras() {
        return palabras;
    }

    public void setPalabras(ArrayList<Coordenadas> palabras) {
        this.palabras = palabras;
    }
    
    
    
}
