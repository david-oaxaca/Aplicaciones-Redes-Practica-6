package sopa.no.bloqueante;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Client_GUI extends JFrame implements ActionListener{
    private JButton [][] Sopa;
    private JLabel [] PalabrasB;
    private JLabel lbl_Titulo, lbl_SubTitulo;
    private JButton btn_Terminar;
    private Client_Sopa Cliente_s = new Client_Sopa();
    
    public Client_GUI() throws IOException, ClassNotFoundException{
        iniciarVentana("Practica 2");
        this.repaint();
        setVisible(true);
        Cliente_s.inicializarTiempo();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new Client_GUI();
            
    }
    
    public void iniciarVentana(String nombre) throws IOException, ClassNotFoundException{
        this.setTitle(nombre);
        this.setSize(850, 650);
        this.setLayout(null);
        
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();
        this.setVisible(true);
    }
    
    
    public void addComponents() throws IOException, ClassNotFoundException{
        lbl_Titulo = new JLabel("Practica 2: Sopa de letras", SwingConstants.CENTER);
        lbl_Titulo.setBounds(0, 0, 825, 70);
        lbl_Titulo.setFont(new Font("Sans", Font.PLAIN, 35));
        this.add(lbl_Titulo);
        
        lbl_SubTitulo = new JLabel("Encuentra las siguientes palabras: ", SwingConstants.CENTER);
        lbl_SubTitulo.setBounds(20, 70, 270, 28);
        lbl_SubTitulo.setFont(new Font("Sans", Font.PLAIN, 17));
        this.add(lbl_SubTitulo);
        
        iniciarSopa();
        
        btn_Terminar = new JButton("Terminar juego");
        btn_Terminar.setBounds(35, 550, 125, 40);
        btn_Terminar.addActionListener(this);
        this.add(btn_Terminar);
    }
    
    public void iniciarSopa() throws IOException, ClassNotFoundException{
        String Sopa_tema = seleccionSopa();
        
        if ((Sopa_tema != null) && (Sopa_tema.length() > 0)) {
            Cliente_s.requestSopa(Sopa_tema);
            crearPalabrasList( Cliente_s.getPalabrasB() );
            crearSopaUI(Cliente_s.getSopaDeLetras());
        }else{
            System.exit(0);
        }
    }
    
    
    public String seleccionSopa(){
        String [] sopa_tema = {"Animales", "Comida", "Paises", "Pokemon"};
        String s = (String)JOptionPane.showInputDialog(
                            null,"Seleccione el tema de la sopa de letras: \t\t\n\n",
                            "Practica 2: Sopa de letras", JOptionPane.QUESTION_MESSAGE,
                            null, sopa_tema, "Animales");
        return s;
    }
    
    public void crearPalabrasList(ArrayList <String> palabrasList) throws IOException, ClassNotFoundException {
        PalabrasB = new JLabel[palabrasList.size()];
        for (int i = 0; i < palabrasList.size(); i++) {
            PalabrasB[i] = new JLabel(palabrasList.get(i));
            PalabrasB[i].setBounds(40, 100 + (i*28), 250, 28);
            PalabrasB[i].setVisible(true);
            PalabrasB[i].setFont(new Font("Sans", Font.PLAIN, 14));
            this.add(PalabrasB[i]);
        }
     }
    
    
    public void crearSopaUI(char [][] Matrix) throws IOException, ClassNotFoundException {
        Sopa = new JButton [Matrix.length][Matrix[0].length];
        for (int i = 0; i < Matrix.length; i++) {
            for (int j = 0; j < Matrix[i].length; j++) {
                Sopa[i][j] = new JButton(/*i + "," + j + */Matrix[i][j] + "");
                Sopa[i][j].setVisible(true);
                Sopa[i][j].setOpaque(true);
                Sopa[i][j].addActionListener(this);
                Sopa[i][j].setBounds(325 + (j*30), 75 + (i*30), 30, 30);
                Sopa[i][j].setBackground(Color.WHITE);
                Sopa[i][j].setBorder(LineBorder.createBlackLineBorder());
                Sopa[i][j].repaint();
                this.add(Sopa[i][j]);
            }
        }
    }
    
    public void destacar(int xPos1, int yPos1, int xPos2, int yPos2, String palabra) {
        //Coloreado de extremos
        Sopa[yPos1][xPos1].setBackground(Color.GREEN);
        Sopa[yPos2][xPos2].setBackground(Color.GREEN);
        //Colorear lo que esta entre las dos ubicaciones
        int aux_min_x= 0;
        int aux_max_x= 0;
        int aux_min_y= 0;
        int aux_max_y= 0;
        if(yPos1 == yPos2) { //Horizontal
            aux_min_x= (xPos1 < xPos2)? xPos1: xPos2;
            aux_max_x= (xPos1 > xPos2)? xPos1: xPos2;
            aux_min_x++;
            for(; aux_min_x < aux_max_x; aux_min_x++) {
                Sopa[yPos1][aux_min_x].setBackground(Color.GREEN);
            }
        } else if(xPos1 == xPos2) { //Vertical
            aux_min_y= (yPos1 < yPos2)? yPos1: yPos2;
            aux_max_y= (yPos1 > yPos2)? yPos1: yPos2;
            aux_min_y++;
            for(; aux_min_y < aux_max_y; aux_min_y++) {
                Sopa[aux_min_y][xPos1].setBackground(Color.GREEN);
            }
        } else { //Diagonal
            aux_min_x= (xPos1 < xPos2)? xPos1: xPos2;
            aux_max_x= (xPos1 > xPos2)? xPos1: xPos2;
            aux_min_y= (yPos1 < yPos2)? yPos1: yPos2;
            aux_max_y= (yPos1 > yPos2)? yPos1: yPos2;
            
            aux_min_x++;
            if((yPos2 > yPos1 && xPos2 < xPos1) || (yPos1 > yPos2 && xPos1 < xPos2)) {//Diagonal arriba
                aux_max_y--;
                for(; aux_max_y > aux_min_y; aux_max_y--) {
                    Sopa[aux_max_y][aux_min_x].setBackground(Color.GREEN);
                    aux_min_x++;
                }
            }else {
                aux_min_y++;
                System.out.println("abajo");
                for(; aux_min_y < aux_max_y; aux_min_y++) {
                    Sopa[aux_min_y][aux_min_x].setBackground(Color.GREEN);
                    aux_min_x++;
                }
            }
        }
        
        //Buscar palabra en lista y tachar
        for (JLabel PalabrasB1 : PalabrasB) {
            if (palabra.equals(PalabrasB1.getText())) {
                PalabrasB1.setVisible(false);
            }
        }
        
        //Condicional para saber si el cliente ya gano
        if(Cliente_s.getPalabrasEncontradas() == 15){
            long tiempo= Cliente_s.terminarTiempo();
            tiempo= tiempo/1000; //segundos
            int segundos= (int) tiempo % 60;
            tiempo= tiempo/60; //minutos
            JOptionPane.showMessageDialog(null, "FELICIDADES!!!  \nHAZ GANADO!!!!! \nTiempo (minutos): " + tiempo + ":" + segundos);
            System.exit(0);
        }
    }
    
    public void despintar(int xPos1, int yPos1, int xPos2, int yPos2) {
        if(Sopa[yPos1][xPos1].getBackground() != Color.GREEN) {
            Sopa[yPos1][xPos1].setBackground(Color.WHITE);
        }
        if(Sopa[yPos2][xPos2].getBackground() != Color.GREEN) {
            Sopa[yPos2][xPos2].setBackground(Color.WHITE);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand();
        
        if(evento.equals("Terminar juego")){
            long tiempo= Cliente_s.terminarTiempo();
            tiempo= tiempo/1000; //segundos
            int segundos= (int) tiempo % 60;
            tiempo= tiempo/60; //minutos
            JOptionPane.showMessageDialog(null, "Juego terminado\nPalabras encontradas: " + Cliente_s.getPalabrasEncontradas() + "\nTiempo (minutos): " + tiempo + ":" + segundos);
            System.exit(0);
        }
        for(int y = 0; y < Sopa.length; y++) { 
            for(int x = 0; x < Sopa[y].length; x++) {
                if(e.getSource() == Sopa[y][x]){
                    JButton boton = (JButton) e.getSource();
                    if(boton.getBackground() != Color.GREEN) {
                        boton.setBackground(Color.CYAN);
                    }
                    System.out.println(x + "," + y);
                    int pintar= Cliente_s.guardarSeleccion(x, y);
                    if(pintar == 2) {
                        destacar(Cliente_s.getxPos1(), Cliente_s.getyPos1(), Cliente_s.getxPos2(), Cliente_s.getyPos2(), Cliente_s.getPalabraEncontrada());
                    }else if(pintar == 0) {
                        despintar(Cliente_s.getxPos1(), Cliente_s.getyPos1(), Cliente_s.getxPos2(), Cliente_s.getyPos2());
                    }
                }
            }
        }
    }
}
