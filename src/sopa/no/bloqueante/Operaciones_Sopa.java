package sopa.no.bloqueante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Operaciones_Sopa {
    
    private ArrayList <Coordenadas> palabrasSDL = new ArrayList();//Palabras dentro de la sopa de letras
    
    public Operaciones_Sopa() {
    }
    
    public boolean inRange(int num, int Min, int Max){
        return num <= Max && num >= Min;
    }
    
    public boolean checarDisponibilidad(char [][] matrix, int coef_col, int coef_fil,  
                                    String palabra, int columna, int fila){
        
        int tam_col = matrix.length;
        int tam_fil = matrix[0].length; //Suponiendo que todas las filas son del mismo tam
        int tam_palabra = palabra.length();
        
        if( inRange(columna+(coef_col*tam_palabra), 0, tam_col) && inRange(fila+(coef_fil*tam_palabra), 0, tam_fil)){ //Disponibiladad Noreste
            
            for (int i = 0; i < tam_palabra; i++) {

                if( matrix[columna + (coef_col*i)][fila + (coef_fil*i)] != 0 &&
                    matrix[columna + (coef_col*i)][fila + (coef_fil*i)] != palabra.charAt(i) ){
            
                    return false;
                    
                }
            }
            return true;

        }
        
        return false;
    }
    
    public char [][] llenarVacios(char [][] matrix){
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == 0){
                    matrix[i][j] = (char) (new Random().nextInt(90 - 65) + 65);
                    //matrix[i][j] = ' ';
                }
                
            }
        }
        
        return matrix;
    }
    
    public char [][] llenarSopa(char [][] matrix, String [] palabras){
        
        ArrayList <String> escogidas = new ArrayList(Arrays.asList(palabras));
        int tam_col = matrix.length;
        int tam_fil = matrix[0].length; //Suponiendo que todas las filas son del mismo tam
        int cont = 0;
        this.palabrasSDL.clear();
        
        while (escogidas.size() > 5) {
            
            if(cont >= escogidas.size()) cont = 0;
            
            int tam_palabra = escogidas.get(cont).length();
            int columna = new Random().nextInt(tam_col);
            int fila = new Random().nextInt(tam_fil); 

            if( checarDisponibilidad(matrix, -1, 1, escogidas.get(cont), columna, fila)){ //Disponibiladad Noreste

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna - i][fila + i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna - tam_palabra + 1, fila + tam_palabra - 1);
                
                escogidas.remove(cont);

            }else if(checarDisponibilidad(matrix, 0, 1, escogidas.get(cont), columna, fila)){ //Disponibiladad Este

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna][fila + i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna, fila + tam_palabra - 1);
                
                escogidas.remove(cont);
                
            }else if(checarDisponibilidad(matrix, 1, 1, escogidas.get(cont), columna, fila)){ //Disponibiladad Sureste

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna + i][fila + i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna + tam_palabra - 1, fila + tam_palabra - 1);
                escogidas.remove(cont);
                
            }else if(checarDisponibilidad(matrix, 1, 0, escogidas.get(cont), columna, fila)){ //Disponibiladad Sur

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna + i][fila] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna + tam_palabra - 1, fila);
                escogidas.remove(cont);

            }else if(checarDisponibilidad(matrix, 1, -1, escogidas.get(cont), columna, fila)){ //Disponibilidad Suroeste

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna + i][fila - i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna + tam_palabra - 1, fila - tam_palabra + 1);
                escogidas.remove(escogidas.get(cont));
                
            }else if(checarDisponibilidad(matrix, 0, -1, escogidas.get(cont), columna, fila)){ //Disponibilidad Oeste

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna][fila - i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna, fila - tam_palabra + 1);
                escogidas.remove(cont);
                
            }else if(checarDisponibilidad(matrix, -1, -1, escogidas.get(cont), columna, fila)){//Disponibilidad Noreste

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna - i][fila - i] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna - tam_palabra + 1, fila - tam_palabra + 1);
                escogidas.remove(cont);
                
            }else if(checarDisponibilidad(matrix, -1, 0, escogidas.get(cont), columna, fila)){//Norte

                for (int i = 0; i < tam_palabra; i++) {
                    matrix[columna - i][fila] = escogidas.get(cont).charAt(i);
                }
                
                addPalabras(escogidas.get(cont), columna, fila, columna - tam_palabra + 1, fila);
                escogidas.remove(cont);
                
            }
            cont++;
        }
        
        return llenarVacios(matrix);
    }
    
    public void addPalabras(String palabra, int y, int x, int y2, int x2){
        Coordenadas actual_word = new Coordenadas(palabra, x, y, x2, y2);
        palabrasSDL.add(actual_word);
    }
    
    public ArrayList <Coordenadas> getPalabras(){
        return this.palabrasSDL;
    }
    
    public void imprimirSopa(char [][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("| " + matrix[i][j] + " |");
            }
            System.out.println("\n");
        }
    }
    
}
